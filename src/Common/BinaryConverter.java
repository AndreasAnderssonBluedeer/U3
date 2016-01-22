package Common;

/**
 * This class converts ints and bytes  to make the compression with two colors values
 * for 3 bytes.
 * Created by Andreas Andersson & David Isberg on 2016-01-21.
 */
public class BinaryConverter {

    /**
     * Converts 3 bytes to 2 ints by using a binary string for 1 byte that will be splitted and converted to values.
     * @param lowBits1 -byte 1
     * @param lowBits2 -byte 2
     * @param convert -byte 3 to split.
     * @return int[]
     */
    public int[] toInt(int lowBits1,int lowBits2,String convert){
        int x=Integer.parseInt(convert);
        x+=100000000;   //To get the zeros.
        convert=String.valueOf(x);
        String strL,strH;

        strL=convert.substring(1,5);
        strH=convert.substring(5, convert.length());

        int[] res=new int[2];

        res[0]=getValue(strL)+lowBits1;
        res[1]=getValue(strH)+lowBits2;

        return res;
    }

    /**
     * Returns the value of a string with bits.
     * @param str string with value
     * @return int
     */
    private int getValue(String str){
        int res=0;
        int i0,i1,i2,i3;

        i3=Integer.parseInt(str.substring(0,1));
        i2=Integer.parseInt(str.substring(1,2));
        i1=Integer.parseInt(str.substring(2,3));
        i0=Integer.parseInt(str.substring(3,4));
        //Get position values from the string
        if(i0==1){
            res+=256;
        }
        if(i1==1){
            res+=512;
        }
        if(i2==1){
            res+=1024;
        }
        if(i3==1){
            res+=2048;
        }

        return res;
    }

    /**
     * Returns 3 bytes with one shared byte for 2 ints.
     * @param nbr1 value 1
     * @param nbr2 value 2
     * @return int[]
     */
    public int[] toByte(int nbr1,int nbr2){
       int[] array=new int[3];

        array[0]=getDifference(nbr1);   //Returns difference between shared byte and value.
        array[2]=getDifference(nbr2);

        //Get values over 255. 8 bit values.
        int nbr1High,nbr2High;
        if(nbr1>255){
            nbr1High=nbr1-array[0];
        }
        else{
            nbr1High=0;
        }

        if(nbr2>255){
            nbr2High=nbr2-array[2];
        }
        else{
            nbr2High=0;
        }
        nbr1High=nbr1High/256;  //Ex: 3840/256=15 (low 4 bit)
        nbr2High=nbr2High/16;   //Ex: 3840/16=240 (high 4 bit)

        array[1]=nbr1High+nbr2High;

        return array;
    }

    /**
     * Returns the difference between the value and the combined positions from the shared byte
     * @param nbr value
     * @return int
     */
    private int getDifference(int nbr){
        int res=nbr;
        int [] highValues= {0,256,512,768,1024,1280,1536,1792,2048,2304,2560,2816,3072,3328,3584,3840};

            for(int i=1;i<highValues.length;i++){
                if (nbr < highValues[i] && nbr >highValues[i-1]){
                    return nbr-highValues[i-1];
                }
                else if(nbr>3840){
                    return nbr-3840;
                }
            }
        return res;
    }

    /**
     * Main method for testing.
     * @param args
     */
    public static void main(String []args){
        BinaryConverter bc=new BinaryConverter();
        int[] a=bc.toByte(1394,201);
        System.out.println(a[0]+" "+a[1]+" "+a[2]);
        String b1=Integer.toBinaryString(a[1]);

        System.out.println("Binary:"+b1);
        int[] b=bc.toInt(a[2],a[0],b1);
        System.out.println(b[0]+" "+b[1]);
    }
}
