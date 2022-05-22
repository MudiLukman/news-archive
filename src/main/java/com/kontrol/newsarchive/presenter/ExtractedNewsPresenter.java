package com.kontrol.newsarchive.presenter;

import com.kontrol.newsarchive.Launcher;
import com.kontrol.newsarchive.model.Document;
import com.kontrol.newsarchive.view.ExtractNewsView;
import com.kontrol.newsarchive.view.ExtractedNewsView;
import javafx.scene.input.MouseEvent;

import java.util.Map;

public class ExtractedNewsPresenter {
    private ExtractedNewsView view;
    private Map<String, Document> documents;

    public ExtractedNewsPresenter(Map<String, Document> documents, Document document, ExtractNewsView parent){
        this.view = new ExtractedNewsView(document, parent);
        this.documents = documents;
        addEventHandlers();
    }

    private void addEventHandlers() {
        this.getView().getMinusBtn().setOnMouseClicked(this::handleMouseClickedEvent);
        this.getView().getLayoutVBox().setOnMouseClicked(this::handleLayoutVBoxClickedEvent);
    }

    private void handleMouseClickedEvent(MouseEvent event){
        event.consume();
        documents.remove(getView().getRelevantDocument().getId());
        getView().getParentPane().getFlowPane().getChildren().remove(getView());
    }

    private void handleLayoutVBoxClickedEvent(MouseEvent event){
        new Launcher().getHostServices().showDocument(getView().getRelevantDocument().getOwner());
    }

    public ExtractedNewsView getView(){
        return this.view;
    }
}
