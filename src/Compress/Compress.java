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
 * Computes the compression of an image file such as PNG etc. Saves the new file as "example".bimz
 * Created by Andreas Andersson & David Isberg on 2016-01-15.
 */
public class Compress {

    private WritableRaster inputRaster; //Raster
    private RGB[][] colorArray; //Pixel array from image.
    private RGB[] customPalette;    //Color palette

    private int[] colorToPrint; //Colors ready to print/save.

    private int printPos=0;
    private String filepath;

    /**
     * Test constructor
     */
    public Compress(){

    }

    /**
     * Constructor, Receives an image and String with filepath to save the new file.
     * @param img -image
     * @param filepath -filepath
     */
    public Compress(BufferedImage img,String filepath){
        inputRaster=img.getRaster();
        this.filepath=filepath;

        //Create Arrays.
        colorArray=new RGB[inputRaster.getHeight()][inputRaster.getWidth()];
        int size=inputRaster.getHeight()*inputRaster.getWidth();
        customPalette=new ColorPalette().getRGBArray();
        colorToPrint=new int[size];

        System.out.println("Height:"+inputRaster.getHeight()+" Width:"+inputRaster.getWidth());

        //Program Sequence
        initColorArray();
        prepareForPrint();
        saveCompressedFile();
    }


    /**
     * Creates an RGB object for each pixel in the picture.
     * Initializes ColorArray.
     */
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

    /**
     * Loops through all pixels to find closest Palette color with addPixel();
     */
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

    /**
     * Receives an pixel color and adds the closest to ColorsToPrint.
     * ColorsToPrint is the array with all pixels/color values to be saved.
     * @param pixel color to find
     */
    private void addPixel(RGB pixel){
            //get values
            int red=pixel.getRed();
            int green=pixel.getGreen();
            int blue=pixel.getBlue();
            //BestPixel = index, Bestresult= min Difference.
            int bestPixel=0,bestResult=Integer.MAX_VALUE;

        //Loop thorugh the palette to find the color with the smallest difference.
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
        //Add color to "Print"
        colorToPrint[printPos++]=bestPixel;

    }

    /**
     * Writes ColorsToPrint to file and executes the compression.
     */
    private void saveCompressedFile(){
        BinaryConverter bc=new BinaryConverter();
        System.out.println("Saving File");
        //Choose filename.
        String filename= JOptionPane.showInputDialog(null,"Skriv in önskat filnamn.");
        try {
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(filepath+filename+".bimz"));

            dos.writeInt(colorArray[0].length);    //Write Width as int    32bits.
            dos.writeInt(colorArray.length);       //Write Height as int   32bits.

            //Write color values, 2 pixels at a time, 1 byte/pixel +1 shared byte for high values.
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

    /**
     * Main method for testing.
     * @param args
     */
    public static void main(String [] args){
        new Compress();
    }

}
