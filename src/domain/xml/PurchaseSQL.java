package domain.xml;

import domain.Purchase;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PurchaseSQL extends SQLElement<Long, Purchase> {
    @Override
    public Purchase fromSQL(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        Long CID = resultSet.getLong("ClientID");
        Long BID = resultSet.getLong("BookID");

        return new Purchase(id, CID, BID);
    }

    @Override
    public String insertSQL(Purchase purchase) {
        return "INSERT INTO Purchases(id, ClientID, BookID) VALUES("+ purchase.getId() + "," + "\"" + purchase.getClientID() + "\"," + "\"" + purchase.getBookID()+"\")";
    }

    @Override
    public String updateSQL(Purchase purchase) {
        return "UPDATE Purchases SET ClientID=\""+purchase.getClientID()+"SET BookID=\""+purchase.getBookID()+"\" WHERE id="+purchase.getId();
    }
}
