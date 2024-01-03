package sogong.ctf.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ChallengeSaveDTO {
    @NotNull
    private String title;
    @NotNull
    private String content;
    private long categoryId;
    @NotNull
    private float time;
    @NotNull
    private float memory;
    private List<MultipartFile> files;
    private String hint;
    @NotNull
    private String input1;
    @NotNull
    private String output1;
    @NotNull
    private String input2;
    @NotNull
    private String output2;
    @NotNull
    private String input3;
    @NotNull
    private String output3;

    @Builder
    public ChallengeSaveDTO(String title, String content, long categoryId, float time, float memory, List<MultipartFile> files, String hint, String input1, String output1, String input2, String output2, String input3, String output3) {
        this.title = title;
        this.content = content;
        this.categoryId = categoryId;
        this.time = time;
        this.memory = memory;
        this.files = files;
        this.hint = hint;
        this.input1 = input1;
        this.output1 = output1;
        this.input2 = input2;
        this.output2 = output2;
        this.input3 = input3;
        this.output3 = output3;
    }

    public void setFiles(List<MultipartFile> files) {
        if (files == null) {
            this.files = new ArrayList<>();
        }
        this.files = files;
    }

}
