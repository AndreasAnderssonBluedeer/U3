package Common;

/**
 * Created by Andreas on 2016-01-21.
 */
public class BinaryConverter {

    //3 bytes , nbr 1= 1 byte, nbr2= 1 byte, String 1 byte that's shared.
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
    private int getValue(String str){
        int res=0;
        int i0,i1,i2,i3;
        //OBS Räknar från vänster till höger!
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
    //2 ints, returnerar en int[] som representerar båda ints med 3 bytes, varav 1 gemensam. Skriv till fil som byte.
    public int[] toByte(int nbr1,int nbr2){
       int[] array=new int[3];// de 3 Bytesen.

        array[0]=getDifference(nbr1);   //Returnerar differansen av rätt högvärde. 8bits, värde 1
        array[2]=getDifference(nbr2);   //8bits, värde 2

        //Ta reda på vilka värden som är högvärde. nbr1( låga 4a) nbr2( höga 4a)
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
        nbr1High=nbr1High/256;  //Ex: 3840/256=15
        nbr2High=nbr2High/16;   //Ex: 3840/16=240 tillsammans = maxvärde 255.

        array[1]=nbr1High+nbr2High;

        return array;
    }
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
