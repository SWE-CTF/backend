package sogong.ctf.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sogong.ctf.config.security.JwtProvider;
import sogong.ctf.domain.CodeStatus;
import sogong.ctf.domain.Member;
import sogong.ctf.domain.Role;
import sogong.ctf.dto.request.MemberRequestDTO;
import sogong.ctf.dto.request.ProfilePostDTO;
import sogong.ctf.dto.request.ProfilePostNotPWDTO;
import sogong.ctf.dto.response.*;
import sogong.ctf.repository.MemberRepository;

import java.security.NoSuchProviderException;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional
    public Long join(MemberRequestDTO memberRequestDTO) {

        validateDuplicateMember(memberRequestDTO);
        memberRequestDTO.setPassword(passwordEncoder.encode(memberRequestDTO.getPassword()));

        Member member = Member.builder()
                .username(memberRequestDTO.getUsername())
                .password(memberRequestDTO.getPassword())
                .name(memberRequestDTO.getName())
                .email(memberRequestDTO.getEmail())
                .nickname(memberRequestDTO.getNickname())
                .role(Role.ROLE_MEMBER)
                .build();

        return memberRepository.save(member).getId();
    }

    private void validateDuplicateMember(MemberRequestDTO memberRequestDTO) {
        Optional<Member> find_member = memberRepository.findByUsername(memberRequestDTO.getUsername());
        if (!find_member.isEmpty())
            throw new IllegalStateException("username duplicate");
    }


    public MemberResponseDTO login(MemberRequestDTO request) throws BadCredentialsException {
        Optional<Member> member = memberRepository.findByUsername(request.getUsername());
        if (member.isEmpty())
            throw new BadCredentialsException("username false");

        if (!passwordEncoder.matches(request.getPassword(), member.get().getPassword()))
            throw new BadCredentialsException("password false");

        return MemberResponseDTO.builder()
                .username(member.get().getUsername())
                .name(member.get().getName())
                .email(member.get().getEmail())
                .nickname(member.get().getNickname())
                .role(member.get().getRole())
                .token(TokenDTO.builder()
                        .token(jwtProvider.createToken(member.get().getUsername(), member.get().getRole())
                        ).build())
                .build();
    }


    public Boolean logout(Member member) throws Exception {

        Optional<Member> find_member = memberRepository.findByUsername(member.getUsername());

        if (find_member.isEmpty())
            throw new NoSuchProviderException("No user");

        return true;

    }

    public boolean IsEquals(Member member, Member writer) {
        return member.getId().equals(writer.getId());
    }

    public Member findMemberById(long id) {
        return memberRepository.findById(id).get();
    }

    public List<RankDTO> rank() {
        List<Member> allOrderByCount = memberRepository.findAllOrderByCount();
        return allOrderByCount.stream()
                .map(Member -> RankDTO.builder()
                        .nickname(Member.getNickname())
                        .count(Member.getCount())
                        .build())
                .collect(Collectors.toList());
    }


    public ProfileResponseDTO showProfile(Member member) {
        Optional<Member> byUsername = memberRepository.findById(member.getId());


        Set<Long> correctChallengeId = byUsername.get().getAttempts().stream()
                .filter(attempt -> CodeStatus.SUCCESS.equals(attempt.getCodeStatus()))
                .map(attempt -> attempt.getChallengeId().getId())
                .collect(Collectors.toCollection(TreeSet::new));

        Set<Long> allChallengeId = byUsername.get().getAttempts().stream()
                .map(attempt -> attempt.getChallengeId().getId())
                .collect(Collectors.toCollection(TreeSet::new));

        Set<Long> failChallengeId = allChallengeId.stream()
                .filter(id -> !correctChallengeId.contains(id))
                .collect(Collectors.toCollection(TreeSet::new));

        int allCount = allChallengeId.size();
        int coCount = correctChallengeId.size();
        int incoCount = failChallengeId.size();

        return ProfileResponseDTO.builder()
                .attemptCount(allCount)
                .correctCount(coCount)
                .incorrectCount(incoCount)
                .allChallengeId(allChallengeId)
                .correctChallengeId(correctChallengeId)
                .failChallengeId(failChallengeId)
                .build();
    }

    public List<AttemptDTO> showAllChallenge(Member member, int challengeId) {
        Optional<Member> byUsername = memberRepository.findByUsername(member.getUsername());

        return byUsername.map(member1 -> member1.getAttempts().stream()
                .filter(attempt -> attempt.getChallengeId().getId() == challengeId)
                .map(attempt -> {
                    AttemptDTO attemptDTO = new AttemptDTO();
                    attemptDTO.setChallengeId(attempt.getChallengeId().getId());
                    attemptDTO.setCode(attempt.getCode());
                    attemptDTO.setCodeStatus(attempt.getCodeStatus());
                    return attemptDTO;
                })
                .collect(Collectors.toList())
        ).orElse(Collections.emptyList());
    }

    @Transactional
    public MemberResponseNotTokenDTO postProfile(ProfilePostDTO profilePostDTO, Member member) {
        Optional<Member> member1 = memberRepository.findById(member.getId());

        if (!passwordEncoder.matches(profilePostDTO.getCurrentPW(), member1.get().getPassword()))
            throw new BadCredentialsException("password false");

        if (!profilePostDTO.getNewPW().isEmpty()) {
            profilePostDTO.setNewPW(passwordEncoder.encode(profilePostDTO.getNewPW()));
            member1.get().updateData(profilePostDTO);
        } else {
            ProfilePostNotPWDTO profilePostNotPWDTO = ProfilePostNotPWDTO.builder()
                    .nickname(profilePostDTO.getNickname())
                    .build();
            member1.get().updateData(profilePostNotPWDTO);
        }

        return MemberResponseNotTokenDTO.builder()
                .username(member1.get().getUsername())
                .name(member1.get().getName())
                .email(member1.get().getEmail())
                .nickname(member1.get().getNickname())
                .role(member1.get().getRole())
                .build();
    }


    @Transactional
    public Long joinAdmin(MemberRequestDTO memberRequestDTO) {

        memberRequestDTO.setPassword(passwordEncoder.encode(memberRequestDTO.getPassword()));

        Member member = Member.builder()
                .username(memberRequestDTO.getUsername())
                .password(memberRequestDTO.getPassword())
                .name(memberRequestDTO.getName())
                .email(memberRequestDTO.getEmail())
                .nickname(memberRequestDTO.getNickname())
                .role(Role.ROLE_ADMIN)
                .build();

        return memberRepository.save(member).getId();
    }

    public boolean checkNickname(String nickname) {
        Optional<Member> byNickname = memberRepository.findByNickname(nickname);
        if (!byNickname.isEmpty())
            throw new IllegalStateException("nickname duplicate");
        return true;
    }
}
