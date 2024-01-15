package sogong.ctf.exception;

public class NoticeNotFoundException extends BusinessException{
    public NoticeNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
