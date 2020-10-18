package com.kontrol.newsarchive.view;

import com.kontrol.newsarchive.presenter.HomePresenter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class MainView extends BorderPane{

    private HBox toolbar;
    private ImageView homeIconView;
    private ImageView profileIconView;
    private ImageView analyticsIconView;
    private ImageView quitIconView;

    private Node content;

    public MainView() {

        toolbar = new HBox();

        homeIconView = new ImageView();
        profileIconView = new ImageView();
        analyticsIconView = new ImageView();
        quitIconView = new ImageView();

        content = new HomePresenter().getView();
        makeGUI();

    }

    private void makeGUI() {

        homeIconView.setImage(new Image(MainView.class.getResource("/home.png").toExternalForm()));
        homeIconView.setFitWidth(30);
        homeIconView.setFitHeight(30);
        profileIconView.setImage(new Image(MainView.class.getResource("/profile.png").toExternalForm()));
        profileIconView.setFitWidth(30);
        profileIconView.setFitHeight(30);
        analyticsIconView.setImage(new Image(MainView.class.getResource("/analytics.png").toExternalForm()));
        analyticsIconView.setFitWidth(30);
        analyticsIconView.setFitHeight(30);
        analyticsIconView.setVisible(false);
        quitIconView.setImage(new Image(MainView.class.getResource("/power.png").toExternalForm()));
        quitIconView.setFitWidth(30);
        quitIconView.setFitHeight(30);

        Pane spacer1 = new Pane();
        Pane spacer2 = new Pane();
        Pane spacer3 = new Pane();
        Pane spacer4 = new Pane();
        Pane spacer5 = new Pane();
        toolbar.getChildren().addAll(spacer1, homeIconView, spacer2, profileIconView, spacer4, quitIconView, spacer5);
        HBox.setHgrow(spacer1, Priority.SOMETIMES);
        HBox.setHgrow(spacer2, Priority.SOMETIMES);
        HBox.setHgrow(spacer3, Priority.SOMETIMES);
        HBox.setHgrow(spacer4, Priority.SOMETIMES);
        HBox.setHgrow(spacer5, Priority.SOMETIMES);
        toolbar.setAlignment(Pos.CENTER);
        toolbar.prefWidthProperty().bind(content.layoutXProperty());
        toolbar.setStyle("-fx-background-color: rgb(62, 73, 86);" +
                "-fx-padding: 10;" +
                "-fx-background-radius: 0;");
        content.setStyle("-fx-background-color: rgb(62, 73, 86);" +
                "-fx-padding: 2;" +
                "-fx-background-radius: 0;");
        this.setTop(toolbar);
        this.setCenter(content);
        BorderPane.setMargin(content, new Insets(20, 0, 0, 0));
        this.setBackground(Background.EMPTY);

    }

    public HBox getToolbar() {
        return toolbar;
    }

    public ImageView getHomeIconView() {
        return homeIconView;
    }

    public ImageView getProfileIconView() {
        return profileIconView;
    }

    public ImageView getAnalyticsIconView() {
        return analyticsIconView;
    }

    public ImageView getQuitIconView() {
        return quitIconView;
    }

    public Node getContent() {
        return content;
    }
}
