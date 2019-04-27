package courageous.webservices.exceptions;

public class NotFoundException extends BaseException {
	private static final long serialVersionUID = -6191446333699535446L;

    public NotFoundException(String message) {
        super(404, message);
    }
}