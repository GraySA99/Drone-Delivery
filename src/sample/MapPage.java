package sample;

import Mapping.Waypoint;
import Simulation.DataTransfer;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
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
import java.text.DecimalFormat;
import java.util.HashMap;

public class MapPage extends BorderPane {

    private JSObject javascriptConnector;
    private JavaConnector javaConnector = new JavaConnector();
    private Text currentPointLabel;

    public MapPage() {

        super(); // Super Constructor
        this.setStyle(Styles.mapPage);
        PageTitle pageTitle = new PageTitle("Map");

        // Left Side - Point List
        StackPane DPListContainer = new StackPane();
        DPListContainer.setStyle(Styles.DPListContainer);
        ListView<HBox> DPList = new ListView<>();
        DPList.setStyle(Styles.DPList);
        DPList.setPrefWidth(600);
        DPList.getItems().add(new HBox());
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

        HBox currentPointContainer = new HBox();
        HBox currentPointEmptySpaceOne = new HBox();
        HBox currentPointEmptySpaceTwo = new HBox();
        HBox.setHgrow(currentPointEmptySpaceOne, Priority.ALWAYS);
        HBox.setHgrow(currentPointEmptySpaceTwo, Priority.ALWAYS);
        currentPointLabel = new Text("(, )");
        currentPointLabel.setStyle(Styles.currentPointLabel);
        currentPointContainer.getChildren().addAll(
                currentPointEmptySpaceOne,
                currentPointLabel,
                currentPointEmptySpaceTwo
        );

        HBox addRemoveContainer = new HBox();
        HBox addRemoveEmptySpaceOne = new HBox();
        HBox addRemoveEmptySpaceTwo = new HBox();
        HBox addRemoveEmptySpaceThree = new HBox();
        HBox.setHgrow(addRemoveEmptySpaceOne, Priority.ALWAYS);
        HBox.setHgrow(addRemoveEmptySpaceTwo, Priority.ALWAYS);
        HBox.setHgrow(addRemoveEmptySpaceThree, Priority.ALWAYS);
        Button addDP = new Button("Add");
        Button removeDP = new Button("Remove");

        addDP.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {

                if (!nameEnt.getText().strip().equals("")
                        && !currentPointLabel.getText().equals("(, )")) {

                    if (DPList.getItems().size() == 1
                            && DPList.getItems().get(0).getChildren().size() == 0) {

                        DPList.getItems().clear();
                    }

                    String name = nameEnt.getText();
                    String[] cords = currentPointLabel.getText().split(", ");
                    double lat = Double.parseDouble(cords[0].substring(1, cords[0].length()));
                    double lng = Double.parseDouble(cords[1].substring(0, cords[1].length()-1));

                    HBox frame = new HBox();
                    HBox frameEmptySpace = new HBox();
                    HBox.setHgrow(frameEmptySpace, Priority.ALWAYS);
                    Text frameName = new Text(name);
                    Text frameData = new Text(String.format("(%.5f, %.5f)", lat, lng));
                    frame.getChildren().addAll(frameName, frameEmptySpace, frameData);

                    frame.setOnMouseClicked(evt -> {

                        nameEnt.setText(((Text)frame.getChildren().get(0)).getText());
                        currentPointLabel.setText(((Text)frame.getChildren().get(2)).getText());
                    });

                    DataTransfer.addWaypoint(new Waypoint(name, lat, lng, DPList.getItems().isEmpty()));

                    javascriptConnector.call("addMarker", name);
                    DPList.getItems().add(frame);
                    nameEnt.setText("");
                    currentPointLabel.setText("(, )");
                }
            }
        });

        removeDP.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {

                int count = 0;

                for (HBox box : DPList.getItems()) {

                    if (box.getChildren().size() > 0
                            && ((Text)box.getChildren().get(0)).getText().equals(nameEnt.getText())) {

                        DPList.getItems().remove(count);
                        DataTransfer.removeWaypoint(nameEnt.getText());
                        break;
                    }
                    count++;
                }

                if (DPList.getItems().isEmpty()) {

                    DPList.getItems().add(new HBox());
                }

                nameEnt.clear();
                currentPointLabel.setText("(, )");

            }
        });

        addRemoveContainer.getChildren().addAll(
                addRemoveEmptySpaceOne,
                addDP,
                addRemoveEmptySpaceTwo,
                removeDP,
                addRemoveEmptySpaceThree
        );

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



        pointEntryContainer.getChildren().addAll(nameContainer, currentPointContainer, addRemoveContainer, webContainer);
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

            if (null != lat && null != lon) {

                currentPointLabel.setText(String.format("(%.5f, %.5f)",
                        Double.parseDouble(lat),
                        Double.parseDouble(lon)));
            }
        }
    }
}


