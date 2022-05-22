package com.kontrol.newsarchive.presenter;

import com.kontrol.newsarchive.Launcher;
import com.kontrol.newsarchive.model.DeskOfficer;
import com.kontrol.newsarchive.model.Keyword;
import com.kontrol.newsarchive.model.Newswire;
import com.kontrol.newsarchive.util.AlertMaker;
import com.kontrol.newsarchive.util.DatabaseHelper;
import com.kontrol.newsarchive.view.CreateKeywordView;
import javafx.event.ActionEvent;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class CreateKeywordPresenter {

    private final CreateKeywordView view = new CreateKeywordView();
    private double dragOffsetX;
    private double dragOffsetY;

    public CreateKeywordPresenter(){
        addDragListeners();
        addListeners();
    }

    private void addListeners() {
        getView().getBackButton().setOnAction(this::handleBackPressed);
        getView().getFinishButton().setOnAction(this::handleFinishClicked);
        getView().getCancelButton().setOnAction(this::handleCancelClicked);
    }

    private void handleCancelClicked(ActionEvent event){
        System.exit(0);
    }

    private void handleFinishClicked(ActionEvent event){
        if(getView().getKeywords().getChips().isEmpty()){
            AlertMaker.showNotification("Error", "You have to specify at least one tag", AlertMaker.image_warning);
            return;
        }

        CreateUserPresenter.keywords.clear();
        for(String tag : getView().getKeywords().getChips()){
            CreateUserPresenter.keywords.add(new Keyword(tag));
        }

        String interval;
        if(getView().getIntervalToggleGroup().getSelectedToggle().equals(getView().getFiveAMRadioBtn())){
            interval = "5";
        }
        else if(getView().getIntervalToggleGroup().getSelectedToggle().equals(getView().getEightAMRadioBtn())){
            interval = "8";
        }
        else if(getView().getIntervalToggleGroup().getSelectedToggle().equals(getView().getElevenAMRadioBtn())){
            interval = "11";
        }
        else if(getView().getIntervalToggleGroup().getSelectedToggle().equals(getView().getTwoPMRadioBtn())){
            interval = "14";
        }
        else if(getView().getIntervalToggleGroup().getSelectedToggle().equals(getView().getFourPMRadioBtn())){
            interval = "16";
        }
        else {
            interval = "5";
        }

        if (!saveRecordToDatabase(interval)) {
            return;
        }
        if(getView().getLaunchAppCheckBox().isSelected()){
            Rectangle2D screenDim = Screen.getPrimary().getVisualBounds();
            Launcher.switchWindow(getView(), new MainPresenter().getView(),
                    (int) screenDim.getWidth() - 5, (int) screenDim.getHeight() - 10);
        }
        else {
            System.exit(0);
        }
    }

    private boolean saveRecordToDatabase(String interval) {
        String insertDeskOfficerSql = "INSERT INTO deskofficer(username, password, intervals, aggregatorname, aggregatorurl) VALUES (" +
                "'" + CreateUserPresenter.officer.getUsername() + "', '" + DeskOfficer.sha1(CreateUserPresenter.officer.getPassword()) + "', " +
                "'" + interval + "', '" + CreateUserPresenter.officer.getAggregatorName() + "', " +
                "'" + CreateUserPresenter.officer.getAggregatorUrl() + "');";

        if (DatabaseHelper.insertRecord(insertDeskOfficerSql) == 0) {
            return false;
        }
        for(Newswire newswire : CreateUserPresenter.newswires){
            String insertNewswireSql = "INSERT INTO newswire(url) VALUES (" + "'" + newswire.getUrl() + "');";
            DatabaseHelper.insertRecord(insertNewswireSql);
        }
        for(Keyword keyword : CreateUserPresenter.keywords){
            String insertKeywordSql = "INSERT INTO keyword(value) VALUES (" + "'" + keyword.getValue() + "');";
            DatabaseHelper.insertRecord(insertKeywordSql);
        }
        AlertMaker.showNotification("Success", "User, Newswire, and Keywords Created Successfully", AlertMaker.image_checked);
        return true;
    }

    private void handleBackPressed(ActionEvent event){
        Launcher.switchWindow(getView(), new SetNewswiresPresenter().getView(), 600, 650);
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

    public CreateKeywordView getView(){
        return view;
    }

}
