package main;

import customUI.Button;
import customUI.Label;
import helper.SvgBtnCreator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
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
    private javafx.scene.control.Label LabelMonth;

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
        ArrayList<Event> eventList = dataController.getAllVisibleEvents(weekStartDateTime, weekStartDateTime.plusDays(7));

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
            label.setTextValue(dayNames[i]);
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

        Group svgDel = new Group(
                SvgBtnCreator.createPath("M0 0h24v24H0z", "transparent", "transparent"),
                SvgBtnCreator.createPath("M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z",
                        "white", "lightgray")
        );
        Button deleteBtn = SvgBtnCreator.cretaeBtn(svgDel, 24);

        deleteBtn.getStyleClass().add("deleteEventBtn");
        deleteBtn.setOnAction(e -> {
            DataController dataController = new DataController();
            dataController.deleteEvent(event.getOwnerId(), event.getId(), event.getDate());
            updateEvents();
        });

        Group svgEdit = new Group(
                SvgBtnCreator.createPath("M0 0h24v24H0z", "transparent", "transparent"),
                SvgBtnCreator.createPath("M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25zM20.71 7.04c.39-.39.39-1.02 0-1.41l-2.34-2.34c-.39-.39-1.02-.39-1.41 0l-1.83 1.83 3.75 3.75 1.83-1.83z",
                        "white", "lightgray")
        );
        Button editBtn = SvgBtnCreator.cretaeBtn(svgEdit, 24);
        editBtn.getStyleClass().add("editEventBtn");
        editBtn.setOnAction(event1 -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(
                        MainApplication.class.getResource("edit-event.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 650, 650);
                scene.getStylesheets().add(Objects.requireNonNull(
                        MainApplication.class.getResource("create-event.css")).toExternalForm());
                Stage stage = new Stage();
                stage.setTitle("Termin bearbeiten");
                stage.setScene(scene);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setResizable(false);
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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

        Label typeLabel = new Label("Wer: " + event.getOwnerName());
        vBox.getChildren().add(typeLabel);

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
            dayLabel[i].setTextValue(dayFormatter.format(weekStartDateTime.plusDays(i)));
        }

        LabelMonth.setText(dateFormatter.format(weekStartDateTime));

    }

    private static SVGPath createPath(String d, String fill, String hoverFill) {
        SVGPath path = new SVGPath();
        path.getStyleClass().add("svg");
        path.setContent(d);
        path.setStyle("-fill:" + fill + ";-hover-fill:"+hoverFill+';');
        return path;
    }

}