// Fichier: src/main/java/org/example/config/CloudinaryConfig.java
package org.example.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class CloudinaryConfig {
    private static Cloudinary cloudinary;
    private static Properties properties;

    static {
        loadProperties();
        initializeCloudinary();
    }

    private static void loadProperties() {
        properties = new Properties();
        try (InputStream input = CloudinaryConfig.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (input == null) {
                System.err.println("Fichier application.properties introuvable");
                return;
            }
            properties.load(input);
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de application.properties: " + e.getMessage());
        }
    }

    private static void initializeCloudinary() {
        String cloudName = properties.getProperty("cloudinary.cloud_name");
        String apiKey = properties.getProperty("cloudinary.api_key");
        String apiSecret = properties.getProperty("cloudinary.api_secret");

        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);

        cloudinary = new Cloudinary(config);
        System.out.println("✓ Configuration Cloudinary initialisée");
    }

    public static Cloudinary getCloudinary() {
        if (cloudinary == null) {
            initializeCloudinary();
        }
        return cloudinary;
    }

    public static String getBurgerFolder() {
        return properties.getProperty("cloudinary.folder.burger", "brasil_burger/burgers");
    }

    public static String getMenuFolder() {
        return properties.getProperty("cloudinary.folder.menu", "brasil_burger/menus");
    }

    public static String getComplementFolder() {
        return properties.getProperty("cloudinary.folder.complement", "brasil_burger/complements");
    }
}