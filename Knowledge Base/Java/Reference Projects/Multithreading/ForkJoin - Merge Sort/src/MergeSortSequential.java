import java.util.Arrays;

public class MergeSortSequential {

    public void mergeSort(int[] iNumbers)
    {
        int iMiddle;
        int[] iArrayLeft, iArrayRight;
    
        if(iNumbers.length <= 1)
            return;
        
        iMiddle = iNumbers.length/2;
        
        iArrayLeft = Arrays.copyOfRange(iNumbers, 0, iMiddle);
        iArrayRight = Arrays.copyOfRange(iNumbers, iMiddle, iNumbers.length);
        
        mergeSort(iArrayLeft);
        mergeSort(iArrayRight);
        
        merge(iArrayLeft, iArrayRight, iNumbers);
    }
    
    private void merge(int[] iArrayLeft, int[] iArrayRight, int[] iNumbers)
    {
        int i, j, k;
        
        i = j = k = 0;
        
        while( (i < iArrayLeft.length) && (j < iArrayRight.length) )
        {
            if(iArrayLeft[i] < iArrayRight[j])      iNumbers[k++] = iArrayLeft[i++];
            else                                    iNumbers[k++] = iArrayRight[j++];
        }
        
        while(i < iArrayLeft.length)
            iNumbers[k++] = iArrayLeft[i++];
        
        while(j < iArrayRight.length)
            iNumbers[k++] = iArrayRight[j++];
    }
}
