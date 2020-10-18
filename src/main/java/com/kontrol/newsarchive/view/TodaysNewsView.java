package com.kontrol.newsarchive.view;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.time.LocalDate;

public class TodaysNewsView extends BorderPane {

    private HBox todaysNewsHeaderHBox;
    private StackPane stackPane;
    private ProgressIndicator progressIndicator;
    private ScrollPane contentArea;
    private FlowPane contentAreaFlowPane;
    private String monthValue = "";
    private Label todaysNewsHeader;

    public TodaysNewsView(){
        todaysNewsHeaderHBox = new HBox();
        stackPane = new StackPane();
        progressIndicator = new ProgressIndicator();
        contentArea = new ScrollPane();
        contentAreaFlowPane = new FlowPane(20, 20);
        designGUI();
    }

    private void designGUI() {
        //make top
        LocalDate now = LocalDate.now();
        int month = now.getMonthValue();

        switch (month){
            case 1:
                monthValue = "January";
                break;
            case 2:
                monthValue = "February";
                break;
            case 3:
                monthValue = "March";
                break;
            case 4:
                monthValue = "April";
                break;
            case 5:
                monthValue = "May";
                break;
            case 6:
                monthValue = "June";
                break;
            case 7:
                monthValue = "July";
                break;
            case 8:
                monthValue = "August";
                break;
            case 9:
                monthValue = "September";
                break;
            case 10:
                monthValue = "October";
                break;
            case 11:
                monthValue = "November";
                break;
            case 12:
                monthValue = "December";
        }

        todaysNewsHeader = new Label("News Today " + monthValue + " " + now.getDayOfMonth() + ", " + now.getYear() + ". ");
        todaysNewsHeader.setFont(new Font(20));
        todaysNewsHeader.setStyle("-fx-font-weight: bold;");
        Pane spaceBeforeTodaysNewsHeader = new Pane();
        Pane spaceAfterTodaysNewsHeader = new Pane();
        HBox.setHgrow(spaceBeforeTodaysNewsHeader, Priority.SOMETIMES);
        HBox.setHgrow(spaceAfterTodaysNewsHeader, Priority.SOMETIMES);
        todaysNewsHeaderHBox.setPadding(new Insets(5, 0, 5, 0));
        todaysNewsHeaderHBox.getChildren().addAll(spaceBeforeTodaysNewsHeader, todaysNewsHeader, spaceAfterTodaysNewsHeader);
        this.setTop(todaysNewsHeaderHBox);

        //init center nodes
        progressIndicator.setMaxHeight(70);
        progressIndicator.setMaxWidth(70);
        contentArea.setContent(contentAreaFlowPane);
        contentArea.setFitToWidth(true);
        contentArea.setFitToHeight(true);
        contentArea.setPannable(true);
        contentAreaFlowPane.setPadding(new Insets(10, 0, 40, 10));
        stackPane.getChildren().addAll(contentArea, progressIndicator);
        this.setCenter(stackPane);

        this.setStyle("-fx-background-color: rgb(244, 244, 243);");
    }

    public FlowPane getContentAreaFlowPane() {
        return contentAreaFlowPane;
    }

    public Label getTodaysNewsHeader() {
        return todaysNewsHeader;
    }

    public StackPane getStackPane() {
        return stackPane;
    }

    public ProgressIndicator getProgressIndicator() {
        return progressIndicator;
    }

    public ScrollPane getContentArea() {
        return contentArea;
    }
}
