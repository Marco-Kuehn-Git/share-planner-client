package customUI;

public class Label extends javafx.scene.control.Label {
    public Label(String content){
        super(Converter.convertString(content));
    }

    public Label(){
        super();
    }

    public void setTextValue(String text){
        super.setText(Converter.convertString(text));
    }
}
