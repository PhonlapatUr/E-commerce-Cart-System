import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
//Name: Phonlapat Urairong
//ID: 6588114
//Section: 3
public class Checkout {
    User_cart cart = new User_cart();
    ProductManagement productmanagement = new ProductManagement();
    double totalAmount = 0.0;
    double itemTotal;
    double discountPercent;
    public boolean checkoutCart(ProductManagement productManagement, String discountCode) {
        StringBuilder cartItems = new StringBuilder();

        //discount apply or not
        if (!discountCode.isEmpty() && isDiscountCodeExistsInCSV(discountCode)) {
            discountPercent = getDiscountPercentFromCSV(discountCode);
            System.out.println("Discount code applied successfully!");
        } else {
            System.out.print("You can enter a discount code (or enter 0 to skip):");
            @SuppressWarnings("resource")
			Scanner scanner = new Scanner(System.in);
            discountCode = scanner.nextLine().trim();
            
            if (!discountCode.equals("0") && !isDiscountCodeExistsInCSV(discountCode)) {
                System.out.println("Invalid discount code. Skipping discount.");
            } else if (!discountCode.equals("0")) {
                discountPercent = getDiscountPercentFromCSV(discountCode);
                System.out.println("Discount code applied successfully!");
            }
        }

        try (BufferedReader csvReader = new BufferedReader(new FileReader("user_cart.csv"))) {
            String row;
            boolean isFirstRow = true; // Skip header 

            while ((row = csvReader.readLine()) != null) {
                if (isFirstRow) {
                    isFirstRow = false;
                    continue; //Check header
                }

                String[] data = row.split(",");
                if (data.length >= 6) {
                    int productId = Integer.parseInt(data[0].trim());
                    int quantity = Integer.parseInt(data[4].trim());

          
                    Product productInProgram = productManagement.findProductInProgram(productId);

                    if (productInProgram == null) {
                        System.out.println("Product with ID " + productId + " not found in the program.");
                        return false;
                    }

                    if (quantity <= 0) {
                        System.out.println("Invalid quantity for product '" + productInProgram.getName() + "'.");
                        return false;
                    }

                    if (quantity > productInProgram.getStockCount()) {
                        System.out.println("Insufficient stock for product '" + productInProgram.getName() + "'. Maximum available quantity: " + productInProgram.getStockCount());
                        return false;
                    }

                    itemTotal = productInProgram.getPrice() * quantity;

                    // Append cart item information to the StringBuilder
                    cartItems.append("ID: ").append(productId).append("\n");
                    cartItems.append("Name: ").append(productInProgram.getName()).append("\n");
                    cartItems.append("Price: $").append(productInProgram.getPrice()).append("\n");
                    cartItems.append("Quantity: ").append(quantity).append("\n");
                    cartItems.append("Item Total: $").append(itemTotal).append("\n");

                    // Update the stock count of the product in the program
                    Product updatedProduct = new Product(
                            productInProgram.getId(),
                            productInProgram.getName(),
                            productInProgram.getDescription(),
                            productInProgram.getPrice(),
                            productInProgram.getStockCount() - quantity
                    );
                    productManagement.overrideProductById(productInProgram.getId(), updatedProduct);
                }
            }
        } catch (IOException e) {
            System.err.println("Error while reading the cart from CSV file: " + e.getMessage());
            return false;
        }

        // Calculate the total amount after discount
        totalAmount = calculateTotalAmountAfterDiscount(itemTotal, discountPercent);

        // Print cart items and total
        System.out.println("--------------------------------------");
        System.out.println("|          CONFIRMATION ORDER        |");
        System.out.println("--------------------------------------");
        System.out.println(cartItems.toString());
        System.out.println("======================================");
        System.out.println("Total Price (Before Discount): " + itemTotal + "Baht");
        System.out.println("Total Amount (After Discount): " + totalAmount + "Baht");
        System.out.println("Thank you for your purchase!");
        System.out.println("======================================");

        // Clear the cart CSV file after successful checkout
        cart.clearCSVFile();
        return true;
    }

   //Method helping to calculate percentage of discount
    private double calculateTotalAmountAfterDiscount(double itemTotal, double discountPercent) {
        return itemTotal * (1 - discountPercent / 100);
    }



    private boolean isDiscountCodeExistsInCSV(String discountCode) {
        try (BufferedReader csvReader = new BufferedReader(new FileReader("discounts.csv"))) {
            String row;
            boolean isFirstRow = true; 

            while ((row = csvReader.readLine()) != null) {
                if (isFirstRow) {
                    isFirstRow = false;
                    continue;
                }

                String[] data = row.split(",");
                if (data.length >= 1) {
                    String code = data[0].trim();
                    if (code.equals(discountCode)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error while checking if the discount code exists in CSV file: " + e.getMessage());
        }
        return false;
    }

    // Read discount from CSV file
    private double getDiscountPercentFromCSV(String discountCode) {
        try (BufferedReader csvReader = new BufferedReader(new FileReader("discounts.csv"))) {
            String row;
            boolean isFirstRow = true;

            while ((row = csvReader.readLine()) != null) {
                if (isFirstRow) {
                    isFirstRow = false;
                    continue;
                }

                String[] data = row.split(",");
                if (data.length >= 2) {
                    String code = data[0].trim();
                    if (code.equals(discountCode)) {
                        //Double.parseDouble(data[1].trim()); 
                        return Double.parseDouble(data[1].trim());
                        
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error while getting the discount percent from CSV file: " + e.getMessage());
        }
        return 0.0;
    }

}
