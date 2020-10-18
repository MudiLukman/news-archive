package com.kontrol.newsarchive.util;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CustomImageView extends ImageView {

    public CustomImageView(String uri, int width, int height){
        Image image = new Image(CustomImageView.class.getResource(uri).toExternalForm());
        this.setImage(image);
        this.setFitWidth(width);
        this.setFitHeight(height);
        this.setSmooth(true);
        this.setPickOnBounds(true);
    }

}
