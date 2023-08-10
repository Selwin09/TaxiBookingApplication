/*
 * Title: Taxi Booking System
 * Author: Selwin isac neilsingh J
 * Created on: 12/10/2022
 * Last Modified on: 04/08/2023
 * Reviewed by: Anitha
 */


package taxibooking;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
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
					exception.printStackTrace();
				}
                    if (customer.getCurrentUser() != null) {
                        handleCustomerMenu(customer);
                    }
                    break;
                case 2:
                    adminLogin(scanner, admin);
                    break;
                case 3:
                    System.out.println("Thank you for using our Taxi Booking System!");
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
        TaxiDAO taxiDAO = new TaxiDAO();
    
    try {
        List<Taxi> existingTaxis = taxiDAO.getAllTaxis();
        if (existingTaxis.size() >= 10) {
            //System.out.println("Taxis are already added to the database.");
            return;
        }
    } catch (SQLException exception) {
        exception.printStackTrace();
        return;
    }

        for (int i = 1; i <= 10; i++) {
            Taxi taxi = new Taxi(i, true); 
            try {
                taxiDAO.addTaxi(taxi);
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    private static void adminLogin(Scanner scanner, Admin admin) {
        System.out.print("Enter Admin Password: ");
        String password = scanner.next();

        if (password.equals("adminpass")) { 
            handleAdminMenu(admin);
        } else {
            System.out.println("Incorrect Admin Password. Please try again.");
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

            switch (choice) {
                case 1:
                    customer.bookTaxi();
                    break;
                case 2:
                    customer.viewBookings();
                    break;
                case 3:
                    System.out.println("Logging out...");
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
                    System.out.println("Logging out...");
                    isRunning = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }
}
