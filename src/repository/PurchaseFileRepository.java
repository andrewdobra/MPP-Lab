package repository;

import domain.Purchase;
import domain.validators.Validator;
import domain.validators.ValidatorException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public class PurchaseFileRepository extends InMemoryRepository<Long, Purchase> {
    private String fileName;

    public PurchaseFileRepository(Validator<Purchase> validator, String fileName) {
        super(validator);
        this.fileName = fileName;

        loadData();
    }


    private void loadData() {
        Path path = Paths.get(fileName);

        try {
        Files.lines(path).forEach(line -> {
        List<String> items = Arrays.asList(line.split(","));

        Long id = Long.valueOf(items.get(0));
        Long CID = Long.valueOf(items.get(1));
        Long BID = Long.valueOf(items.get(2));
        
        Purchase purchase = new Purchase(id, CID, BID);

        try {
        super.save(purchase);
        } catch (ValidatorException e) {
        e.printStackTrace();
        }
        });
        } catch (IOException ex) {
        ex.printStackTrace();
        }
    }

    @Override
    public Optional<Purchase> save(Purchase entity) throws ValidatorException {
        Optional<Purchase> optional = super.save(entity);
        if (optional.isPresent()) {
        return optional;
        }
        saveToFile(entity);
        return Optional.empty();
        }

private void saveToFile(Purchase entity) {
        Path path = Paths.get(fileName);

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
        bufferedWriter.write(
        entity.getId() + entity.getBookID() + "," + entity.getBookID());
        bufferedWriter.newLine();
        } catch (IOException e) {
        e.printStackTrace();
        }
        }
@Override
public Optional<Purchase> delete(Long aLong) {
        Optional<Purchase> result = super.delete(aLong);

        if(result.isPresent())
        saveFile(); //if something was removed then save the changes

        return result;
        }

/* rewrites the file*/
    public void saveFile() {
        Path path = Paths.get(fileName);

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path)) {
        entities.keySet().forEach(key -> {
        try
        {
        bufferedWriter.write(
        key + "," + entities.get(key).getClientID() + "," + entities.get(key).getBookID());
        bufferedWriter.newLine();
        }
        catch (IOException e)
        {
        e.printStackTrace();
        }
        });
        } catch (IOException e) {
        e.printStackTrace();
        }
        }

}
