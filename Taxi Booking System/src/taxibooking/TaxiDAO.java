package taxibooking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TaxiDAO {
    private Connection connection;
    private ConfigManager config = new ConfigManager();

    public TaxiDAO() {
        try {
			connection = DatabaseManager.getConnection();
		} catch (SQLException exception) {
			// TODO Auto-generated catch block
			exception.printStackTrace();
		}
    }

    public void addTaxi(Taxi taxi) throws SQLException {
        String query = config.getProperty("taxi.addTaxi");
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, taxi.getTaxiId());
            statement.setBoolean(2, taxi.isAvailable());
            statement.executeUpdate();
        }
    }

    public Taxi getTaxiById(int taxiId) throws SQLException {
        String query = config.getProperty("taxi.getTaxiById");
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, taxiId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    boolean available = resultSet.getBoolean("available");
                    return new Taxi(taxiId, available);
                }
            }
        }
        return null;
    }

    public List<Taxi> getAllTaxis() throws SQLException {
        List<Taxi> taxiList = new ArrayList<>();
        String query = config.getProperty("taxi.getAllTaxis");
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int taxiId = resultSet.getInt("taxi_id");
                boolean available = resultSet.getBoolean("available");
                taxiList.add(new Taxi(taxiId, available));
            }
        }
        return taxiList;
    }

    public void updateTaxi(Taxi taxi) throws SQLException {
        String query = config.getProperty("taxi.updateTaxi");
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setBoolean(1, taxi.isAvailable());
            statement.setInt(2, taxi.getTaxiId());
            statement.executeUpdate();
        }
    }

    public void deleteTaxi(int taxiId) throws SQLException {
        String query = config.getProperty("taxi.deleteTaxi");
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, taxiId);
            statement.executeUpdate();
        }
    }
}
