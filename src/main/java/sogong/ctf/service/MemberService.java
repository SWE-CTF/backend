package sogong.ctf.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sogong.ctf.config.security.JwtProvider;
import sogong.ctf.domain.Member;
import sogong.ctf.domain.Role;
import sogong.ctf.dto.MemberPasswordDTO;
import sogong.ctf.dto.MemberRequestDTO;
import sogong.ctf.dto.MemberResponseDTO;
import sogong.ctf.dto.TokenDTO;
import sogong.ctf.repository.MemberRepository;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.ValidationException;
import java.security.NoSuchProviderException;
import java.util.List;
import java.util.Optional;


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


    public TokenDTO login(MemberRequestDTO request) throws BadCredentialsException {
        Optional<Member> member = memberRepository.findByUsername(request.getUsername());
        if (member.isEmpty())
            throw new BadCredentialsException("username false");

        if (!passwordEncoder.matches(request.getPassword(), member.get().getPassword()))
            throw new BadCredentialsException("password false");

        return TokenDTO.builder()
                .token(jwtProvider.createToken(member.get().getUsername(),member.get().getRole()))
                .build();
    }


    public Boolean logout(Member member) throws Exception {

        Optional<Member> find_member = memberRepository.findByUsername(member.getUsername());

        if (find_member.isEmpty())
            throw new NoSuchProviderException("No user");

        return true;

    }
    public boolean IsEquals(Member member,Member writer){
        return member.getId().equals(writer.getId());
    }

    @Transactional
    public void increaseCount(Long id){
        Optional<Member> member = memberRepository.findById(id);
        member.get().addCount();
    }

    public List<String> rank(){
        List<String> allOrderByCount = memberRepository.findAllOrderByCount();
        return allOrderByCount;
    }


    public MemberResponseDTO showProfile(Member member) {
        return MemberResponseDTO.builder()
                .username(member.getUsername())
                .name(member.getName())
                .nickname(member.getNickname())
                .team(member.getTeam())
                .email(member.getEmail())
                .build();
    }

    @Transactional
    public MemberResponseDTO postProfile(MemberResponseDTO memberResponseDTO, Member member) {
        Optional<Member> member1 = memberRepository.findById(member.getId());
        member1.get().updateData(memberResponseDTO);
        return memberResponseDTO;
    }

    public boolean checkPassword(MemberPasswordDTO memberPasswordDTO, Member member) {

        if (!passwordEncoder.matches(memberPasswordDTO.getPassword(), member.getPassword()))
            throw new BadCredentialsException("password false");

        return true;
    }

    @Transactional
    public boolean updatePassword(MemberPasswordDTO memberPasswordDTO, Member member) {
        Optional<Member> member1 = memberRepository.findById(member.getId());
        member1.get().updatePW(passwordEncoder.encode(memberPasswordDTO.getPassword()));
        return true;
    }
}
