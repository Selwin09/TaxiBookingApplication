/*
 * Title: Taxi Booking System
 * Author: Selwin isac neilsingh J
 * Created on: 12/10/2022
 * Last Modified on: 04/08/2023
 * Reviewed by: Anitha
 */


package taxibooking;

//import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import taxibooking.controller.TaxiController;
import taxibooking.model.Taxi;
import taxibooking.view.Admin;
import taxibooking.view.Customer;

public class Main {
    private ConfigManager configManager = new ConfigManager();
    private static final Logger logger = Logger.getLogger(Main.class.getName());

	
    public static void main(String[] args) {
        try {
			LogManager.getLogManager().readConfiguration(Main.class.getResourceAsStream("/logging.properties"));
		} catch (IOException exception) {
            System.err.println("Error occurred while configuring logger.");
		}
    	
    	
        Scanner scanner = new Scanner(System.in);
        Customer customer = new Customer();
        Admin admin = new Admin();
        
        addInitialTaxis();


        boolean isRunning = true;
        while (isRunning) {
            System.out.println("Welcome to the Taxi Booking System!");
            System.out.println("1. Customer Login");
            System.out.println("2. Admin Login");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            
            switch (choice) {
                case 1:
				try {
					customer.login();
				} catch (SQLException exception) {
					System.err.println("Error occured");
				}
                    if (customer.getCurrentUser() != null) {
                        handleCustomerMenu(customer);
                    }
                    break;
                case 2:
                    adminLogin(scanner, admin);
                    break;
                case 3:
                    System.out.println("Thank you for using our Taxi Booking System :)");
                    isRunning = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }

        scanner.close();
    }
    
    private static void addInitialTaxis() {
    	TaxiController taxiController = new TaxiController();
    
    List<Taxi> existingTaxis = taxiController.getAllTaxis();
	if (existingTaxis.size() >= 10) {
	    return;
	}

        for (int i = 1; i <= 10; i++) {
            Taxi taxi = new Taxi(i, true, null); 
            taxiController.addTaxi(taxi);
        }
    }

    private static void adminLogin(Scanner scanner, Admin admin) {
    	Properties configProperties = loadConfigProperties();
        if (configProperties == null) {
            System.out.println("Error loading configuration.");
            return;
        }

        String expectedAdminPassword = configProperties.getProperty("admin.password");
    	
    	System.out.print("Enter Admin Password: ");
        String password = scanner.next();

        if (password.equals(expectedAdminPassword)) { 
            handleAdminMenu(admin);
        } else {
            logger.log(Level.SEVERE,"Incorrect Admin Password. Please try again.");
        }
    }
    
    private static Properties loadConfigProperties() {
        Properties configProperties = new Properties();
        try (InputStream inputStream = Main.class.getResourceAsStream("/config.properties")) {
            configProperties.load(inputStream);
            return configProperties;
        } catch (IOException exception) {
            System.out.println("Error loading configuration.");
            return null;
        }
    }

    private static void handleCustomerMenu(Customer customer) {
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        while (isRunning) {
            System.out.println("\nCustomer Menu:");
            System.out.println("1. Book Taxi");
            System.out.println("2. View Bookings");
            System.out.println("3. Logout");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                	System.out.print("Enter the pickup location: ");
                    String pickupLocation = scanner.nextLine();
                    System.out.print("Enter the destination: ");
                    String destination = scanner.nextLine();
                    customer.bookTaxi(pickupLocation, destination);
                    break;
                case 2:
                    customer.viewBookings();
                    break;
                case 3:
                    logger.info("Logging out...");
                    customer.setCurrentUser(null);
                    isRunning = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    private static void handleAdminMenu(Admin admin) {
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        while (isRunning) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. View All Bookings");
            System.out.println("2. Manage Taxis");
            System.out.println("3. Logout");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    admin.viewAllBookings();
                    break;
                case 2:
                    admin.manageTaxis();
                    break;
                case 3:
                    logger.info("Logging out...");
                    isRunning = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }
}
