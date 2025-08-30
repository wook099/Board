package pr1.board.exceptionhandler.exception;

public class AuthCodeExpiredException extends RuntimeException {
    public AuthCodeExpiredException(String message) {
        super(message);
    }
}
