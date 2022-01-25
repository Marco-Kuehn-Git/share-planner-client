//Marco Kühn//
package helper;

import javafx.geometry.Bounds;
import javafx.scene.Group;
import customUI.Button;
import javafx.scene.control.ContentDisplay;
import customUI.Tooltip;
import javafx.scene.shape.SVGPath;

public class SvgBtnCreator {

    public static Button createBtn(Group group, int svgSize) {
        Button btn = new Button();

        Bounds boundsDel = group.getBoundsInParent();
        double scaleDel = Math.min(svgSize / boundsDel.getWidth(), svgSize / boundsDel.getHeight());
        group.setScaleX(scaleDel);
        group.setScaleY(scaleDel);
        btn.setGraphic(group);
        btn.setMaxSize(svgSize, svgSize);
        btn.setMinSize(svgSize, svgSize);
        btn.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        return btn;
    }

    public static Button createBtn(Group group, int svgSize, String styleClass) {
        Button btn = new Button();

        Bounds boundsDel = group.getBoundsInParent();
        double scaleDel = Math.min(svgSize / boundsDel.getWidth(), svgSize / boundsDel.getHeight());
        group.setScaleX(scaleDel);
        group.setScaleY(scaleDel);
        btn.setGraphic(group);
        btn.setMaxSize(svgSize, svgSize);
        btn.setMinSize(svgSize, svgSize);
        btn.getStyleClass().add(styleClass);

        return btn;
    }

    public static Button createBtn(Group group, int svgSize, String styleClass, String toolTip) {
        Button btn = new Button();

        Bounds boundsDel = group.getBoundsInParent();
        double scaleDel = Math.min(svgSize / boundsDel.getWidth(), svgSize / boundsDel.getHeight());
        group.setScaleX(scaleDel);
        group.setScaleY(scaleDel);
        btn.setGraphic(group);
        btn.setMaxSize(svgSize, svgSize);
        btn.setMinSize(svgSize, svgSize);
        btn.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        btn.getStyleClass().add(styleClass);
        Tooltip tooltip = new Tooltip(toolTip);
        btn.setTooltip(tooltip);

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
