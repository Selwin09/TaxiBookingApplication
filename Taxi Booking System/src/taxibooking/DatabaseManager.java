package taxibooking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseManager {
    private static Connection connection;
    private static final ConfigManager config = new ConfigManager();
    private static final Logger logger = Logger.getLogger(DatabaseManager.class.getName());

    public static Connection getConnection() {
        if (connection == null) {
            try {
                String dbUrl = config.getProperty("db.url");
                String dbUser = config.getProperty("db.username");
                String dbPassword = config.getProperty("db.password");

                connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                Class.forName(config.getProperty("db.driver"));
            } catch (SQLException | ClassNotFoundException exception) {
                logger.log(Level.SEVERE, "Error while establishing database connection.", exception);
            }
        }
        return connection;
    }
}
