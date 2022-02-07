package ui;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class DayPane extends ScrollPane {

    private final Label dayLabel;
    private final VBox dayVBox;

    public DayPane() {
        dayLabel = new Label();
        dayLabel.setMaxHeight(Double.MAX_VALUE);
        dayLabel.setMaxWidth(Double.MAX_VALUE);
        dayLabel.getStyleClass().add("labelDays");

        dayVBox = new VBox();

        dayVBox.getStyleClass().add("vBoxDays");
        dayVBox.setSpacing(10);

        setContent(dayVBox);
        setFitToWidth(true);
        setFitToHeight(true);
        setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        getStyleClass().add("scrollDays");
        getStyleClass().add("scrollDays");
    }

    public Label getDayLabel() {
        return dayLabel;
    }

    public VBox getDayVBox() {
        return dayVBox;
    }
}
