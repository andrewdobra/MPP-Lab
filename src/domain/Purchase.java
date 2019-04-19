package domain;

public class Purchase<ID> {
    private ID product;
    private ID client;

    public Purchase(ID product, ID client) {
        this.product = product;
        this.client = client;
    }
}
