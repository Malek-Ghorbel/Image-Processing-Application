package com.example;

import java.io.File;

import javafx.scene.image.ImageView;

public abstract class ImageUI {
    protected static File myObj = new File("outputt.ppm");
    protected static ImageView imageView;
    protected static File file ;

    public static void setAttributes(ImageView i , File f) {
        imageView = i ;
        file = f;
    }

    
}
