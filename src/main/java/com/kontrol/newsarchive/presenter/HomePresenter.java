package com.kontrol.newsarchive.presenter;

import com.kontrol.newsarchive.Launcher;
import com.kontrol.newsarchive.util.CustomImageView;
import com.kontrol.newsarchive.util.DatabaseHelper;
import com.kontrol.newsarchive.view.HomeView;
import com.kontrol.newsarchive.view.TodaysNewsView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HomePresenter {

    private final Logger LOGGER = Logger.getLogger(HomePresenter.class.getName());
    private final HomeView view = new HomeView();
    public static List<String> newswires = new ArrayList<>();
    public static List<String> keywords = new ArrayList<>();
    private final ContextMenu logoutCtxMenu = new ContextMenu();
    private String nextExtractionTime = "";

    public HomePresenter(){
        initNextExtractionTime();
        initListViews();
        fetchTodaysNews();
        addEventHandlers();
        initAutomaticExtractionEngine();
    }

    private void initAutomaticExtractionEngine() {
        int hour = 0;
        switch (SettingsPresenter.getCurrentExtractionInterval()){
            case "5":
                hour = 5;
                break;
            case "8":
                hour = 8;
                break;
            case "11":
                hour = 11;
                break;
            case "14":
                hour = 14;
                break;
            case "16":
                hour = 16;
                break;
        }

        Timer timer = new Timer();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date time = calendar.getTime();

        timer.schedule(new TimerTask() {
                           @Override
                           public void run() {
                               Platform.runLater(()-> {
                                   Launcher.loadWindow("Extract News", Modality.APPLICATION_MODAL,
                                           new ExtractNewsPresenter().getView(), 850, 600);
                               });
                           }
                       },
                time);
    }

    private void initNextExtractionTime() {
        String sql = "";
        if(CreateUserPresenter.officer == null){
            sql = "SELECT * FROM deskofficer WHERE username='" + LoginPresenter.usernameOfLoggedInUser + "'";
        }
        else {
            sql = "SELECT * FROM deskofficer WHERE username='" + CreateUserPresenter.officer.getUsername() + "'" ;
        }
        ResultSet resultSet = DatabaseHelper.executeQuery(sql);
        try {
            while(resultSet.next()){
                nextExtractionTime = resultSet.getString("intervals");
            }
            switch(nextExtractionTime){
                case "5":
                    nextExtractionTime = "5 AM";
                    break;
                case "8":
                    nextExtractionTime = "8 AM";
                    break;
                case "11":
                    nextExtractionTime = "11 AM";
                    break;
                case "14":
                    nextExtractionTime = "2 PM";
                    break;
                case "16":
                    nextExtractionTime = "4 PM";
                break;

            }
        }catch (SQLException ex){
            LOGGER.log(Level.SEVERE, ex.getMessage());
        }
    }

    private void fetchTodaysNews(){
        ((BorderPane) getView().getCenter()).setCenter(new TodaysNewsPresenter().getView());
    }

    private void initListViews(){
        String newswireSql = "SELECT * FROM newswire";
        ResultSet newswireResultSet = DatabaseHelper.executeQuery(newswireSql);
        try {
            while(newswireResultSet.next()){
                String url = newswireResultSet.getString("url");
                newswires.add(url);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
        }

        String keywordSql = "SELECT * FROM keyword";
        ResultSet keywordResultSet = DatabaseHelper.executeQuery(keywordSql);
        try {
            while (keywordResultSet.next()){
                String keyword = keywordResultSet.getString("value");
                keywords.add(keyword);
            }
        }catch (SQLException ex){
            LOGGER.log(Level.SEVERE, ex.getMessage());
        }

        for(String s : newswires){
            getView().getNewswireListView().getItems().add(
                    new HBox(5, new CustomImageView("/web.png", 25, 25), new Label(s)));
        }

        for(String s : keywords){
            getView().getKeywordListView().getItems().add(
                    new HBox(5, new CustomImageView("/tag.png", 25, 25), new Label(s)));
        }
    }

    private void addEventHandlers() {
        getView().getNewsTodayHBox().setOnMouseClicked(event -> {
            if (!(((BorderPane) getView().getCenter()).getCenter() instanceof TodaysNewsView)) {
                ((BorderPane) getView().getCenter()).setCenter(new TodaysNewsPresenter().getView());
            }
        });
        getView().getExtractNewsHBox().setOnMouseClicked(this::handleExtractNewsHBoxClicked);
        getView().getArchiveHBox().setOnMouseClicked(this::handleArchiveClicked);
        getView().getSearchBar().setOnAction(this::handleSearchBarAction);
        getView().getAlertImageView().setOnMouseEntered(this::handleAlertImageViewEntered);
        getView().getLogoutButton().setOnMouseClicked(this::handleLogoutButtonClicked);
        getView().getLogoutItem().setOnAction(this::handleLogoutItemClicked);
        getView().getSettingsHBox().setOnMouseClicked(this::handleSettingsClicked);
        getView().getNewswireListView().setOnMouseClicked(this::handleNewswireListViewClick);
        getView().getKeywordListView().setOnMouseClicked(this::handleKeywordListViewClick);
    }

    private void handleKeywordListViewClick(MouseEvent event){
        HBox clickedKeyword = getView().getKeywordListView().getSelectionModel().getSelectedItem();
        for(Node node : clickedKeyword.getChildren()){
            if(node instanceof Label){
                String keyword = ((Label) node).getText();
                ((BorderPane) getView().getCenter()).setCenter(
                        new SearchResultPresenter(keyword).getView());
            }
        }
    }

    private void handleNewswireListViewClick(MouseEvent event){
        HBox clickedNewswireSource = getView().getNewswireListView().getSelectionModel().getSelectedItem();
        for(Node node : clickedNewswireSource.getChildren()){
            if(node instanceof Label){
                String newswireUrl = ((Label) node).getText();
                ((BorderPane) getView().getCenter()).setCenter(
                        new SearchResultPresenter(newswireUrl, "owner").getView());
            }
        }
    }

    private void handleArchiveClicked(MouseEvent event){
        ((BorderPane) getView().getCenter()).setCenter(
                new SearchResultPresenter().getView());
    }

    private void handleExtractNewsHBoxClicked(MouseEvent event){
        Launcher.loadWindow("Extract News", Modality.APPLICATION_MODAL,
                new ExtractNewsPresenter().getView(), 850, 600);
    }

    private void handleLogoutItemClicked(ActionEvent event){
        LoginPresenter.usernameOfLoggedInUser = null;
        Launcher.switchWindow(getView(), new LoginPresenter().getView(), 360, 327);
    }

    private void handleLogoutButtonClicked(MouseEvent event){
        logoutCtxMenu.getItems().clear();
        logoutCtxMenu.getItems().add(getView().getLogoutItem());
        logoutCtxMenu.show(getView().getScene().getWindow(), event.getScreenX(), event.getScreenY());
    }

    private void handleAlertImageViewEntered(MouseEvent event){
        Tooltip tooltip = new Tooltip();
        tooltip.setText("Next Extraction: " + nextExtractionTime);
        if(tooltip.isShowing()){
            return;
        }
        new Thread(()-> {
            Platform.runLater(()-> {
                tooltip.show(getView().getScene().getWindow(), event.getScreenX(), event.getScreenY());
            });
            try {
                Thread.sleep(2000);
                Platform.runLater(tooltip::hide);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void handleSearchBarAction(ActionEvent event){
        if(getView().getSearchBar().getText().trim().equals("")){
            return;
        }

        String searchTerm = getView().getSearchBar().getText();
        ((BorderPane) getView().getCenter()).setCenter(
                new SearchResultPresenter(searchTerm).getView());
        getView().getSearchBar().selectAll();
    }

    private void handleSettingsClicked(MouseEvent event){
        Launcher.loadWindow("Settings", Modality.APPLICATION_MODAL, new SettingsPresenter().getView());
    }

    public HomeView getView(){
        return view;
    }
}
