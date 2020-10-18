package com.kontrol.newsarchive.presenter;

import com.kontrol.newsarchive.Launcher;
import com.kontrol.newsarchive.model.DeskOfficer;
import com.kontrol.newsarchive.model.Keyword;
import com.kontrol.newsarchive.model.Newswire;
import com.kontrol.newsarchive.util.AlertMaker;
import com.kontrol.newsarchive.view.CreateUserView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class CreateUserPresenter {

    private CreateUserView view;
    private double dragOffsetX;
    private double dragOffsetY;

    public static DeskOfficer officer;
    public static List<Newswire> newswires;
    public static List<Keyword> keywords;

    public CreateUserPresenter(){
        view = new CreateUserView();
        officer = new DeskOfficer();
        newswires = new ArrayList<>();
        keywords = new ArrayList<>();
        addListeners();
        addDragListeners();
    }

    private void addListeners() {
        getView().getUsernameField().setOnAction((e)-> getView().getPasswordField().requestFocus());
        getView().getPasswordField().setOnAction((e)-> getView().getConfirmPasswordField().requestFocus());
        getView().getConfirmPasswordField().setOnAction((e)-> handleNextClicked());
        getView().getNextBtn().setOnAction((e)-> handleNextClicked());
        getView().getCancelBtn().setOnAction((e)-> System.exit(0));
    }

    private void handleNextClicked() {
        if(validateInput()){
            officer.setUsername(getView().getUsernameField().getText());
            officer.setPassword(getView().getPasswordField().getText());
            Launcher.switchWindow(getView(), new SetNewswiresPresenter().getView(), 600, 650);
        }
    }

    private boolean validateInput() {
        String usernameText = getView().getUsernameField().getText();
        String passwordText = getView().getPasswordField().getText();

        if(usernameText.trim().equals("")){
            AlertMaker.showNotification("Input Error", "No value was entered for username", AlertMaker.image_cross);
            return false;
        }
        if(passwordText.trim().equals("")){
            AlertMaker.showNotification("Input Error", "No value was entered for password", AlertMaker.image_cross);
            return false;
        }
        if(passwordText.compareTo(getView().getConfirmPasswordField().getText().trim()) != 0){
            AlertMaker.showNotification("Input Error", "Both passwords do not match", AlertMaker.image_cross);
            return false;
        }
        if(usernameText.length() <= 3){
            AlertMaker.showNotification("Alert", "Username is too short (4 characters at least)", AlertMaker.image_warning);
            return false;
        }
        if(passwordText.length() <= 7){
            AlertMaker.showNotification("Alert", "Password is too short (8 characters at least)", AlertMaker.image_warning);
            return false;
        }

        boolean hasLetter = false;
        boolean hasDigit = false;
        boolean hasSymbol = false;
        for(int i = 0; i < passwordText.length(); i++){
            if(Character.isLetter(passwordText.charAt(i))){
                hasLetter = true;
            }
            else if(Character.isDigit(passwordText.charAt(i))){
                hasDigit = true;
            }
            else {
                hasSymbol = true;
            }
        }

        if(!hasLetter || !hasDigit || !hasSymbol){
            AlertMaker.showNotification(
                    "Warning", "Password must contain an alphabet, a number, and a special character",
                    AlertMaker.image_warning);
            return false;
        }

        return true;
    }

    public CreateUserView getView(){
        return view;
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
}
