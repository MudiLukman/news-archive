package com.kontrol.newsarchive.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class LoginView extends BorderPane {

    private JFXTextField usernameField;
    private JFXPasswordField passwordField;
    private JFXButton loginButton;
    private JFXButton closeButton;

    public LoginView(){
        usernameField = new JFXTextField();
        passwordField = new JFXPasswordField();
        loginButton = new JFXButton("Login");
        closeButton = new JFXButton("Close");
        designGUI();
    }

    private void designGUI() {
        VBox top = new VBox();
        top.setPadding(new Insets(0, 0, 10, 0));
        ImageView logo = new ImageView(new Image(LoginView.class.getResource("/logo.png").toExternalForm()));
        logo.setSmooth(true);
        logo.setFitHeight(120);
        logo.setFitWidth(300);
        top.getChildren().add(logo);
        this.setTop(top);
        VBox center = new VBox(10);
        center.setPadding(new Insets(10, 0, 10, 0));
        Label username = new Label("Username:");
        Label password = new Label("Password:");
        username.setFont(new Font(16));
        username.setTextFill(Color.WHITE);
        password.setFont(new Font(16));
        password.setTextFill(Color.WHITE);
        usernameField.setStyle("-fx-text-fill: rgb(255, 255, 255);");
        usernameField.setFocusColor(Color.rgb(238, 167, 121));
        usernameField.setUnFocusColor(Color.WHITE);
        passwordField.setStyle("-fx-text-fill: rgb(255, 255, 255);");
        passwordField.setFocusColor(Color.rgb(238, 167, 121));
        passwordField.setUnFocusColor(Color.WHITE);
        center.getChildren().addAll(new HBox(10, username, usernameField),
                new HBox(10, password, passwordField));
        this.setCenter(center);
        HBox bottom = new HBox();
        bottom.setPadding(new Insets(20, 0, 0, 0));
        Pane spacer1 = new Pane();
        Pane spacer2 = new Pane();
        Pane spacer3 = new Pane();
        loginButton.setDefaultButton(true);
        loginButton.setPadding(new Insets(10));
        loginButton.setButtonType(JFXButton.ButtonType.RAISED);
        loginButton.setStyle("-fx-background-color: rgb(238, 167, 121);"
                + "-fx-border-radius: 5;"
                + "-fx-background-radius: 5");
        closeButton.setCancelButton(true);
        closeButton.setPadding(new Insets(10));
        closeButton.setButtonType(JFXButton.ButtonType.RAISED);
        closeButton.setStyle("-fx-background-color: rgb(238, 167, 121);"
                + "-fx-border-radius: 5;"
                + "-fx-background-radius: 5");
        bottom.getChildren().addAll(spacer1, loginButton, spacer2, closeButton, spacer3);
        HBox.setHgrow(spacer1, Priority.SOMETIMES);
        HBox.setHgrow(spacer2, Priority.SOMETIMES);
        HBox.setHgrow(spacer3, Priority.SOMETIMES);
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

    public JFXButton getLoginButton() {
        return loginButton;
    }

    public JFXButton getCloseButton() {
        return closeButton;
    }
}
