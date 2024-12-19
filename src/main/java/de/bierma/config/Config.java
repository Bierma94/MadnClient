package de.bierma.config;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Config
 *
 * @author Jannes Bierma
 * @version 1.0 - 17.12.2024
 */
public class Config {

    private static final String CONFIG_FILE = "src/main/resources/config.properties";
    public static final String BASE_URL = "http://localhost:10000";

    private static final Properties properties = new Properties();
    static {
        try (FileInputStream in = new FileInputStream(CONFIG_FILE)) {
            properties.load(in);
        } catch (IOException e) {
            System.err.println("Could not load config file: " + CONFIG_FILE);
        }
    }

    public static String getUsername() {
        return properties.getProperty("username");
    }

    public static void setUsername(String username) {
        properties.setProperty("username", username);
        try (FileOutputStream out = new FileOutputStream(CONFIG_FILE)) {
            properties.store(out, null);
        } catch (IOException e) {
            System.err.println("Could not save config file: " + CONFIG_FILE);
        }
    }

    public static String getToken() {
        return properties.getProperty("token");
    }

    public static void setToken(String token) {
        properties.setProperty("token", token);
        try (FileOutputStream out = new FileOutputStream(CONFIG_FILE)) {
            properties.store(out, null);
        } catch (IOException e) {
            System.err.println("Could not save config file: " + CONFIG_FILE);
        }
    }
}
