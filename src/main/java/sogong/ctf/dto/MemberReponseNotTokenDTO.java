package sogong.ctf.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sogong.ctf.domain.Role;
import sogong.ctf.domain.Team;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberReponseNotTokenDTO {
    private String username;
    private String name;
    private String email;
    private String nickname;
    private Team team;
    private Role role;
}
