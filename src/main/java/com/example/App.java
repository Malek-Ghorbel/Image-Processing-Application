package com.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;



public class App extends Application {
    private Stage primaryStage;
    private ImageView imageView;
    
    @Override
    public void start(Stage stage) throws IOException {
        this.primaryStage = stage;
        primaryStage.setTitle("image processing application");
        VBox root = new VBox();
        root.setSpacing(15);
        root.setStyle("-fx-padding: 0 0 0 5;");
        
        Scene scene = new Scene(root, 600, 600);
        root.setBackground(new Background(
                new BackgroundFill(Color.LIGHTCYAN, null, null)));

        // Add menu
        Menu fileMenu = new Menu("File");
        MenuItem openFileMenuItem = new MenuItem("Open a ppm image");
        fileMenu.getItems().add(openFileMenuItem);
        MenuItem openFileMenuItem2 = new MenuItem("Open a pgm image");
        fileMenu.getItems().add(openFileMenuItem2);
        MenuBar menuBar = new MenuBar(fileMenu);
        menuBar.setUseSystemMenuBar(true);
        root.getChildren().add(menuBar);

        Text title = new Text ("Welcome to the application");
        title.setStyle("-fx-font: 40px Tahoma; -fx-fill: linear-gradient(from 0% 0% to 100% 200%, repeat, aqua 0%, red 75%); -fx-stroke: black; -fx-stroke-width: 1;");

        Text text = new Text();
        text.setStyle("-fx-font: 22 Tahoma; ");
        text.setText("open an image using 'file' menu or drag and drop a file here");

        root.getChildren().add(title);
        root.getChildren().add(text);

        // Add Image Viewer
        imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        // Set up drag and drop for image viewer
        root.setOnDragOver(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });
        root.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                File firstFile = db.getFiles().get(0);
                if (firstFile.canRead() && firstFile.getPath().toLowerCase().endsWith(".ppm")) {
                    loadPpmImage(firstFile, root);
                    success = true;
                    root.getChildren().remove(text);
                    root.getChildren().remove(title);
                }
                else if (firstFile.canRead() && firstFile.getPath().toLowerCase().endsWith(".pgm")) {
                    loadPgmImage(firstFile, root);
                    success = true;
                    root.getChildren().remove(text);
                    root.getChildren().remove(title);
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
        
        // Set open image action
        openFileMenuItem.setOnAction(event -> {
            File ppmFile = getPpmFile();
            if (ppmFile != null) {
                loadPpmImage(ppmFile, root);
                root.getChildren().remove(text);
                root.getChildren().remove(title);
            }
        });

        openFileMenuItem2.setOnAction(event -> {
            File ppmFile = getPgmFile();
            if (ppmFile != null) {
                loadPgmImage(ppmFile, root);
                root.getChildren().remove(text);
                root.getChildren().remove(title);
            }
        });
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    @Override
    public void stop() {
    }

    private File getPpmFile() {
        FileChooser imageChooser = new FileChooser();
        imageChooser.setTitle("Open Image");
        imageChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PPM Image", "*.ppm")
        );
        return imageChooser.showOpenDialog(primaryStage);
    }

    private File getPgmFile() {
        FileChooser imageChooser = new FileChooser();
        imageChooser.setTitle("Open Image");
        imageChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PGM Image", "*.pgm")
        );
        return imageChooser.showOpenDialog(primaryStage);
    }

    private void loadPpmImage(File imageFile, VBox root) {
        PpmUI.setAttributes(imageView, imageFile);
        PpmUI.unloadImage(root);
        PgmUI.unloadImage(root);
        PpmUI.loadImage(imageFile, root);
    }

    private void loadPgmImage(File imageFile, VBox root) {
        PgmUI.setAttributes(imageView, imageFile);
        PpmUI.unloadImage(root);
        PgmUI.unloadImage(root);
        PgmUI.loadImage(imageFile, root);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
