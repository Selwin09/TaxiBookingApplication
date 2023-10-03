package taxibooking.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import taxibooking.ConfigManager;
import taxibooking.DatabaseManager;
import taxibooking.model.Booking;
import taxibooking.model.Taxi;
import taxibooking.model.User;

public class BookingController {
    private Connection connection;
    private ConfigManager configManager = new ConfigManager();
    private static final Logger logger = Logger.getLogger(BookingController.class.getName());

    public BookingController() {
        connection = DatabaseManager.getConnection();
    }

    public void addBooking(Booking booking) {
        String query = configManager.getProperty("booking.addBooking");
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, booking.getUser().getUserId());
            statement.setInt(2, booking.getTaxi().getTaxiId());
            statement.setTimestamp(3, java.sql.Timestamp.valueOf(booking.getStartTime()));
            statement.setTimestamp(4, java.sql.Timestamp.valueOf(booking.getEndTime()));
            statement.setString(5, booking.getPickupLocation());
            statement.setString(6, booking.getDestination());
            statement.executeUpdate();
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, "Error occurred while adding a booking", exception);
        }
    }

    public Booking getBookingById(int bookingId) {
        String query = configManager.getProperty("booking.getBookingById");
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, bookingId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int userId = resultSet.getInt("user_id");
                    int taxiId = resultSet.getInt("taxi_id");
                    LocalDateTime startTime = resultSet.getTimestamp("start_time").toLocalDateTime();
                    LocalDateTime endTime = resultSet.getTimestamp("end_time").toLocalDateTime();

                    UserController userController = new UserController();
                    User user = userController.getUserById(userId);

                    TaxiController taxiController = new TaxiController();
                    Taxi taxi = taxiController.getTaxiById(taxiId);

                    return new Booking(bookingId, user, taxi, startTime, endTime, query, query);
                }
            }
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, "Error occurred while getting a booking by ID", exception);
        }
        return null;
    }

    public List<Booking> getAllBookings() {
        List<Booking> bookingList = new ArrayList<>();
        String query = configManager.getProperty("booking.getAllBookings");
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int bookingId = resultSet.getInt("booking_id");
                int userId = resultSet.getInt("user_id");
                int taxiId = resultSet.getInt("taxi_id");
                LocalDateTime startTime = resultSet.getTimestamp("start_time").toLocalDateTime();
                LocalDateTime endTime = resultSet.getTimestamp("end_time").toLocalDateTime();

                UserController userController = new UserController();
                User user = userController.getUserById(userId);

                TaxiController taxiController = new TaxiController();
                Taxi taxi = taxiController.getTaxiById(taxiId);

                bookingList.add(new Booking(bookingId, user, taxi, startTime, endTime, query, query));
            }
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, "Error occurred while getting all bookings", exception);
        }
        return bookingList;
    }

    public void updateBooking(Booking booking) {
        String query = configManager.getProperty("booking.updateBooking");
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, booking.getUser().getUserId());
            statement.setInt(2, booking.getTaxi().getTaxiId());
            statement.setTimestamp(3, java.sql.Timestamp.valueOf(booking.getStartTime()));
            statement.setTimestamp(4, java.sql.Timestamp.valueOf(booking.getEndTime()));
            statement.setInt(5, booking.getBookingId());
            statement.executeUpdate();
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, "Error occurred while updating a booking", exception);
        }
    }

    public void deleteBooking(int bookingId) {
        String query = configManager.getProperty("booking.deleteBooking");
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, bookingId);
            statement.executeUpdate();
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, "Error occurred while deleting a booking", exception);
        }
    }
}
