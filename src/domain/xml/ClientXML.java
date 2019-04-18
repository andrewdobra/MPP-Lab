package domain.xml;

import domain.Client;

public class ClientXML extends XMLElement<Client> {
    @Override
    public String toXML(Client t) {
        return "<id>"+t.getId()+"</id>\n" +
                "<name>"+t.getName()+"</name>\n";
    }

    @Override
    public Client fromXML(String s) {
        Long id = Long.parseLong(s.substring(s.indexOf("<id>")+"<id>".length(),s.indexOf("</id>")));
        String name = s.substring(s.indexOf("<name>")+"<name>".length(),s.indexOf("</name>"));
        return new Client(id,name);
    }

}
