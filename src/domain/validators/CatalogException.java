package src.domain.validators;

/**
 * Created by radu.
 */
public class BookstoreException extends RuntimeException{

    public BookstoreException(String message) {
        super(message);
    }

    public BookstoreException(String message, Throwable cause) {
        super(message, cause);
    }

    public BookstoreException(Throwable cause) {
        super(cause);
    }
}
