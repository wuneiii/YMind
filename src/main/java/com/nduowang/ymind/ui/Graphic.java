package com.nduowang.ymind.ui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Graphic extends ImageView {
    public Graphic(String file, Double width, Double height) {
        super();
        Image img = new Image(getClass().getResourceAsStream(file));
        setImage(img);
        setFitWidth(width);
        preserveRatioProperty().setValue(true);
    }

    public Graphic(String file, Double width) {
        this(file, width, width);
    }

    public Graphic(String file) {
        this(file, 20d);
    }

}