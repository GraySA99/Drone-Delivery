package sample;

import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class MapPage extends BorderPane {

    public MapPage() {

        super(); // Super Constructor
        this.setStyle(Styles.mapPage);
        Text pageTitleLabel = new Text("Maps");
        HBox pageTitle = new HBox();
        pageTitle.setStyle(Styles.pageTitle);
        HBox pageTitleLabelContainer = new HBox();
        HBox pageTitleES1 = new HBox();
        HBox pageTitleES2 = new HBox();
        HBox.setHgrow(pageTitleES1, Priority.ALWAYS);
        HBox.setHgrow(pageTitleES2, Priority.ALWAYS);
        HBox.setHgrow(pageTitle, Priority.ALWAYS);
        pageTitleLabelContainer.getChildren().add(pageTitleLabel);
        pageTitle.getChildren().addAll(pageTitleES1, pageTitleLabelContainer, pageTitleES2);
        pageTitleLabel.setStyle(Styles.pageTitleLabel);
        pageTitleLabelContainer.setStyle(Styles.pageTitleLabelContainer);

        // Left Side - Point List
        StackPane DPListContainer = new StackPane();
        ListView<String> DPList = new ListView<>();
        DPList.setPrefWidth(600);
        DPList.getItems().add(new String(""));
        DPListContainer.getChildren().add(DPList);

        // Right Side - Map
        StackPane webContainer = new StackPane();
        StackPane webContainerBorder = new StackPane();
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.loadContent(Values.html);
        webContainer.getChildren().add(webContainerBorder);
        webContainerBorder.getChildren().add(webView);
        webContainer.setStyle(Styles.mapWebContainer);
        webContainerBorder.setStyle(Styles.mapWebView);

        this.setTop(pageTitle);
        this.setRight(webContainer);
        this.setLeft(DPListContainer);
    }
}
