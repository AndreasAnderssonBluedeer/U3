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
    private String [][] rgbArray;
    private int[] sortedColors;
    private ArrayList<Integer> colors =new ArrayList<>();
    public Compress(BufferedImage img){
        inputRaster=img.getRaster();

        colorArray=new RGB[inputRaster.getHeight()][inputRaster.getWidth()];
        rgbArray=new String[inputRaster.getHeight()][inputRaster.getWidth()];
        int size=inputRaster.getHeight()*inputRaster.getWidth();
        sortedColors=new int[size];

        System.out.println("Height:"+inputRaster.getHeight()+" Width:"+inputRaster.getWidth());
        initColorArray();

    }

    //Construct an array with RGB for each pixel.
    private void initColorArray(){
        int i=0;
        for (int row=0;row<colorArray.length;row++){
            for (int col=0;col<colorArray[row].length;col++){
                colorArray[row][col]=new RGB(); //init
                colorArray[row][col].setRed(inputRaster.getSample(col, row, 0)); //Red
                colorArray[row][col].setGreen(inputRaster.getSample(col,row,1)); //Green
                colorArray[row][col].setBlue(inputRaster.getSample(col,row,2));  //Blue
                rgbArray[row][col]=inputRaster.getSample(col, row, 0)+""+inputRaster.getSample(col, row, 1)
                        +""+inputRaster.getSample(col, row, 2);
                sortedColors[i]=Integer.parseInt(rgbArray[row][col]);
                i++;
//                System.out.println("Check exist");

//                for(int i=0;i<colors.size();i++){
////
//                if(colors.get(i)==check) {
//////                    System.out.println("Existed");
//                        exist = true;
//                }
//                }
//                if (!exist){
////                    System.out.println("Didn't exist, added.");
//                    colors.add(check);
//
//                }
            }
        }
        Arrays.sort(sortedColors);

//        String.format("%8s", Integer.toBinaryString(b)).replace(' ', '0')
// för vart färg värde o sen expanderar genom att läsa 24/8 bits per färg värde
        int x=colorArray[0][0].getRed();
        System.out.println("Red int/bit:"+x+"/"+String.format("%8s", Integer.toBinaryString(x)).replace(' ', '0')+
                " Green int/bit:"+colorArray[0][0].getGreen()+"/"+Integer.toBinaryString(colorArray[0][0].getGreen())
                +" Blue int/bit:"+colorArray[0][0].getBlue()+"/"+Integer.toBinaryString(colorArray[0][0].getBlue()));
        createColorCollection();
       // saveCompressedFile();
    }
    private void createColorCollection(){
        colors.add(sortedColors[0]);    //Add the first color there is to compare with.
        //Add only the colors that isn't added already.
        for (int i=1;i<sortedColors.length;i++){
            if (sortedColors[i]!=sortedColors[i-1]){
                colors.add(sortedColors[i]);
            }
        }
        System.out.println("colors size:"+colors.size()+" Low color:"+sortedColors[0]
                +" High color:"+sortedColors[sortedColors.length-1]);
    }
    private void saveCompressedFile(){
        String filename= JOptionPane.showInputDialog(null,"Skriv in önskat filnamn.");
        try {
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(filename+".bimz"));

            dos.writeInt(colorArray[0].length);    //Write Width as int    32bits.
            dos.writeInt(colorArray.length);       //Write Height as int   32bits.

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
//                    System.out.println("Red:"+pixelvalues[0]+" Green:"+pixelvalues[1]+" Blue:"+pixelvalues[2]);
                    //Writeboolean?
                    dos.writeUTF(",");
                    dos.writeUTF(",");
                    dos.writeUTF(",");
//                    dos.writeByte(pixelvalues[0]);
//                    dos.writeByte(pixelvalues[1]);
//                    dos.writeByte(pixelvalues[2]);

                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
