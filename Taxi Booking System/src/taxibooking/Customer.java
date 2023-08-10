package taxibooking;

import java.io.Console;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class Customer {
    private User currentUser;
    private boolean loggedIn;
    private UserDAO userDAO;
    private TaxiDAO taxiDAO;
    private BookingDAO bookingDAO;
    private Console console;

    public Customer() {
    	loggedIn = false;
        userDAO = new UserDAO();
        taxiDAO = new TaxiDAO();
        bookingDAO = new BookingDAO();
        console = System.console();
    }

    public void login() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        String password = maskedPasswordInput("Enter your password: ");


        currentUser = userDAO.getUserByEmailAndPassword(email, password);
		if (currentUser == null) {
		    System.out.println("Invalid email or password. Please try again.");
		    loggedIn = false;
		} else {
		    System.out.println("Login successful!");
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

    public void bookTaxi() {
    	
    	if (!loggedIn) {
            System.out.println("You need to log in first.");
            return;
        }
    	
        if (currentUser == null) {
            System.out.println("You need to log in first.");
            return;
        }

        List<Taxi> availableTaxis;
        try {
            availableTaxis = taxiDAO.getAllTaxis();
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

            Taxi selectedTaxi = taxiDAO.getTaxiById(taxiId);
            if (selectedTaxi == null || !selectedTaxi.isAvailable()) {
                System.out.println("Invalid taxi ID or the taxi is not available.");
            } else {
                LocalDateTime startTime = LocalDateTime.now();
                LocalDateTime endTime = startTime.plusHours(1); // Assuming the ride lasts for 1 hour

                Booking newBooking = new Booking(0, currentUser, selectedTaxi, startTime, endTime);
                bookingDAO.addBooking(newBooking);

                selectedTaxi.setAvailable(false);
                taxiDAO.updateTaxi(selectedTaxi);

                System.out.println("Booking successful! Enjoy your ride.");
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void viewBookings() {
    	
    	if (!loggedIn) {
            System.out.println("You need to log in first.");
            return;
        }
    	
        if (currentUser == null) {
            System.out.println("You need to log in first.");
            return;
        }

        try {
        	 Booking userBooking = bookingDAO.getBookingById(currentUser.getUserId());
             if (userBooking == null) {
                System.out.println("You have no bookings yet.");
            } else {
                System.out.println("Your Bookings:");
                    System.out.println("Booking ID: " + userBooking.getBookingId() +
                            ", Taxi ID: " + userBooking.getTaxi().getTaxiId() +
                            ", Start Time: " + userBooking.getStartTime() +
                            ", End Time: " + userBooking.getEndTime());
                }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User user) {
		currentUser = user;
	}
}
