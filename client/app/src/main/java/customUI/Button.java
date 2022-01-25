//Marc Beyer//
package customUI;

public class Button extends javafx.scene.control.Button {

    public void setTextValue(String text){
        super.setText(Converter.convertString(text));
    }
}
