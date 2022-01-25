//Marco Kühn//
package customUI;

public class Tooltip extends javafx.scene.control.Tooltip {

    public Tooltip(String tollTipText){
        super(Converter.convertString(tollTipText));
    }

}
