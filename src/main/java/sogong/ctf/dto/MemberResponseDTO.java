package sogong.ctf.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sogong.ctf.domain.Member;
import sogong.ctf.domain.Role;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDTO {
    private String username;
    private String name;
    private String email;
    private String nickname;
    private TokenDTO token;

    @Builder
    public MemberResponseDTO(Member member){
        this.username = member.getUsername();
        this.name = member.getName();
        this.email = member.getEmail();
        this.nickname = member.getNickname();
    }
}
