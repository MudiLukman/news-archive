package com.kontrol.newsarchive.presenter;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.effects.JFXDepthManager;
import com.kontrol.newsarchive.Launcher;
import com.kontrol.newsarchive.util.AlertMaker;
import com.kontrol.newsarchive.util.CustomImageView;
import com.kontrol.newsarchive.util.DatabaseHelper;
import com.kontrol.newsarchive.util.ElasticClientHelper;
import com.kontrol.newsarchive.view.SearchResultView;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SearchResultPresenter {

    private SearchResultView view;
    private RestHighLevelClient client;
    private SearchRequest searchRequest;
    private SearchResponse searchResponse;
    private SearchHits hits;
    private long totalHits;
    private SearchHit[] searchHits;
    private SearchSourceBuilder sourceBuilder;

    public SearchResultPresenter(String query){
        view = new SearchResultView();
        getView().getContentAreaFlowPane().getChildren().clear();
        client = ElasticClientHelper.getConnection();
        searchRequest = new SearchRequest();
        sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.multiMatchQuery(query, "title", "body"));
        searchRequest.source(sourceBuilder);
        sourceBuilder.from(0);
        sourceBuilder.size(1000);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        new Thread(this::performSearch).start();
    }

    public SearchResultPresenter(){
        view = new SearchResultView();
        getView().getContentAreaFlowPane().getChildren().clear();
        client = ElasticClientHelper.getConnection();
        searchRequest = new SearchRequest();
        sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(sourceBuilder);
        sourceBuilder.from(0);
        sourceBuilder.size(1000);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        new Thread(this::performSearch).start();
    }

    public SearchResultPresenter(String query, String field){
        view = new SearchResultView();
        getView().getContentAreaFlowPane().getChildren().clear();
        client = ElasticClientHelper.getConnection();
        searchRequest = new SearchRequest();
        sourceBuilder = new SearchSourceBuilder();
        QueryBuilder matchQueryBuilder = QueryBuilders
                .matchQuery(field, query)
                .fuzziness(Fuzziness.AUTO)
                .prefixLength(3)
                .maxExpansions(10);
        sourceBuilder.query(matchQueryBuilder);
        searchRequest.source(sourceBuilder);
        sourceBuilder.from(0);
        sourceBuilder.size(1000);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        new Thread(this::performSearch).start();
    }

    private void displaySearchResults() {
        searchHits = hits.getHits();
        if(searchHits.length == 0){
            Label noResultLbl = new Label("Oops. No result matched  your query, try something else");
            noResultLbl.setStyle("-fx-font-weight: bold;");
            noResultLbl.setFont(new Font(20));
            Platform.runLater(()-> {
                getView().getStackPane().getChildren().add(noResultLbl);
            });
        }
        for(SearchHit hit : searchHits){
            String id = hit.getId();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String title = (String) sourceAsMap.get("title");
            String body = (String) sourceAsMap.get("body");
            String date = (String) sourceAsMap.get("date");
            createNodeAndDisplay(id, title, body, date);
        }
    }

    private void createNodeAndDisplay(String url, String title, String body, String date) {
        StackPane pane = new StackPane();
        pane.setPrefSize(1060, 200);
        pane.setStyle("-fx-background-color: rgb(255, 255, 255);" +
                "-fx-background-radius: 5;" +
                "-fx-border-radius: 5;");
        VBox layoutVBox = new VBox(5);
        pane.getChildren().add(layoutVBox);
        JFXDepthManager.setDepth(pane, 1);
        CustomImageView webLogo = new CustomImageView("/globe.png", 30, 30);
        webLogo.setSmooth(true);
        Label urlLbl = new Label(url);
        urlLbl.setFont(new Font(16));
        urlLbl.setTextAlignment(TextAlignment.JUSTIFY);
        urlLbl.setStyle("-fx-font-weight: bold;");
        CustomImageView minusBtn = new CustomImageView("/minus.png", 18, 15);
        minusBtn.setPickOnBounds(true);
        Label titleLbl = new Label(title);
        titleLbl.setFont(new Font(18));
        titleLbl.setWrapText(true);
        titleLbl.setTextAlignment(TextAlignment.JUSTIFY);
        Label bodyLbl = new Label(body);
        bodyLbl.setFont(new Font(15));
        bodyLbl.setWrapText(true);
        bodyLbl.setTextAlignment(TextAlignment.JUSTIFY);
        Label dateLbl = new Label(date);
        Pane spaceBetweenUrlAndDeleteIcon = new Pane();
        Pane spaceAboveDate = new Pane();
        Pane spaceBeforeDate = new Pane();
        HBox.setHgrow(spaceBetweenUrlAndDeleteIcon, Priority.SOMETIMES);
        HBox.setHgrow(spaceBeforeDate, Priority.SOMETIMES);
        VBox.setVgrow(spaceAboveDate, Priority.SOMETIMES);
        layoutVBox.setPadding(new Insets(8));
        HBox sourceHBox = new HBox(5, webLogo, urlLbl, spaceBetweenUrlAndDeleteIcon, minusBtn);
        layoutVBox.getChildren().addAll(sourceHBox, titleLbl, bodyLbl, spaceAboveDate, new HBox(spaceBeforeDate, dateLbl));

        layoutVBox.setOnMouseClicked(event -> {
            new Launcher().getHostServices().showDocument(url);
        });

        minusBtn.setOnMouseClicked(event -> {
            event.consume();
            handleDeleteSearchResultClicked(url);
        });

        Platform.runLater(()-> {
            getView().getContentAreaFlowPane().getChildren().add(pane);
        });
    }

    private void handleDeleteSearchResultClicked(String id) {
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
        Label confirmText = new Label("Are you sure you want to delete this?");
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
            deleteSearchResult(id);
            ok.getScene().getWindow().hide();
        });
        cancel.setOnAction(event1 -> {
            cancel.getScene().getWindow().hide();
        });

    }

    private void deleteSearchResult(String id) {
        String sql = "DELETE FROM oldurl WHERE url='" + id + "'";
        if(DatabaseHelper.insert_record(sql) > 0){
            performDelete(id);
        }
        else {
            AlertMaker.showNotification("Error", "unable to delete record", AlertMaker.image_warning);
        }
    }

    private void obtainSearchResults() {
        hits = searchResponse.getHits();
        totalHits = hits.getTotalHits();
        displaySearchResults();
    }

    public SearchResultView getView(){
        return view;
    }

    public void performSearch() {
        try {
            searchResponse = client.search(searchRequest);
        } catch (IOException e) {
            AlertMaker.showErrorMessage(e);
        }
        obtainSearchResults();
    }

    public void performDelete(String docId){
        RestHighLevelClient client = ElasticClientHelper.getConnection();
        DeleteRequest deleteRequest = new DeleteRequest("newscontents", "doc", docId);
        deleteRequest.timeout("2m");
        try {
            DeleteResponse deleteResponse = client.delete(deleteRequest);
            removeNodeFromSceneGraph(docId);
        } catch (IOException e) {
            Platform.runLater(()-> {
                AlertMaker.showErrorMessage(e);
            });
        }
    }

    private void removeNodeFromSceneGraph(String docId) {
        for(int i = 0; i < getView().getContentAreaFlowPane().getChildren().size(); i++){
            StackPane stackPane = (StackPane) getView().getContentAreaFlowPane().getChildren().get(i);
            VBox child = (VBox) stackPane.getChildren().get(0);
            HBox firstSubChild = ((HBox) child.getChildren().get(0));
            for(Node n : firstSubChild.getChildren()){
                if(n instanceof Label){
                    String lblValue = ((Label) n).getText();
                    if(lblValue.equals(docId)){
                        getView().getContentAreaFlowPane().getChildren().remove(stackPane);
                    }
                }
            }
        }
    }
}
