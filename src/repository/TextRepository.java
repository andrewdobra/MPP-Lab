package repository;

import domain.BaseEntity;
import domain.validators.Validator;


public class TextRepository<ID,T extends BaseEntity<ID>> extends InMemoryRepository<ID,T> {

    public TextRepository(Validator<T> validator) {
        super(validator);
    }
}
