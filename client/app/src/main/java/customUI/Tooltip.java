package customUI;

public class Tooltip extends javafx.scene.control.Tooltip {

    public Tooltip(String tollTipText){
        super(Converter.CONVERT_STR(tollTipText));
    }

}
