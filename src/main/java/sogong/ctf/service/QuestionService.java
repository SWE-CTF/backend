package sogong.ctf.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sogong.ctf.domain.Challenge;
import sogong.ctf.domain.Member;
import sogong.ctf.domain.Question;
import sogong.ctf.dto.request.QuestionSaveDTO;
import sogong.ctf.dto.response.QuestionPagingDTO;
import sogong.ctf.dto.response.QuestionResponseDTO;
import sogong.ctf.exception.ErrorCode;
import sogong.ctf.exception.QuestionNotFoundException;
import sogong.ctf.repository.QuestionRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    @Transactional
    public long save(Member member, QuestionSaveDTO saveForm, Challenge challenge) {

        Question q = Question.builder().title(saveForm.getTitle())
                .challenge(challenge)
                .content(saveForm.getContent())
                .memberId(member)
                .writeTime(LocalDateTime.now())
                .build();

        Question save = questionRepository.save(q);
        return save.getId();
    }

    public Question findByQuestionId(Long questionId) {
        return questionRepository.findById(questionId).orElseThrow(() -> new QuestionNotFoundException(ErrorCode.CHALLENGE_NOT_EXIST));
    }


    public void validateQuestionByMember(final Long member, final Long questionId) {
        if (!questionRepository.existsByMemberIdAndId(member, questionId))
            throw new AccessDeniedException("질문 글 작성자와 일치하지 않는 사용자입니다");
    }

    public QuestionResponseDTO getDetails(Long questionId) {
        Question q = findByQuestionId(questionId);
        return QuestionResponseDTO.toQuestionResponseDTO(q);
    }

    @Transactional
    public void delete(Long questionId) {
        Question q = findByQuestionId(questionId);
        questionRepository.delete(q);
    }

    @Transactional
    public void update(Long questionId, QuestionSaveDTO questionSaveDTO) {
        Question q = findByQuestionId(questionId);
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
        return questionRepository.findAll(PageRequest.of(p, pageLimit, Sort.by(Sort.Direction.DESC, "id")));
    }

    public List<QuestionPagingDTO> getAllQuestion() {
        List<Question> questionList = questionRepository.findAll();
        return questionList.stream()
                .map(question -> QuestionPagingDTO.builder()
                        .title(question.getTitle())
                        .questionId(question.getId())
                        .writeTime(question.getWriteTime())
                        .nickname(question.getMember().getNickname())
                        .build()
                ).collect(Collectors.toList());
    }

    public List<QuestionPagingDTO> getQuestionsByChallengeId(Long challengeId) {
        List<Question> questionList = questionRepository.findAllByChallengeId(challengeId);
        return questionList.stream().map(question -> QuestionPagingDTO.builder()
                .title(question.getTitle())
                .questionId(question.getId())
                .writeTime(question.getWriteTime())
                .nickname(question.getMember().getNickname())
                .build()
        ).collect(Collectors.toList());
    }
}
