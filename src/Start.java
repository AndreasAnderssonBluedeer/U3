import Compress.Compress;

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

        int choice = Integer.parseInt(JOptionPane.showInputDialog(null, "1- Komprimera Bild\n2- Expandera bild"));
        JFileChooser jc = new JFileChooser();
        jc.showDialog(null, "Välj");
        //Edit filename.
        String filename = jc.getSelectedFile().getPath();
        filename = filename.substring(0, filename.length() - 4);

        switch (choice) {
            case 1: //Compress
                try {
                    BufferedImage img=ImageIO.read(jc.getSelectedFile());
                    new Compress(img);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 2: //Decompress

                break;

        }
    }
    public static void main(String [] args){
        new Start();
    }
}
