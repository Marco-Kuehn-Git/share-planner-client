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
        MainApplication.loadScene(
                "Termin erstellen",
                "create-event.fxml",
                "create-event.css",
                650,
                650,
                null
        );
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
                "Termin bearbeiten",
                "edit-event.fxml",
                "create-event.css",
                650,
                650,
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
        Button addBtn = SvgBtnCreator.createAddBtn();
        addBtn.setOnAction(e -> onAddBtnClick());
        addBtn.getStyleClass().add("main-btn");
        leftNav.getChildren().add(addBtn);

        Button settingsBtn = SvgBtnCreator.createSettingBtn();
        settingsBtn.setOnAction(e -> onSettingBtnClick());
        settingsBtn.getStyleClass().add("main-btn");
        leftNav.getChildren().add(settingsBtn);

        Button logoutBtn = SvgBtnCreator.createLogoutBtn();
        logoutBtn.setOnAction(this::onLogoutBtnClick);
        logoutBtn.getStyleClass().add("main-btn");
        leftNav.getChildren().add(logoutBtn);

        Button backBtn = SvgBtnCreator.createBackBtn();
        backBtn.setOnAction(e -> onBackClick());
        backBtn.getStyleClass().add("navBtn");
        GridPane.setColumnIndex(backBtn, 1);
        buttonBox.getChildren().add(backBtn);

        Button todayBtn = SvgBtnCreator.createTodayBtn();
        todayBtn.setOnAction(e -> onTodayClick());
        todayBtn.getStyleClass().add("navBtn");
        GridPane.setColumnIndex(todayBtn, 2);
        buttonBox.getChildren().add(todayBtn);

        Button nextBtn = SvgBtnCreator.createNextBtn();
        nextBtn.setOnAction(e -> onNextClick());
        nextBtn.getStyleClass().add("navBtn");
        GridPane.setColumnIndex(nextBtn, 3);
        buttonBox.getChildren().add(nextBtn);
    }
}