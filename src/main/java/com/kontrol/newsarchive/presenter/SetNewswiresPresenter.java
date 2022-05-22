package com.kontrol.newsarchive.presenter;

import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;
import com.kontrol.newsarchive.Launcher;
import com.kontrol.newsarchive.model.Newswire;
import com.kontrol.newsarchive.util.AlertMaker;
import com.kontrol.newsarchive.util.UrlUtil;
import com.kontrol.newsarchive.view.SetNewswireView;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SetNewswiresPresenter {

    private SetNewswireView view = new SetNewswireView();
    private double dragOffsetX;
    private double dragOffsetY;

    public SetNewswiresPresenter(){
        addDragListeners();
        addListeners();
    }

    private void addListeners() {
        getView().getAggregatorHelp().setOnMouseClicked(this::handleAggregatorHelpClicked);
        getView().getNewswireHelp().setOnMouseClicked(this::handleNewswireHelpClicked);
        getView().getAggregatorName().setOnAction(this::handleAggregatorNameAction);
        getView().getAggregatorUrl().setOnAction(this::handleAddAggregatorUrlAction);
        getView().getBackButton().setOnAction(this::handleBackPressed);
        getView().getNextButton().setOnAction(this::handleNextButton);
        getView().getCancelButton().setOnAction(this::handleCancelPressed);
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

    private void handleBackPressed(ActionEvent event){
        Launcher.switchWindow(getView(), new CreateUserPresenter().getView(), 600, 500);
    }

    private void handleCancelPressed(ActionEvent event){
        System.exit(0);
    }

    private void handleAggregatorHelpClicked(MouseEvent event){
        JFXAlert alert = new JFXAlert((Stage) getView().getScene().getWindow());
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setOverlayClose(false);
        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setHeading(new Label("What is an Aggregator"));
        layout.setBody(new Label("An aggregator is an online website that aggregates " +
                "relevant syndicated news content for easy lookup. An aggregator is needed by ONTES to" +
                " serve as an online source to fetch the news contents to be displayed on " +
                "the main menu"));
        JFXButton closeButton = new JFXButton("Close");
        closeButton.getStyleClass().add("dialog-accept");
        closeButton.setOnAction(e -> alert.hideWithAnimation());
        layout.setActions(closeButton);
        alert.setContent(layout);
        alert.show();
    }

    private void handleNewswireHelpClicked(MouseEvent event){
        JFXAlert alert = new JFXAlert((Stage) getView().getScene().getWindow());
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setOverlayClose(false);
        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setHeading(new Label("Newswire Sources"));
        layout.setBody(new Label("Newswires are needed as the sources to fetch " +
                "relevant news content to be stripped, cleaned, and archived."));
        JFXButton closeButton = new JFXButton("Close");
        closeButton.getStyleClass().add("dialog-accept");
        closeButton.setOnAction(e -> alert.hideWithAnimation());
        layout.setActions(closeButton);
        alert.setContent(layout);
        alert.show();
    }

    private void handleAggregatorNameAction(ActionEvent event){
        getView().getAggregatorUrl().requestFocus();
    }

    private void handleAddAggregatorUrlAction(ActionEvent event){
        getView().getNewswires().requestFocus();
    }

    private void handleNextButton(ActionEvent event){
        if(getView().getAggregatorName().getText().trim().equals("")){
            AlertMaker.showNotification("Error", "You must provide a name for aggregator", AlertMaker.image_warning);
            return;
        }
        if(getView().getAggregatorUrl().getText().trim().equals("")){
            AlertMaker.showNotification("Error", "You must provide a URL for aggregator", AlertMaker.image_warning);
            return;
        }
        if(!UrlUtil.isValid(getView().getAggregatorUrl().getText().trim())){
            AlertMaker.showNotification("Error", "Invalid Url For Aggregator", AlertMaker.image_warning);
            return;
        }
        if(getView().getNewswires().getChips().isEmpty()){
            AlertMaker.showNotification("Error", "You did not specify any newswire source(s)", AlertMaker.image_warning);
            return;
        }

        for(int i = 0; i < getView().getNewswires().getChips().size(); i++){
            getView().getNewswires().getChips().set(i, getView().getNewswires().getChips().get(i).trim());
        }

        for(String url : getView().getNewswires().getChips()){
            if(!UrlUtil.isValid(url)){
                getView().getNewswires().getChips().remove(url);
                AlertMaker.showNotification("Error", "Invalid Url: " + url, AlertMaker.image_warning);
                return;
            }
        }

        CreateUserPresenter.officer.setAggregatorName(getView().getAggregatorName().getText());
        CreateUserPresenter.officer.setAggregatorUrl(getView().getAggregatorUrl().getText());
        CreateUserPresenter.newswires.clear();
        for(String url : getView().getNewswires().getChips()){
            CreateUserPresenter.newswires.add(new Newswire(url));
        }

        Launcher.switchWindow(getView(), new CreateKeywordPresenter().getView(), 600, 600);
    }

    public SetNewswireView getView(){
        return view;
    }
}
