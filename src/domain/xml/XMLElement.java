package domain.xml;

import domain.BaseEntity;

public class XMLElement<T extends BaseEntity<Long>> {

    public String toXML(T t)
    {
        return "";
    }

    public T fromXML(String s)
    {
        return null;
    }

}
