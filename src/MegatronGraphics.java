import java.awt.image.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import javax.imageio.*;
import javax.swing.*;

public class MegatronGraphics {
    /** Magic start string to signify Megatron file format. */
    final static byte[] magic = "mEgaMADNZ!".getBytes(StandardCharsets.US_ASCII);

    public final static class InvalidMegatronFileException extends IOException { }
    
    public static void write(BufferedImage img, String fnam) throws IOException {
        int width  = img.getWidth();
        int height = img.getHeight();
        int[] pxl = new int[3];
        Raster imgr  = img.getRaster();
        OutputStream out = new FileOutputStream(fnam);
        out.write(magic);
        write4bytes(width, out);
        write4bytes(height, out);
        int max=pxl[0];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                imgr.getPixel(i, j, pxl);
                out.write(pxl[0]);
                out.write(pxl[1]);
                out.write(pxl[2]);
                max=Math.max(pxl[0],max);
                max=Math.max(pxl[1],max);
                max=Math.max(pxl[2],max);
            }
        }
        Integer i=max;
        System.out.println(i.toBinaryString(i));
        out.close();
    }
    
    public static BufferedImage read(String fnam) throws IOException {
        InputStream in = new FileInputStream(fnam);

        // Check magic value.
        for (int i = 0; i < magic.length; i++) {
            if (in.read() != magic[i]) { throw new InvalidMegatronFileException(); }
        }
        int width  = read4bytes(in);
        int height = read4bytes(in);
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        byte[] pxlBytes = new byte[3];
        int[] pxl = new int[3];
        WritableRaster imgr  = img.getRaster();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (in.read(pxlBytes) != 3) { throw new EOFException(); }
                pxl[0] = pxlBytes[0];
                pxl[1] = pxlBytes[1];
                pxl[2] = pxlBytes[2];
                imgr.setPixel(i, j, pxl);
            }
        }
        in.close();
        return img;
    }
      
    /** Writes an int as 4 bytes, big endian. */
    private static void write4bytes(int v, OutputStream out) throws IOException {
        System.out.println(v>>>3*8);
        System.out.println(v>>>2*8 & 255);
        System.out.println(v>>>1*8 & 255);
        System.out.println(v       & 255);

        out.write(v>>>3*8);
        out.write(v>>>2*8 & 255);
        out.write(v>>>1*8 & 255);
        out.write(v       & 255);

    }

    /** Reads an int as 4 bytes, big endian. */
    private static int read4bytes(InputStream in) throws IOException {
        int b, v;
        b = in.read(); if (b < 0) { throw new EOFException(); }
        v = b<<3*8;
        b = in.read(); if (b < 0) { throw new EOFException(); }
        v |= b<<2*8;
        b = in.read(); if (b < 0) { throw new EOFException(); }
        v |= b<<1*8;
        b = in.read(); if (b < 0) { throw new EOFException(); }
        v |= b;
        return v;
    }
   
    public static void main(String[] args) throws IOException {
        int choice=Integer.parseInt(JOptionPane.showInputDialog(null,"1- Skapa MTG från Bild\n2- Skapa PNG från MTG"));
        JFileChooser jc=new JFileChooser();
        jc.showDialog(null,"Välj");
        //Edit filename.
        String filename=jc.getSelectedFile().getPath();
        filename=filename.substring(0,filename.length()-4);

        switch (choice){
            case 1: //Create MTG from Image-file
                BufferedImage img  = ImageIO.read(new File(jc.getSelectedFile().getPath()));
                filename+=".mtg";
                MegatronGraphics.write(img, filename);
                break;
            case 2: //Create PNG from MTG
                BufferedImage img2 = MegatronGraphics.read(jc.getSelectedFile().getPath());
                filename+=".png";
                ImageIO.write(img2, "PNG", new File(filename));
                break;
        }
    }
}


