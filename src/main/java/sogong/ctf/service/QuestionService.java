package sogong.ctf.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sogong.ctf.config.security.CustomMemberDetails;
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

    public long save(QuestionSaveDTO saveForm, Challenge challenge) {
        Member member = getAuthentication();

        Question q = Question.builder().title(saveForm.getTitle())
                .challengeId(challenge)
                .content(saveForm.getContent())
                .memberId(member)
                .writeTime(LocalDateTime.now())
                .build();
        Question save = questionRepository.save(q);
        return save.getId();
    }

    private static Member getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomMemberDetails findMember = (CustomMemberDetails) authentication.getPrincipal();//사용자 확인
        Member member = findMember.getMember();
        return member;
    }

    public Optional<Question> findOne(long questionId) {
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

    @Transactional
    public void delete(long questionId) {
        Question q = findOne(questionId).get();
        questionRepository.delete(q);
    }

    @Transactional
    public void update(long questionId, QuestionSaveDTO questionSaveDTO) {
        Question q = findOne(questionId).orElseThrow(() -> new NoSuchElementException());
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
