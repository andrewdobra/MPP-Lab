package repository;

import domain.BaseEntity;
import domain.xml.XMLElement;
import domain.validators.Validator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
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

    private DocumentBuilderFactory factory;
    private DocumentBuilder docBuilder;
    private Document doc;

    public XMLRepository(Validator<T> validator, XMLElement<T> xmlElement, String fileName) {
        super(validator);
        this.fileName = fileName;
        this.xmlElement = xmlElement;

        try
        {
            factory = DocumentBuilderFactory.newInstance();
            docBuilder = factory.newDocumentBuilder();
            doc = docBuilder.newDocument();

            loadData();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<T> delete(Long aLong) {
        Optional<T> result = super.delete(aLong);

        if(result.isPresent())
            try
            {
                saveFile(); //if something was removed then save the changes
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        return result;
    }

    void saveFile() throws Exception
    {
        if(Files.exists(Paths.get(fileName)))
        {
            doc = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(fileName);
        }
        else
        {
            doc = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .newDocument();

            doc.appendChild(doc.createElement("root"));
        }

        Element root = doc.getDocumentElement();

        entities.keySet().forEach(key -> {
            root.appendChild(xmlElement.toXML(entities.get(key),doc));
        });


        Transformer transformer =
                TransformerFactory.newInstance().newTransformer();
        transformer.transform(new DOMSource(root),
                new StreamResult(new FileOutputStream(
                        fileName)));
        /*Path path = Paths.get(fileName);

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
        }*/
    }

    @Override
    public Optional<T> save(T entity)  {
        Optional<T> optional = super.save(entity);
        if (optional.isPresent()) {
            return optional;
        }
        try
        {
            saveFile();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    void loadData() throws Exception
    {
        if(Files.exists(Paths.get(fileName))) //maybe unnecessary
        {
            doc = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(fileName);

            Element root = doc.getDocumentElement();

            NodeList nodes = root.getChildNodes();

            for(int i = 0; i < nodes.getLength(); i++){

                if (nodes.item(i) instanceof Element)
                {
                    T elem = xmlElement.fromXML((Element)nodes.item(i));
                    entities.put(elem.getId(),elem);
                }
            };
        }
        else //create the document
        {

        }
        /*try
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
        }*/
    }
}
