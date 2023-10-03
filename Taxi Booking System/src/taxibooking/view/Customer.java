package taxibooking.view;

import java.io.Console;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import taxibooking.controller.BookingController;
import taxibooking.controller.TaxiController;
import taxibooking.controller.UserController;
import taxibooking.model.Booking;
import taxibooking.model.Taxi;
import taxibooking.model.User;

public class Customer {
    private User currentUser;
    private boolean loggedIn;
    private UserController userController;
    private TaxiController taxiController;
    private BookingController bookingController;
    private Console console;
    private static final Logger logger = Logger.getLogger(Customer.class.getName());


    public Customer() {
        loggedIn = false;
        userController = new UserController();
        taxiController = new TaxiController();
        bookingController = new BookingController();
        console = System.console();
    }

    public void login() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        String password = maskedPasswordInput("Enter your password: ");

        currentUser = userController.getUserByEmailAndPassword(email, password);
        if (currentUser == null) {
            logger.log(Level.SEVERE,"Invalid email or password. Please try again.");
            loggedIn = false;
        } else {
            logger.info("Login successful");
            loggedIn = true;
        }
    }

    private String maskedPasswordInput(String prompt) {
       
    	if (console == null) {
            Scanner scanner = new Scanner(System.in);
            System.out.print(prompt);
            return scanner.nextLine();
        } else {
            char[] passwordChars = console.readPassword(prompt);
            return new String(passwordChars);
        }
    }


    public void bookTaxi(String pickupLocation, String destination) {
        if (!loggedIn) {
            logger.warning("User attempted to book a taxi without logging in.");
            System.out.println("You need to log in first.");
            return;
        }

        if (currentUser == null) {
            System.out.println("You need to log in first.");
            return;
        }

        List<Taxi> availableTaxis;
        availableTaxis = taxiController.getAllTaxis();
		if (availableTaxis.isEmpty()) {
		    System.out.println("No available taxis at the moment. Please try again later.");
		    return;
		}

		System.out.println("Available Taxis:");
		for (Taxi taxi : availableTaxis) {
		    System.out.println("Taxi ID: " + taxi.getTaxiId());
		}

		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter the ID of the taxi you want to book: ");
		int taxiId = scanner.nextInt();

		Taxi selectedTaxi = taxiController.getTaxiById(taxiId);
		if (selectedTaxi == null || !selectedTaxi.isAvailable()) {
		    logger.info("Invalid taxi ID or the taxi is not available.");
		} else {
		    LocalDateTime startTime = LocalDateTime.now();
		    LocalDateTime endTime = startTime.plusHours(1); 
		    
		    Booking newBooking = new Booking(0, currentUser, selectedTaxi, startTime, endTime, pickupLocation, destination);
		    bookingController.addBooking(newBooking);

		    selectedTaxi.setAvailable(false);
		    taxiController.updateTaxi(selectedTaxi);

		    logger.info("Booking successful! Enjoy your ride.");
		}
    }

    public void viewBookings() {
        if (!loggedIn) {
            System.out.println("You need to log in first.");
            return;
        }

        if (currentUser == null) {
            logger.warning("User attempted to view bookings without logging in.");
            System.out.println("You need to log in first.");
            return;
        }

        Booking userBooking = bookingController.getBookingById(currentUser.getUserId());
		if (userBooking == null) {
		    System.out.println("You have no bookings yet.");
		} else {
		    System.out.println("Your Bookings:");
		    System.out.println("Booking ID: " + userBooking.getBookingId() +
		            ", Taxi ID: " + userBooking.getTaxi().getTaxiId() +
		            ", Start Time: " + userBooking.getStartTime() +
		            ", End Time: " + userBooking.getEndTime());
		}
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        currentUser = user;
    }
}
