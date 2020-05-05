package GUI;

import Mapping.Waypoint;
import Simulation.DataTransfer;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class MapPage extends BorderPane {

    private JSObject javascriptConnector;
    private JavaConnector javaConnector = new JavaConnector();
    private Text currentPointLabel;
    private Text nameLabel;
    private TextField nameEnt;
    private ListView<HBox> DPList;
    private StackPane DPListContainer;
    private StackPane mapViewContainer;
    private WebView mapView;
    private VBox mapContainer;
    private VBox mapInfoContainer;
    private Button addDP;
    private Button removeDP;

    public MapPage() {

        super(); // Super Constructor

        // Right Side - Point List
        DPListContainer = new StackPane();
        DPList = new ListView<>();
        DPList.getItems().add(new HBox());
        DPListContainer.getChildren().add(DPList);

        // Left Side - Map
        mapContainer = new VBox();
        mapInfoContainer = new VBox();
        HBox nameContainer = new HBox();
        nameLabel = new Text("Waypoint\nName:");
        nameEnt = new TextField();
        nameEnt.setPromptText("ex. Home");
        nameContainer.getChildren().addAll(new ESHBox(), nameLabel, new ESHBox(), nameEnt, new ESHBox());
        HBox currentPointContainer = new HBox();
        currentPointLabel = new Text("(, )");
        currentPointContainer.getChildren().addAll(
                new ESHBox(),
                currentPointLabel,
                new ESHBox()
        );
        HBox addRemoveContainer = new HBox();
        addDP = new Button("Add");
        removeDP = new Button("Remove");
        mapInfoContainer.getChildren().addAll(nameContainer, currentPointContainer, addRemoveContainer);

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

                    javascriptConnector.call("addMarker", name, lat, lng);
                    DataTransfer.addWaypoint(new Waypoint(name, lat, lng, DPList.getItems().isEmpty()));

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

                javascriptConnector.call("removeMarker", nameEnt.getText());

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

        mapView = new WebView();
        mapViewContainer = new StackPane();
        mapViewContainer.getChildren().add(mapView);
        final WebEngine webEngine = mapView.getEngine();
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
           if (Worker.State.SUCCEEDED == newValue) {
               JSObject window = (JSObject) webEngine.executeScript("window");
               window.setMember("javaConnector", javaConnector);

               javascriptConnector = (JSObject) webEngine.executeScript("getJsConnector()");
               setMarkers();
           }

        });

        mapContainer.getChildren().addAll(mapInfoContainer, mapViewContainer);

        this.setLeft(mapContainer);
        this.setRight(DPListContainer);

        try {
            webEngine.loadContent(new String(Files.readAllBytes(Paths.get(getClass().getResource("googlemap.html").toURI()))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        initFromFile("");
        refresh();
    }

    public void refresh() {

        double pageWidth = Values.windowWidth * (1 - Values.sideMenuWidthPercent);
        double pageHeight = Values.windowHeight;
        this.setMaxWidth(pageWidth);
        this.setPrefWidth(pageWidth);
        this.setPrefHeight(pageHeight);

        mapViewContainer.setPrefWidth(pageWidth * Values.mapPageMapWidthPercent);
        mapViewContainer.setPrefHeight(pageHeight * Values.mapPageMapHeightPercent);

        mapInfoContainer.setPrefWidth(pageWidth * Values.mapPageMapInfoWidthPercent);
        mapInfoContainer.setPrefHeight(pageHeight * Values.mapPageMapInfoHeightPercent);

        DPListContainer.setMaxWidth(pageWidth * Values.mapPageDPListWidthPercent);
        DPListContainer.setMaxHeight(pageHeight * Values.mapPageDPListHeightPercent);
        DPList.setPrefWidth(pageWidth * Values.mapPageDPListWidthPercent);
        DPList.setPrefHeight(pageHeight * Values.mapPageDPListHeightPercent);
        BorderPane.setAlignment(DPListContainer, Pos.CENTER_LEFT);

        // mapInfoContainer Items
        double mapInfoWidth = mapInfoContainer.getWidth();
        double mapInfoHeight = mapInfoContainer.getHeight();

        addDP.setPrefWidth(mapInfoWidth * Values.mapPageBtnWidthPercent);
        addDP.setPrefHeight(mapInfoHeight * Values.mapPageBtnHeightPercent);
        removeDP.setPrefWidth(mapInfoWidth * Values.mapPageBtnWidthPercent);
        removeDP.setPrefHeight(mapInfoHeight * Values.mapPageBtnHeightPercent);

        nameEnt.setPrefWidth(mapInfoWidth * Values.mapPageNameWidthPercent);
        nameEnt.setPrefHeight(mapInfoHeight * Values.mapPageNameHeightPercent);

        // Styles
        this.setStyle(Styles.mapPage);
        nameLabel.setStyle(Styles.mapPageNameLabel + "-fx-font-size: "
                + (mapInfoHeight * Values.mapPageNameFontPercent) + ";\n");
        currentPointLabel.setStyle(Styles.mapPageCurrentPointLabel + "-fx-font-size: "
                + (mapInfoHeight * Values.mapPageLatLngFontPercent) + ";\n");
        mapViewContainer.setStyle(Styles.mapPageMapView);
        mapInfoContainer.setStyle(Styles.mapPageMapInfoContainer);
        DPListContainer.setStyle(Styles.mapPageDPListContainer);
        mapContainer.setStyle(Styles.mapPageMapContainer);
        BorderPane.setAlignment(mapContainer, Pos.CENTER);

    }

    public void setMarkers() {

        if (DataTransfer.getWaypoints() != null) {

            for (Waypoint wp : DataTransfer.getWaypoints()) {

                javascriptConnector.call("addMarker", wp.getName(), wp.getLatitude(), wp.getLongitude());
            }
        }
    }

    public ListView<HBox> getDPList() {
        return DPList;
    }

    public class JavaConnector {

        public void sendLatLong(String lat, String lon) {

            if (null != lat && null != lon) {

                currentPointLabel.setText(String.format("(%.5f, %.5f)",
                        Double.parseDouble(lat),
                        Double.parseDouble(lon)));
            }
        }

        public void highlight(String name) {

            Waypoint target = DataTransfer.getWaypoint(name);
            nameEnt.setText(target.getName());
            currentPointLabel.setText(String.format("(%.5f, %.5f)",
                    target.getLatitude(),
                    target.getLongitude()));
        }
    }

    public void initFromFile(String filename) {

        try {

            FileInputStream fis = new FileInputStream(Values.defaultFileName);
            if(!filename.equals(""))
                fis = new FileInputStream(filename);

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
}


