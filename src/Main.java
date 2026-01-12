import java.util.Scanner;
import java.util.InputMismatchException;
//Name: Phonlapat Urairong
//ID: 6588114
//Section: 3
public class Main {
    public static void main(String[] args) {
        User_cart cart = new User_cart();
        Checkout checkout = new Checkout();
        ProductManagement productManagement = new ProductManagement();

        Scanner scanner = new Scanner(System.in);
        boolean isAdminLoggedIn = false;
        AdminLogin adminLogin = new AdminLogin();
        String discountCode = ""; // Store the discount code
        productManagement.loadProductsFromCSV("filePath.csv");
        cart.loadCartFromCSV();

        while (true) {
        	System.out.println("======================================");
        	System.out.println("|       ENTER YOUR CHARACTER         |");
            System.out.println("======================================");
            System.out.println("| 1. Admin                           |");
            System.out.println("| 2. Customer                        |");
            System.out.println("| 3. Exit                            |");
            System.out.println("......................................");
            int users = getInputInt(scanner, "Enter your choice: ");

            if (users == 1) { // Admin login
                if (!isAdminLoggedIn) {
                    Scanner adminScanner = new Scanner(System.in);
                    System.out.println("--------------------------------------");
                    System.out.println("|             ADMIN LOGIN            |");
                    System.out.println("--------------------------------------");
                    System.out.println("|1. Login                            |");
                    System.out.println("|2. Exit                             |");
                    System.out.println("......................................");
                    System.out.print("Enter your choice: ");
                    
                    try {
                        int choice = getInputInt(adminScanner);

                        switch (choice) {
                            case 1:
                                isAdminLoggedIn = adminLogin.login(); // Check if login is successful
                                break;

                            case 2:
                                System.out.println("Exiting the program.");
                                adminScanner.close();
                                System.exit(0);
                                break;

                            default:
                                System.out.println("Invalid choice. Please try again.");
                                break;
                        }
                    } catch (InputMismatchException e) {
                        System.err.println("Invalid input. Please enter a valid number.");
                        adminScanner.nextLine(); // Check user Inout
                    } catch (IllegalArgumentException e) {
                        System.err.println(e.getMessage());
                    }
                }

                if (isAdminLoggedIn) { // Admin page
                    while (true) {
                        System.out.println("--------------------------------------");
                        System.out.println("|          WELCOME TO ADMIN          |");
                        System.out.println("--------------------------------------");
                        System.out.println("|1. Add product to program           |");
                        System.out.println("|2. Display available products       |");
                        System.out.println("|3. Clear product                    |");
                        System.out.println("|4. Add Discount Code                |");
                        System.out.println("|5. Show Discount Code               |");
                        System.out.println("|6. Change discount                  |");
                        System.out.println("|7. delete discount                  |");
                        System.out.println("|8. Clear all discount               |");
                        System.out.println("|9. Logout                           |");
                        System.out.println("......................................");
                        int choice_1 = getInputInt(scanner, "Enter your choice: ");

                        try {
                            switch (choice_1) {
                                case 1:
                                	System.out.println("++++++++++++++++++++++++++++++++++++++");
                                    System.out.println("|    ADD A NEW PRODUCT TO PROGRAM    |");
                                    System.out.println("++++++++++++++++++++++++++++++++++++++");
                                    int productId = getInputInt(scanner, "Product ID: ");
                                    System.out.print("Product Name: ");
                                    String productName = scanner.next();
                                    System.out.print("Product Description: ");
                                    String productDescription = scanner.next();
                                    System.out.print("Product Price: $");
                                    double productPrice = getInputDouble(scanner);
                                    System.out.print("Product Stock Count: ");
                                    int productStockCount = getInputInt(scanner);

                                    Product product = new Product(productId, productName, productDescription, productPrice, productStockCount);
                                    productManagement.addProductToProgram(product);
                                    // Add the product to the program
                                   // System.out.println("Product added to the program successfully.");
                                    break;

                                case 2:
                                    productManagement.displayAllProductsInProgram();
                                    break;

                                case 3:
                                    productManagement.clearCSVFile();
                                    System.out.println("clear");
                                    break;


                                case 4:
                                	System.out.print("Enter discount code: ");
                                    String code = scanner.next();
                                    if (!productManagement.discountCodeExistsInCSV(code)) {
                                        System.out.print("Enter discount percentage (e.g., 10 for 10%): ");
                                        int discountPercentage = getInputInt(scanner);
                                        productManagement.addDiscountCode(code, discountPercentage);
                                    } else {
                                        System.out.println("Discount code already exists.");
                                    }
                                    break;
                                case 5:
                                	 productManagement.displayAvailableDiscountCodes();
                                	 break;
                                case 6:
                                	 System.out.print("Enter discount code: ");
                                	 String code_1 = scanner.next();
                                	 System.out.print("New percent off: ");
                                	 int percentoff= scanner.nextInt();
                                	 productManagement.modifyDiscountCode(code_1, percentoff);
                                	 break;
                                	 
                                case 7:
                                	System.out.print("Enter discount code: ");
                           	        String code_2 = scanner.next();
                                	productManagement.deleteDiscountCode(code_2);
                                	break;
                                	
                                case 8:
                                	
                                	 productManagement.clearCSVFileDiscount();
                                	 break;
                                	 
                                case 9:
                                	break;
                                	
                                default:
                                    System.out.println("Invalid choice. Please try again.");
                                    break;
                                
                                	
                            }
                            if (choice_1 == 9) {
                                break; // Break out of the admin actions loop and return to the main menu
                            }
                        } catch (InputMismatchException e) {
                            System.err.println("Invalid input. Please enter a valid number.");
                            scanner.nextLine(); // Consume the invalid input
                        } catch (IllegalArgumentException e) {
                            System.err.println(e.getMessage());
                        }
                    }
                }
            } else if (users == 2) { // Customer page
                while (true) {
                    System.out.println("--------------------------------------");
                    System.out.println("|     WELCOME OUR LOVELY CUSTOMER    |");
                    System.out.println("--------------------------------------");
                    System.out.println("|1. Add product to cart              |");
                    System.out.println("|2. Remove product from cart         |");
                    System.out.println("|3. Change order of items in cart    |");
                    System.out.println("|4. Display cart                     |");
                    System.out.println("|5. Checkout                         |");
                    System.out.println("|6. Exit                             |");
                    System.out.println("|7. Clear cart                       |");
                    System.out.println("......................................");
                    
                    int choice_2 = getInputInt(scanner, "Enter your choice: ");

                    try {
                        switch (choice_2) {
                            case 1:
                                // Code for adding a product to the cart
                                productManagement.displayAllProductsInProgram();
                                //cart.loadCartFromCSV();
                                System.out.println("++++++++++++++++++++++++++++++++++++++");
                                System.out.println("|          ADD YOUR PRODUCTS         |");
                                System.out.println("++++++++++++++++++++++++++++++++++++++");
                                System.out.print("Enter ID: ");
                                int productId = getInputInt(scanner);

                                Product productToAdd = productManagement.findProductInProgram(productId);

                                if (productToAdd != null) {
                                   // System.out.print("Enter the quantity to add to your cart: ");
                                    int quantity = getInputInt(scanner, "Enter the quantity to add to your cart: ");

                                    if (quantity <= 0) {
                                        System.out.println("Invalid quantity. Please enter a positive quantity.");
                                    } else if (quantity > productToAdd.getStockCount()) {
                                        System.out.println("Insufficient stock. Maximum available quantity: " + productToAdd.getStockCount());
                                    } else {
                                        // Create a new Product object with updated stock count
                                        Product updatedProduct = new Product(
                                                productToAdd.getId(),
                                                productToAdd.getName(),
                                                productToAdd.getDescription(),
                                                productToAdd.getPrice(),
                                                productToAdd.getStockCount() - quantity  // Decrease stock count
                                        );

                                        cart.addProductToCartAndSaveToCSV(productToAdd, quantity);
                                        //System.out.println(quantity + " " + productToAdd.getName() + "(s) added to your cart.");
                                        //productManagement.loadProductsFromCSV("filePath.csv");
                                        //productManagement.overrideProductById(productId, updatedProduct);
                                    }
                                } else {
                                    System.out.println("Product not found with ID " + productId);
                                }
                                break;

                            case 2:
                                // Code for removing a product from the cart
                                cart.displayUserCart();
                                System.out.print("\nEnter removed ID:");
                                int removeID = scanner.nextInt();
                                System.out.print("Enter removed quantity:");
                                int quantity = scanner.nextInt();
                                cart.removeProductByIdAndQuantityFromCartAndSaveToCSV(removeID, quantity);
                         
                                break;

                            case 3:
                                // Code for changing the order of items in the cart
                            	cart.displayUserCart();
                                System.out.print("\nEnter ID:");
                                int ID = scanner.nextInt();
                                System.out.print("Enter how many customer want:");
                                int Changequantity = scanner.nextInt();
                                cart.adjustQuantityInCart(ID, Changequantity);
                                break;

                            case 4:
                                // Code for displaying the cart
                                cart.displayUserCart();
                                //cart.displayAllProductsInCartFromCSV("user_cart.csv");
                                break;

                            case 5:
                                // Code for checkout
                            	 //System.out.print("Enter discount code (if any, or press Enter to skip): ");
                            	 
                            	 checkout.checkoutCart(productManagement, discountCode);
                                 break;

                            case 6:
                                System.out.println("Exiting the program.");
                                scanner.close();
                                System.exit(0);
                                break;

                            case 7:
                                // Code for clearing the cart
                                cart.clearCSVFile();
                                break;

                            default:
                                System.out.println("Invalid choice. Please try again.");
                                break;
                        }
                    } catch (InputMismatchException e) {
                        System.err.println("Invalid input. Please enter a valid number.");
                        scanner.nextLine(); // Consume the invalid input
                    } catch (IllegalArgumentException e) {
                        System.err.println(e.getMessage());
                    }
                }
            } else if (users == 3) { // Exit the program
                System.out.println("Exiting the program.");
                scanner.close();
                System.exit(0);
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    //Method check input
    private static int getInputInt(Scanner scanner, String... prompt) {
        while (true) {
            try {
                if (prompt.length > 0) {
                    System.out.print(prompt[0]);
                }
                else if (prompt.length > 1) {
                    System.out.print(prompt[1]);
                }
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.err.println(prompt.length > 1 ? prompt[1] : "Invalid input! Please enter a number again.");
                scanner.nextLine(); // Consume the invalid input
            }
        }
    }
    private static double getInputDouble(Scanner scanner) {
        while (true) {
            try {
                System.out.print("");
                return scanner.nextDouble();
            } catch (InputMismatchException e) {
                System.err.println("Invalid input! Please enter number again.");
                scanner.nextLine();
            }
        }
    }
}
