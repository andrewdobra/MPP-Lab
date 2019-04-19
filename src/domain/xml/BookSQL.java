package domain.xml;

import domain.BaseEntity;
import domain.Book;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookSQL extends SQLElement<Long, Book> {
    @Override
    public Book fromSQL(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String name = resultSet.getString("name");

        return new Book(id,name);
    }

    @Override
    public String insertSQL(Book book) {
        return "INSERT INTO Books(id,name) VALUES("+book.getId()+","+"\""+book.getName()+"\")";
    }

    @Override
    public String updateSQL(Book book) {
        return "UPDATE Books SET name=\""+book.getName()+"\" WHERE id="+book.getId();
    }
}
