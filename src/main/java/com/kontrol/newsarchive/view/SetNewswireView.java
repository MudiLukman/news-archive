package com.kontrol.newsarchive.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXChipView;
import com.jfoenix.controls.JFXTextField;
import com.kontrol.newsarchive.presenter.CreateUserPresenter;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class SetNewswireView extends BorderPane {

    private ImageView aggregatorHelp;
    private JFXTextField aggregatorName;
    private JFXTextField aggregatorUrl;
    private ImageView newswireHelp;
    private JFXChipView<String> newswires;
    private JFXButton backButton;
    private JFXButton nextButton;
    private JFXButton cancelButton;

    public SetNewswireView(){
        aggregatorHelp = new ImageView(new Image(SetNewswireView.class.getResource("/informa.png").toExternalForm()));
        aggregatorName = new JFXTextField();
        aggregatorName.setStyle("-fx-text-fill: rgb(255, 255, 255);");
        aggregatorUrl = new JFXTextField();
        aggregatorUrl.setStyle("-fx-text-fill: rgb(255, 255, 255);");
        newswireHelp = new ImageView(SetNewswireView.class.getResource("/informa.png").toExternalForm());

        newswires = new JFXChipView<>();

        backButton = new JFXButton("Back");
        nextButton = new JFXButton("Next");
        cancelButton = new JFXButton("cancel");

        designGUI();

    }

    private void designGUI() {

        VBox center = new VBox(20);
        ImageView logo = new ImageView(new Image(SetNewswireView.class.getResource("/logo.png").toExternalForm()));
        logo.setSmooth(true);
        logo.setFitHeight(120);
        logo.setFitWidth(350);
        Label welcomeText = new Label("User: " + CreateUserPresenter.officer.getUsername() + "!");
        welcomeText.setFont(new Font(16));
        welcomeText.setTextFill(Color.WHITE);
        VBox aggregatorVBox = new VBox(5);
        Label aggregatorHeader = new Label("Default Aggregator ");
        aggregatorHeader.setFont(new Font(16));
        aggregatorHeader.setTextFill(Color.WHITE);
        aggregatorHelp.setFitHeight(20);
        aggregatorHelp.setFitWidth(20);
        aggregatorHelp.setPickOnBounds(true);
        aggregatorVBox.getChildren().add(new HBox(10, aggregatorHeader, aggregatorHelp));
        Label aggName = new Label("Name:");
        aggName.setTextFill(Color.WHITE);
        aggregatorName.setFocusColor(Color.rgb(238, 167, 121));
        aggregatorName.setUnFocusColor(Color.WHITE);
        aggregatorVBox.getChildren().add(new HBox(10, aggName, aggregatorName));
        Label aggUrl = new Label("Url:  ");
        aggUrl.setTextFill(Color.WHITE);
        aggregatorUrl.setFocusColor(Color.rgb(238, 167, 121));
        aggregatorUrl.setUnFocusColor(Color.WHITE);
        aggregatorVBox.getChildren().add(new HBox(20, aggUrl, aggregatorUrl));
        aggregatorVBox.setStyle("-fx-border-radius: 5;" +
                "-fx-border-color: rgb(115, 115, 115);" +
                "-fx-border-width: 2;" +
                "-fx-padding: 20;");
        Label newswireHeader = new Label("Default Newswires");
        newswireHeader.setFont(new Font(16));
        newswireHeader.setTextFill(Color.WHITE);

        newswires.getSuggestions().addAll("https://punchng.com/", "https://www.vanguardngr.com/",
                "https://www.thisdaylive.com/", "https://guardian.ng/", "https://dailytimes.ng/",
                "https://leadership.ng/", "https://www.dailytrust.com.ng/", "https://thenationonlineng.net/",
                "https://tribuneonlineng.com/", "https://businessday.ng/", "https://www.independent.ng/",
                "https://www.newtelegraphng.com/", "https://peoplesdailyng.com/", "https://www.blueprint.ng/",
                "https://www.premiumtimesng.com/", "http://saharareporters.com/", "http://brtnews.ng/",
                "https://theeagleonline.com.ng/", "https://investadvocate.com.ng/", "https://investdata.com.ng/");

        newswires.setStyle("-fx-background-color: WHITE;");
        newswires.setMinHeight(150);
        StackPane newswiresPane = new StackPane();
        newswiresPane.getChildren().add(newswires);
        newswireHelp.setFitWidth(20);
        newswireHelp.setFitHeight(20);
        newswireHelp.setPickOnBounds(true);
        center.getChildren().addAll(logo, welcomeText, aggregatorVBox, new VBox(
                new HBox(10, newswireHeader, newswireHelp), newswiresPane));
        this.setCenter(center);

        HBox bottom = new HBox(30);
        Pane spacer = new Pane();
        Label stepLbl = new Label("Step 2 of 3");
        stepLbl.setTextFill(Color.WHITE);
        HBox.setHgrow(spacer, Priority.SOMETIMES);
        backButton.setPadding(new Insets(10));
        backButton.setPrefSize(70, 40);
        backButton.setButtonType(JFXButton.ButtonType.RAISED);
        backButton.setStyle("-fx-background-color: rgb(238, 167, 121);"
                + "-fx-border-radius: 5;"
                + "-fx-background-radius: 5");
        nextButton.setPadding(new Insets(10));
        nextButton.setPrefSize(70, 40);
        nextButton.setButtonType(JFXButton.ButtonType.RAISED);
        nextButton.setDefaultButton(true);
        nextButton.setStyle("-fx-background-color: rgb(238, 167, 121);"
                + "-fx-border-radius: 5;"
                + "-fx-background-radius: 5");
        cancelButton.setPadding(new Insets(10));
        cancelButton.setPrefSize(70, 40);
        cancelButton.setButtonType(JFXButton.ButtonType.RAISED);
        cancelButton.setCancelButton(true);
        cancelButton.setStyle("-fx-background-color: rgb(238, 167, 121);"
                + "-fx-border-radius: 5;"
                + "-fx-background-radius: 5");
        bottom.getChildren().addAll(stepLbl, spacer, backButton, nextButton, cancelButton);
        this.setBottom(bottom);

        this.setStyle("-fx-background-color: rgb(62, 73, 86);" +
                "-fx-padding: 25;" +
                "-fx-background-radius: 0;");

    }

    public ImageView getAggregatorHelp() {
        return aggregatorHelp;
    }

    public JFXTextField getAggregatorName() {
        return aggregatorName;
    }

    public JFXTextField getAggregatorUrl() {
        return aggregatorUrl;
    }

    public ImageView getNewswireHelp() {
        return newswireHelp;
    }

    public JFXChipView<String> getNewswires() {
        return newswires;
    }

    public JFXButton getBackButton() {
        return backButton;
    }

    public JFXButton getNextButton() {
        return nextButton;
    }

    public JFXButton getCancelButton() {
        return cancelButton;
    }
}
