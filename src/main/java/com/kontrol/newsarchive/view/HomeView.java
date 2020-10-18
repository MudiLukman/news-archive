package com.kontrol.newsarchive.view;

import com.jfoenix.controls.JFXListView;
import com.kontrol.newsarchive.presenter.CreateUserPresenter;
import com.kontrol.newsarchive.presenter.LoginPresenter;
import com.kontrol.newsarchive.util.CustomImageView;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.controlsfx.control.textfield.CustomTextField;

import java.util.ArrayList;
import java.util.List;

public class HomeView extends BorderPane {

    private CustomTextField searchBar;
    private ImageView alertImageView;
    private ImageView userIconImageView;
    private Label userNameLabel;
    private ImageView logoutButton;
    private MenuItem logoutItem;

    private Pane leftSpraySheet;
    private JFXListView<HBox> quickLinksListView;
    private HBox newsTodayHBox;
    private HBox extractNewsHBox;
    private HBox archiveHBox;
    private JFXListView<HBox> newswireListView;
    private JFXListView<HBox> keywordListView;
    private HBox settingsHBox;
    private BorderPane centerPane;

    public HomeView(){
        searchBar = new CustomTextField();
        alertImageView = new CustomImageView("/alert.png", 25, 25);
        userIconImageView = new CustomImageView("/user.png", 25, 25);
        userNameLabel = new Label((CreateUserPresenter.officer == null)
                ? LoginPresenter.usernameOfLoggedInUser : CreateUserPresenter.officer.getUsername());
        logoutButton = new CustomImageView("/down.png", 25, 25);
        logoutItem = new MenuItem("Logout");
        leftSpraySheet = new Pane();
        quickLinksListView = new JFXListView<>();
        Label newsLabel = new Label("News Today");
        Label extractNewsLabel = new Label("Extract News");
        extractNewsLabel.setFont(new Font("Bauhaus 93", 18));
        newsLabel.setFont(new Font("Bauhaus 93", 18));
        Label archiveLbl = new Label("Archive");
        archiveLbl.setFont(new Font("Bauhaus 93", 18));
        newsTodayHBox = new HBox(25, new CustomImageView("/globe.png", 20, 20), newsLabel);
        extractNewsHBox = new HBox(25, new CustomImageView("/download.png", 20, 20), extractNewsLabel);
        archiveHBox = new HBox(25, new CustomImageView("/archive.png", 20, 20), archiveLbl);
        newswireListView = new JFXListView<>();
        keywordListView = new JFXListView<>();
        settingsHBox = new HBox(5, new CustomImageView("/settings.png", 25, 25),
                new Label("Settings"));
        centerPane = new BorderPane();

        designGUI();
    }

    private void designGUI() {
        //make top
        //make toolbar
        searchBar.setLeft(new CustomImageView("/search.png", 18, 18));
        searchBar.setPromptText("Search");
        searchBar.setPrefWidth(300);
        HBox toolBarHBox = new HBox(10);
        toolBarHBox.setPadding(new Insets(40, 40, 20, 50));
        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.SOMETIMES);
        alertImageView.setPickOnBounds(true);
        logoutButton.setPickOnBounds(true);
        userNameLabel.setFont(new Font(16));
        userNameLabel.setTextFill(Color.WHITE);
        toolBarHBox.getChildren().addAll(searchBar, spacer, alertImageView,
                userIconImageView, userNameLabel, logoutButton);
        toolBarHBox.setStyle("-fx-background-color: rgb(62, 73, 86);");
        centerPane.setTop(toolBarHBox);

        //make left
        VBox leftMainVBox = new VBox(3);
        ImageView logo = new ImageView(new Image(HomeView.class.getResource("/logo.png").toExternalForm()));
        logo.setFitWidth(230);
        logo.setFitHeight(90);
        logo.setSmooth(true);
        HBox logoHBox = new HBox(logo);
        logoHBox.setStyle("-fx-background-color: rgb(62, 73, 86);");
        leftMainVBox.getChildren().add(logoHBox);
        quickLinksListView.getItems().add(newsTodayHBox);
        quickLinksListView.getItems().add(extractNewsHBox);
        quickLinksListView.getItems().add(archiveHBox);
        leftMainVBox.getChildren().add(quickLinksListView);
        Separator separator = new Separator();
        leftMainVBox.getChildren().add(separator);
        VBox newswireVBox = new VBox(8);
        newswireVBox.setPrefHeight(210);
        newswireListView.setPrefHeight(newswireVBox.getPrefHeight());
        newswireListView.setPlaceholder(new Label("No newswire sources has been set"));
        newswireVBox.getChildren().addAll(new Label("  Newswire Sources"), newswireListView);
        Separator separator1 = new Separator();
        leftMainVBox.getChildren().addAll(newswireVBox, separator1);
        VBox keywordVBox = new VBox(8);
        keywordVBox.setPrefHeight(160);
        keywordListView.setPrefHeight(keywordVBox.getPrefHeight());
        keywordListView.setPlaceholder(new Label("No keywords has been set"));
        keywordVBox.getChildren().addAll(new Label("  Keywords"), keywordListView);
        Separator separator2 = new Separator();
        leftMainVBox.getChildren().addAll(keywordVBox, separator2);
        StackPane settingsPane = new StackPane();
        ListView<HBox> settingsListView = new ListView<>();
        settingsListView.setPrefSize(40, settingsPane.getPrefHeight());
        settingsListView.getItems().add(settingsHBox);
        settingsPane.getChildren().add(settingsListView);
        leftMainVBox.getChildren().add(settingsPane);
        leftSpraySheet.setPrefWidth(230);
        leftSpraySheet.getChildren().add(leftMainVBox);
        leftSpraySheet.setStyle("-fx-background-color: white;");
        List<Separator> separators = new ArrayList<>();
        separators.add(separator);
        separators.add(separator1);
        separators.add(separator2);
        for(Separator s : separators){
            s.setStyle("-fx-border-style: solid;" +
                    "-fx-border-width: 0 0 2 0;" +
                    "-fx-border-color: rgb(62, 73, 86);");
        }
        this.setLeft(leftSpraySheet);
        centerPane.setPadding(new Insets(5, 5, 5, 7));
        this.setCenter(centerPane);
    }

    public CustomTextField getSearchBar() {
        return searchBar;
    }

    public ImageView getAlertImageView() {
        return alertImageView;
    }

    public ImageView getUserIconImageView() {
        return userIconImageView;
    }

    public Label getUserNameLabel() {
        return userNameLabel;
    }

    public ImageView getLogoutButton() {
        return logoutButton;
    }

    public MenuItem getLogoutItem() {
        return logoutItem;
    }

    public Pane getLeftSpraySheet() {
        return leftSpraySheet;
    }

    public JFXListView<HBox> getQuickLinksListView() {
        return quickLinksListView;
    }

    public HBox getNewsTodayHBox() {
        return newsTodayHBox;
    }

    public HBox getExtractNewsHBox() {
        return extractNewsHBox;
    }

    public HBox getArchiveHBox() {
        return archiveHBox;
    }

    public JFXListView<HBox> getNewswireListView() {
        return newswireListView;
    }

    public JFXListView<HBox> getKeywordListView() {
        return keywordListView;
    }

    public HBox getSettingsHBox() {
        return settingsHBox;
    }

    public BorderPane getCenterPane() {
        return centerPane;
    }
}
