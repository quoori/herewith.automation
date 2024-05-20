package com.qa.base;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyManager extends BaseSetup {
    private static final Properties properties = new Properties();
    private static InputStream inputStream;

    public Properties getProperties(String fileName) throws IOException {
        if (properties.isEmpty()) {
            logger().info("loading " + fileName + " properties");
            try {
                inputStream = PropertyManager.class.getResourceAsStream("/" + fileName);
                properties.load(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
                logger().fatal("Unable to load the property file: " + fileName + " aborting" + e.getMessage());
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        }
        return properties;
    }
}
