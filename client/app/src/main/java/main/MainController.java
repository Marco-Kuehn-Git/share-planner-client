package main;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import res.DataController;
import res.Event;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

public class MainController {

    @FXML
    private GridPane calendarGrid;

    @FXML
    private Label LabelMonth;

    private final String[] dayNames = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag"};
    private final Label[] dayLabel = new Label[7];
    private final VBox[] dayVBoxes = new VBox[7];

    private int weekOffset;
    private LocalDateTime weekStartDateTime;


    public MainController() {
        weekOffset = 0;
    }

    @FXML
    public void initialize() {
        createWeek();
        setDates();

        updateEvents();
    }

    private void updateEvents() {
        for (VBox vBox : dayVBoxes) {
            vBox.getChildren().clear();
        }

        DataController dataController = new DataController();
        ArrayList<Event> eventList = dataController.getAllVisibleEvents();

        for (Event event : eventList) {
            addEvent(event);
        }
    }

    @FXML
    protected void onBackClick() {
        weekOffset--;
        setDates();
        updateEvents();
    }

    @FXML
    protected void onTodayClick() {
        weekOffset = 0;
        setDates();
        updateEvents();
    }

    @FXML
    protected void onNextClick() {
        weekOffset++;
        setDates();
        updateEvents();
    }

    @FXML
    protected void onAddBtnClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    MainApplication.class.getResource("create-event.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 650, 650);
            scene.getStylesheets().add(Objects.requireNonNull(
                    MainApplication.class.getResource("create-event.css")).toExternalForm());
            Stage stage = new Stage();
            stage.setTitle("Termin erstellen");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            //stage.initStyle(StageStyle.UNDECORATED);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateEvents();
    }

    private void createWeek() {
        for (int i = 0; i < 7; i++) {
            Label label = new Label();
            label.setText(dayNames[i]);
            label.setMaxHeight(Double.MAX_VALUE);
            label.setMaxWidth(Double.MAX_VALUE);
            label.getStyleClass().add("labelDays");
            dayLabel[i] = label;
            calendarGrid.add(label, i, 0);

            ScrollPane scrollPane = new ScrollPane();

            VBox vBox = new VBox();
            vBox.getStyleClass().add("vBoxDays");
            vBox.setSpacing(10);
            dayVBoxes[i] = vBox;
            scrollPane.setContent(vBox);

            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.getStyleClass().add("scrollDays");

            calendarGrid.add(scrollPane, i, 1);
        }
    }

    private void addEvent(Event event) {
        VBox vBox = new VBox();
        vBox.getStyleClass().add("event");
        vBox.setSpacing(5);

        HBox btnHBox = new HBox();
        btnHBox.setAlignment(Pos.BOTTOM_RIGHT);
        Button deleteBtn = new Button();
        deleteBtn.setText(" X ");
        deleteBtn.setOnAction(e -> {
            DataController dataController = new DataController();
            dataController.deleteEvent(event.getId());
            updateEvents();
        });
        Button editBtn = new Button();
        editBtn.setText("edit");
        btnHBox.getChildren().add(editBtn);
        btnHBox.getChildren().add(deleteBtn);
        vBox.getChildren().add(btnHBox);

        Label nameLabel = new Label(event.getName());
        vBox.getChildren().add(nameLabel);

        if (event.getStart() != null || event.getEnd() != null) {
            String timeStr = (event.getStart() != null ? formatTime(event.getStart()) : "")
                    + (event.getEnd() != null ? " - " + formatTime(event.getEnd()) : "");
            Label timeLabel = new Label(timeStr);
            vBox.getChildren().add(timeLabel);
        }

        Label typeLabel = new Label("Wer: " + event.getOwnerName().replace("ü", "\u00fc"));
        vBox.getChildren().add(typeLabel);

        /*
        Ä, ä 		\u00c4, \u00e4
        Ö, ö 		\u00d6, \u00f6
        Ü, ü 		\u00dc, \u00fc
        ß 		    \u00df
         */
        Label prioLabel = new Label("Priorit\u00e4t: " + event.getPriority());
        vBox.getChildren().add(prioLabel);

        if (event.isFullDay()) {
            Label fullDayLabel = new Label("Dieser Termin bockiert den ganzen Tag!");
            vBox.getChildren().add(fullDayLabel);
        }


        LocalDateTime eventDate = event.getDate();

        int day = (int) Duration.between(
                weekStartDateTime.toLocalDate().atStartOfDay(), eventDate.toLocalDate().atStartOfDay()).toDays();

        if (day >= 0 && day < 7) {
            dayVBoxes[day].getChildren().add(vBox);
        }
    }

    private String formatTime(String time) {
        String[] timeArr = time.split(":");
        if (timeArr.length > 2) {
            return timeArr[0] + ":" + timeArr[1];
        }
        return time;
    }

    private void setDates() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("LLLL yyyy");
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("E dd.MM");
        DateTimeFormatter dayOfWeekFormatter = DateTimeFormatter.ofPattern("e");

        LocalDateTime now = LocalDateTime.now();
        int dayOfWeek = Integer.parseInt(dayOfWeekFormatter.format(now));

        weekStartDateTime = now.plusDays(weekOffset * 7L - dayOfWeek + 1);

        for (int i = 0; i < 7; i++) {
            dayLabel[i].setText(dayFormatter.format(weekStartDateTime.plusDays(i)));
        }

        LabelMonth.setText(dateFormatter.format(weekStartDateTime));

    }
}