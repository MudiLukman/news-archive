package com.kontrol.newsarchive;

import com.jfoenix.controls.JFXButton;
import com.kontrol.newsarchive.presenter.CreateUserPresenter;
import com.kontrol.newsarchive.presenter.LoginPresenter;
import com.kontrol.newsarchive.util.DatabaseHelper;
import com.kontrol.newsarchive.view.ProfileView;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Launcher extends Application{

    private static ResultSet resultSet;
    private static ExecutorService executor;

    private static class RootAdminFetcherTask implements Runnable{
        private CountDownLatch latch;
        RootAdminFetcherTask(CountDownLatch latch){
            this.latch = latch;
        }

        @Override
        public void run() {
            resultSet = DatabaseHelper.getUserNamePasswordAdmin();
            latch.countDown();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(new LoginPresenter().getView()));
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.centerOnScreen();

        CountDownLatch latch = new CountDownLatch(1);
        executor.execute(new RootAdminFetcherTask(latch));

        latch.await();

        try {
            if(!resultSet.next()){
                primaryStage.setScene(new Scene(new CreateUserPresenter().getView()));
                primaryStage.setWidth(600);
                primaryStage.setHeight(500);
            }
        } catch (SQLException | NullPointerException e) {
            primaryStage.setScene(new Scene(new CreateUserPresenter().getView()));
            primaryStage.setWidth(600);
            primaryStage.setHeight(500);
        }

        primaryStage.show();
    }

    public static void loadWindow(String title, Modality modality, Pane view){
        Stage stage = new Stage(StageStyle.TRANSPARENT);
        stage.setTitle(title);
        stage.setScene(new Scene(view));
        stage.initModality(modality);
        stage.centerOnScreen();
        stage.showAndWait();
    }

    public static void loadWindow(String title, Modality modality, Pane view, int width, int height){
        Stage stage = new Stage(StageStyle.TRANSPARENT);
        stage.setWidth(width);
        stage.setHeight(height);
        stage.setTitle(title);
        if(view instanceof ProfileView){
            stage.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if(!stage.isFocused()){
                    stage.close();
                }
            });
        }
        stage.setScene(new Scene(view));
        stage.initModality(modality);
        stage.centerOnScreen();
        if(view instanceof ProfileView){
            stage.show();
        }
        else {
            stage.showAndWait();
        }
    }

    public static void switchWindow(Pane from, Pane to, int width, int height){
        Scene scene = new Scene(to);
        scene.setFill(Color.TRANSPARENT);
        Stage stage = (Stage) from.getScene().getWindow();
        stage.setOnCloseRequest(Launcher::onWindowCloseRequest);
        stage.setMinHeight(Screen.getPrimary().getVisualBounds().getHeight());
        stage.setMinWidth(Screen.getPrimary().getVisualBounds().getWidth() / 2);
        stage.setWidth(width);
        stage.setHeight(height);
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    public static void onWindowCloseRequest(WindowEvent event){
        HBox buttonsHBox = new HBox(10);
        buttonsHBox.setAlignment(Pos.CENTER_RIGHT);
        JFXButton ok = new JFXButton("Ok");
        ok.setPadding(new Insets(5));
        ok.setStyle("-fx-background-color: rgb(238, 167, 121);"
                + "-fx-border-radius: 5;"
                + "-fx-background-radius: 5");
        ok.setDefaultButton(true);
        ok.setButtonType(JFXButton.ButtonType.RAISED);
        JFXButton cancel = new JFXButton("Cancel");
        cancel.setPadding(new Insets(5));
        cancel.setCancelButton(true);
        cancel.setTextFill(Color.WHITE);
        cancel.setButtonType(JFXButton.ButtonType.RAISED);
        cancel.setStyle("-fx-background-color: rgb(79, 89, 105);"
                + "-fx-border-radius: 5;"
                + "-fx-background-radius: 5");
        buttonsHBox.getChildren().addAll(ok, cancel);
        Label confirmText = new Label("Are you sure you want to exit?");
        confirmText.setTextFill(Color.WHITE);
        VBox pane = new VBox(5,confirmText , buttonsHBox);
        pane.setAlignment(Pos.CENTER);
        pane.setStyle("-fx-background-color: rgb(62, 73, 86);" +
                "-fx-padding: 30;" +
                "-fx-background-radius: 5;" +
                "-fx-border-color: rgb(115, 115, 115);" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 5;");
        Stage stage = new Stage(StageStyle.TRANSPARENT);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(pane));
        stage.show();
        ok.setOnAction(event1 -> {
            System.exit(0);
        });
        cancel.setOnAction(event1 -> {
            cancel.getScene().getWindow().hide();
        });

        if(event != null) {
            event.consume();
        }

    }

    public static void main( String[] args ) {
        try {
            executor = Executors.newFixedThreadPool(2);
            executor.execute(DatabaseHelper::createAllTables);
            launch(args);
        } finally {
            executor.shutdown();
        }
    }
}
