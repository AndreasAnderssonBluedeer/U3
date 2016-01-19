package Compress;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;

/**
 * Created by Andreas on 2016-01-15.
 */
public class Compress {

    private WritableRaster inputRaster;
    private RGB[][] colorArray;
    private RGB[] rgbToPrint;
    private int rgbToPrintSize=0;

    public Compress(BufferedImage img){
        inputRaster=img.getRaster();

        colorArray=new RGB[inputRaster.getHeight()][inputRaster.getWidth()];
        int size=inputRaster.getHeight()*inputRaster.getWidth();
        rgbToPrint =new  RGB[size] ;
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
        int x=colorArray[0][0].getRed();
        System.out.println("Red int/bit:"+x+"/"+String.format("%8s", Integer.toBinaryString(x)).replace(' ', '0')+
                " Green int/bit:"+colorArray[0][0].getGreen()+"/"+Integer.toBinaryString(colorArray[0][0].getGreen())
                +" Blue int/bit:"+colorArray[0][0].getBlue()+"/"+Integer.toBinaryString(colorArray[0][0].getBlue()));
        prepareForPrint();
        saveCompressedFile();
    }
    private void prepareForPrint(){
        for ( int row=0;row<colorArray.length;row++){
            for(int col=0;col<colorArray[row].length;col++){
                //Do we have identical pixels?
                    boolean equal=true;
                    int pos=col;
                    while(equal && pos < colorArray[row].length-1 && pos-col <16){
                        pos++;  //Begränsa pos till arrayen.
                        equal=equalPixels(colorArray[row][col].getRGBString(),colorArray[row][pos].getRGBString());
                    }
                //Set Pixel iterations
                colorArray[row][col].setIterations(pos-(col-1));
                rgbToPrint[rgbToPrintSize++]=colorArray[row][col];
                if (pos>col){
                    col=pos-1;
                }
            }
        }
        System.out.println(rgbToPrintSize);
    }
    private boolean equalPixels(String current,String next){
        return current.equals(next);
    }
    private void saveCompressedFile(){
        String filename= JOptionPane.showInputDialog(null,"Skriv in önskat filnamn.");
        try {
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(filename+".bimz"));

            dos.writeInt(colorArray[0].length);    //Write Width as int    32bits.
            dos.writeInt(colorArray.length);       //Write Height as int   32bits.

            for (int i=0;i<rgbToPrintSize;i++){
                //Måste ändra till 6x3 bits för färg + 4 bits för att få 22 bits, blir annars 32.
                    byte[] pixelvalues=new byte[4];
                    //Get red value as Byte/8bits Divide with 5 to get a number from 0-51
                    //that represents 0-255 with 5 value jumps
                    Integer value=rgbToPrint[i].getRed()/5;
                    pixelvalues[0]=value.byteValue();
                    //Get Green value as Byte/8bits
                    value=rgbToPrint[i].getGreen()/5;
                    pixelvalues[1]=value.byteValue();
                    //Get Blue value as Byte/8bits
                    value=rgbToPrint[i].getBlue()/5;
                    pixelvalues[2]=value.byteValue();

                    value=rgbToPrint[i].getIterations();
                    pixelvalues[3]=value.byteValue();

                    dos.writeByte(pixelvalues[0]);
                    dos.writeByte(pixelvalues[1]);
                    dos.writeByte(pixelvalues[2]);
                 //   dos.writeByte(pixelvalues[3]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
