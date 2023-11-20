package sogong.ctf.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sogong.ctf.config.security.JwtProvider;
import sogong.ctf.domain.Member;
import sogong.ctf.domain.Role;
import sogong.ctf.dto.request.MemberRequestDTO;
import sogong.ctf.dto.response.MemberResponseDTO;
import sogong.ctf.dto.response.TokenDTO;
import sogong.ctf.repository.MemberRepository;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.ValidationException;
import java.security.NoSuchProviderException;
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
                .token(TokenDTO.builder()
                        .token(jwtProvider.createToken(member.get().getUsername(), member.get().getRole())
                        ).build())
                .build();
    }


    public Boolean logout(HttpServletRequest httpServletRequest) throws Exception {

        String token = jwtProvider.resolveToken(httpServletRequest);

        TokenDTO tokenDTO = TokenDTO.builder()
                .token(token)
                .build();

        if (!jwtProvider.validateToken(tokenDTO.getToken().toString()))
            throw new ValidationException("Validation Out");

        tokenDTO.setToken(token.substring(7));

        String username = jwtProvider.getUsername(tokenDTO.getToken().toString());
        Optional<Member> find_member = memberRepository.findByUsername(username);

        if (find_member.isEmpty())
            throw new NoSuchProviderException("No user");

        return true;

    }
    public boolean IsEquals(Member member,Member writer){
        return member.getId().equals(writer.getId());
    }
    public Member findMemberById(long id){
        return memberRepository.findById(id).get();
    }
}
