package Compress;

/**
 * Created by Andreas on 2016-01-15.
 */
public class RGB {

    private int red,green,blue,iterations;

    private int adjustColor(int value){
        int temp;
        temp=value%5;

        if (temp==0){
            //Do nothing, Value is already okay.
        }
        else if (temp>2){   //3 or 4
            temp=5-temp;
            value+=temp;
        }
        else{   //Temp < 3 , 1 or 2
            value-=temp;
        }
        return value;
    }
    public void setIterations(int iterations){
        this.iterations=iterations;
    }
    public void setRed(int red){
        this.red=adjustColor(red);
    }
    public void setGreen(int green){
        this.green=adjustColor(green);
    }
    public void setBlue(int blue){
        this.blue=adjustColor(blue);
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
        return red+""+green+""+blue;
    }
    public int getIterations(){
        return iterations;
    }

}
