package sogong.ctf.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sogong.ctf.domain.Role;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseNotTokenDTO {
    private String username;
    private String name;
    private String email;
    private String nickname;
    private Role role;
}

