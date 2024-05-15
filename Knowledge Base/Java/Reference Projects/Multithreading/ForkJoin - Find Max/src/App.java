import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class App {

    public static int iThreshold = 0;

    private static int[] initNumbers()
    {
        int[] iNumbers;
        Random objRandom;
        
        iNumbers    = new int[100000];
        objRandom   = new Random();
        
        for(int i = 0 ; i < iNumbers.length ; i++)
            iNumbers[i] = objRandom.nextInt(1000);
        
        return iNumbers;
    }
    public static void main(String[] args)
    {
        ForkJoinPool        objForkJoinPool;
        FindMaxSequential   objFindMaxSequential;
        FindMaxParallel     objFindMaxParallel;
        
        int[] iNumbers;
        long lStart, lEnd;
        
        iNumbers        = initNumbers();        
        iThreshold      = iNumbers.length / Runtime.getRuntime().availableProcessors();
        objForkJoinPool = new ForkJoinPool(iThreshold);
        
        lStart  = System.currentTimeMillis();
        objFindMaxSequential = new FindMaxSequential();
        System.out.println("Sequential Maximum: " + objFindMaxSequential.findMax(iNumbers));
        lEnd    = System.currentTimeMillis();
        System.out.println("Sequential Duration: " + (lEnd - lStart) + "mS");
        
        
        lStart  = System.currentTimeMillis();
        objFindMaxParallel = new FindMaxParallel(iNumbers, 0, iNumbers.length);
        System.out.println("Parallel Maximum: " + objForkJoinPool.invoke(objFindMaxParallel));
        lEnd    = System.currentTimeMillis();
        System.out.println("Parallel Duration: " + (lEnd - lStart) + "mS");
    }
}
