package taxibooking;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Admin {
    private TaxiDAO taxiDAO;
    private BookingDAO bookingDAO;

    public Admin() {
        taxiDAO = new TaxiDAO();
        bookingDAO = new BookingDAO();
    }

    public void viewAllBookings() {
        try {
            List<Booking> allBookings = bookingDAO.getAllBookings();
            if (allBookings.isEmpty()) {
                System.out.println("No bookings found.");
            } else {
                System.out.println("All Bookings:");
                for (Booking booking : allBookings) {
                    System.out.println("Booking ID: " + booking.getBookingId() +
                            ", User ID: " + booking.getUser().getUserId() +
                            ", Taxi ID: " + booking.getTaxi().getTaxiId() +
                            ", Start Time: " + booking.getStartTime() +
                            ", End Time: " + booking.getEndTime());
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void manageTaxis() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. Add Taxi");
        System.out.println("2. Update Taxi Availability");
        System.out.println("3. Delete Taxi");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                addTaxi();
                break;
            case 2:
                updateTaxiAvailability();
                break;
            case 3:
                deleteTaxi();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
    }

    private void addTaxi() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the Taxi ID: ");
        int taxiId = scanner.nextInt();

        try {
            Taxi newTaxi = new Taxi(taxiId, false);
            taxiDAO.addTaxi(newTaxi);
            System.out.println("Taxi added successfully.");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private void updateTaxiAvailability() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the Taxi ID: ");
        int taxiId = scanner.nextInt();

        try {
            Taxi taxi = taxiDAO.getTaxiById(taxiId);
            if (taxi == null) {
                System.out.println("Taxi with ID " + taxiId + " not found.");
                return;
            }

            System.out.print("Enter availability (true/false): ");
            boolean availability = scanner.nextBoolean();

            taxi.setAvailable(availability);
            taxiDAO.updateTaxi(taxi);
            System.out.println("Taxi availability updated successfully.");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private void deleteTaxi() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the Taxi ID: ");
        int taxiId = scanner.nextInt();

        try {
            taxiDAO.deleteTaxi(taxiId);
            System.out.println("Taxi deleted successfully.");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
