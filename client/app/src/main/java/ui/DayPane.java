package ui;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class DayPane {

    private Label dayLabel;
    private VBox dayVBox;
    private ScrollPane scrollPane;

    public DayPane(String name) {
        dayLabel = new Label();
        dayLabel.setText(name);
        dayLabel.setMaxHeight(Double.MAX_VALUE);
        dayLabel.setMaxWidth(Double.MAX_VALUE);
        dayLabel.getStyleClass().add("labelDays");

        scrollPane = new ScrollPane();

        dayVBox = new VBox();
        dayVBox.getStyleClass().add("vBoxDays");
        dayVBox.setSpacing(10);
        scrollPane.setContent(dayVBox);

        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.getStyleClass().add("scrollDays");
    }

    public Label getDayLabel() {
        return dayLabel;
    }

    public VBox getDayVBox() {
        return dayVBox;
    }

    public ScrollPane getScrollPane() {
        return scrollPane;
    }
}
