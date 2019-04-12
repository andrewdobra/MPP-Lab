package domain;

/**
 * @author radu.
 *
 */

public class Book extends BaseEntity<Long>{
    private String name;

    public Book(Long id, String name) {
        this.setId(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        return name.equals(book.getName());
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Book{" +
                "ID: " + this.getId() +
                "; Name: " + name +
                "} ";
    }
}
