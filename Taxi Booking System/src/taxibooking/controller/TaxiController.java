package taxibooking.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import taxibooking.ConfigManager;
import taxibooking.DatabaseManager;
import taxibooking.model.Taxi;

public class TaxiController {
    private Connection connection;
    private ConfigManager configManager = new ConfigManager();
    private static final Logger logger = Logger.getLogger(TaxiController.class.getName());

    public TaxiController() {
        connection = DatabaseManager.getConnection();
    }

    public void addTaxi(Taxi taxi) {
        String query = configManager.getProperty("taxi.addTaxi");
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, taxi.getTaxiId());
            statement.setBoolean(2, taxi.isAvailable());
            statement.executeUpdate();
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, "Error occurred while adding a taxi", exception);
        }
    }

    public Taxi getTaxiById(int taxiId) {
        String query = configManager.getProperty("taxi.getTaxiById");
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, taxiId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    boolean available = resultSet.getBoolean("available");
                    return new Taxi(taxiId, available, query);
                }
            } 
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, "Error occurred while getting a taxi by ID", exception);
        }
        return null;
    }

    public List<Taxi> getAllTaxis() {
        List<Taxi> taxiList = new ArrayList<>();
        String query = configManager.getProperty("taxi.getAllTaxis");
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int taxiId = resultSet.getInt("taxi_id");
                boolean available = resultSet.getBoolean("available");
                taxiList.add(new Taxi(taxiId, available, query));
            }
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, "Error occurred while getting all taxis", exception);
        }
        return taxiList;
    }

    public void updateTaxi(Taxi taxi) {
        String query = configManager.getProperty("taxi.updateTaxi");
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setBoolean(1, taxi.isAvailable());
            statement.setInt(2, taxi.getTaxiId());
            statement.executeUpdate();
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, "Error occurred while updating a taxi", exception);
        }
    }

    public void deleteTaxi(int taxiId) {
        String query = configManager.getProperty("taxi.deleteTaxi");
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, taxiId);
            statement.executeUpdate();
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, "Error occurred while deleting a taxi", exception);
        }
    }
}
