package config;

import com.fasterxml.jackson.databind.ObjectMapper;

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
            System.out.println("config.json missing");
            Config config = new Config(false, -1, "");
            save(config);
            return config;
        }
    }

    public static void save(Config config){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        try {
            Files.writeString(
                    Paths.get("config.json"),
                    objectMapper.writeValueAsString(config)
                            .replace(",", ",\n\t")
                            .replace("{", "{\n\t")
                            .replace("}", "\n}")
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
