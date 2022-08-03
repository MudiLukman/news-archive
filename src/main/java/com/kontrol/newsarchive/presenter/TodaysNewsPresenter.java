package com.kontrol.newsarchive.presenter;

import com.jfoenix.effects.JFXDepthManager;
import com.kontrol.newsarchive.Launcher;
import com.kontrol.newsarchive.model.NewsContent;
import com.kontrol.newsarchive.util.AlertMaker;
import com.kontrol.newsarchive.util.CustomImageView;
import com.kontrol.newsarchive.util.DatabaseHelper;
import com.kontrol.newsarchive.view.TodaysNewsView;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class TodaysNewsPresenter {

    private final Logger LOGGER = Logger.getLogger(TodaysNewsPresenter.class.getName());
    private final TodaysNewsView view = new TodaysNewsView();
    private String aggregatorUrl = "";
    private List<String> unTraversedLinks = new ArrayList<>();
    private final List<String> relevantLinks = new ArrayList<>();
    private final List<NewsContent> listOfTodaysNews = new ArrayList<>();
    private static final int DEFAULT_TIMEOUT = 30 * 1000;

    public TodaysNewsPresenter(){
        setAggregatorHomePage();
        final Executor executor = Executors.newCachedThreadPool();
        CompletableFuture<Void> newsHighlightsFuture = CompletableFuture.runAsync(this::fetchNews, executor);
        newsHighlightsFuture.whenComplete((result, ex) -> {
            if (ex != null) {
                LOGGER.log(Level.SEVERE, "Error while fetching daily highlights {}", ex.getMessage());
            }
        });
    }

    private void fetchNews() {
        try {
            Document doc = Jsoup.connect(aggregatorUrl).timeout(DEFAULT_TIMEOUT).get();
            unTraversedLinks.add(aggregatorUrl);
            Set<String> subLinks = getSubLinks(doc);
            unTraversedLinks.addAll(subLinks);
            fetchNextSetOfLinks();
        } catch (IllegalArgumentException | IOException ex){
            Platform.runLater(()-> {
                AlertMaker.showNotification("Error", "Could not connect to " + aggregatorUrl,
                        AlertMaker.image_cross);
                getView().getProgressIndicator().setVisible(false);
            });
            LOGGER.log(Level.SEVERE, ex.getMessage());
        }
    }

    private void fetchNextSetOfLinks() {
        if(unTraversedLinks.size() > 1_000){
            unTraversedLinks = unTraversedLinks.subList(0, 1_000);
        }
        unTraversedLinks.forEach(link -> {
            try {
                Document nextLink = Jsoup.connect(link).timeout(DEFAULT_TIMEOUT).get();
                Set<String> subLinks = getSubLinks(nextLink);
                unTraversedLinks.addAll(subLinks);
                LOGGER.info("Connected to inner-link:" + link);
            }
            catch (IllegalArgumentException | IOException ex) {
                LOGGER.log(Level.SEVERE, String.format("Could not connect to inner-link: %s error: %s", link, ex.getMessage()));
            }
        });

        relevantLinks.addAll(unTraversedLinks);
        unTraversedLinks.clear();
        relevantLinks.removeIf(link -> link.contains(aggregatorUrl));
        checkPageContentsRelevance();

        Platform.runLater(()-> getView().getProgressIndicator().setVisible(false));

        if(listOfTodaysNews.isEmpty()){
            handleNoDailyNews();
        }
    }

    private void checkPageContentsRelevance() {
        relevantLinks.forEach(newsUrl -> {
            try {
                Document newsContent = Jsoup.connect(newsUrl).timeout(DEFAULT_TIMEOUT).get();
                relevantLinks.remove(newsUrl);
                String title = newsContent.title();
                DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                String formattedDate = dateFormat.format(new Date());

                NewsContent relevantNews = new NewsContent(newsUrl, title, formattedDate);
                if(!listOfTodaysNews.contains(relevantNews)){
                    listOfTodaysNews.add(relevantNews);
                    createNodeAndDisplayOnScreen(relevantNews);
                }
            } catch (IllegalArgumentException | IOException ex) {
                LOGGER.log(Level.SEVERE, String.format("Connection timed-out: %s error: %s", newsUrl, ex.getMessage()));
            }
        });
    }

    private void handleNoDailyNews() {
        Label noNewsLabel = new Label("No news content for today");
        noNewsLabel.setFont(new Font(24));
        noNewsLabel.setStyle("-fx-font-weight: bold;");
        Platform.runLater(()->{
            getView().getStackPane().getChildren().clear();
            getView().getStackPane().getChildren().add(noNewsLabel);
        });
    }

    private Set<String> getSubLinks(Document doc){
        Set<String> setOfLinks ;
        try {
            Elements links = doc.select("a[href]");
            setOfLinks = links.stream()
                    .map(link -> link.attr("abs:href"))
                    .filter(link -> !link.contains("#"))
                    .collect(Collectors.toSet());
        }catch (NullPointerException e){
            return new HashSet<>();
        }
        return setOfLinks;
    }

    private void createNodeAndDisplayOnScreen(NewsContent newsContent) {
        StackPane pane = new StackPane();
        pane.setPrefSize(200, 100);
        pane.setStyle("-fx-background-color: rgb(255, 255, 255);" +
                "-fx-background-radius: 5;" +
                "-fx-border-radius: 5;");
        VBox layoutVBox = new VBox(5);
        pane.getChildren().add(layoutVBox);
        JFXDepthManager.setDepth(pane, 1);
        CustomImageView webLogo = new CustomImageView("/globe.png", 20, 20);
        webLogo.setSmooth(true);
        Label sourceLbl = new Label(newsContent.getSource());
        sourceLbl.setStyle("-fx-font-weight: bold;");
        Label titleLbl = new Label(newsContent.getTitle());
        titleLbl.setWrapText(true);
        titleLbl.setTextAlignment(TextAlignment.JUSTIFY);
        Label timeLbl = new Label(newsContent.getTime());
        Pane verticalSpace = new Pane();
        Pane horizontalSpace = new Pane();
        VBox.setVgrow(verticalSpace, Priority.SOMETIMES);
        HBox.setHgrow(horizontalSpace, Priority.SOMETIMES);
        layoutVBox.setPadding(new Insets(5));
        HBox sourceHBox = new HBox(5, webLogo, sourceLbl);
        layoutVBox.getChildren().addAll(sourceHBox, titleLbl, verticalSpace, new HBox(horizontalSpace, timeLbl));
        layoutVBox.setOnMouseClicked(event -> new Launcher()
                .getHostServices().showDocument(newsContent.getSource()));
        Platform.runLater(()-> getView().getContentAreaFlowPane().getChildren().add(pane));

    }

    public TodaysNewsView getView(){
        return view;
    }

    public void setAggregatorHomePage() {
        String aggregatorSQL = "SELECT * FROM deskofficer WHERE username='" + LoginPresenter.usernameOfLoggedInUser + "'";
        ResultSet aggregatorResultSet = DatabaseHelper.executeQuery(aggregatorSQL);
        try {
            while(aggregatorResultSet.next()){
                aggregatorUrl = aggregatorResultSet.getString("aggregatorurl");
                String aggregatorName = aggregatorResultSet.getString("aggregatorname");
                Platform.runLater(()-> getView()
                        .getTodaysNewsHeader().setText(getView().getTodaysNewsHeader().getText() + " Source: " + aggregatorName));
            }
        }catch (SQLException ex){
            LOGGER.log(Level.SEVERE, ex.getMessage());
        }
    }

    public boolean containsLinkInitials(String link) {
        for(String newswire : HomePresenter.newswires){
            if(link.contains(newswire)){
                return true;
            }
        }
        return false;
    }
}
