package domain.xml;

import domain.Purchase;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PurchaseXML extends XMLElement<Purchase>{
    @Override
    public Element toXML(Purchase t, Document doc) {
        Element e = doc.createElement("elem");
        e.setAttribute("id",t.getId().toString());
        e.setAttribute("ClientID",t.getClientID().toString());
        e.setAttribute("BookID",t.getBookID().toString());

        return e;
    }

    @Override
    public Purchase fromXML(Element e) {

        Long id = Long.parseLong(e.getAttribute("id"));
        Long CID = Long.parseLong(e.getAttribute("ClientID"));
        Long BID = Long.parseLong(e.getAttribute("BookID"));
        return new Purchase(id,CID,BID);
    }

}
