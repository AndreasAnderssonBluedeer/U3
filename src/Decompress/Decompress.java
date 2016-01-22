package Decompress;

import Common.BinaryConverter;
import Common.ColorPalette;
import Common.RGB;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.*;
import java.util.ArrayList;

/**
 * Decompression class. Reads two ints for width and height, then 3 bytes for 2 color values.
 * FILE FORMAT ".BIMZ"
 * Created by Andreas on 2016-01-15.
 */
public class Decompress {

    private int width,height;
    private RGB[] colorPalette;
    private WritableRaster raster;
    private ArrayList<Integer> readArray=new ArrayList<Integer>();
    private String filename;
    private BufferedImage img;

    /**
     * Constructor that recieves an file to decompress.
     * @param file
     */
    public Decompress(File file){
        filename=file.getPath()+".png";
        colorPalette=new ColorPalette().getRGBArray();
        BinaryConverter bc=new BinaryConverter();

    //First read 2x 32 bits = 2x 4 bytes for width/Height.
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream(file));

            width=dis.readInt();    //Read Image Width
            height=dis.readInt();   //Reade Image Height

            //Create new image and get raster
            img=new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
            raster=img.getRaster();
            System.out.println("W:"+width+" H:"+height);

        //Read pixel values
            System.out.println("Reading pixel values");
        int length=height*width;

            //Add all pixel values from the file.
            for (int i=0;i<length;i++){
                //Range -128->127
                int value0=dis.readUnsignedByte();
                int value1=dis.readUnsignedByte();
                int value2=dis.readUnsignedByte();

                int[] x=bc.toInt(value2,value0,Integer.toBinaryString(value1));
                readArray.add(x[1]);
                readArray.add(x[0]);

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Read:"+readArray.get(0));
        printPalette();
        initRaster();
        saveFile();
    }

    /**
     * Prints the ColorPalette.
     */
    private void printPalette(){
        for (int i=0;i<colorPalette.length;i++){
            System.out.println("I:"+i+"    "+colorPalette[i].getRed()+","+
                    colorPalette[i].getGreen()+","+colorPalette[i].getBlue());
        }
    }

    /**
     * Initializes the Image Raster.
     */
    private void initRaster(){
        System.out.println("Init Raster");
        int i=0;
        for (int row=0;row<height;row++){
            for (int col=0;col<width;col++){

                RGB temp=colorPalette[readArray.get(i++)];
                raster.setSample(col,row,0,temp.getRed());
                raster.setSample(col,row,1,temp.getGreen());
                raster.setSample(col,row,2,temp.getBlue());
            }
        }
    }

    /**
     * Save the file to the same directory as input file.
     */
    private void saveFile(){
        try {
            ImageIO.write(img, "PNG", new File(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("File saved as"+filename+"- Decompression Complete");
    }
}
