package sogong.ctf.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sogong.ctf.MemberFormDTO;
import sogong.ctf.domain.Member;
import sogong.ctf.domain.Role;
import sogong.ctf.repository.MemberRepository;

import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long join(MemberFormDTO memberFormDTO){

        validateDuplicateMember(memberFormDTO);
        memberFormDTO.setPassword(passwordEncoder.encode(memberFormDTO.getPassword()));

        Member member = Member.builder()
                .username(memberFormDTO.getUsername())
                .password(memberFormDTO.getPassword())
                .name(memberFormDTO.getName())
                .email(memberFormDTO.getEmail())
                .role(Role.ROLE_MEMBER)
                .build();

        return memberRepository.save(member).getId();
    }

    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    public Optional<Member> findOne(Long id){
        return memberRepository.findById(id);
    }

    private void validateDuplicateMember(MemberFormDTO memberFormDTO){
        Optional<Member> find_member = memberRepository.findByUsername(memberFormDTO.getUsername());
        if(!find_member.isEmpty())
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> find_member = memberRepository.findByUsername(username);
        if(find_member.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }

        return User.builder()
                .username(find_member.get().getUsername())
                .password(find_member.get().getPassword())
                .roles(find_member.get().getRole().getValue())
                .build();
    }
}
