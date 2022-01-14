package customUI;

public class Label extends javafx.scene.control.Label {
    public Label(String content){
        super(Converter.CONVERT_STR(content));
    }

    public Label(){
        super();
    }

    public void setTextValue(String text){
        super.setText(Converter.CONVERT_STR(text));
    }
}
