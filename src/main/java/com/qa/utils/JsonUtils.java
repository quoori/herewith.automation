package com.qa.utils;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonUtils {

    public static Object readJsonFile(String filename) throws IOException {
        return readJsonFile("\\src\\test\\resources\\Testdata\\", filename);
    }

    public static Object readJsonFile(String path, String jsonFile) throws IOException {
        Object json = null;
        String filename = null;
        try {
            filename = SystemProperties.getUserDirectory() + path + jsonFile;
            FileReader reader = new FileReader(filename);
            String jsonString = "";
            int temp = 0;
            while ((temp = reader.read()) != -1) {
                if (temp != 10 && temp != 13 && temp != 9) {
                    jsonString += (char) temp;
                }
            }
            reader.close();
            JSONParser parser = new JSONParser();
            json = parser.parse(jsonString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return json;
    }
}
