package sogong.ctf.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfilePostDTO {

    private String nickname;
    private String currentPW;
    private String newPW;
    private String checkNewPW;
}
