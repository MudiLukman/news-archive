package com.kontrol.newsarchive.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class CreateUserView extends BorderPane{

    private JFXTextField usernameField;
    private JFXPasswordField passwordField;
    private JFXPasswordField confirmPasswordField;
    private JFXButton nextBtn;
    private JFXButton cancelBtn;

    public CreateUserView(){
        usernameField = new JFXTextField();
        passwordField = new JFXPasswordField();
        confirmPasswordField = new JFXPasswordField();
        nextBtn = new JFXButton("Next");
        cancelBtn = new JFXButton("Cancel");

        designGUI();
    }

    private void designGUI() {

        VBox center = new VBox(15);
        ImageView logo = new ImageView(new Image(CreateUserView.class.getResource("/logo.png").toExternalForm()));
        logo.setSmooth(true);
        logo.setFitWidth(350);
        logo.setFitHeight(120);
        logo.setPreserveRatio(true);
        center.getChildren().add(logo);
        Label welcomeText = new Label("WELCOME USER!");
        welcomeText.setFont(new Font(18));
        welcomeText.setTextFill(Color.WHITE);
        Label guideText = new Label("This setup will guide you through the installation process");
        guideText.setFont(new Font(16));
        guideText.setTextFill(Color.WHITE);
        VBox mainContent = new VBox(10);
        Label helpText = new Label("Pick a Username and Password");
        helpText.setFont(new Font(16));
        helpText.setTextFill(Color.WHITE);
        Label usernameLbl = new Label("Username:");
        usernameLbl.setFont(new Font(16));
        usernameLbl.setTextFill(Color.WHITE);
        Label passwordLbl = new Label("Password: ");
        passwordLbl.setFont(new Font(16));
        passwordLbl.setTextFill(Color.WHITE);
        Label confirmLbl = new Label("Confirm:*  ");
        confirmLbl.setFont(new Font(16));
        confirmLbl.setTextFill(Color.WHITE);
        usernameField.setFont(new Font(14));
        usernameField.setStyle("-fx-text-fill: rgb(255, 255, 255);");
        usernameField.setFocusColor(Color.rgb(238, 167, 121));
        usernameField.setUnFocusColor(Color.WHITE);
        passwordField.setFont(new Font(14));
        passwordField.setStyle("-fx-text-fill: rgb(255, 255, 255);");
        passwordField.setFocusColor(Color.rgb(238, 167, 121));
        passwordField.setUnFocusColor(Color.WHITE);
        confirmPasswordField.setFont(new Font(14));
        confirmPasswordField.setStyle("-fx-text-fill: rgb(255, 255, 255);");
        confirmPasswordField.setFocusColor(Color.rgb(238, 167, 121));
        confirmPasswordField.setUnFocusColor(Color.WHITE);
        mainContent.getChildren().addAll(helpText, new HBox(5, usernameLbl, usernameField),
                new HBox(5, passwordLbl, passwordField),
                new HBox(5, confirmLbl, confirmPasswordField));
        center.getChildren().addAll(welcomeText, guideText, mainContent);
        this.setCenter(center);

        HBox bottom = new HBox(5);
        Pane space = new Pane();
        Label step = new Label("Step 1 of 3");
        step.setTextFill(Color.WHITE);
        bottom.getChildren().addAll(step, space, nextBtn, cancelBtn);
        nextBtn.setPadding(new Insets(10));
        nextBtn.setPrefSize(70, 40);
        nextBtn.setDefaultButton(true);
        nextBtn.setButtonType(JFXButton.ButtonType.RAISED);
        nextBtn.setStyle("-fx-background-color: rgb(238, 167, 121);"
                + "-fx-border-radius: 5;"
                + "-fx-background-radius: 5");
        cancelBtn.setPadding(new Insets(10));
        cancelBtn.setPrefSize(70, 40);
        cancelBtn.setButtonType(JFXButton.ButtonType.RAISED);
        cancelBtn.setCancelButton(true);
        cancelBtn.setStyle("-fx-background-color: rgb(238, 167, 121);"
                + "-fx-border-radius: 5;"
                + "-fx-background-radius: 5");
        HBox.setHgrow(space, Priority.SOMETIMES);
        this.setBottom(bottom);

        this.setStyle("-fx-background-color: rgb(62, 73, 86);" +
                "-fx-padding: 30;" +
                "-fx-background-radius: 0;");
    }

    public JFXTextField getUsernameField() {
        return usernameField;
    }

    public JFXPasswordField getPasswordField() {
        return passwordField;
    }

    public JFXPasswordField getConfirmPasswordField() {
        return confirmPasswordField;
    }

    public JFXButton getNextBtn() {
        return nextBtn;
    }

    public JFXButton getCancelBtn() {
        return cancelBtn;
    }
}
