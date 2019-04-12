package domain;

/**
 * @author radu.
 *
 */

public class Client extends BaseEntity<Long>{
    private String name;

    public Client(Long id, String name) {
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

        Client client = (Client) o;

        return name.equals(client.name);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Client{" +
                "ID: " + this.getId() +
                "; Name: " + name +
                "} ";
    }
}
