package taxibooking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private Connection connection;
    private ConfigManager config = new ConfigManager();

    public UserDAO() {
        try {
			connection = DatabaseManager.getConnection();
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
    }

    public void addUser(User user) throws SQLException {
        String query = config.getProperty("user.add");
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.executeUpdate();
        }
    }

    public User getUserById(int userId) throws SQLException {
    	String query = config.getProperty("user.getById");
    	try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String email = resultSet.getString("email");
                    String password = resultSet.getString("password");
                    return new User(userId, name, email, password);
                }
            }
        }
        return null;
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> userList = new ArrayList<>();
        String query = config.getProperty("user.getAll");
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
        return userList;
    }

    public void updateUser(User user) throws SQLException {
        String query = config.getProperty("user.update");
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setInt(4, user.getUserId());
            statement.executeUpdate();
        }
    }

    public void deleteUser(int userId) throws SQLException {
    	String query = config.getProperty("user.delete");
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.executeUpdate();
        }
    }

    public User getUserByEmailAndPassword(String email, String password) {
    	String query = config.getProperty("user.getByEmailAndPassword");
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
            exception.printStackTrace();
        }
        return null;
    }

}
