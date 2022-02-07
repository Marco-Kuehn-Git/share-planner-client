//Marc Beyer//
package main;

import config.Config;
import config.ConfigLoader;
import container.DataController;
import container.HttpRequest;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;


public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Config config = ConfigLoader.load();
        if(config == null){
            config = new Config(false, -1, "");
        }

        DataController.SERVER_URL = config.toServerUrl();

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

    private void loadLoginScene() {
        loadScene(
                "Anmelden",
                "../users/login.fxml",
                "../users/login.css",
                650,
                500
        );
    }

    public static void main(String[] args) {
        launch();
    }

    public static void loadScene(
            String title,
            String fxml,
            String css,
            int width,
            int height
    ) {
        loadScene(title, fxml, css, width, height, null);
    }

    public static void loadScene(
            String title,
            String fxml,
            String css,
            int width,
            int height,
            Consumer<FXMLLoader> method
    ) {
        FXMLLoader fxmlLoader = new FXMLLoader(
                MainApplication.class.getResource(fxml));
        try {
            Scene scene = new Scene(fxmlLoader.load(), width, height);
            scene.getStylesheets().add(Objects.requireNonNull(
                    MainApplication.class.getResource(css)).toExternalForm());
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);

            if(method != null)method.accept(fxmlLoader);

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}