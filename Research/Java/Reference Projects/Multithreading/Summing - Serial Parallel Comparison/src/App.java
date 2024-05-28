import java.util.Random;

public class App {

    public static void main(String[] args)
    {
        Random          objRandom;
        SumSequential   objSumSequential;
        SumParallel     objSumParallel;
        
        int[]           iNumbers;
        long            lStart, lEnd;
        
        objRandom           = new Random();
        objSumSequential    = new SumSequential();
        objSumParallel      = new SumParallel();
        
        //Generate a random array of numbers between 0-99
        iNumbers = new int[100000000];
        for(int i = 0 ; i<iNumbers.length ; i++)
            iNumbers[i] = objRandom.nextInt(100);
        
        lStart  = System.currentTimeMillis();
        System.out.println("Sequential Sum Total:  "    + objSumSequential.findTotal(iNumbers));
        lEnd    = System.currentTimeMillis();
        System.out.println("Sequential Duration: "      + (lEnd-lStart) + "mS");
        
        lStart  = System.currentTimeMillis();
        System.out.println("Parallel Sum Total:  "      + objSumParallel.findTotal(iNumbers));
        lEnd    = System.currentTimeMillis();
        System.out.println("Parallel Duration: "        + (lEnd-lStart) + "mS");
    }
}
