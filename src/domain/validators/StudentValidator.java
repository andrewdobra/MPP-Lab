package src.domain.validators;
import src.domain.Client;

/**
 * @author radu.
 */
public class ClientValidator implements Validator<Client> {
    @Override
    public void validate(Client entity) throws ValidatorException {
        //TODO validate client
        if (entity != null)
            return;
        else throw ValidatorException;
    }
}
