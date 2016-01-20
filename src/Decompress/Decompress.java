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
        colorPalette=new ColorPalette().getRGBArray();

    //First read 2x 32 bits = 2x 4 bytes for width/Height.
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream(file));
            String widthStr="",heightStr="";
            width=dis.readInt();
            height=dis.readInt();
            img=new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
            raster=img.getRaster();
            System.out.println("W:"+width+" H:"+height);

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
        initRaster();
        saveFile();
    }
    private void initRaster(){
        System.out.println(readArray.get(10));
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
    }
}
