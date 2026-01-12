//Name: Phonlapat Urairong
//ID: 6588114
//Section: 3
public class Product {
    private int id;
    private String name;
    private String description;
    private double price;
    private int stockCount;

    public Product(int id, String name, String description, double price, int stockCount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockCount = stockCount;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getStockCount() {
        return stockCount;
    }

    public void setStockCount(int stockCount) {
        this.stockCount = stockCount;
    }

    @Override
    public String toString() {
        return "Product ID: " + id +
               "\nName: " + name +
               "\nDescription: " + description +
               "\nPrice: $" + price +
               "\nStock Count: " + stockCount;
    }
}
