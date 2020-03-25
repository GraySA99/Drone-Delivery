package sample;

import javafx.concurrent.Worker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class MapPage extends BorderPane {

    private JSObject javascriptConnector;
    private JavaConnector javaConnector = new JavaConnector();
    ListView<String> DPList;

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
        DPList = new ListView<>();
        DPList.setPrefWidth(600);
        DPList.getItems().add(new String(""));
        DPListContainer.getChildren().add(DPList);

        // Right Side - Map
        VBox pointEntryContainer = new VBox();

        HBox nameContainer = new HBox();
        HBox nameEmptySpaceOne = new HBox();
        HBox nameEmptySpaceTwo = new HBox();
        HBox.setHgrow(nameEmptySpaceOne, Priority.ALWAYS);
        HBox.setHgrow(nameEmptySpaceTwo, Priority.ALWAYS);
        Text nameLabel = new Text("Name:");
        TextField nameEnt = new TextField();
        nameEnt.setPromptText("ex. Home");
        nameContainer.getChildren().addAll(nameEmptySpaceOne, nameLabel, nameEnt, nameEmptySpaceTwo);

        StackPane webContainer = new StackPane();
        WebView webView = new WebView();
        final WebEngine webEngine = webView.getEngine();

        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
           if (Worker.State.SUCCEEDED == newValue) {
               JSObject window = (JSObject) webEngine.executeScript("window");
               window.setMember("javaConnector", javaConnector);

               javascriptConnector = (JSObject) webEngine.executeScript("getJsConnector()");
           }

        });

        pointEntryContainer.getChildren().addAll(nameContainer, webContainer);
        webContainer.getChildren().add(webView);
        pointEntryContainer.setStyle(Styles.mapWebContainer);
        webContainer.setStyle(Styles.mapWebView);

        this.setTop(pageTitle);
        this.setRight(pointEntryContainer);
        this.setLeft(DPListContainer);

        webEngine.loadContent(Values.googleMapsJavaScript);
    }

    public class JavaConnector {

        public void sendLatLong(String lat, String lon) {

            System.out.println("Lat and Long reported");

            if (null != lat && null != lon) {
                javascriptConnector.call("showResult", lat.toLowerCase());

                if (DPList.getItems().size() == 1 && DPList.getItems().get(0).isBlank()) {
                    DPList.getItems().clear();
                }

                DPList.getItems().add(lat + " " + lon);
            }

            System.out.println("End Connector");
        }
    }
}


