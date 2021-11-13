module com.vpr.client {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.vpr.client to javafx.fxml;
    exports com.vpr.client;
}