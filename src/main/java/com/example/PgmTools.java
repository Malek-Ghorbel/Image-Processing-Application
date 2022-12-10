package com.example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class PgmTools {
    public static int[][] d ;
    private static int width;
    private static int height;

    public static Image readImage(File file){
        WritableImage image1 = null ;
        try {
            Scanner scan = new Scanner(new FileInputStream(file.toPath().toString()));
            
            // Discard the magic number
            scan.nextLine();
            // Discard the comment line
            scan.nextLine();
            // Read pic width, height and max value
            width = scan.nextInt();
            height = scan.nextInt();
            int maxvalue = scan.nextInt();

            image1 = new WritableImage(width, height);
            PixelWriter pixelWriter = image1.getPixelWriter();

            // Read the image data
            d = new int[height][width];
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    d[row][col] = scan.nextInt();
                    int gray = (d[row][col] & 0xff) * 255 / maxvalue; // Normalize [0...255]
                    pixelWriter.setArgb(col, row, 0xff000000 | gray << 16 | gray << 8 | gray);
                
                    // System.out.print(data2D[row][col] + " ");
                }
                // System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image1;
    }

    public static void ecrireImage(String fileName){
        try{
            //specify the name of the output..
            FileWriter fstream = new FileWriter(fileName);
            //we create a new BufferedWriter
            BufferedWriter out = new BufferedWriter(fstream);
            //we add the header
            out.write("P2\n# Comment here :) \n"+width+" " +height+"\n255\n");
            //2 loops to read the 2d array
            for(int i = 0 ; i<d.length;i++) {
               for(int j = 0 ; j<d[i].length;j++)
                   //we write in the output the value in the position ij of the array
                   out.write(d[i][j]+" ");
                out.write("\n");
            }
            //we close the bufferedwritter
            out.close();
            }
       catch (Exception e){
            System.err.println("Error : " + e.getMessage());
        }
    }

    public static void transLineare(int x1, int y1, int x2, int y2){
        double a1 = y1 / x1;
        double a2 = (double)(y2 - y1) /(x2 - x1) ;
        double b2 = y2 - a2 * x2 ;
        double a3 = (double)(255 - y2) / (255 - x2) ;
        double b3 = 255 - a3 * 255 ;
        
        for(int i=0 ; i<d.length ; i++){
            for(int j=0; j<d[i].length ; j++) {
                if(d[i][j] <= x1) {
                    d[i][j] =(int) ( a1 * d[i][j] );
                }
                else if(d[i][j] <= x2) {
                    d[i][j] =(int) ( a2 * d[i][j] + b2 );
                }
                else{
                    d[i][j] = (int) (a3 * d[i][j] + b3) ;
                }
            }
        }
    }

    public static void filtreMoy(int size) {
        int output [][] = new int[height][width];

        for (int v=1; v<height; v++) {
            for (int u=1; u<width; u++) {
                int sum = 0;
                int f = size/2;
                for (int j=-f; j<=f; j++) {
                    for (int i=-f; i<=f; i++) {
                        if((u+(j)>=0 && v+(i)>=0 && u+(j)<width && v+(i)<height)){
                            int p = d[v+(i)][u+(j)];
                            sum = sum + p;
                        }
                    }
                }

                int q = (int) (sum /(size*size));
                output[v][u] = q;
            }
        }
        d=output ;
    }

    public static void bruit() {
        for(int i=0 ; i<d.length ; i++){
            for(int j=0; j<d[i].length ; j++) {
                Random rand = new Random();
                int rand_int1 = rand.nextInt(21);
                if(rand_int1 == 0) {
                    d[i][j] = 0;
                }
                else if(rand_int1 == 20 ) {
                    d[i][j] = 255 ; 
                }
            }
        }
    }

    public static void filtreMedian(int tailleFiltre){
        int tab[];
        int [][] output = new int[height][width];

        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                tab = new int[tailleFiltre * tailleFiltre];
                int count = 0;
                for(int r = y - (tailleFiltre / 2); r <= y + (tailleFiltre / 2); r++){
                    for(int c = x - (tailleFiltre / 2); c <= x + (tailleFiltre / 2); c++){
                        if(r < 0 || r >= height || c < 0 || c >= width){
                            continue;
                        }else{
                            tab[count] = d[r][c];
                            count++;
                        }
                    }
                }
                
                Arrays.sort(tab);
                output[y][x] = tab[count/2];
            }
        }
        d=output;
    }

    public static void filtrePasseHaut() {
        int [][] filtre = {
            {-1 , -1 , -1 },
            {-1 , 9 ,-1},
            {-1 , -1 , -1}
        };
        int output [][] = new int[height][width];

        for (int v=1; v<height; v++) {
            for (int u=1; u<width; u++) {
                
                int sum = 0;
                for (int j=-1; j<=1; j++) {
                    for (int i=-1; i<=1; i++) {
                        if((u+(j)>=0 && v+(i)>=0 && u+(j)<width && v+(i)<height)){
                            int p = d[v+(i)][u+(j)];
                            sum = sum + (filtre[1+i][1+j]) * p;
                        }
                    }
                }
                int val = (sum < 0) ? 0 : sum ;
                val = (val > 255) ? 255 : val ;


                output[v][u] = val;
            }
        }
        d=output;
    }

    public static double rapport( int[][] base) {
        double r = 0 ;
        long sum=0;
        for(int i=0 ; i<base.length ; i++){
            for(int j=0; j<base[i].length ; j++) {
                sum += base[i][j];
            }
        }
        double moy = sum /(base.length *base[0].length) ;
        double s = 0 ;
        double b=0 ;
        for(int i=0 ; i<base.length ; i++) {
            for(int j=0 ; j<base[0].length ; j++) {
                s += (base[i][j] - moy)*(base[i][j] - moy) ;
                b += (d[i][j] - base[i][j])*(d[i][j] - base[i][j]) ;
            }
        }
        r = Math.sqrt((double) s / b) ;
        return r;
    }
    public static int[][] getData(File file){
        int [][] d2 = new int[height][width];
        try {
            Scanner scan = new Scanner(new FileInputStream(file.toPath().toString()));
            
            // Discard the magic number
            scan.nextLine();
            // Discard the comment line
            scan.nextLine();
            // Read pic width, height and max value
            width = scan.nextInt();
            height = scan.nextInt();
            int maxvalue = scan.nextInt();


            // Read the image data
            
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    d2[row][col] = scan.nextInt();
                   
                    // System.out.print(data2D[row][col] + " ");
                }
                // System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d2;
    }

    public static int [] histogramGrid() {
        int [][] tab = d ;
        int[] tabPixel=new int[256];
        for (int j = 0; j <= 255; j++){
            tabPixel[j]=0;
        } 
        for  (int j = 0; j < tab.length; j++){
            for  (int i = 0; i < tab[j].length; i++){
               tabPixel[tab[j][i]]++ ;
            }
        }
        return tabPixel ;
    }
    public static double[] histogramCumulatif() {
        int [][] tab = d ;
        int width = tab.length ;
        int height = tab[0].length;
        int[] tabPixel=new int[256];
        for (int j = 0; j <= 255; j++){
            tabPixel[j]=0;
        } 
        for  (int j = 0; j <width; j++){
            for  (int i = 0; i < height; i++){
               tabPixel[tab[j][i]]++ ;
            }
        }
        double[] tabCumul=new double[256];
        tabCumul[0]=tabPixel[0];
        for  (int j = 1; j < tabPixel.length; j++){
            tabCumul[j]=tabCumul[j-1]+tabPixel[j];
        }
        for (int j = 0; j <= 255; j++){
            tabCumul[j]=tabCumul[j]/(height*width);
        }
        return tabCumul;
    }

    public static void histogrammeEgalise() {
        int[] h = histogramGrid();
        double [] tabcumul = histogramCumulatif() ;
        int total = d.length * d[0].length;
        double [] a = new double[tabcumul.length];
        for(int i=0; i<tabcumul.length ; i++){
            a[i] =  (tabcumul.length - 1)*tabcumul[i];
        }
        int hp = total / tabcumul.length;
        int [] n1 = new int[tabcumul.length];

        for(int i=0 ; i<n1.length ; i++) {
            n1[i] = (int) a[i];
        }
        int []heg = new int[tabcumul.length] ;
        for(int i=0 ; i<tabcumul.length ; i++) {
            int val = 0;
            for(int j=0 ; j<n1.length ; j++) {
                if(n1[j] == i) 
                    val += h[j];
            }
            heg[i] = val ;
        }
        for(int i= 0 ; i<d.length ; i++) {
            for(int j= 0 ; j<d[0].length ; j++) {
                int pixel = d[i][j] ;
                pixel = n1[pixel] ;
                d[i][j] = pixel ;
            }
        }
    }
}
