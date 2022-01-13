package customUI;

public class Button extends javafx.scene.control.Button {

    public void setTextValue(String text){
        super.setText(Converter.CONVERT_STR(text));
    }
}
