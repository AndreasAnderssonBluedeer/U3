package Compress;

import Common.BinaryConverter;
import Common.ColorPalette;
import Common.RGB;
import Hashtable.Hashtable;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.*;
import java.util.ArrayList;

/**
 * Created by Andreas on 2016-01-15.
 */
public class Compress {

    private WritableRaster inputRaster;
    private RGB[][] colorArray;
    private RGB[] customPalette;

    private int[] colorToPrint;
    //Hasha till RGB med Nyckel av Röd resp. Grön och Blå. Var position innehåller de RGB objekten som innehåller samma
    //Föregående Map nyckel.
    private Hashtable redMap=new Hashtable(3375);   //Antal färger
    private Hashtable greenMap=new Hashtable(3375);
    private Hashtable blueMap=new Hashtable(3375);



    private int printPos=0;
    private String filepath;

    public Compress(){

    }
    public Compress(BufferedImage img,String filepath){
        inputRaster=img.getRaster();
        this.filepath=filepath;

        colorArray=new RGB[inputRaster.getHeight()][inputRaster.getWidth()];
        int size=inputRaster.getHeight()*inputRaster.getWidth();
        customPalette=new ColorPalette().getRGBArray();

        colorToPrint=new int[size];
        System.out.println("Height:"+inputRaster.getHeight()+" Width:"+inputRaster.getWidth());

        initColorArray();
        initHashMaps();
        prepareForPrint();
        saveCompressedFile();
    }





    private void initHashMaps(){
        System.out.println("Init Hashmaps");
        //Loop through all colors
        for (int i=0;i< customPalette.length;i++){
            //Loop through to find all with the same Color values.
            ArrayList<RGB> red=new ArrayList<>();
            ArrayList<RGB> green=new ArrayList<>();
            ArrayList<RGB> blue=new ArrayList<>();
            System.out.println(customPalette[i].getRed()+","+ customPalette[i].getGreen()+","+ customPalette[i].getBlue());
        for (int k=0;k< customPalette.length;k++) {
            //If current position = color value-> add to rgb list.
            // (All colors with value x for r,g or b will be connected to a list of rgb object.

            if(i== customPalette[k].getRed()){
                red.add(customPalette[k]);
            }
            if(i== customPalette[k].getGreen()){
                green.add(customPalette[k]);
            }
            if(i== customPalette[k].getBlue()){
                blue.add(customPalette[k]);
            }

        }
            redMap.put(i,red);
            greenMap.put(i,green);
            blueMap.put(i,blue);
        }
    }

    //Construct an array with RGB for each pixel.
    private void initColorArray(){
        System.out.println("Init color array");
        for (int row=0;row<colorArray.length;row++){
            for (int col=0;col<colorArray[row].length;col++){

                colorArray[row][col]=new RGB(); //init

                colorArray[row][col].setRed(inputRaster.getSample(col, row, 0)); //Red
                colorArray[row][col].setGreen(inputRaster.getSample(col,row,1)); //Green
                colorArray[row][col].setBlue(inputRaster.getSample(col,row,2));  //Blue

            }
        }

    }
    private void prepareForPrint(){
        System.out.println("Prepare for print");
        for ( int row=0;row<colorArray.length;row++){
            for(int col=0;col<colorArray[row].length;col++){
                //Find colors!
               addPixel(colorArray[row][col]);

            }
        }
        System.out.println(printPos);
    }
    private void addPixel(RGB pixel){
        //Loopa igenom färglistorna tills en färg hittas
            int red=pixel.getRed();
            int green=pixel.getGreen();
            int blue=pixel.getBlue();

            int bestPixel=0,bestResult=Integer.MAX_VALUE;

        for (int i=0;i< customPalette.length;i++){
            int res;
            int r,g,b;

            r=Math.abs(red - customPalette[i].getRed());
            g=Math.abs(green - customPalette[i].getGreen());
            b=Math.abs(blue - customPalette[i].getBlue());

            res=r+g+b;
            bestResult=Math.min(bestResult,res);
            if(bestResult==res){
                bestPixel=i;
            }

        }
        colorToPrint[printPos++]=bestPixel;

    }
    private void saveCompressedFile(){
        BinaryConverter bc=new BinaryConverter();
        System.out.println("Saving File");
        String filename= JOptionPane.showInputDialog(null,"Skriv in önskat filnamn.");
        try {
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(filepath+filename+".bimz"));

            dos.writeInt(colorArray[0].length);    //Write Width as int    32bits.
            dos.writeInt(colorArray.length);       //Write Height as int   32bits.

            //Slutligen-> skriv pixlarnas värden kopplat till palette positionerna.
            for (int i=1;i<colorToPrint.length;i+=2){
              int[] arr=  bc.toByte(colorToPrint[i-1],colorToPrint[i]);
                dos.writeByte(arr[0]);  //pixel 1
                dos.writeByte(arr[1]);  //High values for both pixel 1 and 2
                dos.writeByte(arr[2]);  //pixel 2


            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("File saved- Compression Complete");
    }
    public static void main(String [] args){
        new Compress();
    }

}
