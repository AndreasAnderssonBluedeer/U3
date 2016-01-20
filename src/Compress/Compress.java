package Compress;

import Common.ColorPalette;
import Common.RGB;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.*;

/**
 * Created by Andreas on 2016-01-15.
 */
public class Compress {

    private WritableRaster inputRaster;
    private RGB[][] colorArray;
    private RGB[] colorPalette;
    private int[] colorToPrint;

    private int printPos=0;

    public Compress(BufferedImage img){
        inputRaster=img.getRaster();

        colorPalette=new ColorPalette().getRGBArray();

        colorArray=new RGB[inputRaster.getHeight()][inputRaster.getWidth()];

        int size=inputRaster.getHeight()*inputRaster.getWidth();
        colorToPrint=new int[size];
        System.out.println("Height:"+inputRaster.getHeight()+" Width:"+inputRaster.getWidth());
        initColorArray();
    }

    //Construct an array with RGB for each pixel.
    private void initColorArray(){

        for (int row=0;row<colorArray.length;row++){
            for (int col=0;col<colorArray[row].length;col++){

                colorArray[row][col]=new RGB(); //init

                colorArray[row][col].setRed(inputRaster.getSample(col, row, 0)); //Red
                colorArray[row][col].setGreen(inputRaster.getSample(col,row,1)); //Green
                colorArray[row][col].setBlue(inputRaster.getSample(col,row,2));  //Blue

            }
        }

//        String.format("%8s", Integer.toBinaryString(b)).replace(' ', '0')

        prepareForPrint();
        saveCompressedFile();
    }
    private void prepareForPrint(){
        for ( int row=0;row<colorArray.length;row++){
            for(int col=0;col<colorArray[row].length;col++){
                //Do we have identical pixels?
                int value=colorArray[row][col].getValue();
                int i=0;
                    while(value < colorPalette[i++].getValue() && i<colorPalette.length){
                    }
                //Colorpalette =256 colors 0-255, i= maximum 255
                if (value==colorPalette[i].getValue()){
                    colorToPrint[printPos++]=i;
                }
                else{
                    int high,low;
                    if(i<255){
                        high=colorPalette[i+1].getValue();
                    }
                    else {
                        high=colorPalette[i].getValue();
                    }
                    if(i>0){
                       low=colorPalette[i-1].getValue();
                    }
                    else {
                        low=colorPalette[i].getValue();
                    }
                    //Find the closest match
                    low=value-low;
                    high=high-value;
                    int res=Math.min(low,high);
                    //Low value was the closest one.
                    if (res==low){
                        colorToPrint[printPos++]=i-1;
                    }
                    //High
                    else{
                        colorToPrint[printPos++]=i+1;
                    }
                }

            }
        }
        System.out.println(printPos);
    }
    private void saveCompressedFile(){
        System.out.println(colorToPrint[10]);
        String filename= JOptionPane.showInputDialog(null,"Skriv in önskat filnamn.");
        try {
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(filename+".bimz"));

            dos.writeInt(colorArray[0].length);    //Write Width as int    32bits.
            dos.writeInt(colorArray.length);       //Write Height as int   32bits.

            for (int i=0;i<colorToPrint.length;i++){
                    dos.writeByte(colorToPrint[i]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
