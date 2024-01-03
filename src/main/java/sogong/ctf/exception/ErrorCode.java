package sogong.ctf.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // common
    INVALID_INPUT_VALUE(400, " Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, " Invalid Input Value"),
    ENTITY_NOT_FOUND(400, " Entity Not Found"),
    INTERNAL_SERVER_ERROR(500, "Server Error"),
    INVALID_TYPE_VALUE(400, " Invalid Type Value"),
    HANDLE_UNAUTHORIZED_USER(401,"인증되지 않은 사용자입니다."),
    HANDLE_ACCESS_DENIED(403, "Access is Denied"),
    // challenge
    CATEGORY_NOT_EXIST(400, "No Such Category"),
    CHALLENGE_NOT_EXIST(400, "No Such Challenge");


    private final int status;
    private final String msg;

    ErrorCode(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }
}
