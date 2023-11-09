package sogong.ctf.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@RequiredArgsConstructor

public class ChallengeSaveDTO {
    private String title;
    private String content;
    private float time;
    private float memory;
    private List<MultipartFile> files;
    private String hint;

    @Builder
    public ChallengeSaveDTO(String title, String content, float time, float memory, List<MultipartFile> files, String hint) {
        this.title = title;
        this.content = content;
        this.time = time;
        this.memory = memory;
        this.files = files;
        this.hint = hint;
    }

    public void setFiles(List<MultipartFile> files) {
        this.files = files;
    }
    //입력 배열
    //출력 배열

}
