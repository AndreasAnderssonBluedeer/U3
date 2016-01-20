package Common;

/**
 * Created by Andreas on 2016-01-15.
 */
public class RGB implements Comparable{

    private int red,green,blue,iterations;


    public void setIterations(int iterations){
        this.iterations=iterations;
    }
    public void setRed(int red){
        this.red=red;
    }
    public void setGreen(int green){
        this.green=green;
    }
    public void setBlue(int blue){
        this.blue=blue;
    }
    public int getRed(){
        return red;
    }
    public int getGreen(){
        return green;
    }
    public int getBlue(){
        return blue;
    }
    public String getRGBString(){
        String r,g,b;
        r=red+"";
        g=gbString(green);
        b=gbString(blue);
        return r+g+b;
    }
    public int getValue(){
        int x;
        x=Integer.parseInt(getRGBString());
        return x+ 1000000000;
    }

    private String gbString(int x){
        //As RedString but doesnt add 1 -> x=0 -> x= 000 in "Print" etc.
        String str=x+"";
        if (x<100){
            str="0"+x;
        }
        if (x<10){
            str="00"+x;
        }
        return str;
    }
    public int getIterations(){
        return iterations;
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
