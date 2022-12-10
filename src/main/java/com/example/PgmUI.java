package com.example;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class PgmUI extends ImageUI  {

    private static HBox hbox2 ;
    private static HBox hbox ;
    private static HBox hbox3 ;
    private static HBox hbox4 ;
    private static HBox hbox5 ;
    
    private static void updateImage() {
        myObj = new File("output.pgm");
        try {
            PgmTools.ecrireImage(myObj.toPath().toString());
            imageView.setImage(PgmTools.readImage(myObj));
        } catch (Exception ef) {
            System.out.println("An error occurred.");
        }
    }
   
    public static void loadImage( File file ,VBox root ) {
        try {
            imageView.setImage(PgmTools.readImage(file));
            root.getChildren().add(imageView);
            root.getChildren().add(bruitHBox(root));
            root.getChildren().add(tranLineaireHBox(root));
            root.getChildren().add(filtreMoyHBox(root));
            root.getChildren().add(filtreMedianHBox(root));
            root.getChildren().add(filtrePassehautHBox(root));
        } catch(Exception e) {
            System.out.println("err : " + e.getMessage());
        }
    }

    public static void unloadImage(VBox root) {
        root.getChildren().remove(imageView);
        root.getChildren().remove(hbox);
        root.getChildren().remove(hbox2);
        root.getChildren().remove(hbox3);
        root.getChildren().remove(hbox4);
        root.getChildren().remove(hbox5);
    }

    private static HBox bruitHBox(VBox root) {
        hbox = new HBox();
        hbox.setSpacing(10);
        Button button= new Button("faire un bruit");
        Button button2= new Button("egalisation histogramme");
        Button resetButton= new Button("reset");
        
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                PgmTools.bruit();
                updateImage();
            }
        });
        button2.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                PgmTools.histogrammeEgalise();
                updateImage();
            }
        });
        resetButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                imageView.setImage(PgmTools.readImage(file));
            }
        });
        button.setStyle("-fx-font: 16 arial; -fx-base: #b6e7c9;");
        button2.setStyle("-fx-font: 16 arial; -fx-base: #b6e7c9;");
        resetButton.setStyle("-fx-font: 16 arial; -fx-base: #E06666;");
        
        hbox.getChildren().add(button);
        hbox.getChildren().add(button2);
        hbox.getChildren().add(resetButton);
        return hbox;
    }

    private static HBox filtreMoyHBox(VBox root) {
        hbox2 = new HBox();
        hbox2.setSpacing(10);
        Text label = new Text("filtre moyenne :");
        
        label.setStyle("-fx-font: 18 arial; ");
        Button button= new Button("apply");
        TextField seuil = new TextField ();
        seuil.setPromptText("size");
        seuil.setMaxWidth(40);
        Button button2= new Button("rapport");
        Text rapport = new Text();
        rapport.setStyle("-fx-font: 18 arial; ");
        Text error = new Text("please provide the size");
        error.setFill(Color.RED);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                try {
                    int i=Integer.parseInt(seuil.getText()); 
                    PgmTools.filtreMoy(i);
                    hbox2.getChildren().remove(error);
                } catch (NumberFormatException m) {
                    hbox2.getChildren().add(error);
                }
                updateImage();
            }
        });
        button2.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                rapport.setText(" " +PgmTools.rapport(PgmTools.getData(file)));
            }
        });

        button.setStyle("-fx-font: 16 arial; -fx-base: #b6e7c9;");
        button2.setStyle("-fx-font: 16 arial; -fx-base: #b6e7c9;");
        
        hbox2.getChildren().add(label);
        hbox2.getChildren().add(seuil);
        hbox2.getChildren().add(button);
      //  hbox2.getChildren().add(button2);
     //   hbox2.getChildren().add(rapport);
        
        return hbox2;
    }

    private static HBox filtreMedianHBox(VBox root) {
        hbox3 = new HBox();
        hbox3.setSpacing(10);
        Text label = new Text("filtre median :");
        
        label.setStyle("-fx-font: 18 arial; ");
        Button button= new Button("apply");
        TextField seuil = new TextField ();
        seuil.setPromptText("size");
        seuil.setMaxWidth(40);
        Text error = new Text("please provide the size");
        error.setFill(Color.RED);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                try {
                    int i=Integer.parseInt(seuil.getText()); 
                    PgmTools.filtreMedian(i);
                    hbox3.getChildren().remove(error);
                } catch (NumberFormatException m) {
                    hbox3.getChildren().add(error);
                }
                updateImage();
            }
        });
        Button button2= new Button("rapport");
        Text rapport = new Text();
        rapport.setStyle("-fx-font: 18 arial; ");
        button2.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                rapport.setText(" " +PgmTools.rapport(PgmTools.getData(file)));
            }
        });

        button2.setStyle("-fx-font: 16 arial; -fx-base: #b6e7c9;");
        button.setStyle("-fx-font: 16 arial; -fx-base: #b6e7c9;");
        
        hbox3.getChildren().add(label);
        hbox3.getChildren().add(seuil);
        hbox3.getChildren().add(button);
     //   hbox3.getChildren().add(button2);
      //  hbox3.getChildren().add(rapport);
        return hbox3;
    }
    
    private static HBox filtrePassehautHBox(VBox root) {
        hbox4 = new HBox();
        hbox4.setSpacing(10);
        Text label = new Text("rehaussement de contour :");
        
        label.setStyle("-fx-font: 18 arial; ");
        Button button= new Button("apply");
        
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                PgmTools.filtrePasseHaut();
                updateImage();
            }
        });
        ;

        button.setStyle("-fx-font: 16 arial; -fx-base: #b6e7c9;");
        
        hbox4.getChildren().add(label);
        hbox4.getChildren().add(button);
        return hbox4;
    }

    private static HBox tranLineaireHBox(VBox root) {
        hbox5 = new HBox();
        hbox5.setSpacing(10);
        Text label = new Text("transformation lineaire :");
        
        label.setStyle("-fx-font: 18 arial; ");
        Button button= new Button("apply");
        TextField a1 = new TextField ();
        a1.setPromptText("x1");
        a1.setMaxWidth(30);
        TextField b1 = new TextField ();
        b1.setPromptText("y1");
        b1.setMaxWidth(30);
        TextField a2 = new TextField ();
        a2.setPromptText("x2");
        a2.setMaxWidth(30);
        TextField b2 = new TextField ();
        b2.setPromptText("y2");
        b2.setMaxWidth(30);
        Text error = new Text("please provide all the values");
        error.setFill(Color.RED);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                try {
                    int a1i=Integer.parseInt(a1.getText()); 
                    int b1i=Integer.parseInt(b1.getText()); 
                    int a2i=Integer.parseInt(a2.getText()); 
                    int b2i=Integer.parseInt(b2.getText()); 
                    PgmTools.transLineare(a1i, b1i, a2i, b2i);
                    hbox5.getChildren().remove(error);
                } catch (NumberFormatException m) {
                    hbox5.getChildren().add(error);
                }
                updateImage();
            }
        });
        ;

        button.setStyle("-fx-font: 16 arial; -fx-base: #b6e7c9;");
        
        hbox5.getChildren().add(label);
        hbox5.getChildren().add(a1);
        hbox5.getChildren().add(b1);
        hbox5.getChildren().add(a2);
        hbox5.getChildren().add(b2);
        hbox5.getChildren().add(button);
        return hbox5;
    }
}