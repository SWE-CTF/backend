package sogong.ctf.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@RequiredArgsConstructor
public class NoticeSaveDTO {
    @NotNull
    private String title;
    @NotNull
    private String content;
}
