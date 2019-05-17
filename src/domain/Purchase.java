package domain;

public class Purchase extends BaseEntity<Long>{
    private Long client;
    private Long book;

    public Purchase(Long id, Long client, Long book) {
        this.setId(id);
        this.client = client;
        this.book = book;
    }

    public Long getBookID() {
        return book;
    }

    public Long getClientID() {
        return client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Purchase purchase = (Purchase) o;

        return (book.equals(purchase.getBookID()) && client.equals(purchase.getClientID()));
    }

    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "ID: " + this.getId() +
                "; Client ID: " + this.getClientID() +
                "; Product ID: " + this.getBookID() +
                "} ";
    }
}
