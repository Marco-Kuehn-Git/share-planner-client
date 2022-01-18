package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import res.DataController;

import java.io.IOException;
import java.util.Objects;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        System.out.println("Ignore 'Illegal reflective access operation'-Warning. See https://github.com/sshahine/JFoenix/issues/1170");

        // Load login-scene
        FXMLLoader fxmlLoaderLogin = new FXMLLoader(MainApplication.class.getResource("../users/login.fxml"));
        Scene sceneLogin = new Scene(fxmlLoaderLogin.load(), 650, 500);
        sceneLogin.getStylesheets().add(Objects.requireNonNull(
                MainApplication.class.getResource("../users/login.css")).toExternalForm()
        );
        Stage stageLogin = new Stage();
        stageLogin.setTitle("Anmelden");
        stageLogin.setScene(sceneLogin);
        stageLogin.showAndWait();

        if (DataController.USER_ID >= 0) {
            // Load main-scene
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 1200, 700);
            scene.getStylesheets().add(Objects.requireNonNull(
                    MainApplication.class.getResource("main-view.css")).toExternalForm()
            );
            stage.setTitle("SharePlaner");
            stage.setScene(scene);
            stage.show();

            System.out.println("Logged in...");
        }
    }

    public static void main(String[] args) {
        launch();
    }
}