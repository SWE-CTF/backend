package sogong.ctf.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequestDTO {

    private String username;
    private String password;
    private String name;
    private String email;
    private String nickname;

}
