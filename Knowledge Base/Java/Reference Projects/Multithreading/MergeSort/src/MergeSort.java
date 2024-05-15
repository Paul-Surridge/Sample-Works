public class MergeSort {
    
    private int[] iNums,iTempNums;
    
    public MergeSort(int[] iNums)
    {
        this.iNums = iNums;
        iTempNums = new int[iNums.length];
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
    public void sort(int iLow, int iHigh)
    {
        if(iLow >= iHigh)
            return;
        
        int iMiddle = (iLow + iHigh) / 2;
        
        sort(iLow, iMiddle);
        sort((iMiddle + 1), iHigh);
        merge(iLow, iMiddle, iHigh);
    }
    public void showResult()
    {
        for(int i = 0 ; i<iNums.length ; ++i)
            System.out.println(iNums[i] + " ");
    }
}
