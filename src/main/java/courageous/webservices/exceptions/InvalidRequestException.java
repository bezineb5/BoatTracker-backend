package courageous.webservices.exceptions;

public class InvalidRequestException extends BaseException {
    private static final long serialVersionUID = 1L;

    public InvalidRequestException(String message) {
        super(400, message);
    }
}