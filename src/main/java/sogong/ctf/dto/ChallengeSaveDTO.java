package sogong.ctf.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Getter
@RequiredArgsConstructor
public class ChallengeSaveDTO {
    String title;
    String content;
    float time;
    float memory;
    List<MultipartFile> files;
    String hint;
    //입력 배열
    //출력 배열

}
