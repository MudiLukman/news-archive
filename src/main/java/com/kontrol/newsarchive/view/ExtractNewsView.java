package com.kontrol.newsarchive.view;

import com.jfoenix.controls.JFXButton;
import com.kontrol.newsarchive.util.CustomImageView;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class ExtractNewsView extends BorderPane {

    private CustomImageView plusButton;
    private StackPane stackPane;
    private ScrollPane contentArea;
    private FlowPane flowPane;
    private ProgressIndicator progressIndicator;
    private CustomImageView cancelIcon;
    private JFXButton archiveButton;
    private JFXButton closeButton;

    public ExtractNewsView(){
        plusButton = new CustomImageView("/plus.png", 30, 30);
        stackPane = new StackPane();
        contentArea = new ScrollPane();
        flowPane = new FlowPane(20, 20);
        progressIndicator = new ProgressIndicator();
        cancelIcon = new CustomImageView("/cross.png", 15, 15);
        archiveButton = new JFXButton("ARCHIVE");
        closeButton = new JFXButton("CLOSE");
        designGUI();
    }

    private void designGUI() {
        //make top
        HBox top = new HBox();
        Label headerLbl = new Label("Extract News Content");
        headerLbl.setFont(new Font(20));
        headerLbl.setStyle("-fx-font-weight: bold;");
        headerLbl.setTextFill(Color.WHITE);
        top.setPadding(new Insets(7, 10, 20, 10));
        Pane spaceAtTop = new Pane();
        plusButton.setPickOnBounds(true);
        top.getChildren().addAll(headerLbl, spaceAtTop, plusButton);
        HBox.setHgrow(spaceAtTop, Priority.SOMETIMES);
        this.setTop(top);

        //make center
        progressIndicator.setMaxHeight(70);
        progressIndicator.setMaxWidth(70);
        flowPane.setPadding(new Insets(10, 0, 40, 10));
        contentArea.setContent(flowPane);
        contentArea.setFitToHeight(true);
        contentArea.setFitToWidth(true);
        contentArea.setPannable(true);
        cancelIcon.setPickOnBounds(true);
        stackPane.getChildren().addAll(contentArea, progressIndicator, cancelIcon);
        stackPane.setStyle("-fx-background-color: rgb(244, 244, 243);");
        this.setCenter(stackPane);

        //make bottom
        HBox bottom = new HBox(40);
        bottom.setPadding(new Insets(10, 10, 5, 0));
        Pane spaceBeforeButtons = new Pane();
        archiveButton.setDefaultButton(true);
        archiveButton.setPadding(new Insets(10, 15, 10, 15));
        archiveButton.setStyle("-fx-background-color: rgb(238, 167, 121);"
                + "-fx-border-radius: 5;"
                + "-fx-background-radius: 5");
        closeButton.setCancelButton(true);
        closeButton.setPadding(new Insets(10, 15, 10, 15));
        closeButton.setStyle("-fx-background-color: rgb(238, 167, 121);"
                + "-fx-border-radius: 5;"
                + "-fx-background-radius: 5");
        HBox.setHgrow(spaceBeforeButtons, Priority.SOMETIMES);
        bottom.getChildren().addAll(spaceBeforeButtons, archiveButton, closeButton);
        this.setBottom(bottom);

        this.setPadding(new Insets(5));
        this.setStyle("-fx-background-color: rgb(62, 73, 86);" +
                "-fx-border-color: rgb(115, 115, 115);" +
                "-fx-border-width: 2;");
    }

    public CustomImageView getPlusButton() {
        return plusButton;
    }

    public StackPane getStackPane() {
        return stackPane;
    }

    public ScrollPane getContentArea() {
        return contentArea;
    }

    public FlowPane getFlowPane() {
        return flowPane;
    }

    public ProgressIndicator getProgressIndicator() {
        return progressIndicator;
    }

    public CustomImageView getCancelIcon() {
        return cancelIcon;
    }

    public JFXButton getArchiveButton() {
        return archiveButton;
    }

    public JFXButton getCloseButton() {
        return closeButton;
    }

}
