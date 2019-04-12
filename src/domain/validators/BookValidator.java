package domain.validators;
import domain.Book;

/**
 * @author radu.
 */
public class BookValidator implements Validator<Book> {
    @Override
    public void validate(Book entity) throws ValidatorException {
        //TODO validate client
        if (entity != null)
            return;
        else throw new ValidatorException("Client can not be null.");
    }
}
