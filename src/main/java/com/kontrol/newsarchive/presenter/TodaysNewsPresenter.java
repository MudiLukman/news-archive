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
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class TodaysNewsPresenter {

    private TodaysNewsView view;
    private String aggregatorUrl = "";
    private List<String> unTraversedLinks;
    private List<String> relevantLinks;
    private List<String> traversedLinks;
    private List<NewsContent> listOfTodaysNews;

    public TodaysNewsPresenter(){
        view = new TodaysNewsView();
        setAggregatorHomePage();
        unTraversedLinks = new ArrayList<>();
        relevantLinks = new ArrayList<>();
        traversedLinks = new ArrayList<>();
        listOfTodaysNews = new ArrayList<>();
        Thread fetchTodayNewsThread = new Thread(this::fetchNewsFromWeb);
        fetchTodayNewsThread.setPriority(Thread.MAX_PRIORITY);
        fetchTodayNewsThread.start();
    }

    private void fetchNewsFromWeb() {
        Document doc = null;
        try {
            doc = Jsoup.connect(aggregatorUrl).timeout(30 * 1000).get();
            unTraversedLinks.add(aggregatorUrl);
            System.out.println("Connected to " + aggregatorUrl);
        }
        catch (IllegalArgumentException e){
            Platform.runLater(()-> {
                AlertMaker.showNotification("Error", aggregatorUrl + " is not a valid URL", AlertMaker.image_cross);
            });
        }
        catch (UnknownHostException e){
            Platform.runLater(()-> {
                AlertMaker.showNotification("Error", "Could not connect to " + e.getMessage(),
                        AlertMaker.image_cross);
                getView().getProgressIndicator().setVisible(false);
            });
            return;
        }
        catch (IOException e) {
            Platform.runLater(()-> {
                AlertMaker.showErrorMessage(e);
                getView().getProgressIndicator().setVisible(false);
            });
            return;
        }

        unTraversedLinks.addAll(getSubLinks(doc));

        //fetch next set of link
        for(int i = 0; i < unTraversedLinks.size(); i++){
            if(unTraversedLinks.size() >= 1000){
                break;
            }
            String link = unTraversedLinks.get(i);
            Document nextLink = null;
            try {
                nextLink = Jsoup.connect(link).timeout(30 * 1000).get();
                System.out.println("Connected to " + i + " of " + unTraversedLinks.size() + ": inner-link:" + link);
            }
            catch (IllegalArgumentException e){
                System.out.println("Inner-link: " + link + " is not a valid URL");
                continue;
            }
            catch (UnknownHostException e){
                System.out.println();
                continue;
            }
            catch (IOException e) {
                System.out.println("Could not connect to inner-link: " + link);
                continue;
            }
            unTraversedLinks.addAll(getSubLinks(nextLink));
        }

        relevantLinks.addAll(unTraversedLinks);
        unTraversedLinks.clear();

        relevantLinks.removeIf((link)-> link.contains(aggregatorUrl));

        System.out.println("Total Number of relevant Links: " + relevantLinks.size());

        //check page content to determine relevance
        Document newsContent = null;
        for(int l = 0; l < relevantLinks.size(); l++){
            String newsUrl = relevantLinks.get(l);
            if(traversedLinks.contains(newsUrl)){
                relevantLinks.remove(newsUrl);
                continue;
            }
            try {
                newsContent = Jsoup.connect(newsUrl).timeout(15 * 1000).get();
            }
            catch (IllegalArgumentException e){
                continue;
            }
            catch (UnknownHostException e){
                System.out.println("Unable to get document from: " + newsUrl);
                continue;
            }
            catch (IOException e) {
                System.out.println("Connection timed-out: " + newsUrl);
                continue;
            }
            finally {
                relevantLinks.remove(newsUrl);
                traversedLinks.add(newsUrl);
            }

            String title = newsContent.title();
            DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
            String formattedDate = dateFormat.format(new Date());

            NewsContent relevantNews = new NewsContent(newsUrl, title, formattedDate);
            if(!listOfTodaysNews.contains(relevantNews)){
                listOfTodaysNews.add(relevantNews);
                createNodeAndDisplayOnScreen(relevantNews);
                System.out.println("Found " + listOfTodaysNews.size() + " today's news");
            }

            System.out.println("Walked through " + l + " of " + relevantLinks.size() + ": " + newsUrl);
        }

        System.out.println("Total Number of unique links: " + relevantLinks.size());
        System.out.println("Total number Of Today's news: " + listOfTodaysNews.size());

        Platform.runLater(()-> {
            getView().getProgressIndicator().setVisible(false);
        });

        if(listOfTodaysNews.isEmpty()){
            Label noNewsLabel = new Label("No news content for today");
            noNewsLabel.setFont(new Font(24));
            noNewsLabel.setStyle("-fx-font-weight: bold;");
            Platform.runLater(()->{
                getView().getStackPane().getChildren().clear();
                getView().getStackPane().getChildren().add(noNewsLabel);
            });
        }

    }

    private Set<String> getSubLinks(Document doc){
        HashSet<String> setOfLinks = new HashSet<>();

        Elements links = null;
        try {
            links = doc.select("a[href]");
        }catch (NullPointerException e){
            return new HashSet<>();
        }
        for (Element e : links) {
            String link = e.attr("abs:href");
            if (link.contains("#")) {
                continue;
            }
            setOfLinks.add(link);
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
        layoutVBox.setOnMouseClicked((event)-> {
            new Launcher().getHostServices().showDocument(newsContent.getSource());
            System.gc();
        });
        Platform.runLater(()-> {
            getView().getContentAreaFlowPane().getChildren().add(pane);
        });

    }

    public TodaysNewsView getView(){
        return view;
    }

    public void setAggregatorHomePage() {
        String aggregatorSQL = "";
        if(CreateUserPresenter.officer == null){
            aggregatorSQL = "SELECT * FROM deskofficer WHERE username='" + LoginPresenter.usernameOfLoggedInUser + "'";
        }
        else {
            aggregatorSQL = "SELECT * FROM deskofficer WHERE username='" + CreateUserPresenter.officer.getUsername() + "'";
        }
        ResultSet aggregatorResultSet = DatabaseHelper.executeQuery(aggregatorSQL);
        try {
            while(aggregatorResultSet.next()){
                aggregatorUrl = aggregatorResultSet.getString("aggregatorurl");
                String aggregatorName = aggregatorResultSet.getString("aggregatorname");
                Platform.runLater(()-> {
                    getView().getTodaysNewsHeader().setText(getView().getTodaysNewsHeader().getText() + " Source: " + aggregatorName);
                });
            }
        }catch (SQLException e){
            System.out.println("TodaysNewsPresenter: setAggregatorHomePage: " + e);
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
