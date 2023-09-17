package sogong.ctf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberFormDTO {

    private String username;
    private String password;
    private String name;
    private String email;

}
