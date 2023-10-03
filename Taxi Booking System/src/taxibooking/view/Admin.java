package taxibooking.view;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import taxibooking.controller.BookingController;
import taxibooking.controller.TaxiController;
import taxibooking.model.Booking;
import taxibooking.model.Taxi;

public class Admin {
    private TaxiController taxiController;
    private BookingController bookingController;
    private static final Logger logger = Logger.getLogger(Admin.class.getName());


    public Admin() {
        taxiController = new TaxiController();
        bookingController = new BookingController();
    }

    public void viewAllBookings() {
        List<Booking> allBookings = bookingController.getAllBookings();
		if (allBookings.isEmpty()) {
		    System.out.println("No bookings found.");
		} else {
		    logger.info("All Bookings:");
		    for (Booking booking : allBookings) {
		        System.out.println("Booking ID: " + booking.getBookingId() +
		                ", User ID: " + booking.getUser().getUserId() +
		                ", Taxi ID: " + booking.getTaxi().getTaxiId() +
		                ", Start Time: " + booking.getStartTime() +
		                ", End Time: " + booking.getEndTime());
		    }
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

        Taxi newTaxi = new Taxi(taxiId, false, null);
		taxiController.addTaxi(newTaxi);
		logger.info("Admin added a new taxi with ID " + taxiId);
    }

    private void updateTaxiAvailability() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the Taxi ID: ");
        int taxiId = scanner.nextInt();

        Taxi taxi = taxiController.getTaxiById(taxiId);
		if (taxi == null) {
		    System.out.println("Taxi with ID " + taxiId + " not found.");
		    return;
		}

		System.out.print("Enter availability (true/false): ");
		boolean availability = scanner.nextBoolean();

		taxi.setAvailable(availability);
		taxiController.updateTaxi(taxi);
		logger.info("Admin updated availability for taxi with ID " + taxiId);
    }

    private void deleteTaxi() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the Taxi ID: ");
        int taxiId = scanner.nextInt();

        taxiController.deleteTaxi(taxiId);
		logger.info("Admin deleted taxi with ID " + taxiId);
    }
}
