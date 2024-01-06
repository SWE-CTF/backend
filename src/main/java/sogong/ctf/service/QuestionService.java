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
    private final MemberService memberService;
    private final ChallengeService challengeService;

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

    public Question findByQuestionId(long questionId) {
        return questionRepository.findById(questionId).orElseThrow(()->new QuestionNotFoundException(ErrorCode.CHALLENGE_NOT_EXIST));
    }

    private Member findWriter(long questionId) {
        Member writer = findByQuestionId(questionId).getMemberId();
        return writer;
    }

    public QuestionResponseDTO getDetails(long questionId) {
        Question q = findByQuestionId(questionId);
        return QuestionResponseDTO.toQuestionResponseDTO(q);
    }

    @Transactional
    public void delete(long questionId, Member member) {
        Question q = findByQuestionId(questionId);
        Member writer = findWriter(questionId);
        if (memberService.IsEquals(member, writer)) { // 글 작성자와 지우려고 하는 사람 일치 여부 확인
            questionRepository.delete(q);
        } else {
            throw new AccessDeniedException("질문 글 작성자와 일치하지 않는 사용자입니다");
        }
    }

    @Transactional
    public void update(long questionId, QuestionSaveDTO questionSaveDTO, Member member) {
        Question q = findByQuestionId(questionId);
        Member writer = findWriter(questionId);
        if (memberService.IsEquals(member, writer)) {
            q.updateQuestion(questionSaveDTO.getTitle(), questionSaveDTO.getContent());
        } else {
            throw new AccessDeniedException("질문 글 작성자와 일치하지 않는 사용자입니다");
        }
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
                        .nickname(question.getMemberId().getNickname())
                        .build()
                ).collect(Collectors.toList());
    }

    public List<QuestionPagingDTO> getQuestionsByChallengeId(long challengeId) {
        Challenge challenge = challengeService.findByChallengeId(challengeId);
        List<Question> questionList = questionRepository.findAllByChallengeId(challenge);
        return questionList.stream().map(question -> QuestionPagingDTO.builder()
                .title(question.getTitle())
                .questionId(question.getId())
                .writeTime(question.getWriteTime())
                .nickname(question.getMemberId().getNickname())
                .build()
        ).collect(Collectors.toList());
    }
}
