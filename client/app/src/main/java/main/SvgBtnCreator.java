package main;

import javafx.geometry.Bounds;
import javafx.scene.Group;
import customUI.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.shape.SVGPath;

public class SvgBtnCreator {

    private static final int SVG_SIZE = 24;

    public static Button cretaeBtn(Group group) {
        Button btn = new Button();

        Bounds boundsDel = group.getBoundsInParent();
        double scaleDel = Math.min(20 / boundsDel.getWidth(), 20 / boundsDel.getHeight());
        group.setScaleX(scaleDel);
        group.setScaleY(scaleDel);
        btn.setGraphic(group);
        btn.setMaxSize(SVG_SIZE, SVG_SIZE);
        btn.setMinSize(SVG_SIZE, SVG_SIZE);
        btn.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        return btn;
    }

    public static SVGPath createPath(String d, String fill, String hoverFill) {
        SVGPath path = new SVGPath();
        path.getStyleClass().add("svg");
        path.setContent(d);
        path.setStyle("-fill:" + fill + ";-hover-fill:"+hoverFill+';');
        return path;
    }

}
