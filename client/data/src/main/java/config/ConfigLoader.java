package config;

import com.fasterxml.jackson.databind.ObjectMapper;
import res.DataController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ConfigLoader {

    public static Config load(){
        try {
            String jsonString = Files.readString(Paths.get("config.json"));

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.findAndRegisterModules();
            return objectMapper.readValue(jsonString, Config.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void save(Config config){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        try {
            Files.writeString(Paths.get(
                    "config.json"),
                    objectMapper.writeValueAsString(config)
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
