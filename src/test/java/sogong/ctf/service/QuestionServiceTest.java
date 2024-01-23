package sogong.ctf.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;
import sogong.ctf.domain.Question;
import sogong.ctf.dto.request.QuestionSaveDTO;
import sogong.ctf.dto.response.QuestionPagingDTO;
import sogong.ctf.exception.QuestionNotFoundException;
import sogong.ctf.repository.QuestionRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static sogong.ctf.fixture.ChallengeFixture.DFS_CHALLENGE;
import static sogong.ctf.fixture.MemberFixture.MEMBER;
import static sogong.ctf.fixture.QuestionFixture.BFS_QUESTION;
import static sogong.ctf.fixture.QuestionFixture.DP_QUESTION;

@Transactional
@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {
    @InjectMocks
    private QuestionService questionService;
    @Mock
    private QuestionRepository questionRepository;

    @DisplayName("질문을 저장한 후 questionId를 반환한다")
    @Test
    void save() {
        // given
        final QuestionSaveDTO questionSaveDTO = QuestionSaveDTO.builder()
                .title("질문 제목")
                .content("질문 내용")
                .build();

        given(questionRepository.save(any(Question.class)))
                .willReturn(BFS_QUESTION);

        // when
        long save = questionService.save(MEMBER, questionSaveDTO, DFS_CHALLENGE);

        // then
        assertThat(save).isEqualTo(1L);
    }

    @DisplayName("올바르지 않은 questionId를 입력받으면 예외가 발생한다")
    @Test
    void findByQuestionId_NotExistQuestionIdFail() {
        // given
        final Long invalidQuestionId = 2L;
        given(questionRepository.findById(invalidQuestionId))
                .willReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> questionService.findByQuestionId(invalidQuestionId))
                .isInstanceOf(QuestionNotFoundException.class);

    }

    @DisplayName("memberId와 questionId로 질문이 존재하는 지 검증한다.")
    @Test
    void validateQuestionByMember() {
        // given
        given(questionRepository.existsByMemberIdAndId(1L, 1L)).willReturn(true);

        // when
        questionService.validateQuestionByMember(1L, 1L);

        // then
        verify(questionRepository).existsByMemberIdAndId(anyLong(), anyLong());
    }
    @DisplayName("질문글을 삭제한다")
    @Test
    void delete(){
        // given
        given(questionRepository.findById(anyLong()))
                .willReturn(Optional.of(BFS_QUESTION));

        // when
        questionService.delete(1L);

        // then
        verify(questionRepository).delete(any());
    }

    @DisplayName("질문글의 내용을 수정한다")
    @Test
    void update() {
        // given
        final String updateTitle = "글 수정";
        final String updateContent = "글을 수정합니다";
        QuestionSaveDTO updateDTO = QuestionSaveDTO.builder()
                .title(updateTitle)
                .content(updateContent)
                .build();

        given(questionRepository.findById(anyLong()))
                .willReturn(Optional.of(BFS_QUESTION));

        // when
        questionService.update(1L, updateDTO);

        // then
        Question question = questionRepository.findById(1L).get();
        assertThat(question.getTitle()).isEqualTo(updateTitle);
        assertThat(question.getContent()).isEqualTo(updateContent);
    }

    @DisplayName("문제에 해당하는 모든 질문의 DTO을 반환한다")
    @Test
    void GetQuestionsByChallengeId() {
        // given
        given(questionRepository.findAllByChallengeId(1L))
                .willReturn(List.of(BFS_QUESTION,DP_QUESTION));

        // when
        List<QuestionPagingDTO> questions = questionService.getQuestionsByChallengeId(1L);

        // then
        assertThat(questions.size()).isEqualTo(2);
    }

}
