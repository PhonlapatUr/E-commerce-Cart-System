import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
//Name: Phonlapat Urairong
//ID: 6588114
//Section: 3
public class AdminLogin {
    private static final String CREDENTIALS_FILE_PATH = "admin_credentials.csv";

    public boolean login() {
        @SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);

        System.out.print("Enter username (Please enter 'Ananta'): ");
        String username = scanner.nextLine();

        System.out.print("Enter password (Please enter '1234'): ");
        String password = scanner.nextLine();

        if (isValidCredentials(username, password)) {
            System.out.println("</Login successful. Welcome, " + username + "/>");
            return true;
        } else {
            System.out.println("Login failed. Invalid username or password.");
        }
		return false;
    }

    private boolean isValidCredentials(String username, String password) {
        try (BufferedReader csvReader = new BufferedReader(new FileReader(CREDENTIALS_FILE_PATH))) {
            String row;
            boolean isFirstRow = true; 
            
            while ((row = csvReader.readLine()) != null) {
            	
            	 if (isFirstRow) {
                     isFirstRow = false;
                     continue; // Skip the header row
                 }
            	String[] data = row.split(",");
                if (data.length == 2) {
                    String storedUsername = data[0].trim();
                    String storedPassword = data[1].trim();
                    if (username.equals(storedUsername) && password.equals(storedPassword)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error while reading admin credentials file: " + e.getMessage());
        }

        return false;
    }

    public void addCredentials(String username, String password) {
        try (BufferedWriter csvWriter = new BufferedWriter(new FileWriter(CREDENTIALS_FILE_PATH, true))) {
        	if (new File("admin_credentials.csv").length() == 0) {
                csvWriter.write("Username,Password\n");
            }
            csvWriter.write(username + "," + password + "\n");
            System.out.println("Credentials added to CSV file.");
        } catch (IOException e) {
            System.err.println("Error while adding credentials to CSV file: " + e.getMessage());
        }
    }
}



