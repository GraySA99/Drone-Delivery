package sample;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class TopBar extends ToolBar {

    private Button CloseWindowBtn;
    private Button MiniWindowBtn;
    private final Styles myStyles = new Styles();

    public TopBar() {

        super();
        CloseWindowBtn = new Button("X");
        MiniWindowBtn = new Button("-");
        HBox emptySpace = new HBox();
        HBox.setHgrow(emptySpace, Priority.ALWAYS);
        this.getItems().addAll(emptySpace, MiniWindowBtn, CloseWindowBtn);
        this.setPrefHeight(30);
        this.setMaxHeight(30);
        this.setMinHeight(30);
        CloseWindowBtn.setMaxSize(40, 25);
        CloseWindowBtn.setMinSize(40, 25);
        CloseWindowBtn.setPrefSize(40, 25);
        MiniWindowBtn.setMaxSize(40, 25);
        MiniWindowBtn.setMinSize(40, 25);
        MiniWindowBtn.setPrefSize(40, 25);
        this.setStyle(myStyles.TopBar);

        CloseWindowBtn.styleProperty().bind(
                Bindings.when(CloseWindowBtn.hoverProperty())
                        .then(
                                new SimpleStringProperty(myStyles.CloseWindowBtnHover)
                        )
                        .otherwise(
                                new SimpleStringProperty(myStyles.CloseWindowBtn)
                        )
        );

        CloseWindowBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {

                Platform.exit();
                System.exit(0);
            }
        });

        MiniWindowBtn.styleProperty().bind(
                Bindings.when(MiniWindowBtn.hoverProperty())
                        .then(
                                new SimpleStringProperty(myStyles.MiniWindowBtnHover)
                        )
                        .otherwise(
                                new SimpleStringProperty(myStyles.MiniWindowBtn)
                        )
        );

        MiniWindowBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {

                Values.primaryStage.setIconified(true);
            }
        });
    }
}
