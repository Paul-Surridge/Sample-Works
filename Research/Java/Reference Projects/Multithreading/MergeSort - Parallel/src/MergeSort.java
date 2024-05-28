import java.util.logging.Level;
import java.util.logging.Logger;

public class MergeSort {
    
    private int[] iNums,iTempNums;
    
    public MergeSort(int[] iNums)
    {
        this.iNums = iNums;
        iTempNums = new int[iNums.length];
    }
    
    public void sortParallel(int iLow, int iHigh, int iNumberOfThreads)
    {
        if(iNumberOfThreads <= 1)
        {
            sortSequential(iLow, iHigh);
            return;
        }

        int iMiddleIndex = (iLow + iHigh)/ 2;

        Thread threadLeftArray  = generateThread(iLow, iMiddleIndex, iNumberOfThreads);
        Thread threadRightArray = generateThread((iMiddleIndex + 1), iHigh, iNumberOfThreads);

        threadLeftArray.start();
        threadRightArray.start();
        
        try {
            threadLeftArray.join();
            threadRightArray.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(MergeSort.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        merge(iLow, iMiddleIndex, iHigh);
    }
    public void sortSequential(int iLow, int iHigh)
    {
        if(iLow >= iHigh)
            return;
        
        int iMiddle = (iLow + iHigh) / 2;
        
        //Use separate threads to handle the left and right sub-array recursively. 
        sortSequential(iLow, iMiddle);
        sortSequential((iMiddle + 1), iHigh);
        merge(iLow, iMiddle, iHigh);
    }
    public void showResult()
    {
        for(int i = 0 ; i<iNums.length ; ++i)
            System.out.println(iNums[i] + " ");
    }
    
    private void merge(int iLow, int iMiddle, int iHigh)
    {
        //Copy iNums[i] -> iTempNums[i]
        for(int i = iLow ; i<=iHigh ; i++)
            iTempNums[i] = iNums[i];
        
        int i = iLow;
        int j = iMiddle + 1;
        int k = iLow;
        
        //Copy the smallest values from either the left or the right side back to the original array
        while((i <= iMiddle) && (j <= iHigh))
        {
            if(iTempNums[i] <= iTempNums[j])
            {
                iNums[k] = iTempNums[i];
                i++;
            }
            else
            {
                iNums[k] = iTempNums[j];
                j++;
            }
            
            k++;
        }
        
        //Copy the remainders of the left side of the array into the target array
        while(i <= iMiddle)
        {
            iNums[k] = iTempNums[i];
            k++;
            i++;
        }
        
        //Copy the remainders of the right side of the array into the target array
        while(j <= iHigh)
        {
            iNums[k] = iTempNums[j];
            k++;
            j++;
        }
    }
    private Thread generateThread(int iLow, int iHigh, int iNumberOfThreads)
    {
        return new Thread(){

            @Override
            public void run() {
                sortParallel(iLow, iHigh, (iNumberOfThreads/2));
            }
        };
    }
}
