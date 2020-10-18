package com.kontrol.newsarchive.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXChipView;
import com.jfoenix.controls.JFXRadioButton;
import com.kontrol.newsarchive.presenter.CreateUserPresenter;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class CreateKeywordView extends BorderPane {

    private JFXChipView<String> keywords;
    private JFXCheckBox launchAppCheckBox;
    private ToggleGroup intervalToggleGroup;
    private JFXRadioButton fiveAMRadioBtn;
    private JFXRadioButton eightAMRadioBtn;
    private JFXRadioButton elevenAMRadioBtn;
    private JFXRadioButton twoPMRadioBtn;
    private JFXRadioButton fourPMRadioBtn;
    private JFXButton backButton;
    private JFXButton finishButton;
    private JFXButton cancelButton;

    public CreateKeywordView(){
        keywords = new JFXChipView<>();
        launchAppCheckBox = new JFXCheckBox("Launch Application on exit");
        intervalToggleGroup = new ToggleGroup();
        fiveAMRadioBtn = new JFXRadioButton("5 AM");
        eightAMRadioBtn = new JFXRadioButton("8 AM");
        elevenAMRadioBtn = new JFXRadioButton("11 AM");
        twoPMRadioBtn = new JFXRadioButton("2 PM");
        fourPMRadioBtn = new JFXRadioButton("4 PM");
        backButton = new JFXButton("Back");
        finishButton = new JFXButton("Finish");
        cancelButton = new JFXButton("Cancel");

        designGUI();
    }

    private void designGUI(){
        VBox center = new VBox(15);
        ImageView logo = new ImageView(new Image(CreateKeywordView.class.getResource("/logo.png").toExternalForm()));
        logo.setSmooth(true);
        logo.setFitWidth(350);
        logo.setFitHeight(120);
        logo.setPreserveRatio(true);
        Label welcomeText = new Label("User: " + CreateUserPresenter.officer.getUsername());
        welcomeText.setFont(new Font(18));
        welcomeText.setTextFill(Color.WHITE);
        Text infoText = new Text("Specify Tags or Keywords that describe your establishment i.e. " +
                "company name" + "\nor acronym and interests. Note that these tags will be used to determine re-\n" +
                "levant news content worthy of retrieval. Select diligently.");
        infoText.setFont(new Font(16));
        infoText.setFill(Color.WHITE);
        keywords.setStyle("-fx-background-color: WHITE;");
        keywords.setMinWidth(350);
        StackPane keywordsPane = new StackPane();
        keywordsPane.getChildren().add(keywords);
        launchAppCheckBox.setTextFill(Color.WHITE);
        launchAppCheckBox.setSelected(true);
        HBox centerHBox = new HBox(20, keywordsPane, launchAppCheckBox);
        Label intervalLbl = new Label("Time interval for automatic extraction:");
        intervalLbl.setTextFill(Color.WHITE);
        //set toogle group for btns
        HBox choicesHBox = new HBox(5);
        fiveAMRadioBtn.setTextFill(Color.WHITE);
        fiveAMRadioBtn.setSelected(true);
        eightAMRadioBtn.setTextFill(Color.WHITE);
        elevenAMRadioBtn.setTextFill(Color.WHITE);
        twoPMRadioBtn.setTextFill(Color.WHITE);
        fourPMRadioBtn.setTextFill(Color.WHITE);
        intervalToggleGroup.getToggles().addAll(fiveAMRadioBtn, eightAMRadioBtn, elevenAMRadioBtn, twoPMRadioBtn, fourPMRadioBtn);
        choicesHBox.getChildren().addAll(fiveAMRadioBtn, eightAMRadioBtn, elevenAMRadioBtn, twoPMRadioBtn, fourPMRadioBtn);
        VBox intervalVBox = new VBox(10, intervalLbl, choicesHBox);
        center.getChildren().addAll(logo, welcomeText, infoText, centerHBox, intervalVBox);
        this.setCenter(center);

        Label stepText = new Label("Step 3 of 3");
        stepText.setTextFill(Color.WHITE);
        Pane spacer = new Pane();
        backButton.setPadding(new Insets(10));
        backButton.setPrefSize(70, 40);
        backButton.setButtonType(JFXButton.ButtonType.RAISED);
        backButton.setStyle("-fx-background-color: rgb(238, 167, 121);"
                + "-fx-border-radius: 5;"
                + "-fx-background-radius: 5");
        finishButton.setDefaultButton(true);
        finishButton.setPadding(new Insets(10));
        finishButton.setButtonType(JFXButton.ButtonType.RAISED);
        finishButton.setPrefSize(70, 40);
        finishButton.setStyle("-fx-background-color: rgb(238, 167, 121);"
                + "-fx-border-radius: 5;"
                + "-fx-background-radius: 5");
        cancelButton.setCancelButton(true);
        cancelButton.setPadding(new Insets(10));
        cancelButton.setButtonType(JFXButton.ButtonType.RAISED);
        cancelButton.setPrefSize(70, 40);
        cancelButton.setStyle("-fx-background-color: rgb(238, 167, 121);"
                + "-fx-border-radius: 5;"
                + "-fx-background-radius: 5");
        HBox bottom = new HBox(10, stepText, spacer, backButton, finishButton, cancelButton);
        HBox.setHgrow(spacer, Priority.SOMETIMES);
        this.setBottom(bottom);

        this.setStyle("-fx-background-color: rgb(62, 73, 86);" +
                "-fx-padding: 30;" +
                "-fx-background-radius: 0;");
    }

    public JFXChipView<String> getKeywords() {
        return keywords;
    }

    public JFXCheckBox getLaunchAppCheckBox() {
        return launchAppCheckBox;
    }

    public ToggleGroup getIntervalToggleGroup() {
        return intervalToggleGroup;
    }

    public JFXRadioButton getFiveAMRadioBtn() {
        return fiveAMRadioBtn;
    }

    public JFXRadioButton getEightAMRadioBtn() {
        return eightAMRadioBtn;
    }

    public JFXRadioButton getElevenAMRadioBtn() {
        return elevenAMRadioBtn;
    }

    public JFXRadioButton getTwoPMRadioBtn() {
        return twoPMRadioBtn;
    }

    public JFXRadioButton getFourPMRadioBtn() {
        return fourPMRadioBtn;
    }

    public JFXButton getBackButton() {
        return backButton;
    }

    public JFXButton getFinishButton() {
        return finishButton;
    }

    public JFXButton getCancelButton() {
        return cancelButton;
    }
}
