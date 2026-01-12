//Name: Phonlapat Urairong
//ID: 6588114
//Section: 3
public class MyLinkedList {
    private LinkedNode head;
    private int size;

    public MyLinkedList() {
        head = null;
        size = 0;
    }
    
    public void clear() {
        //for clear
        head = null;
        size = 0;
    }


    public LinkedNode get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }

        LinkedNode current = head;
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }

        return current;
    }
    
    
    public void remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }

        if (index == 0) {
            // If removing the head node
            head = head.getNext();
        } else {
            LinkedNode prev = get(index - 1);
            LinkedNode current = prev.getNext();
            prev.setNext(current.getNext());
        }

        size--;
    }
    public void add(Product product, int quantity) {
        LinkedNode newNode = new LinkedNode(product, quantity, head);
        if (head == null) {
            head = newNode;
        } else {
            LinkedNode current = head;
            while (current.getNext() != null) {
                current = current.getNext();
            }
            current.setNext(newNode);
        }
        size++;
    }
    public void removeById(int productId) {
        if (head == null) {
            return;
        }

        if (head.getProduct().getId() == productId) {
            head = head.getNext();
            size--;
            return;
        }

        LinkedNode prev = null;
        LinkedNode current = head;

        while (current != null && current.getProduct().getId() != productId) {
            prev = current;
            current = current.getNext();
        }

        if (current != null) {
            prev.setNext(current.getNext());
            size--;
        }
    }


    public int size() {
        return size;
    }

    public void display() {
        LinkedNode current = head;
        while (current != null) {
            System.out.println("Product ID: " + current.getProduct().getId());
            System.out.println("Name: " + current.getProduct().getName());
            System.out.println("Quantity: " + current.getQuantity());
            System.out.println("-----------------------");
            current = current.getNext();
        }
    }

    public Product findById(int productId) {
        LinkedNode current = head;
        while (current != null) {
            if (current.getProduct().getId() == productId) {
                return current.getProduct();
            }
            current = current.getNext();
        }
        return null;
    }
}
