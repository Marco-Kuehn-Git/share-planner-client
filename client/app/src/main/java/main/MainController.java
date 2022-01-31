package main;

import config.Config;
import config.ConfigLoader;
import events.EditEventController;
import ui.DayPane;
import ui.EventPane;
import ui.SvgBtnCreator;
import helper.HttpRequestException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import container.DataController;
import container.Event;

import javafx.event.ActionEvent;
import container.HttpRequest;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

public class MainController {

    @FXML
    public VBox leftNav;
    @FXML
    public GridPane mainGridPane;
    @FXML
    public HBox buttonBox;
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
        createBtns();
        leftNav.setSpacing(40);
    }

    private void updateEvents() {
        for (VBox vBox : dayVBoxes) {
            vBox.getChildren().clear();
        }

        DataController dataController = new DataController();
        try {
            ArrayList<Event> eventList = dataController.getAllVisibleEvents(weekStartDateTime, weekStartDateTime.plusDays(7));

            for (Event event : eventList) {
                addEvent(event);
            }
        } catch (HttpRequestException e) {
            e.printStackTrace();
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

    protected void onSettingBtnClick(){
        MainApplication.loadScene(
                "Einstellungen",
                "option-view.fxml",
                "option-view.css",
                650,
                600,
                null
                );
    }

    protected void onLogoutBtnClick(ActionEvent event){
        ConfigLoader.save(new Config());
        DataController.USER_ID = -1;
        HttpRequest.TOKEN = "";
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void createWeek() {
        for (int i = 0; i < 7; i++) {
            DayPane dayPane = new DayPane(dayNames[i]);
            this.dayLabel[i] = dayPane.getDayLabel();
            calendarGrid.add(dayPane.getDayLabel(), i, 0);
            dayVBoxes[i] = dayPane.getDayVBox();
            calendarGrid.add(dayPane.getScrollPane(), i, 1);
        }
    }

    private void addEvent(Event event) {
        EventPane eventPane = new EventPane(event);
        eventPane.getEditBtn().setOnAction(event1 -> MainApplication.loadScene(
                "edit-event.fxml",
                "create-event.css",
                "Termin bearbeiten",
                600,
                600,
                fxmlLoader -> {
                    EditEventController editEventController = fxmlLoader.getController();
                    editEventController.setCurrentEvent(event);
                }
        ));

        eventPane.getDeleteBtn().setOnAction(e -> {
            DataController dataController = new DataController();
            try {
                dataController.deleteEvent(event.getOwnerId(), event.getId(), event.getDate());
            } catch (HttpRequestException ex) {
                new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
            }
            updateEvents();
        });

        LocalDateTime eventDate = event.getDate();
        int day = (int) Duration.between(
                weekStartDateTime.toLocalDate().atStartOfDay(), eventDate.toLocalDate().atStartOfDay()).toDays();

        if (day >= 0 && day < 7) {
            dayVBoxes[day].getChildren().add(eventPane);
        }
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

    private void createBtns(){
        Group svgAdd = new Group(
                SvgBtnCreator.createPath("M0 0h24v24H0z", "transparent", "transparent"),
                SvgBtnCreator.createPath("M19 13h-6v6h-2v-6H5v-2h6V5h2v6h6v2z",
                        "white", "gray")
        );
        Button addBtn = SvgBtnCreator.createBtn(svgAdd, 40, "main-btn", "Erstellt einen neuen Termin");
        addBtn.setOnAction(e -> onAddBtnClick());
        addBtn.getStyleClass().add("main-btn");
        leftNav.getChildren().add(addBtn);

        Group svgSettings = new Group(
                SvgBtnCreator.createPath("M0 0h24v24H0V0z", "transparent", "transparent"),
                SvgBtnCreator.createPath("M19.14,12.94c0.04-0.3,0.06-0.61,0.06-0.94c0-0.32-0.02-0.64-0.07-0.94l2.03-1.58c0.18-0.14,0.23-0.41,0.12-0.61 l-1.92-3.32c-0.12-0.22-0.37-0.29-0.59-0.22l-2.39,0.96c-0.5-0.38-1.03-0.7-1.62-0.94L14.4,2.81c-0.04-0.24-0.24-0.41-0.48-0.41 h-3.84c-0.24,0-0.43,0.17-0.47,0.41L9.25,5.35C8.66,5.59,8.12,5.92,7.63,6.29L5.24,5.33c-0.22-0.08-0.47,0-0.59,0.22L2.74,8.87 C2.62,9.08,2.66,9.34,2.86,9.48l2.03,1.58C4.84,11.36,4.8,11.69,4.8,12s0.02,0.64,0.07,0.94l-2.03,1.58 c-0.18,0.14-0.23,0.41-0.12,0.61l1.92,3.32c0.12,0.22,0.37,0.29,0.59,0.22l2.39-0.96c0.5,0.38,1.03,0.7,1.62,0.94l0.36,2.54 c0.05,0.24,0.24,0.41,0.48,0.41h3.84c0.24,0,0.44-0.17,0.47-0.41l0.36-2.54c0.59-0.24,1.13-0.56,1.62-0.94l2.39,0.96 c0.22,0.08,0.47,0,0.59-0.22l1.92-3.32c0.12-0.22,0.07-0.47-0.12-0.61L19.14,12.94z M12,15.6c-1.98,0-3.6-1.62-3.6-3.6 s1.62-3.6,3.6-3.6s3.6,1.62,3.6,3.6S13.98,15.6,12,15.6z",
                        "white", "gray")
        );
        Button settingsBtn = SvgBtnCreator.createBtn(svgSettings, 40, "main-btn", "Öffnet die Einstellungen");
        settingsBtn.setOnAction(e -> onSettingBtnClick());
        settingsBtn.getStyleClass().add("main-btn");
        leftNav.getChildren().add(settingsBtn);

        Group svgLogout = new Group(
                SvgBtnCreator.createPath("M0 0h24v24H0z", "transparent", "transparent"),
                SvgBtnCreator.createPath("M17 7l-1.41 1.41L18.17 11H8v2h10.17l-2.58 2.58L17 17l5-5zM4 5h8V3H4c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h8v-2H4V5z",
                        "white", "gray")
        );
        Button logoutBtn = SvgBtnCreator.createBtn(svgLogout, 40, "main-btn", "Abmelden");
        logoutBtn.setOnAction(this::onLogoutBtnClick);
        logoutBtn.getStyleClass().add("main-btn");
        leftNav.getChildren().add(logoutBtn);

        Group svgBack = new Group(
                SvgBtnCreator.createPath("M0 0h24v24H0z", "transparent", "transparent"),
                SvgBtnCreator.createPath("M20 11H7.83l5.59-5.59L12 4l-8 8 8 8 1.41-1.41L7.83 13H20v-2z",
                        "white", "lightgray")
        );
        Button backBtn = SvgBtnCreator.createBtn(svgBack, 40, "navBtn", "Zeigt die vorherige Woche");
        backBtn.setOnAction(e -> onBackClick());
        backBtn.getStyleClass().add("navBtn");
        GridPane.setColumnIndex(backBtn, 1);
        buttonBox.getChildren().add(backBtn);

        Group svgToday = new Group(
                SvgBtnCreator.createPath("M0 0h24v24H0z", "transparent", "transparent"),
                SvgBtnCreator.createPath("M20 3h-1V1h-2v2H7V1H5v2H4c-1.1 0-2 .9-2 2v16c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm0 18H4V8h16v13z",
                        "white", "lightgray")
        );
        Button todayBtn = SvgBtnCreator.createBtn(svgToday, 40, "navBtn", "Zeigt die aktuelle Woche");
        todayBtn.setOnAction(e -> onTodayClick());
        todayBtn.getStyleClass().add("navBtn");
        GridPane.setColumnIndex(todayBtn, 2);
        buttonBox.getChildren().add(todayBtn);

        Group svgNext = new Group(
                SvgBtnCreator.createPath("M0 0h24v24H0z", "transparent", "transparent"),
                SvgBtnCreator.createPath("M12 4l-1.41 1.41L16.17 11H4v2h12.17l-5.58 5.59L12 20l8-8z",
                        "white", "lightgray")
        );
        Button nextBtn = SvgBtnCreator.createBtn(svgNext, 40, "navBtn", "Zeigt die nächste Woche");
        nextBtn.setOnAction(e -> onNextClick());
        nextBtn.getStyleClass().add("navBtn");
        GridPane.setColumnIndex(nextBtn, 3);
        buttonBox.getChildren().add(nextBtn);
    }
}