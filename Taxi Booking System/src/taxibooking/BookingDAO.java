package taxibooking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {
    private Connection connection;
    private ConfigManager config = new ConfigManager();

    public BookingDAO() {
        try {
			connection = DatabaseManager.getConnection();
		} catch (SQLException exception) {
			// TODO Auto-generated catch block
			exception.printStackTrace();
		}
    }

    public void addBooking(Booking booking) throws SQLException {
        String query = config.getProperty("booking.addBooking");
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, booking.getUser().getUserId());
            statement.setInt(2, booking.getTaxi().getTaxiId());
            statement.setTimestamp(3, java.sql.Timestamp.valueOf(booking.getStartTime()));
            statement.setTimestamp(4, java.sql.Timestamp.valueOf(booking.getEndTime()));
            statement.executeUpdate();
        }
    }

    public Booking getBookingById(int bookingId) throws SQLException {
        String query = config.getProperty("booking.getBookingById");
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, bookingId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int userId = resultSet.getInt("user_id");
                    int taxiId = resultSet.getInt("taxi_id");
                    LocalDateTime startTime = resultSet.getTimestamp("start_time").toLocalDateTime();
                    LocalDateTime endTime = resultSet.getTimestamp("end_time").toLocalDateTime();

                    UserDAO userDAO = new UserDAO();
                    User user = userDAO.getUserById(userId);

                    TaxiDAO taxiDAO = new TaxiDAO();
                    Taxi taxi = taxiDAO.getTaxiById(taxiId);

                    return new Booking(bookingId, user, taxi, startTime, endTime);
                }
            }
        }
        return null;
    }

    public List<Booking> getAllBookings() throws SQLException {
        List<Booking> bookingList = new ArrayList<>();
        String query = config.getProperty("booking.getAllBookings");
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int bookingId = resultSet.getInt("booking_id");
                int userId = resultSet.getInt("user_id");
                int taxiId = resultSet.getInt("taxi_id");
                LocalDateTime startTime = resultSet.getTimestamp("start_time").toLocalDateTime();
                LocalDateTime endTime = resultSet.getTimestamp("end_time").toLocalDateTime();

                UserDAO userDAO = new UserDAO();
                User user = userDAO.getUserById(userId);

                TaxiDAO taxiDAO = new TaxiDAO();
                Taxi taxi = taxiDAO.getTaxiById(taxiId);

                bookingList.add(new Booking(bookingId, user, taxi, startTime, endTime));
            }
        }
        return bookingList;
    }

    public void updateBooking(Booking booking) throws SQLException {
        String query = config.getProperty("booking.updateBooking");
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, booking.getUser().getUserId());
            statement.setInt(2, booking.getTaxi().getTaxiId());
            statement.setTimestamp(3, java.sql.Timestamp.valueOf(booking.getStartTime()));
            statement.setTimestamp(4, java.sql.Timestamp.valueOf(booking.getEndTime()));
            statement.setInt(5, booking.getBookingId());
            statement.executeUpdate();
        }
    }

    public void deleteBooking(int bookingId) throws SQLException {
        String query = config.getProperty("booking.deleteBooking");
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, bookingId);
            statement.executeUpdate();
        }
    }
}
