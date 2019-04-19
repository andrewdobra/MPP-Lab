package domain.xml;

import domain.Client;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ClientXML extends XMLElement<Client> {
    @Override
    public Element toXML(Client t, Document doc) {
        Element e = doc.createElement("elem");
        e.setAttribute("id",t.getId().toString());
        e.setAttribute("name",t.getName());

        return e;
    }

    @Override
    public Client fromXML(Element e) {

        Long id =Long.parseLong(e.getAttribute("id"));
        String name = e.getAttribute("name");
        return new Client(id,name);
    }

}
