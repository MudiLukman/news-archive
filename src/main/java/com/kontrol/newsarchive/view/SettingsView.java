package com.kontrol.newsarchive.view;

import com.jfoenix.controls.*;
import com.kontrol.newsarchive.presenter.LoginPresenter;
import com.kontrol.newsarchive.util.CustomImageView;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class SettingsView extends BorderPane {

    private TabPane settingsTabPane;

    private Tab passwordTab;
    private JFXPasswordField currentPasswordField;
    private JFXPasswordField newPasswordField;
    private JFXPasswordField confirmPasswordField;
    private JFXButton updatePasswordBtn;

    private Tab intervalTab;
    private ToggleGroup radioBtnToggleGroup;
    private JFXRadioButton fiveAMRadioBtn;
    private JFXRadioButton eightAMRadioBtn;
    private JFXRadioButton elevenAMRadioBtn;
    private JFXRadioButton twoPMRadioBtn;
    private JFXRadioButton fourPMRadioBtn;
    private JFXButton updateIntervalBtn;

    private Tab aggregatorTab;
    private JFXTextField aggregatorNameField;
    private JFXTextField aggregatorUrlField;
    private JFXButton updateAggregatorBtn;

    private Tab newswireTab;
    private JFXChipView<String> newswireChipView;
    private JFXButton editNewswireBtn;
    private JFXButton updateNewswireBtn;

    private Tab keywordTab;
    private JFXChipView<String> keywordsChipView;
    private JFXButton editKeywords;
    private JFXButton updateKeywords;

    private CustomImageView closeIcon;
    private Label usernameLbl;

    public SettingsView(){
        settingsTabPane = new TabPane();
        passwordTab = new Tab("Password");
        currentPasswordField = new JFXPasswordField();
        newPasswordField = new JFXPasswordField();
        confirmPasswordField = new JFXPasswordField();
        updatePasswordBtn = new JFXButton("Update Password");
        intervalTab = new Tab("Extraction Interval");
        radioBtnToggleGroup = new ToggleGroup();
        updateIntervalBtn = new JFXButton("Update Interval");
        fiveAMRadioBtn = new JFXRadioButton("5 AM");
        eightAMRadioBtn = new JFXRadioButton("8 AM");
        elevenAMRadioBtn = new JFXRadioButton("11 AM");
        twoPMRadioBtn = new JFXRadioButton("2 PM");
        fourPMRadioBtn = new JFXRadioButton("4 PM");
        aggregatorTab = new Tab("Aggregator");
        aggregatorNameField = new JFXTextField();
        aggregatorUrlField = new JFXTextField();
        updateAggregatorBtn = new JFXButton("Update Aggregator");
        newswireTab = new Tab("Newswire");
        newswireChipView = new JFXChipView<>();
        editNewswireBtn = new JFXButton("Edit");
        updateNewswireBtn = new JFXButton("Update");
        keywordTab = new Tab("Keyword");
        keywordsChipView = new JFXChipView<>();
        editKeywords = new JFXButton("Edit");
        updateKeywords = new JFXButton("Update");
        closeIcon = new CustomImageView("/cross.png", 20, 20);
        usernameLbl = new Label("User: " + LoginPresenter.usernameOfLoggedInUser);
        designGUI();
    }

    private void designGUI() {
        settingsTabPane.getTabs().addAll(passwordTab, intervalTab, aggregatorTab, newswireTab, keywordTab);
        settingsTabPane.getStylesheets().add(SettingsView.class.getResource("/style/styles.css").toExternalForm());
        settingsTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        closeIcon.setPickOnBounds(true);
        usernameLbl.setFont(new Font(20));
        usernameLbl.setTextFill(Color.WHITE);
        Label settingsLbl = new Label("Settings");
        settingsLbl.setFont(new Font(20));
        settingsLbl.setTextFill(Color.WHITE);

        //make top
        Pane spaceInTop = new Pane();
        VBox topVBox = new VBox(settingsLbl, usernameLbl);
        HBox top = new HBox(topVBox, spaceInTop, closeIcon);
        HBox.setHgrow(spaceInTop, Priority.SOMETIMES);
        this.setTop(top);

        this.setCenter(settingsTabPane);

        //make password tab
        currentPasswordField.setStyle("-fx-text-fill: rgb(255, 255, 255);");
        currentPasswordField.setFocusColor(Color.rgb(238, 167, 121));
        currentPasswordField.setUnFocusColor(Color.WHITE);
        newPasswordField.setStyle("-fx-text-fill: rgb(255, 255, 255);");
        newPasswordField.setFocusColor(Color.rgb(238, 167, 121));
        newPasswordField.setUnFocusColor(Color.WHITE);
        confirmPasswordField.setStyle("-fx-text-fill: rgb(255, 255, 255);");
        confirmPasswordField.setFocusColor(Color.rgb(238, 167, 121));
        confirmPasswordField.setUnFocusColor(Color.WHITE);
        BorderPane passwordPane = new BorderPane();
        passwordPane.setPadding(new Insets(10));
        VBox passwordVBox = new VBox(10);
        Label changePasswordLbl = new Label("Change user password for this account");
        changePasswordLbl.setFont(new Font(15));
        changePasswordLbl.setTextFill(Color.WHITE);
        Label currentPasswordLbl = new Label("Current  Password:");
        currentPasswordLbl.setFont(new Font(18));
        currentPasswordLbl.setTextFill(Color.WHITE);
        HBox currentPasswordHBox = new HBox(10, currentPasswordLbl, currentPasswordField);
        Label newPasswordLbl = new Label("New Password:");
        newPasswordLbl.setFont(new Font(18));
        newPasswordLbl.setTextFill(Color.WHITE);
        HBox newPasswordHBox = new HBox(10, newPasswordLbl, newPasswordField);
        Label confirmPasswordLbl = new Label("Confirm Password:");
        confirmPasswordLbl.setTextFill(Color.WHITE);
        confirmPasswordLbl.setFont(new Font(18));
        HBox confirmPasswordHBox = new HBox(10, confirmPasswordLbl, confirmPasswordField);
        passwordVBox.getChildren().addAll(changePasswordLbl, currentPasswordHBox, newPasswordHBox, confirmPasswordHBox);
        passwordVBox.setPadding(new Insets(5, 50, 70, 5));
        passwordPane.setCenter(passwordVBox);
        Pane spaceBeforeUpdatePassword = new Pane();
        updatePasswordBtn.setPadding(new Insets(10));
        updatePasswordBtn.setButtonType(JFXButton.ButtonType.RAISED);
        updatePasswordBtn.setStyle("-fx-background-color: rgb(238, 167, 121);"
                + "-fx-border-radius: 5;"
                + "-fx-background-radius: 5");
        HBox passwordBottomHBox = new HBox(spaceBeforeUpdatePassword, updatePasswordBtn);
        HBox.setHgrow(spaceBeforeUpdatePassword, Priority.SOMETIMES);
        passwordPane.setBottom(passwordBottomHBox);
        passwordTab.setContent(passwordPane);

        //make interval tab
        BorderPane intervalPane = new BorderPane();
        intervalPane.setPadding(new Insets(10));
        radioBtnToggleGroup = new ToggleGroup();
        radioBtnToggleGroup.getToggles().addAll(fiveAMRadioBtn, eightAMRadioBtn,
                elevenAMRadioBtn, twoPMRadioBtn, fourPMRadioBtn);
        updateIntervalBtn.setButtonType(JFXButton.ButtonType.RAISED);
        updateIntervalBtn.setPadding(new Insets(10));
        updateIntervalBtn.setStyle("-fx-background-color: rgb(238, 167, 121);"
                + "-fx-border-radius: 5;"
                + "-fx-background-radius: 5");
        Label changeIntervalLbl = new Label("Change Default Automatic Extraction Intervals");
        changeIntervalLbl.setFont(new Font(15));
        changeIntervalLbl.setTextFill(Color.WHITE);
        Label timeOfDayLbl = new Label("Time Of Day");
        timeOfDayLbl.setTextFill(Color.WHITE);
        timeOfDayLbl.setFont(new Font(18));
        fiveAMRadioBtn.setTextFill(Color.WHITE);
        eightAMRadioBtn.setTextFill(Color.WHITE);
        elevenAMRadioBtn.setTextFill(Color.WHITE);
        twoPMRadioBtn.setTextFill(Color.WHITE);
        fourPMRadioBtn.setTextFill(Color.WHITE);
        HBox radioBtnHBox = new HBox(5, fiveAMRadioBtn, eightAMRadioBtn,
                elevenAMRadioBtn, twoPMRadioBtn, fourPMRadioBtn);
        VBox intervalVBox = new VBox(10, changeIntervalLbl, timeOfDayLbl, radioBtnHBox);
        intervalVBox.setPadding(new Insets(5, 12, 12, 5));
        intervalPane.setCenter(intervalVBox);
        Pane spaceBeforeUpdateIntervalBtn = new Pane();
        HBox intervalBottom = new HBox(spaceBeforeUpdateIntervalBtn, updateIntervalBtn);
        HBox.setHgrow(spaceBeforeUpdateIntervalBtn, Priority.SOMETIMES);
        intervalPane.setBottom(intervalBottom);
        intervalTab.setContent(intervalPane);

        //make aggregator tab
        BorderPane aggregatorPane = new BorderPane();
        aggregatorPane.setPadding(new Insets(10));
        aggregatorNameField.setStyle("-fx-text-fill: rgb(255, 255, 255);");
        aggregatorNameField.setFocusColor(Color.rgb(238, 167, 121));
        aggregatorNameField.setUnFocusColor(Color.WHITE);
        aggregatorUrlField.setStyle("-fx-text-fill: rgb(255, 255, 255);");
        aggregatorUrlField.setFocusColor(Color.rgb(238, 167, 121));
        aggregatorUrlField.setUnFocusColor(Color.WHITE);
        updateAggregatorBtn.setButtonType(JFXButton.ButtonType.RAISED);
        updateAggregatorBtn.setPadding(new Insets(10));
        updateAggregatorBtn.setStyle("-fx-background-color: rgb(238, 167, 121);"
                + "-fx-border-radius: 5;"
                + "-fx-background-radius: 5");
        Label updateAggregatorLbl = new Label("Update Default Aggregator to fetch daily news from");
        updateAggregatorLbl.setFont(new Font(15));
        updateAggregatorLbl.setTextFill(Color.WHITE);
        updateAggregatorLbl.setPadding(new Insets(10));
        Label aggregatorNameLbl = new Label("Name:");
        aggregatorNameLbl.setTextFill(Color.WHITE);
        aggregatorNameLbl.setFont(new Font(18));
        Label aggregatorUrlLbl = new Label("Url:");
        aggregatorUrlLbl.setFont(new Font(18));
        aggregatorUrlLbl.setTextFill(Color.WHITE);
        aggregatorPane.setTop(updateAggregatorLbl);
        VBox aggregatorVBox = new VBox(15, new HBox(5, aggregatorNameLbl, aggregatorNameField),
                new HBox(5, aggregatorUrlLbl, aggregatorUrlField));
        aggregatorVBox.setPadding(new Insets(5, 15, 20, 5));
        HBox aggregatorBottom = new HBox();
        Pane spaceBeforeUpdateAggregatorBtn = new Pane();
        aggregatorBottom.getChildren().addAll(spaceBeforeUpdateAggregatorBtn, updateAggregatorBtn);
        HBox.setHgrow(spaceBeforeUpdateAggregatorBtn, Priority.SOMETIMES);
        aggregatorPane.setBottom(aggregatorBottom);
        aggregatorPane.setCenter(aggregatorVBox);
        aggregatorTab.setContent(aggregatorPane);

        //make newswire pane
        BorderPane newswirePane = new BorderPane();
        newswirePane.setPadding(new Insets(10));
        Label updateNewswireLbl = new Label("Update Newswire Sources");
        updateNewswireLbl.setTextFill(Color.WHITE);
        updateNewswireLbl.setFont(new Font(20));
        newswireChipView.setDisable(true);
        newswireChipView.setStyle("-fx-opacity: 1;");
        newswireChipView.getSuggestions().addAll("https://punchng.com/", "https://www.vanguardngr.com/",
                "https://www.thisdaylive.com/", "https://guardian.ng/", "https://dailytimes.ng/",
                "https://leadership.ng/", "https://www.dailytrust.com.ng/", "https://thenationonlineng.net/",
                "https://tribuneonlineng.com/", "https://businessday.ng/", "https://www.independent.ng/",
                "https://www.newtelegraphng.com/", "https://peoplesdailyng.com/", "https://www.blueprint.ng/",
                "https://www.premiumtimesng.com/", "http://saharareporters.com/", "http://brtnews.ng/",
                "https://theeagleonline.com.ng/", "https://investadvocate.com.ng/", "https://investdata.com.ng/");
        newswireChipView.setStyle("-fx-background-color: WHITE;");
        newswireChipView.setMinHeight(150);
        VBox newswireVBox = new VBox(5, updateNewswireLbl, newswireChipView);
        newswirePane.setCenter(newswireVBox);
        Pane spaceAboveNewswireButtons = new Pane();
        editNewswireBtn.setButtonType(JFXButton.ButtonType.RAISED);
        editNewswireBtn.setPadding(new Insets(10));
        editNewswireBtn.setStyle("-fx-background-color: rgb(238, 167, 121);"
                + "-fx-border-radius: 5;"
                + "-fx-background-radius: 5");
        updateNewswireBtn.setButtonType(JFXButton.ButtonType.RAISED);
        updateNewswireBtn.setPadding(new Insets(10));
        updateNewswireBtn.setStyle("-fx-background-color: rgb(238, 167, 121);"
                + "-fx-border-radius: 5;"
                + "-fx-background-radius: 5");
        VBox newswireRightVBox = new VBox(10,spaceAboveNewswireButtons, editNewswireBtn, updateNewswireBtn);
        newswireRightVBox.setPadding(new Insets(0, 0, 0, 10));
        VBox.setVgrow(spaceAboveNewswireButtons, Priority.SOMETIMES);
        newswirePane.setRight(newswireRightVBox);
        newswireTab.setContent(newswirePane);

        //make keyword pane
        BorderPane keywordsPane = new BorderPane();
        keywordsPane.setPadding(new Insets(10));
        Label editKeywordLbl = new Label("Add/Remove tags or keywords that describe your establishments' interests");
        editKeywordLbl.setFont(new Font(16));
        editKeywordLbl.setTextFill(Color.WHITE);
        keywordsChipView.setDisable(true);
        keywordsChipView.setStyle("-fx-opacity: 1;");
        keywordsChipView.setStyle("-fx-background-color: WHITE;");
        keywordsChipView.setMinHeight(150);
        VBox keywordsVBox = new VBox(5, editKeywordLbl, keywordsChipView);
        keywordsPane.setCenter(keywordsVBox);
        Pane spaceAboveKeywordsButtons = new Pane();
        VBox keywordsRightVBox = new VBox(10, spaceAboveKeywordsButtons, editKeywords, updateKeywords);
        keywordsRightVBox.setPadding(new Insets(0, 0, 0, 10));
        VBox.setVgrow(spaceAboveKeywordsButtons, Priority.SOMETIMES);
        keywordsPane.setRight(keywordsRightVBox);
        editKeywords.setButtonType(JFXButton.ButtonType.RAISED);
        editKeywords.setPadding(new Insets(10));
        editKeywords.setStyle("-fx-background-color: rgb(238, 167, 121);"
                + "-fx-border-radius: 5;"
                + "-fx-background-radius: 5");
        updateKeywords.setButtonType(JFXButton.ButtonType.RAISED);
        updateKeywords.setPadding(new Insets(10));
        updateKeywords.setStyle("-fx-background-color: rgb(238, 167, 121);"
                + "-fx-border-radius: 5;"
                + "-fx-background-radius: 5");
        keywordTab.setContent(keywordsPane);

        this.setStyle("-fx-background-color: rgb(62, 73, 86);" +
                "-fx-border-color: rgb(115, 115, 115);" +
                "-fx-padding: 10;" +
                "-fx-border-width: 2;");
    }

    public TabPane getSettingsTabPane() {
        return settingsTabPane;
    }

    public Tab getPasswordTab() {
        return passwordTab;
    }

    public JFXPasswordField getCurrentPasswordField() {
        return currentPasswordField;
    }

    public JFXPasswordField getNewPasswordField() {
        return newPasswordField;
    }

    public JFXPasswordField getConfirmPasswordField() {
        return confirmPasswordField;
    }

    public JFXButton getUpdatePasswordBtn() {
        return updatePasswordBtn;
    }

    public Tab getIntervalTab() {
        return intervalTab;
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

    public JFXButton getUpdateIntervalBtn() {
        return updateIntervalBtn;
    }

    public Tab getAggregatorTab() {
        return aggregatorTab;
    }

    public JFXTextField getAggregatorNameField() {
        return aggregatorNameField;
    }

    public JFXTextField getAggregatorUrlField() {
        return aggregatorUrlField;
    }

    public JFXButton getUpdateAggregatorBtn() {
        return updateAggregatorBtn;
    }

    public Tab getNewswireTab() {
        return newswireTab;
    }

    public JFXChipView<String> getNewswireChipView() {
        return newswireChipView;
    }

    public JFXButton getEditNewswireBtn() {
        return editNewswireBtn;
    }

    public JFXButton getUpdateNewswireBtn() {
        return updateNewswireBtn;
    }

    public Tab getKeywordTab() {
        return keywordTab;
    }

    public JFXChipView getKeywordsChipView() {
        return keywordsChipView;
    }

    public JFXButton getEditKeywords() {
        return editKeywords;
    }

    public JFXButton getUpdateKeywords() {
        return updateKeywords;
    }

    public CustomImageView getCloseIcon() {
        return closeIcon;
    }

    public Label getUsernameLbl() {
        return usernameLbl;
    }

    public ToggleGroup getRadioBtnToggleGroup() {
        return radioBtnToggleGroup;
    }
}
