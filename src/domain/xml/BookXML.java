package domain.xml;

import domain.BaseEntity;
import domain.Book;

public class BookXML extends XMLElement<Book> {
    @Override
    public String toXML(Book t) {
        return "<id>"+t.getId()+"</id>\n" +
                "<name>"+t.getName()+"</name>\n";
    }

    @Override
    public Book fromXML(String s) {
        Long id = Long.parseLong(s.substring(s.indexOf("<id>")+"<id>".length(),s.indexOf("</id>")));
        String name = s.substring(s.indexOf("<name>")+"<name>".length(),s.indexOf("</name>"));
        return new Book(id,name);
    }
}
