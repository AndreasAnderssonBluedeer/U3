package Common;

import java.awt.*;
import java.util.Arrays;

/**
 * Created by Andreas on 2016-01-19.
 */
public class ColorPalette {

    String[] hexColors;
    RGB[] rgbColors=new RGB[256];

    public ColorPalette(){
        hexColors=new HexColorStringArray().getArray();
        initRGB();
    }
    private void initRGB(){
        for (int i=0;i<hexColors.length;i++){
            //java.awt.Color[r=255,g=204,b=238]
            int r,g,b;
            rgbColors[i]=new RGB();
            String str=(Color.decode(hexColors[i]).toString());
            //Red Value
            str=str.substring(str.indexOf("=")+1);
            r=Integer.parseInt(str.substring(0,str.indexOf(",")));
            //Green Value
            str=str.substring(str.indexOf("=")+1);
            g=Integer.parseInt(str.substring(0,str.indexOf(",")));
            //Blue Value
            str=str.substring(str.indexOf("=")+1);
            b=Integer.parseInt(str.substring(0,str.indexOf("]")));
            //Set RGB colors.
            rgbColors[i].setRed(r);
            rgbColors[i].setGreen(g);
            rgbColors[i].setBlue(b);

        }

       Arrays.sort(rgbColors);

      //  for (int i=0;i<rgbColors.length;i++){
     //       System.out.println(rgbColors[i].getValue());
      //  }
    }
    public RGB[] getRGBArray(){
        return rgbColors;
    }


    public static void main(String [] args){
        new ColorPalette();
      //  System.out.println(Color.decode("#FFCCEE"));
    }

}
