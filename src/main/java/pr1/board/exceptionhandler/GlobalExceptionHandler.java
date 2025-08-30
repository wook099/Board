package pr1.board.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pr1.board.exceptionhandler.exception.AuthCodeExpiredException;
import pr1.board.exceptionhandler.exception.AuthCodeMismatchException;
import pr1.board.exceptionhandler.exception.AuthCodeSendException;

//@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({AuthCodeSendException.class})
    public ResponseEntity<String> handleSendError(AuthCodeSendException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    @ExceptionHandler({AuthCodeExpiredException.class, AuthCodeMismatchException.class})
    public ResponseEntity<String> handleVerifyError(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleOtherErrors(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 에러: " + e.getMessage());
    }
}
