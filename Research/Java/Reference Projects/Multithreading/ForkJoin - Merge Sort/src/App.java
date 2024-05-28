import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class App {

    public static int[] generateNumbers()
    {
        Random objRandom = new Random();
        
        int[] iNumbers = new int[100000];
        
        for(int i = 0 ; i < iNumbers.length ; i++)
            iNumbers[i] = objRandom.nextInt(1000);
        
        return iNumbers;
    }
    public static void main(String[] args)
    {
        ForkJoinPool                objForkJoinPool;
        MergeSortSequential         objMergeSortSequential;
        MergeSortParallelThread     objMergeSortParallelThread;
        
        //int[] iNumbers = {1,4,2,6,3,6,7,4,9,4,6,3,9,1,0,5};
        int[] iNumbersSequential    = generateNumbers();
        int[] iNumbersParallel      = Arrays.copyOf(iNumbersSequential, iNumbersSequential.length);
        long lStart, lEnd;
        
        //Sequential Sort
        lStart  = System.currentTimeMillis();
        objMergeSortSequential      = new MergeSortSequential();
        objMergeSortSequential.mergeSort(iNumbersSequential);
        lEnd    = System.currentTimeMillis();
        System.out.println("Sequential Sort: " + (lEnd - lStart) + "mS");
        
        
        //Parallel Sort
        lStart  = System.currentTimeMillis();
        objForkJoinPool             = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
        objMergeSortParallelThread  = new MergeSortParallelThread(iNumbersParallel);
        objForkJoinPool.invoke(objMergeSortParallelThread);
        lEnd    = System.currentTimeMillis();
        System.out.println("Parallel Sort: " + (lEnd - lStart) + "mS");
        
//        for(int i = 0 ; i<iNumbers.length ; i++)
//            System.out.println(iNumbers[i] + " ");
        
        /*
            - The above illustrates the general approach that sequential is quicker when the number of elements to be sorted are smaller <10000
            - Parallel is quicker when array size is large.
            - This is primarily due to the large overhead of multithreading.
        */
    }
}
