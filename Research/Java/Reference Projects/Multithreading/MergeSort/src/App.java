import java.util.Random;

public class App {

    public static void main(String[] args)
    {
        MergeSort objMergeSort;
        Random objRandom = new Random();
        
        int[] iNums = new int[30];
        
        for(int j = 0 ; j < iNums.length ; j++)
            iNums[j] = objRandom.nextInt(1000) - 500;
        
        objMergeSort = new MergeSort(iNums);
        
        //Time Complexity O(n log(n))
        objMergeSort.sort(0, (iNums.length - 1));
        objMergeSort.showResult();
    }
}
