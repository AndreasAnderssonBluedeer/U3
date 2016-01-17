package Decompress;

import java.io.*;

/**
 * Created by Andreas on 2016-01-15.
 */
public class Decompress {

    private int width,height;
    public Decompress(File file){
    //First read 2x 32 bits = 2x 4 bytes for width/Height.
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream(file));
            String widthStr="",heightStr="";
            width=dis.readInt();
            height=dis.readInt();
            System.out.println("W:"+width+" H:"+height);

            String s=String.valueOf(dis.readByte());
            s+=", "+String.valueOf(dis.readByte());
            s+=", "+String.valueOf(dis.readByte());
            s+=", "+String.valueOf(dis.readByte());
            System.out.println("Str: "+s);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
