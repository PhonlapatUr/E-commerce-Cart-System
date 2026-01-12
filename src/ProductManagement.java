import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
//Name: Phonlapat Urairong
//ID: 6588114
//Section: 3
public class ProductManagement {
    private MyArrayList<Product> products;
    private static final String CSV_FILE_PATH = "filePath.csv";
    private static final String DISCOUNTS_CSV_PATH = "discounts.csv";

    public ProductManagement() {
        products = new MyArrayList<>();
    }

    public void addProductToProgram(Product product) {
        if (!productExists(product.getId())) {
            products.add(product);
            saveProductToCSV(product); // Save the product to CSV
           // System.out.println("Product added to the program successfully.");
        } else {
            System.out.println("Product with ID " + product.getId() + " already exists.");
        }
    }

    public boolean removeProductByIdAndQuantity(int productId, int quantity) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == productId) {
                Product product = products.get(i);
                if (product.getStockCount() >= quantity) {
                    product.setStockCount(product.getStockCount() - quantity);

                    if (product.getStockCount() == 0) {
                        products.remove(i);
                    }

                    // Update the CSV file with the new stock 
                    updateProductStockCountInCSV(productId, product.getStockCount());

                    return true;
                } else {
                    System.out.println("Insufficient stock. Maximum available quantity: " + product.getStockCount());
                    return false;
                }
            }
        }
        System.out.println("Product with ID " + productId + " not found in the program.");
        return false;
    }


    public Product findProductInProgram(int productId) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == productId) {
                return products.get(i);
            }
        }
        return null;
    }

    public void saveProductToCSV(Product product) {
        try (FileWriter csvWriter = new FileWriter(CSV_FILE_PATH, true)) {
            if (new File(CSV_FILE_PATH).length() == 0) {
                csvWriter.write("ID,Name,Description,Price,StockCount\n");
            }

            if (!productExistsInCSV(product.getId())) {
                csvWriter.write(product.getId() + "," + product.getName() + ","
                        + product.getDescription() + "," + product.getPrice() + ","
                        + product.getStockCount() + "\n");
                System.out.println("Product saved to CSV file: " + CSV_FILE_PATH);
            }
        } catch (IOException e) {
            System.err.println("Error while saving product to CSV file: " + e.getMessage());
        }
    }


    private boolean productExistsInCSV(int productId) {
        try (BufferedReader csvReader = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String row;
            boolean isFirstRow = true; // Skip the header row

            while ((row = csvReader.readLine()) != null) {
                if (isFirstRow) {
                    isFirstRow = false;
                    continue; // Skip the header row
                }

                String[] data = row.split(",");
                if (data.length >= 1) { // Ensure there is at least one field
                    int id = Integer.parseInt(data[0].trim());
                    if (id == productId) {
                    	
                        return true; 
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error while checking exists of product e in CSV file: " + e.getMessage());
        }
        return false; 
    }

    public void clearCSVFile() {
        try (FileWriter writer = new FileWriter(CSV_FILE_PATH, false)) {
            writer.write("");
            System.out.println("CSV file cleared.");
        } catch (IOException e) {
            System.out.println("Error clearing CSV file " + e.getMessage());
        }
    }

    public boolean loadProductsFromCSV(String csvFilePath) {
        try (BufferedReader csvReader = new BufferedReader(new FileReader(csvFilePath))) {
            String row;
            boolean isFirstRow = true; 

            while ((row = csvReader.readLine()) != null) {
                if (isFirstRow) {
                    isFirstRow = false;
                    continue;
                }

                String[] data = row.split(",");
                if (data.length == 5) { 
                    int id = Integer.parseInt(data[0].trim());
                    String name = data[1].trim();
                    String description = data[2].trim();
                    double price = Double.parseDouble(data[3].trim());
                    int stockCount = Integer.parseInt(data[4].trim());

                    Product product = new Product(id, name, description, price, stockCount);
                    addProductToProgram(product);
                }
            }
            System.out.println("Products loaded from " + csvFilePath);
            return true;
        } catch (IOException e) {
            System.err.println("Error loading products from CSV file " + e.getMessage());
        }
		return false;
    }

    public void displayAllProductsInProgram() {
    	System.out.println("**************************************");
        System.out.println("|     LIST OF AVAILABLE PRODUCTS     |");
       	System.out.println("**************************************");
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            System.out.println("Product ID: " + product.getId());
            System.out.println("Name: " + product.getName());
            System.out.println("Description: " + product.getDescription());
            System.out.println("Price: $" + product.getPrice());
            System.out.println("Stock Count: " + product.getStockCount());
            System.out.println("......................................");
        }
    }

    public void overrideProductById(int productId, Product newStock) {
        boolean productFound = false;

        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == productId) {
                //update product with new stock.
                products.set(i, newStock);
                System.out.println("Product with ID " + productId + " overridden.");

                // Update with stock change indicate by id in the CSV file
                updateProductStockCountInCSV(productId, newStock.getStockCount());

                productFound = true;
                break;
            }
        }

        if (!productFound) {
            System.out.println("Product with ID " + productId + " not found.");
        }
    }

    private void updateProductStockCountInCSV(int productId, int newStockCount) {
        File csvFile = new File(CSV_FILE_PATH);
        File tempFile = new File("temp.csv");

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            boolean isFirstLine = true;
            boolean foundProduct = false;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    writer.write(line + System.lineSeparator()); 
                    isFirstLine = false;
                    continue;
                }

                String[] data = line.split(",");
                if (data.length == 5) {
                    int id = Integer.parseInt(data[0].trim());

                    if (id == productId) {
                        data[4] = Integer.toString(newStockCount);
                        line = String.join(",", data);
                        foundProduct = true;
                    }
                }

                writer.write(line + System.lineSeparator());
            }

            if (!foundProduct) {
                System.out.println("Not found ID" + productId + "in CSV file");
            } else {
                if (tempFile.renameTo(csvFile)) {
                    System.out.println("Stock count for product with ID " + productId + " is successfully updated to " + newStockCount);
                } else {
                    System.err.println("Error can't replace CSV file");
                }
            }
        } catch (IOException e) {
            System.err.println("Error updating " + e.getMessage());
        }
    }

    private boolean productExists(int productId) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == productId) {
            	//System.out.println("Product with ID " + products.get(i).getId() + " already exists.");
                return true;
            }
        }
        return false;
    }
    
    
    public void addDiscountCode(String code, int percentOff) {
        if (!discountCodeExistsInCSV(code)) {
            try (FileWriter csvWriter = new FileWriter(DISCOUNTS_CSV_PATH, true)) {
                csvWriter.write(code + "," + percentOff + "\n");
                System.out.println("Discount code " + code + "added");
            } catch (IOException e) {
                System.err.println("Error while adding discount code " + e.getMessage());
            }
        } else {
            System.out.println("Discount code " + code + " already exists.");
        }
    }


    public boolean discountCodeExistsInCSV(String discountCode) {
        try (BufferedReader csvReader = new BufferedReader(new FileReader(DISCOUNTS_CSV_PATH))) {
            String row;
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                if (data.length >= 2) {
                    String code = data[0].trim();
                    if (code.equals(discountCode)) {
                        return true; 
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error checking discount code exist or not " + e.getMessage());
        }
        return false; 
    }
    
    
    public void displayAvailableDiscountCodes() {
    	System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        System.out.println("|       AVAILABLE DISCOUNT CODES     |");
    	System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        try (BufferedReader csvReader = new BufferedReader(new FileReader("discounts.csv"))) {
            String line;
            while ((line = csvReader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 2) {
                    System.out.println("Code: " + data[0]);
                    System.out.println("Discount: "+ data[1] + "%");
                    System.out.println("-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.-.");
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading discount codes from CSV: " + e.getMessage());
        }
    }


    public void modifyDiscountCode(String existingCode, int newPercentOff) {
        if (discountCodeExistsInCSV(existingCode)) {
            try {
                File csvFile = new File(DISCOUNTS_CSV_PATH);
                File tempFile = new File("temp_discounts.csv");
 
                try (BufferedReader reader = new BufferedReader(new FileReader(csvFile));
                     BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

                    String line;
                    boolean isFirstLine = true;
                    boolean foundCode = false;

                    while ((line = reader.readLine()) != null) {
                        if (isFirstLine) {
                            writer.write(line + System.lineSeparator()); 
                            isFirstLine = false;
                            continue;
                        }

                        String[] data = line.split(",");
                        if (data.length == 2) {
                            String code = data[0].trim();
                            int percentOff = Integer.parseInt(data[1].trim());

                            if (code.equals(existingCode)) {
                                // Modify the discount percentage
                                percentOff = newPercentOff;
                                foundCode = true;
                            }

                            line = code + "," + percentOff;
                        }

                        writer.write(line + System.lineSeparator());
                    }

                    if (!foundCode) {
                        System.out.println("Discount code " + existingCode + " not found in the CSV file.");
                    } else {
                        if (tempFile.renameTo(csvFile)) {
                            System.out.println("Discount code " + existingCode + " modified to " + newPercentOff + "% successfully.");
                        } else {
                            System.err.println("Error replacing the original CSV file with the updated file.");
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Error while modifying discount code in CSV file: " + e.getMessage());
            }
        } else {
            System.out.println("Discount code " + existingCode + " does not exist.");
        }
    }
    
    
    
    public void deleteDiscountCode(String codeToDelete) {
        if (discountCodeExistsInCSV(codeToDelete)) {
            try {
                File csvFile = new File(DISCOUNTS_CSV_PATH);
                File tempFile = new File("tempo_discounts.csv");

                try (BufferedReader reader = new BufferedReader(new FileReader(csvFile));
                     BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

                    String line;
                    boolean isFirstLine = true;
                    boolean foundCode = false;

                    while ((line = reader.readLine()) != null) {
                        if (isFirstLine) {
                            writer.write(line + System.lineSeparator()); 
                            isFirstLine = false;
                            continue;
                        }

                        String[] data = line.split(",");
                        if (data.length == 2) {
                            String code = data[0].trim();

                            if (code.equals(codeToDelete)) {
                                foundCode = true;
                                continue;
                            }
                        }

                        writer.write(line + System.lineSeparator());
                    }

                    if (!foundCode) {
                        System.out.println("Discount code " + codeToDelete + " not found");
                    } else {
                        if (tempFile.renameTo(csvFile)) {
                            System.out.println("Discount code " + codeToDelete + " deleted successful.");
                        } else {
                            System.err.println("Error replacing the original CSV file with the updated file.");
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Errordeleting discount code in CSV file: " + e.getMessage());
            }
        } else {
            System.out.println("Discount code " + codeToDelete + " does not exist.");
        }
    }
    
    
    public void clearCSVFileDiscount() {
        try (FileWriter writer = new FileWriter("discounts.csv", false)) {
            writer.write("");
            System.out.println("CSV file cleared.");
        } catch (IOException e) {
            System.out.println("Error clearing CSV file " + e.getMessage());
        }
    }

}






