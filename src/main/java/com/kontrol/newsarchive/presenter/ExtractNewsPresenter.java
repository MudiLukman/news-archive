package com.kontrol.newsarchive.presenter;

import com.jfoenix.controls.JFXButton;
import com.kontrol.newsarchive.Launcher;
import com.kontrol.newsarchive.model.Document;
import com.kontrol.newsarchive.util.AlertMaker;
import com.kontrol.newsarchive.util.DatabaseHelper;
import com.kontrol.newsarchive.util.ElasticClientHelper;
import com.kontrol.newsarchive.view.ExtractNewsView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExtractNewsPresenter {

    private ExtractNewsView view;
    private double dragOffsetX;
    private double dragOffsetY;
    private Map<String, Document> documents;
    private Set<String> oldUrls;
    public static ExecutorService threadPool;

    public ExtractNewsPresenter(){
        view = new ExtractNewsView();
        documents = new HashMap<>();
        oldUrls = new HashSet<>();
        threadPool = Executors.newFixedThreadPool(5);
        addDragListeners();
        addEventHandlers();
        loadOldURLs();
        startExtraction();
        threadPool.shutdown();
    }

    private void loadOldURLs() {
        String sql = "SELECT * FROM oldurl";
        ResultSet resultSet = DatabaseHelper.executeQuery(sql);
        try {
            while (resultSet.next()){
                String url = resultSet.getString("url");
                String date = resultSet.getString("date");
                oldUrls.add(url);
                System.out.println("Archive: OldUrl: " + url);
            }
        }catch (SQLException e){
            System.out.println("Unable to load old urls");
        }
    }

    private void addEventHandlers() {
        getView().getPlusButton().setOnMouseClicked(this::handlePlusButtonClick);
        getView().getCancelIcon().setOnMouseClicked(this::handleCancelIconClick);
        getView().getArchiveButton().setOnAction(this::handleArchiveClick);
        getView().getCloseButton().setOnAction(this::handleCloseClicked);
    }

    private void handleCancelIconClick(MouseEvent event){
        getView().getProgressIndicator().setVisible(false);
        getView().getCancelIcon().setVisible(false);
        threadPool = null;
    }

    private void handleArchiveClick(ActionEvent actionEvent){
        threadPool = null;
        saveUrlsToDb();
        saveDocumentsToInvertedIndex();
        AlertMaker.showNotification("Success", "News has been archived", AlertMaker.image_checked);
    }

    private void saveDocumentsToInvertedIndex() {
        RestHighLevelClient client = ElasticClientHelper.getConnection();
        IndexRequest indexRequest = null;
        IndexResponse indexResponse = null;
        for(String newsId : documents.keySet()){
            Map<String, String> docMap = new HashMap<>();
            docMap.put("title", documents.get(newsId).getTitle());
            docMap.put("owner", documents.get(newsId).getOwner());
            docMap.put("body", documents.get(newsId).getBody());
            docMap.put("date", documents.get(newsId).getDate());

            indexRequest = new IndexRequest("newscontents", "doc", newsId).source(docMap);
            indexRequest.timeout("20s");

            try {
                indexResponse = client.index(indexRequest);
                System.out.println("Indexed: " + newsId);
            }catch (IOException e){
                System.out.println(e);
            }
        }
        System.out.println("Done Indexing " + documents.size() + "files");
    }

    private void saveUrlsToDb() {
        for(String docKey : documents.keySet()){
            String sql = "INSERT INTO oldurl(url, date) VALUES ('" + docKey + "', '" + LocalDate.now() + "');";
            //the if statement says if i cant save it as an old url,
            //don't index it so that when i fetch it as a
            //news content tomorrow, i won't get error trying to index
            if(DatabaseHelper.insert_record(sql) == 0){
                documents.remove(docKey);
            }
        }
    }

    private void handlePlusButtonClick(MouseEvent mouseEvent){
        BorderPane addNewsPane = new BorderPane();
        addNewsPane.setPadding(new Insets(30));
        TextField newSourceTxtField = new TextField();
        newSourceTxtField.setPrefWidth(300);
        JFXButton addBtn = new JFXButton("Add");
        JFXButton closeBtn = new JFXButton("Close");
        Label urlLbl = new Label("URL:");
        urlLbl.setFont(new Font(20));
        urlLbl.setTextFill(Color.WHITE);
        addNewsPane.setCenter(new HBox(10, urlLbl, newSourceTxtField));
        Pane space = new Pane();
        HBox addNewsBottom = new HBox(20);
        addNewsBottom.setPadding(new Insets(15));
        addBtn.setDefaultButton(true);
        addBtn.setPadding(new Insets(3));
        addBtn.setStyle("-fx-background-color: rgb(238, 167, 121);"
                + "-fx-border-radius: 5;"
                + "-fx-background-radius: 5");
        closeBtn.setPadding(new Insets(3));
        closeBtn.setCancelButton(true);
        closeBtn.setStyle("-fx-background-color: rgb(238, 167, 121);"
                + "-fx-border-radius: 5;"
                + "-fx-background-radius: 5");
        addNewsBottom.getChildren().addAll(space, addBtn, closeBtn);
        HBox.setHgrow(space, Priority.SOMETIMES);
        addNewsPane.setBottom(addNewsBottom);
        addNewsPane.setStyle("-fx-background-color: rgb(62, 73, 86);" +
                "-fx-border-color: rgb(255, 255, 255);" +
                "-fx-border-width: 5;");

        addBtn.setOnAction(event -> {
            if(newSourceTxtField.getText().trim().equals("")){
                return;
            }
            String newsUrl = newSourceTxtField.getText().trim();
            if(!SetNewswiresPresenter.isUrlValid(newsUrl)){
                return;
            }

            org.jsoup.nodes.Document newNewsSource = null;
            try {
                newNewsSource = Jsoup.connect(newsUrl).timeout(30 * 1000).get();
            }
            catch (IllegalArgumentException | IOException e){
                AlertMaker.showErrorMessage(e);
                return;
            }

            String title = newNewsSource.title();
            String body = getNewsBody(newNewsSource, newsUrl);
            DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
            String formattedDate = dateFormat.format(new Date());

            Document relevantDocument = new Document(newsUrl, title, newsUrl, body, formattedDate);
            documents.put(newsUrl, relevantDocument);

            createNodeAndDisplayOnScreen(relevantDocument);

            addNewsPane.getScene().getWindow().hide();

        });
        closeBtn.setOnAction(event -> addNewsPane.getScene().getWindow().hide());
        Launcher.loadWindow("Add New Source", Modality.APPLICATION_MODAL, addNewsPane, 500, 120);
    }

    private void handleCloseClicked(ActionEvent event){
        threadPool = null;
        getView().getCloseButton().getScene().getWindow().hide();
    }

    private void startExtraction() {
        for(int j = 0; j < HomePresenter.newswires.size(); j++){
            int finalJ = j;
            threadPool.execute(()-> crawlNewswire(HomePresenter.newswires.get(finalJ)));
        }

        new Thread(()->{
            if(threadPool != null){
                try {
                    while (!threadPool.isTerminated()){
                    }
                }catch (NullPointerException e){
                    hideProgressIndicatorPane();
                }
            }
            hideProgressIndicatorPane();
        }).start();

    }

    private void hideProgressIndicatorPane() {
        Platform.runLater(()->{
            getView().getProgressIndicator().setVisible(false);
            getView().getCancelIcon().setVisible(false);
        });
    }

    private void crawlNewswire(String startAddress) {
        List<String> traversedLinks = new ArrayList<>();
        List<String> unTraversedLinks = new ArrayList<>();
        Set<Document> setOfRelevantNews = new HashSet<>();

        org.jsoup.nodes.Document doc = null;
        try {
            doc = Jsoup.connect(startAddress).timeout(30 * 1000).get();
            traversedLinks.add(startAddress);
            System.out.println("Archive: Connected to " + startAddress);
        }
        catch (IllegalArgumentException e){
            System.out.println(startAddress + " is not a valid URL");
            return;
        }
        catch (UnknownHostException e){
            System.out.println("Could not connect to " + startAddress);
            return;
        }
        catch (IOException e) {
            System.out.println(e);
            return;
        }

        Elements links = null;
        try {
            links = doc.select("a[href]");
        }catch (NullPointerException e){
            return;
        }
        for(int i = 0; i < links.size(); i++){
            if(threadPool == null){
                return;
            }
            String link = links.get(i).attr("abs:href");
            if(link.contains("#")){
                continue;
            }
            unTraversedLinks.add(link);
        }

        //remove old urls
        unTraversedLinks.removeAll(oldUrls);

        System.out.println("Total number of traversable links from @ " + startAddress + " : " + unTraversedLinks.size());

        System.out.println("Archive: Total number of links: " + unTraversedLinks.size() + " @ " + startAddress);
        System.out.println("Archive: Total Number of traverseable Links: " + unTraversedLinks.size());

        //check page content to determine relevance
        org.jsoup.nodes.Document newsContent = null;
        for(int l = 0; l < unTraversedLinks.size(); l++){
            if(threadPool == null){
                return;
            }
            String newsUrl = unTraversedLinks.get(l);
            if(traversedLinks.contains(newsUrl)){
                unTraversedLinks.remove(newsUrl);
                continue;
            }
            try {
                newsContent = Jsoup.connect(newsUrl).timeout(20 * 1000).get();
            }
            catch (IllegalArgumentException e){
                unTraversedLinks.remove(l);
                continue;
            }
            catch (UnknownHostException e){
                System.out.println("Archive: Unable to get document from: " + newsUrl);
                unTraversedLinks.remove(newsUrl);
                continue;
            }
            catch (IOException e) {
                System.out.println("Archive: Connection timed-out: " + newsUrl);
                unTraversedLinks.remove(newsUrl);
                continue;
            }
            finally {
                unTraversedLinks.remove(newsUrl);
                traversedLinks.add(newsUrl);
            }

            String title = newsContent.title();
            String body = getNewsBody(newsContent, startAddress);
            DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
            String formattedDate = dateFormat.format(new Date());

            for(String tag : HomePresenter.keywords){
                if(threadPool == null){
                    return;
                }

                Document relevantDocument = new Document(newsUrl, title, startAddress, body, formattedDate);
                if((title.contains(tag) || title.contains(tag.toLowerCase()) )
                        && !setOfRelevantNews.contains(relevantDocument)){
                    setOfRelevantNews.add(relevantDocument);
                    documents.put(newsUrl, relevantDocument);
                    createNodeAndDisplayOnScreen(relevantDocument);
                    System.out.println("Archive: " + newsUrl + " matches " + tag);
                }
            }
            System.out.println("Archive: Walked through " + l + " of " + unTraversedLinks.size());
        }
    }

    private String getNewsBody(org.jsoup.nodes.Document newsContent, String source) {
        String body = "";

        try {
            if(source.contains("https://punchng.com/")){
                body = newsContent.select("div.row").first().text();
            }
            else if(source.contains("https://www.vanguardngr.com/") || source.contains("https://www.dailytrust.com.ng/")
                    || source.contains("https://tribuneonlineng.com/") || source.contains("https://businessday.ng/")
                    || source.contains("http://brtnews.ng/")){
                body = newsContent.select("div.entry-content").first().text();
            }
            else if(source.contains("https://www.thisdaylive.com/") || source.contains("https://www.independent.ng/")){
                body = newsContent.select("div.td-post-content").first().text();
            }
            else if(source.contains("https://guardian.ng/")){
                body = newsContent.select("div.single-article-content").first().text();
            }
            else if(source.contains("https://dailytimes.ng/")){
                body = newsContent.select("div.entry-content no-share").first().text();
            }
            else if(source.contains("https://leadership.ng/") || source.contains("https://www.newtelegraphng.com/")){
                body = newsContent.select("div.left relative").first().text();
            }
            else if(source.contains("https://thenationonlineng.net/") || source.contains("https://theeagleonline.com.ng/")){
                body = newsContent.select("div.content-inner").first().text();
            }
            else if(source.contains("https://peoplesdailyng.com/")){
                body = newsContent.select("div.entry").first().text();
            }
            else if(source.contains("https://www.blueprint.ng/")){
                body = newsContent.select("div.entry-content clearfix").first().text();
            }
            else if(source.contains("https://www.premiumtimesng.com/")){
                body = newsContent.select("div.entry-content manoj single-add-content").first().text();
            }
            else if(source.contains("http://saharareporters.com/")){
                body = newsContent.select("div.panel-pane pane-node-content").first().text();
            }
            else if(source.contains("https://investadvocate.com.ng/")){
                body = newsContent.select("div.article-body").first().text();
            }
            else if(source.contains("https://investdata.com.ng/")){
                body = newsContent.select("div.post-body-inner").first().text();
            }
            else {
                body = newsContent.body().text();
            }
        }catch (NullPointerException e){
            body = newsContent.body().text();
        }

        return (body == null || body.equals("")) ? newsContent.body().text() : body;
    }

    private void createNodeAndDisplayOnScreen(Document relevantDocument) {
        ExtractedNewsPresenter extractedNewsPresenter =
                new ExtractedNewsPresenter(documents, relevantDocument, getView());

        Platform.runLater(()-> getView().getFlowPane().getChildren()
                .add(extractedNewsPresenter.getView()));

    }

    private void addDragListeners() {
        getView().getTop().setOnMousePressed(this::handleMousePressed);
        getView().getTop().setOnMouseDragged(this::handleMouseDragged);
    }

    private void handleMousePressed(MouseEvent event){
        this.dragOffsetX = event.getScreenX() - ((Stage) this.getView().getScene().getWindow()).getX();
        this.dragOffsetY = event.getScreenY() - ((Stage) this.getView().getScene().getWindow()).getY();
    }

    private void handleMouseDragged(MouseEvent event){
        getView().getScene().getWindow().setX(event.getScreenX() - this.dragOffsetX);
        getView().getScene().getWindow().setY(event.getScreenY() - this.dragOffsetY);
    }

    public ExtractNewsView getView(){
        return view;
    }

}
