package domain.xml;

import domain.Client;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientSQL extends SQLElement<Long, Client> {
    @Override
    public Client fromSQL(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String name = resultSet.getString("name");

        return new Client(id,name);
    }

    @Override
    public String insertSQL(Client client) {
        return "INSERT INTO Clients(id,name) VALUES("+client.getId()+","+"\""+client.getName()+"\")";
    }

    @Override
    public String updateSQL(Client client) {
        return "UPDATE Clients SET name=\""+client.getName()+"\" WHERE id="+client.getId();
    }
}
