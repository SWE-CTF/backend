package sogong.ctf.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class NoticeSaveDTO {
    @NotNull
    private String title;
    @NotNull
    private String content;
}
