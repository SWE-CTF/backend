package sogong.ctf.exception;

public class CategoryNotFoundException extends BusinessException {

    public CategoryNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
