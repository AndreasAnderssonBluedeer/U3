package Decompress;

import Common.ColorPalette;
import Common.RGB;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.*;
import java.util.ArrayList;

/**
 * Created by Andreas on 2016-01-15.
 */
public class Decompress {

    private int width,height;
    private RGB[] colorPalette;
    private WritableRaster raster;
    private ArrayList<Integer> readArray=new ArrayList<Integer>();
    private String filename;
    private BufferedImage img;

    public Decompress(File file){
        filename=file.getPath()+".png";
       // colorPalette=new ColorPalette().getRGBArray();
        colorPalette=new RGB[256];

    //First read 2x 32 bits = 2x 4 bytes for width/Height.
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream(file));

            width=dis.readInt();    //Read Image Width
            height=dis.readInt();   //Reade Image Height

            //Create new image and get raster
            img=new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
            raster=img.getRaster();
            System.out.println("W:"+width+" H:"+height);
            //Read Palette (size=256)
            System.out.println("Reading Color palette");
            for (int i=0;i<256;i++){
                int palette=dis.readUnsignedByte();
                int colorValue=dis.readInt();
                //Create new RGB
                colorPalette[palette]=new RGB();
                //Split color value int to RGB values.
                String str=String.valueOf(colorValue);
                colorPalette[palette].setRed(Integer.parseInt(str.substring(1,4)));
                colorPalette[palette].setGreen(Integer.parseInt(str.substring(4, 7)));
                colorPalette[palette].setBlue(Integer.parseInt(str.substring(7, 10)));

            }

        //Read pixel values
            System.out.println("Reading pixel values");
        int length=height*width;
            //Add all pixel values from the file.
            for (int i=0;i<length;i++){
                //Range -128->127
                int value=dis.readUnsignedByte();
                readArray.add(value);

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        printPalette();
        initRaster();
        saveFile();
    }
    private void printPalette(){
        for (int i=0;i<colorPalette.length;i++){
            System.out.println("I:"+i+"    "+colorPalette[i].getRed()+","+
                    colorPalette[i].getGreen()+","+colorPalette[i].getBlue());
        }
    }
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
    private void saveFile(){
        try {
            ImageIO.write(img, "PNG", new File(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("File saved as"+filename+"- Decompression Complete");
    }
}
