import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
//Name: Phonlapat Urairong
//ID: 6588114
//Section: 3
public class User_cart {
	private LinkedNode head;
    private MyLinkedList cart;
    private static final String CART_CSV_FILE = "user_cart.csv";

    public User_cart() {
        cart = new MyLinkedList();
    }

    public LinkedNode getHead() {
        return head;
    }
    
    public void addProductToCart(Product product, int quantity) {
        if (quantity <= 0) {
            System.out.println("Invalid quantity. Please enter a positive quantity.");
            return;
        }

        cart.add(product, quantity);
        System.out.println(quantity + " " + product.getName() + "(s) added to cart.");
    }

    public void displayUserCart() {
        System.out.println("\n                              ::Products in the user's cart::\n");
        System.out.println("ID\t\tName\t\tDescription\t\tPrice\t\tQuantity\tTotal");

        for (int i = 0; i < cart.size(); i++) {
            LinkedNode node = cart.get(i);
            Product product = node.getProduct();
            int quantity = node.getQuantity();

            // Print product details
            System.out.println(
                    product.getId() + "\t\t" + product.getName() + "\t\t" + product.getDescription() + "\t\t        "
                            + product.getPrice() + "\t\t" + quantity + "\t\t" + (product.getPrice() * quantity)
            );
        }
    }

    public void addProductToCartAndSaveToCSV(Product product, int quantity) {
        if (quantity <= 0) {
            System.out.println("Invalid quantity. Please enter a positive quantity.");
            return;
        }

        boolean productFound = false;

        for (int i = 0; i < cart.size(); i++) {
            LinkedNode node = cart.get(i);
            if (node.getProduct().getId() == product.getId()) {
                node.setQuantity(node.getQuantity() + quantity);
                product.setStockCount(product.getStockCount() - quantity);

                productFound = true;
                saveCartToCSV();

                System.out.println(quantity + " " + product.getName() + "(s) added to cart and saved to CSV file.");
                break;
            }
        }

        if (!productFound) {
            cart.add(product, quantity);

            //product.setStockCount(product.getStockCount() - quantity);

            // Update the CSV file with the added product
            saveCartToCSV();

            System.out.println(quantity + " " + product.getName() + "(s) added to cart and saved to CSV file.");
        }
    }

    public void removeProductByIdAndQuantityFromCartAndSaveToCSV(int productId, int quantityToRemove) {
        for (int i = 0; i < cart.size(); i++) {
            LinkedNode node = cart.get(i);
            if (node.getProduct().getId() == productId) {
                int currentQuantity = node.getQuantity();

                if (currentQuantity <= quantityToRemove) {
                    cart.remove(i);
                    Product product = node.getProduct();
                    product.setStockCount(product.getStockCount() + currentQuantity);
                    saveCartToCSV();

                    System.out.println("Product ID " + productId + " removed from the cart and CSV file.");
                } else {
                    node.setQuantity(currentQuantity - quantityToRemove);
                    Product product = node.getProduct();
                    product.setStockCount(product.getStockCount() + quantityToRemove);

                    saveCartToCSV();

                    System.out.println(quantityToRemove + " of Product ID " + productId + " removed from the cart and CSV file.");
                }
                return;
            }
        }

        System.out.println("Product with ID " + productId + " not found.");
    }

    public void removeAllProductsByIdFromCartAndSaveToCSV(int productId) {
        for (int i = 0; i < cart.size(); i++) {
            LinkedNode node = cart.get(i);
            if (node.getProduct().getId() == productId) {
                Product product = node.getProduct();
                product.setStockCount(product.getStockCount() + node.getQuantity());
                cart.remove(i);
                saveCartToCSV();

                System.out.println("roduct ID " + productId + " removed from the cart and CSV file.");
                return;
            }
        }

        System.out.println("Product with ID " + productId + " not found in the cart.");
    }

    private void saveCartToCSV() {
        try (FileWriter csvWriter = new FileWriter(CART_CSV_FILE, false)) {
            csvWriter.write("ID,Name,Description,Price,Quantity,Total\n"); 

            for (int i = 0; i < cart.size(); i++) {
                LinkedNode node = cart.get(i);
                Product product = node.getProduct();
                int quantity = node.getQuantity();
                csvWriter.write(product.getId() + "," + product.getName() + ","
                        + product.getDescription() + "," + product.getPrice() + ","
                        + quantity + "," + (product.getPrice() * quantity) + "\n");
            }

            System.out.println("Cart saved to CSV file " + CART_CSV_FILE);
        } catch (IOException e) {
            System.err.println("Error saving cart to CSV file " + e.getMessage());
        }
    }

    public void loadCartFromCSV() {
        try (BufferedReader csvReader = new BufferedReader(new FileReader(CART_CSV_FILE))) {
            String row;
            boolean isFirstRow = true; // Skip the header row

            while ((row = csvReader.readLine()) != null) {
                if (isFirstRow) {
                    isFirstRow = false;
                    continue; 
                }

                String[] data = row.split(",");
                if (data.length == 6) { 
                    int id = Integer.parseInt(data[0].trim());
                    String name = data[1].trim();
                    String description = data[2].trim();
                    double price = Double.parseDouble(data[3].trim());
                    int quantity = Integer.parseInt(data[4].trim());

                    Product product = new Product(id, name, description, price, quantity);
                    cart.add(product, quantity);
                }
            }

            System.out.println("Cart loaded from CSV file: " + CART_CSV_FILE);
        } catch (IOException e) {
            System.err.println("Error while loading cart from CSV file: " + e.getMessage());
        }
    }

    public void adjustQuantityInCart(int productId, int newQuantity) {
        if (newQuantity <= 0) {
            System.out.println("Invalid quantity. Please enter a positive quantity.");
            return;
        }

        for (int i = 0; i < cart.size(); i++) {
            LinkedNode node = cart.get(i);
            if (node.getProduct().getId() == productId) {
                node.setQuantity(newQuantity);
                System.out.println("Quantity for Product ID " + productId + " adjusted to " + newQuantity);
                saveCartToCSV();
                return;
            }
        }

        System.out.println("Product ID " + productId + " not found in the cart.");
    }
    public boolean clearCSVFile() {
        try (FileWriter writer = new FileWriter(CART_CSV_FILE, false)) {
            // Overwrite the CSV file with an empty content
            writer.write("");
            System.out.println("CSV file cleared.");
            cart.clear();
            return true;
        } catch (IOException e) {
            System.out.println("Error clearing CSV file " + e.getMessage());
            return false;
        }
    }


}


