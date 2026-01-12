//Name: Phonlapat Urairong
//ID: 6588114
//Section: 3
public class LinkedNode {
    private Product product;
    private int quantity;
    private LinkedNode next;


    public LinkedNode(Product product, int quantity, LinkedNode next) {
        this.product = product;
        this.quantity = quantity;
        this.next = next;
    }
    
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LinkedNode getNext() {
        return next;
    }
    
    public void setNext(LinkedNode next) {
        this.next = next;
    }
}

