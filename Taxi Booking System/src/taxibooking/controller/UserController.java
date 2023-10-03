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
import taxibooking.model.User;

public class UserController {
    private Connection connection;
    private ConfigManager configManager = new ConfigManager();
    private static final Logger logger = Logger.getLogger(UserController.class.getName());

    public UserController() {
        connection = DatabaseManager.getConnection();
    }

    public void addUser(User user) throws SQLException {
        String query = configManager.getProperty("user.add");
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.executeUpdate();
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, "Error occurred while adding a user", exception);
        }
    }

    public User getUserById(int userId) throws SQLException {
    	String query = configManager.getProperty("user.getById");
    	try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String email = resultSet.getString("email");
                    String password = resultSet.getString("password");
                    return new User(userId, name, email, password);
                }
            }catch (SQLException exception) {
                logger.log(Level.SEVERE, "Error occurred while getting a user by ID", exception);
            }
        }
        return null;
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        String query = configManager.getProperty("user.getAll");
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                userList.add(new User(userId, name, email, password));
            }
        }
        catch (SQLException exception) {
            logger.log(Level.SEVERE, "Error occurred while getting all users", exception);
        }
        return userList;
    }

    public void updateUser(User user) {
        String query = configManager.getProperty("user.update");
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setInt(4, user.getUserId());
            statement.executeUpdate();
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, "Error occurred while updating a user", exception);
        }
    }

    public void deleteUser(int userId) {
    	String query = configManager.getProperty("user.delete");
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.executeUpdate();
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, "Error occurred while deleting a user", exception);
        }
    }

    public User getUserByEmailAndPassword(String email, String password) {
    	String query = configManager.getProperty("user.getByEmailAndPassword");
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int userId = resultSet.getInt("user_id");
                    String name = resultSet.getString("name");
                    return new User(userId, name, email, password);
                }
            }
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, "Error occurred while getting a user by email and password", exception);
        }
        return null;
    }
}
