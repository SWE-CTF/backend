package sogong.ctf.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sogong.ctf.config.security.JwtProvider;
import sogong.ctf.domain.Member;
import sogong.ctf.domain.Role;
import sogong.ctf.dto.MemberRequestDTO;
import sogong.ctf.dto.MemberResponseDTO;
import sogong.ctf.dto.TokenDTO;
import sogong.ctf.repository.MemberRepository;

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
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
    }

    public Optional<Member> findOne(Long id) {
        return memberRepository.findById(id);
    }

    @Transactional
    public MemberResponseDTO login(MemberRequestDTO request) throws BadCredentialsException {
        Optional<Member> member = memberRepository.findByUsername(request.getUsername());
        if (member.isEmpty())
            throw new BadCredentialsException("아이디가 틀렸습니다.");

        if (!passwordEncoder.matches(request.getPassword(), member.get().getPassword()))
            throw new BadCredentialsException("비번이 틀렸습니다.");

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

    @Transactional
    public Boolean logout(TokenDTO tokenDTO) {
        String username = jwtProvider.getUsername(tokenDTO.getToken().toString());
        Optional<Member> find_member = memberRepository.findByUsername(username);

        if (find_member.isEmpty())
            return false;

        return true;
    }


}
