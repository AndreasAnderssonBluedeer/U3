import Compress.Compress;
import Decompress.Decompress;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Andreas on 2016-01-15.
 */
public class Start {

    public Start() {
        Integer i=Integer.MAX_VALUE;
        System.out.println(i.toBinaryString(i));
        int choice = Integer.parseInt(JOptionPane.showInputDialog(null, "1- Komprimera Bild\n2- Expandera bild"));
        JFileChooser jc = new JFileChooser();
        jc.showDialog(null, "Välj");

        switch (choice) {
            case 1: //Compress
                try {
                    BufferedImage img=ImageIO.read(jc.getSelectedFile());
                    new Compress(img,jc.getSelectedFile().getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 2: //Decompress
                new Decompress(jc.getSelectedFile());
                break;

        }
    }
    public static void main(String [] args){
        new Start();
    }
}
