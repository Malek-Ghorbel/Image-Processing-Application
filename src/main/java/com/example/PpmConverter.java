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

    public static int[][][] seuillerVariante(int seuilR, int seuilG, int seuilB,boolean ou) {
        int r=0 , g=1 , b=2 ;
        int [][][] image = d;
        for(int i =0 ; i<image.length; i++) {
            for(int j =0 ; j<image[0].length ; j++) {
                if (ou ) {
                    if(image[i][j][r] >= seuilR || image[i][j][g] >= seuilG || image[i][j][b] >= seuilB ) {
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
                    if(image[i][j][r] >= seuilR && image[i][j][g] >= seuilG && image[i][j][b] >= seuilB ) {
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

    public static double otsuRGB( int color) {
        int bins_num = 256;
        int [][][] testImage = d ;
        // Get the histogram
         double[] histogram = new double[256];
        
        // initialize all intensity values to 0
        for(int i = 0; i < 256; i++)
            histogram[i] = 0;
        
        // calculate the no of pixels for each intensity values
        for(int y = 0; y < testImage.length; y++)
            for(int x = 0; x < testImage[0].length; x++)
                histogram[(int)testImage[y][x][color]]++;
        
        // Calculate the bin_edges
        double[] bin_edges = new double[256];
        bin_edges[0] = 0.0;
        double increment = 0.99609375;
        for(int i = 1; i < 256; i++)
            bin_edges[i] = bin_edges[i-1] + increment;
        
        // Calculate bin_mids
        double[] bin_mids= new double[256];
        for(int i = 0; i < 255; i++)
        bin_mids[i] = (bin_edges[i] + bin_edges[i+1])/2;
        
        // Iterate over all thresholds (indices) and get the probabilities weight1, weight2
        double[]  weight1= new double[256];
        weight1[0] = histogram[0];
        for(int i = 1; i < 256; i++)
            weight1[i] = histogram[i] + weight1[i-1];
        
        double total_sum=0;
        for(int i = 0; i < 256; i++)
            total_sum = total_sum + histogram[i];
        double[] weight2= new double[256];
        weight2[0] = total_sum;
        for(int i = 1; i < 256; i++)
            weight2[i] = weight2[i-1] - histogram[i - 1];
        
        // Calculate the class means: mean1 and mean2
        double[] histogram_bin_mids= new double[256];
        for(int i = 0; i < 256; i++)
            histogram_bin_mids[i] = histogram[i] * bin_mids[i];
        
        double[] cumsum_mean1= new double[256];
        cumsum_mean1[0] = histogram_bin_mids[0];
        for(int i = 1; i < 256; i++)
            cumsum_mean1[i] = cumsum_mean1[i-1] + histogram_bin_mids[i];
        
        double[] cumsum_mean2= new double[256];
        cumsum_mean2[0] = histogram_bin_mids[255];
        for(int i = 1, j=254; i < 256 && j>=0; i++, j--)
            cumsum_mean2[i] = cumsum_mean2[i-1] + histogram_bin_mids[j];
        
        double[] mean1= new double[256];
        for(int i = 0; i < 256; i++)
            mean1[i] = cumsum_mean1[i] / weight1[i];
        
        double[] mean2= new double[256];
        for(int i = 0, j = 255; i < 256 && j >= 0; i++, j--)
            mean2[j] = cumsum_mean2[i] / weight2[j];
        
        // Calculate Inter_class_variance
        double[] Inter_class_variance= new double[256];
        double dnum = 1000000000;
        for(int i = 0; i < 255; i++)
            Inter_class_variance[i] = ((weight1[i] * weight2[i] * (mean1[i] - mean2[i+1])) / dnum) * (mean1[i] - mean2[i+1]); 
        
        // Maximize interclass variance
         double maxi = 0;
        int getmax = 0;
        for(int i = 0;i < 255; i++){
            if(maxi < Inter_class_variance[i]){
                maxi = Inter_class_variance[i];
                getmax = i;
            }
        }
        return bin_mids[getmax];
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
