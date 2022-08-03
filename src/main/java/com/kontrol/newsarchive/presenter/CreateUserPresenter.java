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

    private final CreateUserView view = new CreateUserView();
    private double dragOffsetX;
    private double dragOffsetY;

    public static final DeskOfficer officer = new DeskOfficer();
    public static final List<Newswire> newswires = new ArrayList<>();
    public static final List<Keyword> keywords = new ArrayList<>();

    public CreateUserPresenter(){
        addListeners();
        addDragListeners();
    }

    private void addListeners() {
        getView().getUsernameField().setOnAction(e -> getView().getPasswordField().requestFocus());
        getView().getPasswordField().setOnAction(e -> getView().getConfirmPasswordField().requestFocus());
        getView().getConfirmPasswordField().setOnAction(e -> handleNextClicked());
        getView().getNextBtn().setOnAction(e -> handleNextClicked());
        getView().getCancelBtn().setOnAction(e -> System.exit(0));
    }

    private void handleNextClicked() {
        if(!validateInput()){
          return;
        }
        officer.setUsername(getView().getUsernameField().getText());
        officer.setPassword(getView().getPasswordField().getText());
        Launcher.switchWindow(getView(), new SetNewswiresPresenter().getView(), 600, 650);
    }

    private boolean validateInput() {
        String username = getView().getUsernameField().getText();
        String password = getView().getPasswordField().getText();
        String confirmPassword = getView().getConfirmPasswordField().getText();

        return isValidUsername(username) && isValidPassword(password, confirmPassword);
    }

    private boolean isValidUsername(String username) {
        if(username.trim().equals("")){
            AlertMaker.showNotification("Input Error", "No value was entered for username", AlertMaker.image_cross);
            return false;
        }

        if(username.length() <= 3){
            AlertMaker.showNotification("Alert", "Username is too short (4 characters at least)", AlertMaker.image_warning);
            return false;
        }
        return true;
    }

    private boolean isValidPassword(String password, String confirmPassword) {
        if(password.trim().equals("")){
            AlertMaker.showNotification("Input Error", "No value was entered for password", AlertMaker.image_cross);
            return false;
        }

        if(!password.equals(confirmPassword)){
            AlertMaker.showNotification("Input Error", "Both passwords do not match", AlertMaker.image_cross);
            return false;
        }

        if(password.length() < 8){
            AlertMaker.showNotification("Alert", "Password is too short (8 characters at least)", AlertMaker.image_warning);
            return false;
        }

        boolean hasLetter = false;
        boolean hasDigit = false;
        for(int i = 0; i < password.length(); i++){
            if(Character.isLetter(password.charAt(i))){
                hasLetter = true;
            }
            else if(Character.isDigit(password.charAt(i))){
                hasDigit = true;
            }
        }

        if(!hasLetter || !hasDigit){
            AlertMaker.showNotification(
                    "Warning", "Password must contain an alphabet, and a number",
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
