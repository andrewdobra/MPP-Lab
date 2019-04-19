package repository;

import domain.BaseEntity;
import domain.validators.Validator;

public class DatabaseRepository<T extends BaseEntity<Long>> extends InMemoryRepository<Long,T> {
    public DatabaseRepository(Validator<T> validator) {
        super(validator);
    }
}
