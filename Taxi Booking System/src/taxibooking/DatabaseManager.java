package taxibooking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static Connection connection;
    private static final ConfigManager config = new ConfigManager();

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            String dbUrl = config.getProperty("db.url");
            String dbUser = config.getProperty("db.username");
            String dbPassword = config.getProperty("db.password");

            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            try {
                Class.forName(config.getProperty("db.driver"));
            } catch (ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        }
        return connection;
    }
}
