package com.kontrol.newsarchive.presenter;

import com.kontrol.newsarchive.model.DeskOfficer;
import com.kontrol.newsarchive.util.AlertMaker;
import com.kontrol.newsarchive.util.DatabaseHelper;
import com.kontrol.newsarchive.view.SettingsView;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SettingsPresenter {

    private SettingsView view;

    public SettingsPresenter(){
        view = new SettingsView();
        presetValues();
        addEventHandlers();
    }

    private void presetValues() {
        switch (getCurrentExtractionInterval()){
            case "5":
                getView().getFiveAMRadioBtn().setSelected(true);
                break;
            case "8":
                getView().getEightAMRadioBtn().setSelected(true);
                break;
            case "11":
                getView().getElevenAMRadioBtn().setSelected(true);
                break;
            case "14":
                getView().getTwoPMRadioBtn().setSelected(true);
                break;
            case "16":
                getView().getFourPMRadioBtn().setSelected(true);
                break;
        }
        getView().getAggregatorNameField().setText(getAggregatorName());
        getView().getAggregatorUrlField().setText(getAggregatorUrl());
        getView().getNewswireChipView().getChips().addAll(getNewswires());
        getView().getKeywordsChipView().getChips().addAll(getKeywords());
    }

    private void addEventHandlers() {
        getView().getCloseIcon().setOnMouseClicked(this::handleCloseClicked);
        getView().getUpdatePasswordBtn().setOnAction(this::handleUpdatePassword);
        getView().getUpdateIntervalBtn().setOnAction(this::handleUpdateIntervalClicked);
        getView().getUpdateAggregatorBtn().setOnAction(this::handleUpdateAggregator);
        getView().getEditNewswireBtn().setOnAction(event -> {
            if(getView().getNewswireChipView().isDisabled()){
                getView().getNewswireChipView().setDisable(false);
            }
        });
        getView().getEditKeywords().setOnAction(event -> {
            if(getView().getKeywordsChipView().isDisabled()){
                getView().getKeywordsChipView().setDisable(false);
            }
        });
        getView().getUpdateNewswireBtn().setOnAction(this::handleUpdateNewswireClicked);
        getView().getUpdateKeywords().setOnAction(this::handleUpdateKeywordClicked);
    }

    private void handleUpdateKeywordClicked(ActionEvent event){
        DatabaseHelper.insert_record("DELETE FROM keyword");
        if(getView().getKeywordsChipView().getChips().isEmpty()){
            AlertMaker.showNotification("Error", "You have to specify at least one tag", AlertMaker.image_warning);
            return;
        }

        for(Object k : getView().getKeywordsChipView().getChips()){
            String keyword = (String) k;
            String sql = "INSERT INTO keyword(value) VALUES (" + "'" + keyword + "');";
            DatabaseHelper.insert_record(sql);
        }
        AlertMaker.showNotification("Success", "Keywords updated Successfully", AlertMaker.image_checked);
        getView().getKeywordsChipView().setDisable(true);
    }

    private void handleUpdateNewswireClicked(ActionEvent event){
        DatabaseHelper.insert_record("DELETE FROM newswire");
        if(getView().getNewswireChipView().getChips().isEmpty()){
            AlertMaker.showNotification("Error", "You have to specify at least one newswire source", AlertMaker.image_warning);
            return;
        }

        for(Object n : getView().getNewswireChipView().getChips()){
            String newswire = (String) n;
            String sql = "INSERT INTO newswire(url) VALUES (" + "'" + n + "');";
            DatabaseHelper.insert_record(sql);
        }
        AlertMaker.showNotification("Success", "Keywords updated Successfully", AlertMaker.image_checked);
        getView().getNewswireChipView().setDisable(true);
    }

    private void handleUpdateAggregator(ActionEvent event){
        if(getView().getAggregatorNameField().getText().trim().equals("")){
            AlertMaker.showNotification("Error", "Empty input or 'aggregator name'", AlertMaker.image_warning);
            return;
        }
        if(getView().getAggregatorUrlField().getText().trim().equals("")){
            AlertMaker.showNotification("Error", "Empty input or 'aggregator url'", AlertMaker.image_warning);
            return;
        }
        if(!SetNewswiresPresenter.isUrlValid(getView().getAggregatorUrlField().getText())){
            AlertMaker.showNotification("Error", "Invalid url", AlertMaker.image_cross);
            return;
        }
        String aggregatorName = getView().getAggregatorNameField().getText();
        String aggregatorUrl = getView().getAggregatorUrlField().getText();
        String sql = "UPDATE deskofficer set aggregatorname='"
                + aggregatorName + "', aggregatorurl='" + aggregatorUrl
                + "' WHERE username='" + LoginPresenter.usernameOfLoggedInUser + "'";
        if(DatabaseHelper.insert_record(sql) != 0){
            AlertMaker.showNotification("Success", "Aggregator Updated Successfully", AlertMaker.image_checked);
        }
        else {
            AlertMaker.showNotification("Error", "Failed to update aggregator", AlertMaker.image_cross);
        }
    }

    private void handleUpdateIntervalClicked(ActionEvent event){
        String interval;
        if(getView().getRadioBtnToggleGroup().getSelectedToggle().equals(getView().getFiveAMRadioBtn())){
            interval = "5";
        }
        else if(getView().getRadioBtnToggleGroup().getSelectedToggle().equals(getView().getEightAMRadioBtn())){
            interval = "8";
        }
        else if(getView().getRadioBtnToggleGroup().getSelectedToggle().equals(getView().getElevenAMRadioBtn())){
            interval = "11";
        }
        else if(getView().getRadioBtnToggleGroup().getSelectedToggle().equals(getView().getTwoPMRadioBtn())){
            interval = "14";
        }
        else if(getView().getRadioBtnToggleGroup().getSelectedToggle().equals(getView().getFourPMRadioBtn())){
            interval = "16";
        }
        else {
            interval = "5";
        }

        String sql = "UPDATE deskofficer set intervals='"
                + interval + "' WHERE username='" + LoginPresenter.usernameOfLoggedInUser + "'";
        if(DatabaseHelper.insert_record(sql) != 0){
            AlertMaker.showNotification("Success", "Interval updated successfully", AlertMaker.image_checked);
        }
        else {
            AlertMaker.showNotification("Error", "Unable to update interval", AlertMaker.image_warning);
        }
    }

    private void handleUpdatePassword(ActionEvent event){
        if(getView().getCurrentPasswordField().getText().trim().equals("")){
            AlertMaker.showNotification("Error", "No input value for 'current password'",
                    AlertMaker.image_warning);
            return;
        }
        if(getView().getNewPasswordField().getText().trim().equals("")){
            AlertMaker.showNotification("Error", "No input value for 'new password'",
                    AlertMaker.image_warning);
            return;
        }
        if(getView().getConfirmPasswordField().getText().trim().equals("")){
            AlertMaker.showNotification("Error", "No input value for 'confirm password'",
                    AlertMaker.image_warning);
            return;
        }
        if(!getView().getNewPasswordField().getText().equals(getView().getConfirmPasswordField().getText())){
            AlertMaker.showNotification("Error",
                    "Both value for new password and confirm password must be the same",
                    AlertMaker.image_warning);
            return;
        }
        if(!DeskOfficer.sha1(getView().getCurrentPasswordField().getText()).equals(getCurrentPassword())){
            AlertMaker.showNotification("Error", "Wrong old password", AlertMaker.image_warning);
            return;
        }

        String hashedPassword = DeskOfficer.sha1(getView().getNewPasswordField().getText());
        String sql = "UPDATE deskofficer set password='"
                + hashedPassword + "' WHERE username='" + LoginPresenter.usernameOfLoggedInUser + "'";
        if(DatabaseHelper.insert_record(sql) != 0){
            AlertMaker.showNotification("Success", "Password has been changed successfully",
                    AlertMaker.image_checked);
            getView().getCurrentPasswordField().setText("");
            getView().getNewPasswordField().setText("");
            getView().getConfirmPasswordField().setText("");
        }
        else {
            AlertMaker.showNotification("Error", "Unable to change current password", AlertMaker.image_cross);
        }
    }

    private void handleCloseClicked(MouseEvent mouseEvent){
        getView().getScene().getWindow().hide();
    }

    public SettingsView getView(){
        return view;
    }

    public static String getCurrentExtractionInterval() {
        String timeInterval = "";
        String sql = "SELECT * FROM deskofficer WHERE username='" + LoginPresenter.usernameOfLoggedInUser + "'";
        ResultSet resultSet = DatabaseHelper.executeQuery(sql);
        try {
            resultSet.next();
            timeInterval = resultSet.getString("intervals");
        }catch (SQLException e){
            System.out.println(e);
        }
        return timeInterval;
    }

    private String getAggregatorName() {
        String aggregatorName = "";
        String sql = "SELECT * FROM deskofficer WHERE username='" + LoginPresenter.usernameOfLoggedInUser + "'";
        ResultSet resultSet = DatabaseHelper.executeQuery(sql);

        try {
            resultSet.next();
            aggregatorName = resultSet.getString("aggregatorname");
        }catch (SQLException e){
            System.out.println(e);
        }
        return aggregatorName;
    }

    private String getAggregatorUrl() {
        String aggregatorUrl = "";
        String sql = "SELECT * FROM deskofficer WHERE username='" + LoginPresenter.usernameOfLoggedInUser + "'";
        ResultSet resultSet = DatabaseHelper.executeQuery(sql);

        try {
            resultSet.next();
            aggregatorUrl = resultSet.getString("aggregatorurl");
        }catch (SQLException e){
            System.out.println(e);
        }
        return aggregatorUrl;
    }

    private List<String> getNewswires() {
        List<String> newswires = new ArrayList<>();
        String sql = "SELECT * FROM newswire";
        ResultSet resultSet = DatabaseHelper.executeQuery(sql);

        try {
            while (resultSet.next()){
                newswires.add(resultSet.getString("url"));
            }
        }catch (SQLException e){
            System.out.println(e);
        }
        return newswires;
    }

    private List<String> getKeywords() {
        List<String> keywords = new ArrayList<>();
        String sql = "SELECT * FROM keyword";
        ResultSet resultSet = DatabaseHelper.executeQuery(sql);

        try {
            while (resultSet.next()){
                keywords.add(resultSet.getString("value"));
            }
        }catch (SQLException e){
            System.out.println(e);
        }
        return keywords;
    }

    private String getCurrentPassword() {
        String currentPassword = "";
        String sql = "SELECT * FROM deskofficer WHERE username='" + LoginPresenter.usernameOfLoggedInUser + "'";
        ResultSet resultSet = DatabaseHelper.executeQuery(sql);

        try {
            resultSet.next();
            currentPassword = resultSet.getString("password");
        }catch (SQLException e){
            System.out.println(e);
        }
        return currentPassword;
    }
}
