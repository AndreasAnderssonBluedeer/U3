package Common;

/**
 * This class holds information about a RGB color. Each separated value, special sorting, Total value as int etc.
 * Created by Andreas Andersson & David Isberg on 2016-01-15.
 */
public class RGB implements Comparable{

    private int red,green,blue;

    /**
     * Sets Red value
     * @param red -value
     */
    public void setRed(int red){
        this.red=red;
    }
    /**
     * Sets Green value
     * @param green -value
     */
    public void setGreen(int green){
        this.green=green;
    }
    /**
     * Sets Blue value
     * @param blue -value
     */
    public void setBlue(int blue){
        this.blue=blue;
    }

    /**
     * Returns Red value.
     * @return int
     */
    public int getRed(){
        return red;
    }
    /**
     * Returns Green value.
     * @return int
     */
    public int getGreen(){
        return green;
    }
    /**
     * Returns Blue value.
     * @return int
     */
    public int getBlue(){
        return blue;
    }

    /**
     * Returns a string with all color values. Ex: 192192192
     * @return String
     */
    public String getRGBString(){
        String r,g,b;
        r=red+"";
        g=gbString(green);
        b=gbString(blue);
        return r+g+b;
    }

    /**
     * Returns all color values as an big int. + 1 000 000 000 to get all zeros.
     * @return int
     */
    public int getValue(){
        int x;
        x=Integer.parseInt(getRGBString());
        return x+ 1000000000;
    }

    /**
     * Returns the string of Green or Blue.
     * @param x -value
     * @return String
     */
    private String gbString(int x){
        String str=x+"";
        if (x<100){
            str="0"+x;
        }
        if (x<10){
            str="00"+x;
        }
        return str;
    }

    @Override
    /**
     * Sorts Descending with special sorting for numbers with 10 positions.
     */
    public int compareTo(Object o) {
        RGB comp=(RGB)o;

        int compare=comp.getValue();
        int current=getValue();

        //Compare
        if(current>compare){
            return -1;
        }
        else if (current<compare){
            return 1;
        }

        return 0;
    }
}
