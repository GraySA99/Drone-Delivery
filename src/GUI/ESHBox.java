package GUI;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 ** This class is used as an cleaner alternative to HBoxes that take up space
 **/

public class ESHBox extends HBox {

    public ESHBox() {

        super();
        HBox.setHgrow(this, Priority.ALWAYS);
    }
}
