package repository;

import domain.Client;
import domain.validators.*;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author radu.
 */
public class ClientFileRepository extends InMemoryRepository<Long, Client> {
    private String fileName;

    public ClientFileRepository(Validator<Client> validator, String fileName) {
        super(validator);
        this.fileName = fileName;

        loadData();
    }

    /*@Override
    public Optional<Client> delete(Long aLong) {
        Optional<Client> result = super.delete(aLong);

        if(result.isPresent())
            saveFile(); //if something was removed then save the changes

        return result;
    }*/

    /* rewrites the file*/
    public void saveFile() {
        Path path = Paths.get(fileName);

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path)) {
            entities.keySet().forEach(key -> {
                try
                {
                    bufferedWriter.write(
                            key + "," + entities.get(key).getName());
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

    private void loadData() {
        Path path = Paths.get(fileName);

        try {
            Files.lines(path).forEach(line -> {
                List<String> items = Arrays.asList(line.split(","));

                Long id = Long.valueOf(items.get(0));
                String name = items.get(1);

                Client client = new Client(id, name);

                try {
                    super.save(client);
                } catch (ValidatorException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Optional<Client> save(Client entity) throws ValidatorException {
        Optional<Client> optional = super.save(entity);
        if (optional.isPresent()) {
            return optional;
        }
        saveToFile(entity);
        return Optional.empty();
    }

    /*@Override
    public Optional<Client> delete(Long id) throws IllegalArgumentException {
        Optional<Client> optional = super.delete(id);
        File orig = new File(fileName);
        File tmp = new File(fileName+".tmp");

        try {
            BufferedReader input = new BufferedReader(new FileReader(orig));
            BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get(tmp.getPath()), StandardOpenOption.APPEND);
            for (String data; (data = input.readLine()) != null;)
                if (!data.split(",")[0].equals(id.toString())) {
                    bufferedWriter.write(data);
                    bufferedWriter.newLine();
                }


            bufferedWriter = Files.newBufferedWriter(Paths.get(orig.getPath()));
            input = new BufferedReader(new FileReader(tmp));
            for (String data; (data = input.readLine()) != null;)
                {
                    bufferedWriter.write(data);
                    bufferedWriter.newLine();
                }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        tmp.delete();

        if (optional.isPresent()) {
            return optional;
        }

        return Optional.empty();
    }*/

    private void saveToFile(Client entity) {
        Path path = Paths.get(fileName);

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
            bufferedWriter.write(
                    entity.getId() + "," + entity.getName());
            bufferedWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
