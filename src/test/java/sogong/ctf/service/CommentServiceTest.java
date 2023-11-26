package sogong.ctf.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sogong.ctf.domain.*;
import sogong.ctf.dto.request.MemberRequestDTO;
import sogong.ctf.repository.ChallengeRepository;
import sogong.ctf.repository.QuestionRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
@ActiveProfiles("test")
@SpringBootTest
@Transactional
class CommentServiceTest {
    @Autowired
    private QuestionCommentService commentService;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private ChallengeRepository challengeRepository;
    @Autowired
    private MemberService memberService;
    private Member member1;
    private Member member2;
    private Question question;

    @BeforeEach
    void setup(){
        Long test1 = memberService.join(new MemberRequestDTO("test", "test", "test", "test", "test"));
        Long test2 = memberService.join(new MemberRequestDTO("test2", "test", "test", "test", "test"));
        member1 = memberService.findMemberById(test1);
        member2 = memberService.findMemberById(test2);
        //문제 저장
        Challenge challenge = new Challenge("challenge", "content", new Category("category"),1.0f, 1.0f, member1,"hint",false);
        challengeRepository.save(challenge);
        //질문 저장
        question = new Question("question","content", challenge,member1, LocalDateTime.now());
        questionRepository.save(question);
    }
    @Test @DisplayName("댓글 작성")
    void saveComment(){
        //given
        String content = "댓글 내용";
        //when
        long save = commentService.save(member1, question.getId(), content);
        //then
        Comment findComment = commentService.findByCommentId(save);
        assertThat(save).isEqualTo(findComment.getId());
    }
    @Test @DisplayName("댓글 수정 성공")
    void updateSuc(){
        //given
        long comment = commentService.save(member1, question.getId(), "수정 성공 케이스");
        //when
        boolean update = commentService.update(comment, "수정", member1);
        //then
        assertThat(update).isTrue();
    }
    @Test @DisplayName("댓글 수정 실패")
    void updateFail(){
        //given
        long comment = commentService.save(member1, question.getId(), "수정 성공 케이스");
        //when
        boolean update = commentService.update(comment, "수정", member2);
        //then
        assertThat(update).isFalse();
    }

    @Test @DisplayName("댓글 삭제 성공")
    void deleteSucc(){
        //given
        long comment = commentService.save(member1, question.getId(), "삭제 성공 케이스");
        //when
        boolean delete = commentService.delete(comment, member1);
        //then
        assertThat(delete).isTrue();
    }
    @Test @DisplayName("댓글 삭제 실패")
    void deleteFail(){
        //given
        long comment = commentService.save(member1, question.getId(), "삭제 실패 케이스");
        //when
        boolean delete = commentService.delete(comment, member2);
        //then
        assertThat(delete).isFalse();
    }
    @Test @DisplayName("댓글 채택")
    void adopt(){
        //when
        long comment = commentService.save(member1, question.getId(), "댓글 채택 케이스");
        //given
        commentService.adopt(comment);
        //then
        long ogQuestion = commentService.findByCommentId(comment).getQuestionId().getId();
        Question question = questionRepository.findById(ogQuestion).get();
        assertThat(comment).isEqualTo(question.getAdoptedComment().getId());
    }


}