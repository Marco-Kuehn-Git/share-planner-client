package ui;

import container.Event;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class EventPane extends VBox {

    private Button deleteBtn;
    private Button editBtn;

    public EventPane(Event event) {
        this.getStyleClass().add("event");
        this.setSpacing(5);

        HBox btnHBox = new HBox();
        btnHBox.setAlignment(Pos.BOTTOM_RIGHT);

        Group svgDel = new Group(
                SvgBtnCreator.createPath("M0 0h24v24H0z", "transparent", "transparent"),
                SvgBtnCreator.createPath("M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z",
                        "white", "gray")
        );
        deleteBtn = SvgBtnCreator.createBtn(svgDel, 24, "", "Löschen des Termins");
        deleteBtn.getStyleClass().add("deleteEventBtn");

        Group svgEdit = new Group(
                SvgBtnCreator.createPath("M0 0h24v24H0z", "transparent", "transparent"),
                SvgBtnCreator.createPath("M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25zM20.71 7.04c.39-.39.39-1.02 0-1.41l-2.34-2.34c-.39-.39-1.02-.39-1.41 0l-1.83 1.83 3.75 3.75 1.83-1.83z",
                        "white", "gray")
        );
        editBtn = SvgBtnCreator.createBtn(svgEdit, 24, "", "Bearbeiten des Termins");
        editBtn.getStyleClass().add("editEventBtn");

        btnHBox.getChildren().add(editBtn);
        btnHBox.getChildren().add(deleteBtn);
        this.getChildren().add(btnHBox);

        Label nameLabel = new Label(event.getName());
        nameLabel.setWrapText(true);
        this.getChildren().add(nameLabel);

        if (event.getStart() != null || event.getEnd() != null) {
            String timeStr = (event.getStart() != null ? formatTime(event.getStart()) : "")
                    + (event.getEnd() != null ? " - " + formatTime(event.getEnd()) : "");
            Label timeLabel = new Label(timeStr);
            this.getChildren().add(timeLabel);
        }

        Label typeLabel = new Label("Wer: " + event.getOwnerName());
        this.getChildren().add(typeLabel);

        Label prioLabel = new Label("Priorität: " + event.getPriority());
        this.getChildren().add(prioLabel);

        if (event.isFullDay()) {
            Label fullDayLabel = new Label("Dieser Termin bockiert den ganzen Tag!");
            fullDayLabel.setWrapText(true);
            this.getChildren().add(fullDayLabel);
        }
    }

    public Button getDeleteBtn() {
        return deleteBtn;
    }

    public Button getEditBtn() {
        return editBtn;
    }


    private String formatTime(String time) {
        String[] timeArr = time.split(":");
        if (timeArr.length > 2) {
            return timeArr[0] + ":" + timeArr[1];
        }
        return time;
    }
}
