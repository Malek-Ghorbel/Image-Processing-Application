package com.example;

import javafx.scene.image.PixelBuffer;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.nio.IntBuffer;
import java.util.Scanner;

public class PpmConverter {
    public static int[][][] d ;
    private static int width;
    private static int height;

    public static void ecrireImagePPM(String fileName){
        try{
            int[][][] tab=d;
            //specify the name of the output..
            FileWriter fstream = new FileWriter(fileName);
            //we create a new BufferedWriter
            BufferedWriter out = new BufferedWriter(fstream);
            //we add the header, 128 128 is the width-height and 63 is the max value-1 of ur data
            out.write("P3\n"+width+" " +height+"\n255\n");
            //2 loops to read the 2d array
            for(int i = 0 ; i<height;i++) {

            
               for(int j = 0 ; j<width ;j++){
                    //we write in the output the value in the position ij of the array
                    out.write(tab[i][j][0]+" ");
                    out.write(tab[i][j][1]+" ");
                    out.write(tab[i][j][2]+" ");
               }
                   
                out.write("\n");
            }
            //we close the bufferedwritter
            out.close();
            }
       catch (Exception e){
            System.err.println("Error : " + e.getMessage());
       }
    }

    public static int[][][] seuiller( int seuilR, int seuilG, int seuilB) {
        int r=0 , g=1 , b=2 ;
        int [][][] image= d ;
        for(int i =0 ; i<image.length; i++) {
            for(int j =0 ; j<image[0].length ; j++) {
                if(image[i][j][r] < seuilR)
                    image[i][j][r] = 0 ;
                else 
                    image[i][j][r] = 255 ;

                if(image[i][j][g] < seuilG)
                    image[i][j][g] = 0 ;
                else 
                    image[i][j][g] = 255 ;

                if(image[i][j][b] < seuilB)
                    image[i][j][b] = 0 ;
                else 
                    image[i][j][b] = 255 ;
            }
        }
        return image ;
    }

    public static int[][][] seuillerVariante(int seuil,boolean ou) {
        int r=0 , g=1 , b=2 ;
        int [][][] image = d;
        for(int i =0 ; i<image.length; i++) {
            for(int j =0 ; j<image[0].length ; j++) {
                if (ou ) {
                    if(image[i][j][r] >= seuil || image[i][j][g] >= seuil || image[i][j][b] >= seuil ) {
                        image[i][j][r] = 255 ;
                        image[i][j][g] = 255 ;
                        image[i][j][b] = 255 ;
                    }
                    
                    else  {
                        image[i][j][r] = 0 ;
                        image[i][j][g] = 0 ;
                        image[i][j][b] = 0 ;
                    }
                    
                } 
                else {
                    if(image[i][j][r] >= seuil && image[i][j][g] >= seuil && image[i][j][b] >= seuil ) {
                        image[i][j][r] = 255 ;
                        image[i][j][g] = 255 ;
                        image[i][j][b] = 255 ;
                    }
                    else {
                        image[i][j][r] = 0 ;
                        image[i][j][g] = 0 ;
                        image[i][j][b] = 0 ;
                    }
                }
            }
        }
        return image ;
    }
    
    public static WritableImage convert(File ppmFile) {
        try (Scanner scanner = new Scanner(ppmFile)) {
            

            // Discard the magic number
            scanner.nextLine();
            // Discard the comment line
       //     scanner.nextLine();
            // Read pic width, height and max value
             width = scanner.nextInt();
             height = scanner.nextInt();
            int colorRange = scanner.nextInt();
            // Create the buffer for the pixels
            IntBuffer intBuffer = IntBuffer.allocate(width * height);
            d=new int[height][width][3];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    int pixel = 0;
                    for (int k = 0; k < 3; k++) {
                        if (!scanner.hasNext()) {
                            System.err.println("R, G, or B value missing from pixel in ppm file");
                            return null;
                        }
                        int value = scanner.nextInt();
                        d[i][j][k] = value;
                        // Shift in byte to pixel
                        pixel <<= 8;
                        pixel |= value;
                    }
                    // Add opaque alpha value to first byte
                    pixel |= 0xff000000;
//                    System.out.println((pixel & 0xff) + " " + ((pixel >>> 8) & 0xff) + " " + (pixel >>> 16));
                    intBuffer.put(pixel);
                }
            }
            intBuffer.flip();

            // Create pixel buffer
            PixelFormat<IntBuffer> pixelFormat = PixelFormat.getIntArgbPreInstance();
            PixelBuffer<IntBuffer> pixelBuffer = new PixelBuffer<>(width, height,
                    intBuffer, pixelFormat);
            WritableImage writableImage = new WritableImage(pixelBuffer);

            // Return writable image
            return writableImage;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
