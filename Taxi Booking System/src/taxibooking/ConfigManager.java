package taxibooking;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigManager {
    private Properties properties = new Properties();
    private static final Logger logger = Logger.getLogger(ConfigManager.class.getName());

    public ConfigManager() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(inputStream);
        } catch (IOException exception) {
            logger.log(Level.SEVERE, "Error occurred while loading config.properties", exception);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
