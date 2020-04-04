package sample;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;

public class PageTitle extends HBox {

    private Text pageTitleLabel;

    public PageTitle(String titleName) {

        super();
        pageTitleLabel = new Text(titleName);
        this.setStyle(Styles.pageTitle);
        HBox pageTitleLabelContainer = new HBox();
        HBox pageTitleES1 = new HBox();
        HBox pageTitleES2 = new HBox();
        HBox.setHgrow(pageTitleES1, Priority.ALWAYS);
        HBox.setHgrow(pageTitleES2, Priority.ALWAYS);
        HBox.setHgrow(this, Priority.ALWAYS);
        pageTitleLabelContainer.getChildren().add(pageTitleLabel);
        this.getChildren().addAll(pageTitleES1, pageTitleLabelContainer, pageTitleES2);
        pageTitleLabel.setStyle(Styles.pageTitleLabel);
        pageTitleLabelContainer.setStyle(Styles.pageTitleLabelContainer);
    }

    public PageTitle() {

        super();
        pageTitleLabel = new Text("titleName");
        this.setStyle(Styles.pageTitle);
        HBox pageTitleLabelContainer = new HBox();
        HBox pageTitleES1 = new HBox();
        HBox pageTitleES2 = new HBox();
        HBox.setHgrow(pageTitleES1, Priority.ALWAYS);
        HBox.setHgrow(pageTitleES2, Priority.ALWAYS);
        HBox.setHgrow(this, Priority.ALWAYS);
        pageTitleLabelContainer.getChildren().add(pageTitleLabel);
        this.getChildren().addAll(pageTitleES1, pageTitleLabelContainer, pageTitleES2);
        pageTitleLabel.setStyle(Styles.pageTitleLabel);
        pageTitleLabelContainer.setStyle(Styles.pageTitleLabelContainer);
    }

    public void changeTitleName(String titleName) {

        pageTitleLabel.setText(titleName);
    }
}
