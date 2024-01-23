package sogong.ctf.fixture;

import sogong.ctf.domain.Member;
import sogong.ctf.domain.Role;

public class MemberFixture {
    public static final Member MEMBER = new Member(
            1L,
            "testUser",
            "test1234!",
            Role.ROLE_MEMBER
    );
}
