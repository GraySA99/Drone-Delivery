package GUI;

import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 ** This class is used as an cleaner alternative to HBoxes that take up space
 **/

public class ESVBox extends VBox {

    public ESVBox() {

        super();
        VBox.setVgrow(this, Priority.ALWAYS);
    }
}
