package com.kontrol.newsarchive.view;

import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;

public class SearchResultView extends BorderPane {

    private StackPane stackPane;
    private ScrollPane contentArea;
    private FlowPane contentAreaFlowPane;

    public SearchResultView(){
        stackPane = new StackPane();
        contentArea = new ScrollPane();
        contentAreaFlowPane = new FlowPane(0, 20);
        designTheGUI();
    }

    private void designTheGUI() {
        //init node's config
        contentArea.setContent(contentAreaFlowPane);
        contentArea.setFitToHeight(true);
        contentArea.setFitToWidth(true);
        contentArea.setPannable(true);
        contentAreaFlowPane.setPadding(new Insets(10, 5, 50, 5));
        stackPane.getChildren().add(contentArea);
        this.setCenter(stackPane);
        this.setStyle("-fx-background-color: rgb(244, 244, 243);");
    }

    public ScrollPane getContentArea() {
        return contentArea;
    }

    public StackPane getStackPane() {
        return stackPane;
    }

    public FlowPane getContentAreaFlowPane() {
        return contentAreaFlowPane;
    }
}
