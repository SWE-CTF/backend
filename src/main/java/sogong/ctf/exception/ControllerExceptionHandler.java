package sogong.ctf.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@Slf4j
@RestControllerAdvice(basePackages = "sogong.ctf.controller")
public class ControllerExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        return ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    protected ErrorResponse handleAccessDeniedException(AccessDeniedException e) {
        log.error(e.getMessage(), e);
        return ErrorResponse.of(ErrorCode.HANDLE_ACCESS_DENIED);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BusinessException.class)
    protected ErrorResponse handleBusinessException(BusinessException e) {
        log.error(e.getErrorCode().getMsg(), e);
        return ErrorResponse.of(e.getErrorCode());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    protected ErrorResponse handleInternalException(Exception e) {
        log.error(e.getMessage(), e);
        return ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
    }


}
