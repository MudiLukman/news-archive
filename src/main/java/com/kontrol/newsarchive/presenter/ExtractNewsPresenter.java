package com.kontrol.newsarchive.presenter;

import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import com.jfoenix.controls.JFXButton;
import com.kontrol.newsarchive.Launcher;
import com.kontrol.newsarchive.model.Document;
import com.kontrol.newsarchive.util.AlertMaker;
import com.kontrol.newsarchive.util.DatabaseHelper;
import com.kontrol.newsarchive.util.ElasticClientHelper;
import com.kontrol.newsarchive.util.UrlUtil;
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
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ExtractNewsPresenter {

    private static final Logger LOGGER = Logger.getLogger(ExtractNewsPresenter.class.getName());
    private final ExtractNewsView view = new ExtractNewsView();
    private double dragOffsetX;
    private double dragOffsetY;
    private final Map<String, Document> documents = new HashMap<>();
    private final Set<String> oldUrls = new HashSet<>();
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private static final int DEFAULT_TIMEOUT = 30 * 1_000;

    public ExtractNewsPresenter(){
        addDragListeners();
        addEventHandlers();
        loadOldURLs();
        startExtraction();
    }

    private void loadOldURLs() {
        String sql = "SELECT * FROM oldurl";
        ResultSet resultSet = DatabaseHelper.executeQuery(sql);
        try {
            while (resultSet.next()){
                String url = resultSet.getString("url");
                oldUrls.add(url);
                LOGGER.info("Archive: OldUrl: " + url);
            }
        }catch (SQLException ex){
            LOGGER.log(Level.SEVERE, "Unable to load old urls {}", ex.getMessage());
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
    }

    private void handleArchiveClick(ActionEvent actionEvent){
        saveUrlsToDb();
        saveDocumentsToInvertedIndex();
        AlertMaker.showNotification("Success", "News has been archived", AlertMaker.image_checked);
    }

    private void saveDocumentsToInvertedIndex() {
        documents.forEach((link, document) -> {
            Map<String, String> docMap = new HashMap<>();
            docMap.put("title", document.getTitle());
            docMap.put("owner", document.getOwner());
            docMap.put("body", document.getBody());
            docMap.put("date", document.getDate());

            //IndexRequest indexRequest = new IndexRequest("newscontents", "doc", newsId).source(docMap);
            IndexRequest indexRequest = new IndexRequest.Builder()
                    .id(link)
                    .index("newscontents")
                    .document(docMap)
                    //.timeout(20s)
                    .build();

            try {
                IndexResponse indexResponse = ElasticClientHelper.getConnection().index(indexRequest);
                LOGGER.info("Indexed: " + link);
            }catch (IOException ex){
                LOGGER.log(Level.SEVERE, ex.getMessage());
            }
        });
        LOGGER.info("Done Indexing " + documents.size() + "files");
    }

    private void saveUrlsToDb() {
        for(String docKey : documents.keySet()){
            String sql = "INSERT INTO oldurl(url, date) VALUES ('" + docKey + "', '" + LocalDate.now() + "');";
            //if I can't save it as an old url,
            //don't index it so that when next I fetch it as a
            //news content tomorrow, I won't get error trying to index
            if(DatabaseHelper.insertRecord(sql) == 0){
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
            String newsUrl = newSourceTxtField.getText().trim();
            if(!UrlUtil.isValid(newsUrl)){
                return;
            }

            try {
                Document relevantDocument = urlAsDomainDocument(newsUrl);
                documents.put(newsUrl, relevantDocument);
                createNodeAndDisplayOnScreen(relevantDocument);
                addNewsPane.getScene().getWindow().hide();
            }
            catch (IllegalArgumentException | IOException ex){
                AlertMaker.showErrorMessage(ex);
                LOGGER.log(Level.SEVERE, ex.getMessage());
            }
        });
        closeBtn.setOnAction(event -> addNewsPane.getScene().getWindow().hide());
        Launcher.loadWindow("Add New Source", Modality.APPLICATION_MODAL, addNewsPane, 500, 120);
    }

    private Document urlAsDomainDocument(String url) throws IOException {
        org.jsoup.nodes.Document newNewsSource = Jsoup.connect(url).timeout(DEFAULT_TIMEOUT).get();
        String title = newNewsSource.title();
        String body = getNewsBody(newNewsSource);
        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        String formattedDate = dateFormat.format(new Date());
        return new Document(url, title, url, body, formattedDate);
    }

    private void handleCloseClicked(ActionEvent event){
        getView().getCloseButton().getScene().getWindow().hide();
    }

    private void startExtraction() {
        HomePresenter.newswires.forEach(newswire -> {
            CompletableFuture<Void> extractionFuture = CompletableFuture.runAsync(
                    () -> startExtraction(newswire), executor);

            extractionFuture.thenRunAsync(this::hideProgressIndicatorPane, executor);

            extractionFuture.whenComplete((result, ex) -> {
                if (ex != null) {
                    LOGGER.log(Level.SEVERE, ex.getCause().getMessage());
                }
            });
        });
    }

    private void hideProgressIndicatorPane() {
        Platform.runLater(()->{
            getView().getProgressIndicator().setVisible(false);
            getView().getCancelIcon().setVisible(false);
        });
    }

    private void startExtraction(String startAddress) {
        org.jsoup.nodes.Document startPage = toJsoupDocument(startAddress);
        Elements links = getLinksAsElements(startPage);

        List<String> unTraversedLinks = getLinksFromElements(links);
        unTraversedLinks.removeAll(oldUrls);

        unTraversedLinks.forEach(link -> {
            try {
                Document document = urlAsDomainDocument(link);
                HomePresenter.keywords.forEach(keyword -> {
                    if(document.getTitle().toLowerCase().contains(keyword.toLowerCase())){
                        documents.put(link, document);
                        createNodeAndDisplayOnScreen(document);
                        LOGGER.info(String.format("Archive: %s matches %s", link, keyword));
                    }
                });
            } catch (IllegalArgumentException | IOException e) {
                LOGGER.info("Archive: Connection timed-out: " + link);
            }
        });
    }

    private org.jsoup.nodes.Document toJsoupDocument(String startAddress) {
        try {
            org.jsoup.nodes.Document doc = Jsoup.connect(startAddress).timeout(DEFAULT_TIMEOUT).get();
            LOGGER.info("Archive: Connected to " + startAddress);
            return doc;
        } catch (IllegalArgumentException | IOException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
            throw new IllegalStateException();
        }
    }

    private Elements getLinksAsElements(org.jsoup.nodes.Document document) {
        try {
            return document.select("a[href]");
        }catch (NullPointerException ex){
            LOGGER.log(Level.SEVERE, ex.getMessage());
            return new Elements();
        }
    }

    private List<String> getLinksFromElements(Elements links) {
        return links.stream()
                .map(link -> link.attr("abs:href"))
                .filter(link -> !link.contains("#"))
                .collect(Collectors.toList());
    }

    private String getNewsBody(org.jsoup.nodes.Document newsContent) {
        return newsContent.body().text();
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
