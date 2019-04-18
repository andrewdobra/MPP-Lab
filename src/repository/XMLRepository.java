package repository;

import domain.BaseEntity;
import domain.xml.XMLElement;
import domain.validators.Validator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class XMLRepository<ID, T extends BaseEntity<Long>> extends InMemoryRepository<Long,T> {
    private String fileName;
    private XMLElement<T> xmlElement;
    /*private DocumentBuilderFactory factory;
    private DocumentBuilder docBuilder;
    private Document doc;*/

    public XMLRepository(Validator<T> validator, XMLElement<T> xmlElement, String fileName) {
        super(validator);
        this.fileName = fileName;
        this.xmlElement = xmlElement;
        loadData();
    }

    @Override
    public Optional<T> delete(Long aLong) {
        Optional<T> result = super.delete(aLong);

        if(result.isPresent())
            saveFile(); //if something was removed then save the changes

        return result;
    }

    void saveFile()
    {
        Path path = Paths.get(fileName);

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path)) {
            bufferedWriter.write("<root>\n");
            entities.keySet().forEach(key -> {
                try
                {
                    bufferedWriter.write("<elem>\n");
                    bufferedWriter.write(xmlElement.toXML(entities.get(key)));
                    bufferedWriter.write("</elem>\n");
                    bufferedWriter.newLine();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            });
            bufferedWriter.write("</root>\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<T> save(T entity)  {
        Optional<T> optional = super.save(entity);
        if (optional.isPresent()) {
            return optional;
        }
        saveFile();
        return Optional.empty();
    }

    void loadData()
    {
        try
        {
            String text = Files.lines(Paths.get(fileName)).collect(Collectors.joining());

            text = text.replace("<root>","").trim();
            text = text.replace("</root>","").trim();

            Arrays.asList(text.split("<elem>")).stream().filter(s -> !s.isEmpty()).forEach(
                    elemString -> {
                        T entity = xmlElement.fromXML(elemString.substring(0,elemString.indexOf("</elem>")));

                        entities.put(entity.getId(),entity);
                    }
            );
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
