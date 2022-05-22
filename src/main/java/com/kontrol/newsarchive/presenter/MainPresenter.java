package com.kontrol.newsarchive.presenter;

import com.kontrol.newsarchive.Launcher;
import com.kontrol.newsarchive.view.MainView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainPresenter {

    private MainView view = new MainView();
    private double dragOffsetX;
    private double dragOffsetY;

    public MainPresenter(){
        addDragListeners();
        addToolbarIconClickedListener();
    }

    private void addToolbarIconClickedListener() {
        getView().getHomeIconView().setPickOnBounds(true);

        getView().getProfileIconView().setPickOnBounds(true);
        getView().getProfileIconView().addEventHandler(MouseEvent.MOUSE_CLICKED, this::profileClicked);

        getView().getAnalyticsIconView().setPickOnBounds(true);
        getView().getAnalyticsIconView().addEventHandler(MouseEvent.MOUSE_CLICKED, this::analyticsClicked);

        getView().getQuitIconView().setPickOnBounds(true);
        getView().getQuitIconView().addEventHandler(MouseEvent.MOUSE_CLICKED, this::quitClicked);
    }

    private void addDragListeners() {
        getView().getToolbar().setOnMousePressed(this::handleMousePressed);
        getView().getToolbar().setOnMouseDragged(this::handleMouseDragged);
    }

    private void handleMousePressed(MouseEvent event){
        this.dragOffsetX = event.getScreenX() - ((Stage) this.getView().getScene().getWindow()).getX();
        this.dragOffsetY = event.getScreenY() - ((Stage) this.getView().getScene().getWindow()).getY();
    }

    private void handleMouseDragged(MouseEvent event){
        getView().getScene().getWindow().setX(event.getScreenX() - this.dragOffsetX);
        getView().getScene().getWindow().setY(event.getScreenY() - this.dragOffsetY);
    }

    private void profileClicked(MouseEvent event){
        Launcher.loadWindow("Profile", Modality.NONE, new ProfilePresenter().getView(), 600, 400);
    }

    private void analyticsClicked(MouseEvent event){
    }

    private void quitClicked(MouseEvent event){
        Launcher.onWindowCloseRequest(null);
    }

    public MainView getView() {
        return view;
    }
}
