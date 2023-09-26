package sogong.ctf.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sogong.ctf.domain.Challenge;
import sogong.ctf.domain.Member;
import sogong.ctf.domain.Question;
import sogong.ctf.dto.QuestionResponseDTO;
import sogong.ctf.dto.QuestionSaveDTO;
import sogong.ctf.repository.QuestionRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    public void save(QuestionSaveDTO saveForm, Challenge challenge, Member memberId) {

        Question q = new Question(saveForm.getTitle(),saveForm.getContent(),challenge,memberId);
        questionRepository.save(q);

    }

    public QuestionResponseDTO findOne(long questionId) {
        Optional<Question> find = questionRepository.findById(questionId);
        if(find.isEmpty()){
            return null;
        }
        else{
            Member member = find.get().getMemberId();
            return QuestionResponseDTO.toQuestionResponseDTO(find.get(),member.getNickname());
        }
    }
}
