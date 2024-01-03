package sogong.ctf.exception;

public class ChallengeNotFoundException extends BusinessException{

    public ChallengeNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
