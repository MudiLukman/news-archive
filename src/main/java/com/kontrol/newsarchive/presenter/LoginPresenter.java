package com.kontrol.newsarchive.presenter;

import com.kontrol.newsarchive.Launcher;
import com.kontrol.newsarchive.model.DeskOfficer;
import com.kontrol.newsarchive.util.AlertMaker;
import com.kontrol.newsarchive.util.DatabaseHelper;
import com.kontrol.newsarchive.view.LoginView;
import javafx.event.ActionEvent;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginPresenter {

    private LoginView view;
    public static String usernameOfLoggedInUser;
    private double dragOffsetX;
    private double dragOffsetY;

    public LoginPresenter(){
        view = new LoginView();
        addDragListeners();
        addListeners();
    }

    private void addListeners() {
        getView().getUsernameField().setOnAction(this::handleUsernameAction);
        getView().getPasswordField().setOnAction(this::handlePasswordFieldAction);
        getView().getLoginButton().setOnAction(this::handleLoginClicked);
        getView().getCloseButton().setOnAction(this::handleCloseClicked);
    }

    private void handleUsernameAction(ActionEvent event){
        getView().getPasswordField().requestFocus();
    }

    private void handlePasswordFieldAction(ActionEvent event){
        handleLoginClicked(null);
    }

    private void handleLoginClicked(ActionEvent event){
        if(!inputsAreValid()){
            return;
        }
        if(fetchUserFromDb()){
            Rectangle2D screenDim = Screen.getPrimary().getVisualBounds();
            Launcher.switchWindow(getView(), new MainPresenter().getView(),
                    (int) screenDim.getWidth() - 5, (int) screenDim.getHeight() - 10);
        }
        else {
            AlertMaker.showNotification("Error", "Invalid Username and password combination", AlertMaker.image_warning);
        }
    }

    private boolean fetchUserFromDb() {
        ResultSet resultSet = DatabaseHelper.getUserNamePassword_admin();
        try {
            while (resultSet.next()){
                if(getView().getUsernameField().getText().equals(resultSet.getString("username"))
                        && DeskOfficer.sha1(getView().getPasswordField().getText()).equals(resultSet.getString("password"))){
                    usernameOfLoggedInUser = resultSet.getString("username");
                    return true;
                }
            }
        } catch (SQLException e) {
            AlertMaker.showErrorMessage(e);
        }

        return false;
    }

    private boolean inputsAreValid() {
        if(getView().getUsernameField().getText().trim().equals("")){
            AlertMaker.showNotification("Error", "No value for username", AlertMaker.image_warning);
            return false;
        }
        if(getView().getPasswordField().getText().trim().equals("")){
            AlertMaker.showNotification("Error", "No value for password", AlertMaker.image_warning);
            return false;
        }

        return true;
    }

    private void handleCloseClicked(ActionEvent event){
        System.exit(0);
    }

    private void addDragListeners() {
        getView().setOnMousePressed(this::handleMousePressed);
        getView().setOnMouseDragged(this::handleMouseDragged);
    }

    private void handleMousePressed(MouseEvent event){
        this.dragOffsetX = event.getScreenX() - ((Stage) this.getView().getScene().getWindow()).getX();
        this.dragOffsetY = event.getScreenY() - ((Stage) this.getView().getScene().getWindow()).getY();
    }

    private void handleMouseDragged(MouseEvent event){
        getView().getScene().getWindow().setX(event.getScreenX() - this.dragOffsetX);
        getView().getScene().getWindow().setY(event.getScreenY() - this.dragOffsetY);
    }

    public LoginView getView(){
        return view;
    }

}
