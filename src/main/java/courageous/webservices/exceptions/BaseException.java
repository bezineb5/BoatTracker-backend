package courageous.webservices.exceptions;

public class BaseException extends Exception {
    private static final long serialVersionUID = -8794024966252013003L;
    private int httpCode;

    public BaseException(int httpCode, String message) {
        super(message);
        this.httpCode = httpCode;
    }

    public int getHttpCode() {
        return this.httpCode;
    }
}