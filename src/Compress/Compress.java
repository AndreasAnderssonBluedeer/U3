package Compress;

import Common.ColorPalette;
import Common.RGB;
import com.sun.source.tree.BinaryTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.TreeVisitor;
import lab2AndAnd.Hashtable;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Andreas on 2016-01-15.
 */
public class Compress {

    private WritableRaster inputRaster;
    private RGB[][] colorArray;
    private RGB[] colorPalette;
    private int[] colorToPrint;
    //Hasha till RGB med Nyckel av Röd resp. Grön och Blå. Var position innehåller de RGB objekten som innehåller samma
    //Föregående Map nyckel.
    private Hashtable redMap=new Hashtable(256);
    private Hashtable greenMap=new Hashtable(256);
    private Hashtable blueMap=new Hashtable(256);

    private int printPos=0;

    public Compress(BufferedImage img){
        inputRaster=img.getRaster();

        colorPalette=new ColorPalette().getRGBArray();

        colorArray=new RGB[inputRaster.getHeight()][inputRaster.getWidth()];

        int size=inputRaster.getHeight()*inputRaster.getWidth();
        colorToPrint=new int[size];
        System.out.println("Height:"+inputRaster.getHeight()+" Width:"+inputRaster.getWidth());
        initHashMaps();
        initColorArray();
    }
    private void initHashMaps(){
        //Loop through all colors
        for (int i=0;i<colorPalette.length;i++){
            //Loop through to find all with the same Color values.
            ArrayList<RGB> red=new ArrayList<>();
            ArrayList<RGB> green=new ArrayList<>();
            ArrayList<RGB> blue=new ArrayList<>();

        for (int k=0;k<colorPalette.length;k++) {
            //If current position = color value-> add to rgb list.
            // (All colors with value x for r,g or b will be connected to a list of rgb object.
            if(i==colorPalette[k].getRed()){
                red.add(colorPalette[k]);
            }
            if(i==colorPalette[k].getGreen()){
                green.add(colorPalette[k]);
            }
            if(i==colorPalette[k].getBlue()){
                blue.add(colorPalette[k]);
            }

        }
            redMap.put(i,red);
            greenMap.put(i,green);
            blueMap.put(i,blue);
        }
        System.out.println("Red Size:"+redMap.count()+"Green Size:"+greenMap.count()+"Blue Size:"+blueMap.count());
        //Ex. för att hitta rgb redMap.get(redValue)
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

        for (int i=0;i<colorPalette.length;i++){
            int res;
            int r,g,b;

            r=calculateDifference(red,colorPalette[i].getRed());
            g=calculateDifference(green,colorPalette[i].getGreen());
            b=calculateDifference(blue,colorPalette[i].getBlue());

            res=r+g+b;
            bestResult=Math.min(bestResult,res);
            if(bestResult==res){
                bestPixel=i;
            }

        }
        colorToPrint[printPos++]=bestPixel;

    }
    private int calculateDifference(int x,int y){
        int res=0;
        if(x>y){
            res=x-y;
        }
        //X> or =
        else{
            res=y-x;
        }
        return res;
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
