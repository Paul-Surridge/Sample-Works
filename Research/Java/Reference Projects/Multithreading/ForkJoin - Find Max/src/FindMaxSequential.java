public class FindMaxSequential {
    
    //linear search O(n)
    public int findMax(int[] iNums)
    {
        int iMax = iNums[0];
        
        for(int i = 0 ; i<iNums.length ; i++)
            if(iNums[i] > iMax)
                iMax = iNums[i];
        
        return iMax;
    }
}
