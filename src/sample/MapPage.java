package sample;

import Mapping.Waypoint;
import Simulation.DataTransfer;
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MapPage extends BorderPane {

    private JSObject javascriptConnector;
    private JavaConnector javaConnector = new JavaConnector();
    private Text currentPointLabel;
    private TextField nameEnt;
    private ListView<HBox> DPList;

    public MapPage() {

        super(); // Super Constructor
        this.setStyle(Styles.mapPage);
        PageTitle pageTitle = new PageTitle("Map");

        // Right Side - Point List
        StackPane DPListContainer = new StackPane();
        DPListContainer.setStyle(Styles.DPListContainer);
        DPList = new ListView<>();
        DPList.setStyle(Styles.DPList);
        DPList.setPrefWidth(600);
        DPList.getItems().add(new HBox());
        DPListContainer.getChildren().add(DPList);

        // Left Side - Map
        VBox pointEntryContainer = new VBox();

        HBox nameContainer = new HBox();
        Text nameLabel = new Text("Name:");
        nameEnt = new TextField();
        nameEnt.setPromptText("ex. Home");
        nameContainer.getChildren().addAll(new ESHBox(), nameLabel, nameEnt, new ESHBox());

        HBox currentPointContainer = new HBox();
        currentPointLabel = new Text("(, )");
        currentPointLabel.setStyle(Styles.currentPointLabel);
        currentPointContainer.getChildren().addAll(
                new ESHBox(),
                currentPointLabel,
                new ESHBox()
        );

        HBox addRemoveContainer = new HBox();
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
                    Text frameName = new Text(name);
                    Text frameData = new Text(String.format("(%.5f, %.5f)", lat, lng));
                    frame.getChildren().addAll(frameName, new ESHBox(), frameData);

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
                new ESHBox(),
                addDP,
                new ESHBox(),
                removeDP,
                new ESHBox()
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
        this.setLeft(pointEntryContainer);
        this.setRight(DPListContainer);

        webEngine.loadContent(Values.googleMapsJavaScript);
        initFromFile();
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

    private void initFromFile() {

        try {

            FileInputStream fis = new FileInputStream(Values.defaultWaypointFileName);
            Scanner fileIn = new Scanner(fis);
            if (!fileIn.hasNextLine()) { return; }
            String fileLine = fileIn.nextLine();

            while (fileIn.hasNextLine() && !fileLine.equals("@Waypoint")) { fileLine = fileIn.nextLine(); }
            if (!fileIn.hasNextLine()) { return; }

            fileLine = fileIn.nextLine();
            DPList.getItems().clear();
            while (fileIn.hasNextLine()) {

                if (fileLine.strip().equals("@/Waypoint")) { break; }

                String name = fileLine.strip().split("&")[0];
                double lat = Double.parseDouble(fileLine.strip().split("&")[1].split(",")[0]);
                double lng = Double.parseDouble(fileLine.strip().split("&")[1].split(",")[1]);

                HBox frame = new HBox();
                Text frameName = new Text(name);
                Text frameData = new Text(String.format("(%.5f, %.5f)", lat, lng));
                frame.getChildren().addAll(frameName, new ESHBox(), frameData);

                frame.setOnMouseClicked(evt -> {

                    nameEnt.setText(((Text)frame.getChildren().get(0)).getText());
                    currentPointLabel.setText(((Text)frame.getChildren().get(2)).getText());
                });

                DPList.getItems().add(frame);

                DataTransfer.addWaypoint(new Waypoint(name, lat, lng, DPList.getItems().isEmpty()));
                fileLine = fileIn.nextLine();
            }
        } catch (FileNotFoundException e) {

            System.out.println("Problem With File");
            e.printStackTrace();
        }
    }

    public void resizeWindow() {

        this.setMinWidth(Values.windowWidth * Values.mainPageWidthPercent);
    }
}


