package sogong.ctf.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sogong.ctf.domain.Challenge;
import sogong.ctf.domain.Member;
import sogong.ctf.domain.Question;
import sogong.ctf.dto.QuestionPagingDTO;
import sogong.ctf.dto.QuestionResponseDTO;
import sogong.ctf.dto.QuestionSaveDTO;
import sogong.ctf.repository.QuestionRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    @Transactional
    public long save(Member member, QuestionSaveDTO saveForm, Challenge challenge) {

        Question q = Question.builder().title(saveForm.getTitle())
                .challengeId(challenge)
                .content(saveForm.getContent())
                .memberId(member)
                .writeTime(LocalDateTime.now())
                .build();

        Question save = questionRepository.save(q);
        return save.getId();
    }

    public Optional<Question> findByQuestionId(long questionId) {
        return questionRepository.findById(questionId);
    }

    public Member findWriter(long questionId) {
        Member writer = findByQuestionId(questionId).get().getMemberId();
        return writer;
    }

    public QuestionResponseDTO getDetails(long questionId) {
        Optional<Question> q = findByQuestionId(questionId);
        if (q.isEmpty()) return null;
        return QuestionResponseDTO.toQuestionResponseDTO(q.get());
    }

    @Transactional
    public void delete(long questionId) {
        Question q = findByQuestionId(questionId).get();
        questionRepository.delete(q);
    }

    @Transactional
    public void update(long questionId, QuestionSaveDTO questionSaveDTO) {
        Question q = findByQuestionId(questionId).orElseThrow(() -> new NoSuchElementException());
        q.updateQuestion(questionSaveDTO.getTitle(), questionSaveDTO.getContent());
    }

    public List<QuestionPagingDTO> paging(Pageable page) {
        Page<Question> questions = findAllPage(page);
        List<QuestionPagingDTO> pagingList = new ArrayList<>();
        for (Question question : questions) {
            pagingList.add(QuestionPagingDTO.toDTO(question));
        }
        return pagingList;
    }

    private Page<Question> findAllPage(Pageable page) {
        int p = page.getPageNumber() - 1;
        int pageLimit = 10;
        Page<Question> questions = questionRepository.findAll(PageRequest.of(p, pageLimit, Sort.by(Sort.Direction.DESC, "id")));
        return questions;
    }


    public int getTotalPage(Pageable page) {
        Page<Question> questions = findAllPage(page);
        return questions.getTotalPages();
    }

}
