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
import java.util.Arrays;
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
    private Hashtable redMap=new Hashtable(8192);   //( max value for 12 bits)
    private Hashtable greenMap=new Hashtable(8192);
    private Hashtable blueMap=new Hashtable(8192);

    private Hashtable colorMap,mostCommonColors; // (number of possible colors)

    private int printPos=0;
    private String filepath;

    public Compress(BufferedImage img,String filepath){
        inputRaster=img.getRaster();
        this.filepath=filepath;

        //colorPalette=new ColorPalette().getRGBArray();

        colorArray=new RGB[inputRaster.getHeight()][inputRaster.getWidth()];

        int size=inputRaster.getHeight()*inputRaster.getWidth();

        colorMap=new Hashtable(size);   //All colors with value of used color
      //  mostCommonColors=new Hashtable(size);   //Colors sorted by number of usage, Descending.

        colorToPrint=new int[size];
        System.out.println("Height:"+inputRaster.getHeight()+" Width:"+inputRaster.getWidth());


        initColorArray();
        mostCommonRGBs();
        adjustColors();
        initHashMaps();
        prepareForPrint();
        saveCompressedFile();
    }

    /**
     * +1 to color value usage, adds color to hashTable if it doesn't exist.
     * @param rgb
     */
    private void findImageColors(RGB rgb){
        //Try to add it to color hashtable
        colorMap.put(rgb.getValue(),rgb);
        //Update nbr of pixels that use this color.
        RGB temp=(RGB) colorMap.get(rgb.getValue());
        temp.addNbrofPixels();
    }
    private void mostCommonRGBs(){
        System.out.println("Most Common RGBs");
        colorPalette=new RGB[256];
        int palettePos=0;
        Object[] temp=colorMap.getKeys();  //Hämta alla nycklar
        int[] array=new int[temp.length];
        for(int x=0;x<temp.length;x++){
            array[x]=(int)temp[x];
        }

        int [] usage=new int[array.length];     //Lagra alla "Usage värden"

        for (int i=0;i<array.length;i++){
            RGB rgb=(RGB) colorMap.get(array[i]);
            usage[i]=rgb.getNbrOfPixels(); //Placera alla usage värden
        }
        Arrays.sort(usage); //Sortera i växande ordning

        for (int k=usage.length-1;k>usage.length-256;k--){  //Från det mest använda värdet till minsta möjliga i paletten (256 platser)

            for (int z=0;z<array.length;z++){
                RGB rgb=(RGB) colorMap.get(array[z]);

                if(colorPalette[0]==null){  //If it's the first color-> add it.
                    colorPalette[palettePos++]=rgb;
                }
                else if(usage[k]==rgb.getNbrOfPixels() && rgb.getValue()!=colorPalette[palettePos-1].getValue()){ //Kan ge dubbla värden...
                   colorPalette[palettePos++]=rgb;  //lägg till färg i paletten.
                    z=array.length; //Bryt loopen
                }
            }
        }
        //Paletten är färdig.

    }

    /**
     * Spread color values to get different hues if they're close to each other.
     */
    private void adjustColors(){
        System.out.println("Adjust Colors.");
        int adjust=5;
        for (int count=0;count<10;count++) {
            for (int i = 0; i < colorPalette.length - 1; i++) {
                for (int k = 0; k < colorPalette.length; k++) {
                    int r1, r2, g1, g2, b1, b2, diff;
                    r1 = colorPalette[i].getRed();
                    r2 = colorPalette[k].getRed();
                    g1 = colorPalette[i].getGreen();
                    g2 = colorPalette[k].getGreen();
                    b1 = colorPalette[i].getBlue();
                    b2 = colorPalette[k].getBlue();

                    diff = Math.abs(r1 - r2);
                    if (diff < adjust && r2 < 255 - adjust) {
                        colorPalette[k].setRed(r2 + adjust);
                    }
                    diff = Math.abs(g1 - g2);
                    if (diff < adjust && g2 < 255 - adjust) {
                        colorPalette[k].setGreen(g2 + adjust);
                    }
                    diff = Math.abs(b1 - b2);
                    if (diff < adjust && b2 < 255 - adjust) {
                        colorPalette[k].setBlue(b2 + adjust);
                    }

                }
            }
        }
    }
    private void initHashMaps(){
        System.out.println("Init Hashmaps");
        //Loop through all colors
        for (int i=0;i<colorPalette.length;i++){
            //Loop through to find all with the same Color values.
            ArrayList<RGB> red=new ArrayList<>();
            ArrayList<RGB> green=new ArrayList<>();
            ArrayList<RGB> blue=new ArrayList<>();
            System.out.println(colorPalette[i].getRed()+","+colorPalette[i].getGreen()+","+colorPalette[i].getBlue());
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
        System.out.println("Init color array");
        for (int row=0;row<colorArray.length;row++){
            for (int col=0;col<colorArray[row].length;col++){

                colorArray[row][col]=new RGB(); //init

                colorArray[row][col].setRed(inputRaster.getSample(col, row, 0)); //Red
                colorArray[row][col].setGreen(inputRaster.getSample(col,row,1)); //Green
                colorArray[row][col].setBlue(inputRaster.getSample(col,row,2));  //Blue
                findImageColors(colorArray[row][col]);

            }
        }

//        String.format("%8s", Integer.toBinaryString(b)).replace(' ', '0')

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

        System.out.println("Saving File");
        String filename= JOptionPane.showInputDialog(null,"Skriv in önskat filnamn.");
        try {
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(filepath+filename+".bimz"));

            dos.writeInt(colorArray[0].length);    //Write Width as int    32bits.
            dos.writeInt(colorArray.length);       //Write Height as int   32bits.

            //Skriv färgtabell-> Byte=position Palette följt av Int=GetValue från RGB, t.ex 1 092 092 092
            for (int i=0;i<colorPalette.length;i++){
                dos.writeByte(i);
                dos.writeInt(colorPalette[i].getValue());
            }

            //Slutligen-> skriv pixlarnas värden kopplat till palette positionerna.
            for (int i=0;i<colorToPrint.length;i++){
                    dos.writeByte(colorToPrint[i]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("File saved- Compression Complete");
    }


}
