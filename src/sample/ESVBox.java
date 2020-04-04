package sample;

import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ESVBox extends VBox {

    public ESVBox() {

        super();
        VBox.setVgrow(this, Priority.ALWAYS);
    }
}
