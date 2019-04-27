package courageous.webservices.exceptions;

public class NotAuthorizedException extends BaseException {
	private static final long serialVersionUID = 2334014827081716920L;

    public NotAuthorizedException(String message) {
        super(403, message);
    }
}