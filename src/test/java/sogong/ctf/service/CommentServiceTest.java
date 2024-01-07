package sogong.ctf.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;
import sogong.ctf.config.security.CustomMemberDetails;
import sogong.ctf.domain.*;
import sogong.ctf.mockConfig.WithCustomMockUser;
import sogong.ctf.repository.ChallengeRepository;
import sogong.ctf.repository.CommentRepository;
import sogong.ctf.repository.MemberRepository;
import sogong.ctf.repository.QuestionRepository;


import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class CommentServiceTest {
    @Autowired
    private QuestionCommentService commentService;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private ChallengeRepository challengeRepository;

    private Member tester;

    private Question question;

    @BeforeEach
    void setup() {
        //사용자 저장
        tester = memberRepository.save(Member.builder()
                .name("test")
                .role(Role.ROLE_MEMBER).build());
        // 문제 저장
        Challenge challenge = challengeRepository.save(Challenge.builder()
                .title("은주오의 덧셈 계산기")
                .content("입력값 A,B가 주어지면 덧셈 결과를 출력하세요.")
                .memory(128f)
                .time(6f)
                .examiner(tester)
                .fileExist(false)
                .build());
        // 질문 저장
        question = questionRepository.save(Question.builder()
                .memberId(tester)
                .title("댓글 서비스 테스트")
                .content("내용")
                .challengeId(challenge)
                .build());
    }

    @Test
    @DisplayName("사용자는 질문 게시글에 댓글을 작성할 수 있다")
    void saveComment() {
        // given
        String content = "댓글 내용";
        // when
        long save = commentService.save(tester, question.getId(), content);
        // then
        Comment findComment = commentService.findByCommentId(save);
        assertThat(save).isEqualTo(findComment.getId());
    }

    @Test
    @DisplayName("댓글 작성자는 댓글을 수정할 수 있다")
    void updateSuc() {
        // given
        Comment save = commentRepository.save(Comment.builder()
                .writer(tester)
                .questionId(question)
                .content("댓글")
                .writeTime(LocalDateTime.now())
                .build()
        );
        String update = "댓글 수정";
        // when
        commentService.update(save.getId(), update, tester);
        // then
        Comment find = commentRepository.findById(save.getId()).get();
        assertThat(find.getContent()).isEqualTo(update);

    }

    @Test
    @WithCustomMockUser
    @DisplayName("댓글 작성자가 아닌 사용자는 댓글을 수정할 수 없다")
    void updateFail() {
        // given
        Comment save = commentRepository.save(Comment.builder()
                .writer(tester)
                .questionId(question)
                .content("댓글")
                .writeTime(LocalDateTime.now())
                .build()
        );
        String update = "댓글 수정";

        CustomMemberDetails principal = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member user = principal.getMember();
        ReflectionTestUtils.setField(user, "id", 2L);

        // when, then
        Assertions.assertThrows(AccessDeniedException.class, () -> commentService.update(save.getId(), update, user));
    }

    @Test
    @DisplayName("댓글 작성자는 댓글을 수정할 수 없다")
    void deleteSucc() {
        // given
        Comment save = commentRepository.save(Comment.builder()
                .writer(tester)
                .questionId(question)
                .content("댓글")
                .writeTime(LocalDateTime.now())
                .build()
        );
        // when
        commentService.delete(save.getId(), tester);
        // then
        Optional<Comment> find = commentRepository.findById(save.getId());
        assertThat(find).isEmpty();
    }

    @Test
    @WithCustomMockUser
    @DisplayName("댓글 작성자가 아닌 사용자는 댓글을 삭제할 수 없다")
    void deleteFail() {
        // given
        Comment save = commentRepository.save(Comment.builder()
                .writer(tester)
                .questionId(question)
                .content("댓글")
                .writeTime(LocalDateTime.now())
                .build()
        );

        CustomMemberDetails principal = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member user = principal.getMember();
        ReflectionTestUtils.setField(user, "id", 2L);
        // when, then
        Assertions.assertThrows(AccessDeniedException.class, () -> commentService.delete(save.getId(), user));
    }

    @Test
    @DisplayName("질문 작성자는 댓글을 채택할 수 있다")
    void adoptSucc() {
        // given
        Comment save = commentRepository.save(Comment.builder()
                .writer(tester)
                .questionId(question)
                .content("댓글")
                .writeTime(LocalDateTime.now())
                .build()
        ); // 채택할 댓글

        // when
        commentService.adopt(save.getId(), tester);
        // then
        Comment adoptedComment = questionRepository.findById(question.getId()).get().getAdoptedComment();
        assertThat(adoptedComment.getId()).isEqualTo(save.getId());
    }

    @Test
    @WithCustomMockUser
    @DisplayName("질문 작성자가 아닌 사용자는 댓글을 채택할 수 없다")
    void adoptFail() {
        // given
        Comment save = commentRepository.save(Comment.builder()
                .writer(tester)
                .questionId(question)
                .content("댓글")
                .writeTime(LocalDateTime.now())
                .build()
        );

        CustomMemberDetails principal = (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member user = principal.getMember();
        ReflectionTestUtils.setField(user, "id", 2L);
        // when, then
        Assertions.assertThrows(AccessDeniedException.class, () -> commentService.adopt(save.getId(), user));
    }


}