package sogong.ctf.exception;

public class CommentNotFoundException extends BusinessException{
    public CommentNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
