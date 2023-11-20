package sogong.ctf.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import sogong.ctf.domain.Challenge;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class ChallengeResponseDTO {
    private Long id;
    private String title;
    private String content;
    private int correctCnt;
    private float memory;
    private float time;
    private String hint;
    private String examiner;
    private String category;
    private List<byte[]> files;
    private List<TestCaseDTO> testcases;

    public static ChallengeResponseDTO toDTO(Challenge challenge, List<TestCaseDTO> testCases, List<byte[]> files){
        return ChallengeResponseDTO.builder()
                .id(challenge.getId())
                .title(challenge.getTitle())
                .content(challenge.getContent())
                .correctCnt(challenge.getCorrectCnt())
                .hint(challenge.getHint())
                .examiner(challenge.getExaminer().getUsername())
                .memory(challenge.getMemory())
                .time(challenge.getTime())
                .testcases(testCases)
                .files(files)
                .category((challenge.getCategoryId()==null)?"":challenge.getCategoryId().getName())
                .build();
    }
}
