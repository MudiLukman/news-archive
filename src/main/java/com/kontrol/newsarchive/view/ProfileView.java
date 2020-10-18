package com.kontrol.newsarchive.view;

import com.kontrol.newsarchive.util.CustomImageView;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ProfileView extends BorderPane {

    public ProfileView(){
        designGUI();
    }

    private void designGUI() {
        VBox vBox = new VBox(15);
        vBox.setAlignment(Pos.CENTER);
        CustomImageView controlIcon= new CustomImageView("/control.jpg", 150, 150);
        controlIcon.setSmooth(true);
        Label kontrolLbl = new Label("Kontrol");
        kontrolLbl.setFont(new Font(24));
        kontrolLbl.setTextFill(Color.WHITE);
        Label descLbl = new Label("Language-agnostic Programmer");
        descLbl.setFont(new Font(20));
        descLbl.setTextFill(Color.WHITE);
        vBox.getChildren().addAll(controlIcon, kontrolLbl, descLbl);
        this.setCenter(vBox);
        this.setStyle("-fx-background-color: rgb(62, 73, 86);" +
                "-fx-padding: 2;" +
                "-fx-border-width: 5;" +
                "-fx-border-color: rgb(225, 225, 255);" +
                "-fx-background-radius: 0;");
    }

}
