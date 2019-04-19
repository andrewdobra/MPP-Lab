package domain.xml;

import domain.BaseEntity;
import domain.Book;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class BookXML extends XMLElement<Book> {
    @Override
    public Element toXML(Book t, Document doc) {
        Element e = doc.createElement("elem");
        e.setAttribute("id",t.getId().toString());
        e.setAttribute("name",t.getName());

        return e;
    }

    @Override
    public Book fromXML(Element e) {

        Long id =Long.parseLong(e.getAttribute("id"));
        String name = e.getAttribute("name");
        return new Book(id,name);
    }
}
