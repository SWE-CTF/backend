package sogong.ctf.fixture;

import sogong.ctf.domain.Challenge;

import static sogong.ctf.fixture.CategoryFixture.DFS;
import static sogong.ctf.fixture.MemberFixture.MEMBER;

public class ChallengeFixture {
    public static final Challenge DFS_CHALLENGE = new Challenge(
            1L,
            "근력 운동",
            "모든 부위를 운동할 수 있는 최소 분할을 구하시오.",
            DFS,
            2f,
            128f,
            MEMBER,
            "none",
            false
    );

}
