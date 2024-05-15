import java.util.Random;

public class App {

    public static Random objRandom = new Random();

    public static int[] generateRandomNumberArray()
    {
        /*
            - Parallel processing is quicker on large array.
            - Sequential processing is quicker on small array.
        */
        int[] a = new int[10000];
        for(int i = 0 ; i< 10000 ; i++)
            a[i] = objRandom.nextInt(10000);

        return a;
    }
    public static void main(String[] args)
    {
        int iNumberOfThreads = Runtime.getRuntime().availableProcessors();
        long lStartTime, lEndTime;
        
        System.out.println("Number of cores/threads: " + iNumberOfThreads);
        System.out.println("");
        
        int[] iNumsSerial   = generateRandomNumberArray();
        int[] iNumsParallel = new int[iNumsSerial.length];
        
        System.arraycopy(iNumsSerial, 0, iNumsParallel, 0, iNumsSerial.length);
        
        MergeSort objMergeSortSerial    = new MergeSort(iNumsSerial);
        MergeSort objMergeSortParallel  = new MergeSort(iNumsParallel);


        lStartTime  = System.currentTimeMillis();
        objMergeSortSerial.sortParallel(0, (iNumsSerial.length - 1), iNumberOfThreads);
        lEndTime    = System.currentTimeMillis();
        
        System.out.println("Parallel Duration: " + (lEndTime - lStartTime));
        

        lStartTime  = System.currentTimeMillis();
        objMergeSortParallel.sortSequential(0, (iNumsParallel.length - 1));
        lEndTime    = System.currentTimeMillis();
        
        System.out.println("Sequential Duration: " + (lEndTime - lStartTime));
    }
}
