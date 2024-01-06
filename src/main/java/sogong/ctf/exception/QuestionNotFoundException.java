package sogong.ctf.exception;

public class QuestionNotFoundException extends BusinessException{
    public QuestionNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
