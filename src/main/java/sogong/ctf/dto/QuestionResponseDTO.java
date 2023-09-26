package sogong.ctf.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import sogong.ctf.domain.Question;

import java.time.LocalDateTime;
@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class QuestionResponseDTO {
    String title;
    String content;
    String writer;
    LocalDateTime write_time;

    public QuestionResponseDTO(String title, String content, String writer, LocalDateTime write_time) {
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.write_time = write_time;
    }

    public static QuestionResponseDTO toQuestionResponseDTO(Question q, String nickname){
        return QuestionResponseDTO.builder()
                .title(q.getTitle())
                .content(q.getContent())
                .writer(nickname)
                .write_time(q.getWriteTime())
                .build();
    }
}
