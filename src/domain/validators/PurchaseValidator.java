package domain.validators;

import domain.Purchase;

public class PurchaseValidator implements Validator<Purchase>{
    public void validate(Purchase entity) throws ValidatorException {
        //TODO validate client
        if (entity != null)
            return;
        else throw new ValidatorException("Client can not be null.");
    }
}
