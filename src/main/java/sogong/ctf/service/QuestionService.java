package sogong.ctf.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sogong.ctf.domain.Challenge;
import sogong.ctf.domain.Member;
import sogong.ctf.domain.Question;
import sogong.ctf.dto.QuestionResponseDTO;
import sogong.ctf.dto.QuestionSaveDTO;
import sogong.ctf.repository.QuestionRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    public void save(QuestionSaveDTO saveForm, Challenge challenge, Member memberId) {
        Question q = Question.builder().title(saveForm.getTitle())
                .challengeId(challenge)
                .content(saveForm.getContent())
                .memberId(memberId)
                .writeTime(LocalDateTime.now())
                .build();
        questionRepository.save(q);

    }

    public Optional<Question> findOne(long questionId) {
        if (questionRepository.findById(questionId).isEmpty()) {
            return null;
        }
        return questionRepository.findById(questionId);
    }

    public Member findWriter(long questionId) {
        Member writer = findOne(questionId).get().getMemberId();
        return writer;
    }

    public QuestionResponseDTO findQuestion(long questionId) {
        Optional<Question> q = findOne(questionId);
        if (q.isEmpty()) return null;
        return QuestionResponseDTO.toQuestionResponseDTO(q.get());
    }

    public void delete(long questionId) {
        Question q = findOne(questionId).get();
        questionRepository.delete(q);
    }

    public void put(long questionId, QuestionSaveDTO questionSaveDTO) {

    }
}
