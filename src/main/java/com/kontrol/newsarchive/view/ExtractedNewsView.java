package com.kontrol.newsarchive.view;

import com.jfoenix.effects.JFXDepthManager;
import com.kontrol.newsarchive.model.Document;
import com.kontrol.newsarchive.util.CustomImageView;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;

public class ExtractedNewsView extends StackPane {

    private Document relevantDocument;

    private VBox layoutVBox;
    private CustomImageView webLogo = new CustomImageView("/globe.png", 20, 20);
    private Label sourceLbl;
    private CustomImageView minusBtn = new CustomImageView("/minus.png", 16, 14);
    private Label titleLbl;
    private Label timeLbl;

    private ExtractNewsView parentPane;

    public ExtractedNewsView(Document relevantDocument, ExtractNewsView parentPane){
        this.relevantDocument = relevantDocument;
        layoutVBox = new VBox(5);
        sourceLbl = new Label(this.relevantDocument.getOwner());
        titleLbl = new Label(this.relevantDocument.getTitle());
        timeLbl = new Label(this.relevantDocument.getDate());
        this.parentPane = parentPane;

        designUI();
    }

    private void designUI() {
        this.setPrefSize(250, 100);
        this.setStyle("-fx-background-color: rgb(255, 255, 255);" +
                "-fx-background-radius: 5;" +
                "-fx-border-radius: 5;");
        JFXDepthManager.setDepth(this, 1);

        webLogo.setSmooth(true);
        sourceLbl.setStyle("-fx-font-weight: bold;");

        minusBtn.setPickOnBounds(true);

        titleLbl.setWrapText(true);
        titleLbl.setTextAlignment(TextAlignment.JUSTIFY);

        Pane verticalSpace = new Pane();
        Pane horizontalSpace = new Pane();
        VBox.setVgrow(verticalSpace, Priority.SOMETIMES);
        HBox.setHgrow(horizontalSpace, Priority.SOMETIMES);
        layoutVBox.setPadding(new Insets(5));
        Pane spaceInHeader = new Pane();
        HBox sourceHBox = new HBox(5, webLogo, sourceLbl, spaceInHeader, minusBtn);
        HBox.setHgrow(spaceInHeader, Priority.SOMETIMES);
        layoutVBox.getChildren().addAll(sourceHBox, titleLbl, verticalSpace, new HBox(horizontalSpace, timeLbl));
        this.getChildren().add(layoutVBox);
    }

    public Document getRelevantDocument() {
        return relevantDocument;
    }

    public VBox getLayoutVBox() {
        return layoutVBox;
    }

    public CustomImageView getWebLogo() {
        return webLogo;
    }

    public Label getSourceLbl() {
        return sourceLbl;
    }

    public CustomImageView getMinusBtn() {
        return minusBtn;
    }

    public Label getTitleLbl() {
        return titleLbl;
    }

    public Label getTimeLbl() {
        return timeLbl;
    }

    public ExtractNewsView getParentPane() {
        return parentPane;
    }
}
