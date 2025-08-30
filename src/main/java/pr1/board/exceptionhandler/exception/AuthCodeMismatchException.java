package pr1.board.exceptionhandler.exception;

public class AuthCodeMismatchException extends RuntimeException {
    public AuthCodeMismatchException(String message) {
        super(message);
    }
}