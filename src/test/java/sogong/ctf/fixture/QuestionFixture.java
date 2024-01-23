package sogong.ctf.fixture;

import sogong.ctf.domain.Question;

import java.time.LocalDateTime;

import static sogong.ctf.fixture.ChallengeFixture.DFS_CHALLENGE;
import static sogong.ctf.fixture.MemberFixture.MEMBER;

public class QuestionFixture {
    public static final Question BFS_QUESTION = new Question(
            1L,
            "bfs로 풀이하였는데..",
            "30%만 맞다고 뜨네요.",
            DFS_CHALLENGE,
            MEMBER,
            LocalDateTime.now()
    );
    public static final Question DP_QUESTION = new Question(
            2L,
                    "dp로 푸는 법",
                    "이 문제 DP로 풀 수 있을까요?",
            DFS_CHALLENGE,
            MEMBER,
            LocalDateTime.now()
    );
}
