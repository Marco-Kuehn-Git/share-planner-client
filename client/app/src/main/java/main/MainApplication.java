//Marc Beyer//
package main;

import config.Config;
import config.ConfigLoader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import res.DataController;
import res.HttpRequest;

import java.io.IOException;
import java.util.Objects;


public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Config config = ConfigLoader.load();
        if(config == null){
            config = new Config(false, -1, "");
        }

        System.out.println("Ignore 'Illegal reflective access operation'-Warning. See https://github.com/sshahine/JFoenix/issues/1170");

        if(
            !config.isSaveLogin()
            || !new DataController().loginWithToken(config.getId(), config.getToken())
        ){
            // Load login-scene
            loadLoginScene();
        }

        if (DataController.USER_ID >= 0) {
            if(config.isSaveLogin()){
                config.setId(DataController.USER_ID);
                config.setToken(HttpRequest.TOKEN);
                ConfigLoader.save(config);
            }
            // Load main-scene
            loadMainScene(stage);

            System.out.println("Logged in...");
        }
    }

    private void loadMainScene(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 1200, 700);
        scene.getStylesheets().add(Objects.requireNonNull(
                MainApplication.class.getResource("main-view.css")).toExternalForm()
        );
        stage.setTitle("SharePlaner");
        stage.setScene(scene);
        stage.show();
    }

    private void loadLoginScene() throws IOException {
        FXMLLoader fxmlLoaderLogin = new FXMLLoader(MainApplication.class.getResource("../users/login.fxml"));
        Scene sceneLogin = new Scene(fxmlLoaderLogin.load(), 650, 500);
        sceneLogin.getStylesheets().add(Objects.requireNonNull(
                MainApplication.class.getResource("../users/login.css")).toExternalForm()
        );
        Stage stageLogin = new Stage();
        stageLogin.setTitle("Anmelden");
        stageLogin.setScene(sceneLogin);
        stageLogin.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}