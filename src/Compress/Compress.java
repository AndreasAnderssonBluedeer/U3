package Compress;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.*;
import java.util.BitSet;

/**
 * Created by Andreas on 2016-01-15.
 */
public class Compress {

    private WritableRaster inputRaster;
    private RGB[][] colorArray;

    public Compress(BufferedImage img){
        inputRaster=img.getRaster();
        colorArray=new RGB[inputRaster.getHeight()][inputRaster.getWidth()];
        System.out.println(inputRaster.getHeight());
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
// för vart färg värde o sen expanderar genom att läsa 24/8 bits per färg värde
        int x=colorArray[0][0].getRed();
        System.out.println("Red int/bit:"+x+"/"+String.format("%8s", Integer.toBinaryString(x)).replace(' ', '0')+
                " Green int/bit:"+colorArray[0][0].getGreen()+"/"+Integer.toBinaryString(colorArray[0][0].getGreen())
                +" Blue int/bit:"+colorArray[0][0].getBlue()+"/"+Integer.toBinaryString(colorArray[0][0].getBlue()));
        saveCompressedFile();
    }
    private void saveCompressedFile(){
        String filename= JOptionPane.showInputDialog(null,"Skriv in önskat filnamn.");
        try {
            OutputStream bos = new BufferedOutputStream(new FileOutputStream(filename+".bimz"));

            bos.write(colorArray[0].length);    //Write Width as int    32bits.
            bos.write(colorArray.length);       //Write Height as int   32bits.

            for (int row=0;row<colorArray.length;row++){
                for (int col=0;col<colorArray[row].length;col++){
                    byte[] pixelvalues=new byte[3];
                    //Get red value as Byte/8bits
                    Integer value=colorArray[row][col].getRed();
                    byte temp=value.byteValue();
                    pixelvalues[0]=temp;
                    //Get Green value as Byte/8bits
                    value=colorArray[row][col].getGreen();
                    temp=value.byteValue();
                    pixelvalues[1]=temp;
                    //Get Blue value as Byte/8bits
                    value=colorArray[row][col].getBlue();
                    temp=value.byteValue();
                    pixelvalues[1]=temp;
               //     System.out.println("Red:"+pixelvalues[0]+" Green:"+pixelvalues[1]+" Blue:"+pixelvalues[2]);
                    bos.write(temp);

                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
