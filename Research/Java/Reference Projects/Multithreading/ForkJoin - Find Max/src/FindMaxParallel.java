import java.util.concurrent.RecursiveTask;

public class FindMaxParallel extends RecursiveTask<Integer>{
    
    private int[] iNums;
    private int iLowIndex, iHighIndex;
    
    public FindMaxParallel(int[] iNums, int iLowIndex, int iHighIndex)
    {
        this.iNums      = iNums;
        this.iLowIndex  = iLowIndex;
        this.iHighIndex = iHighIndex;
        
    }

    @Override
    protected Integer compute() {
        //If the array is small, then use sequential processing
        if(iHighIndex - iLowIndex < 1000)
            return findMaxSequential();
        else
        {
            int iMidIndex = (iHighIndex+iLowIndex)/2;
            
            FindMaxParallel task1 = new FindMaxParallel(iNums, iLowIndex, iMidIndex);
            FindMaxParallel task2 = new FindMaxParallel(iNums, iMidIndex+1, iHighIndex);
            
            invokeAll(task1, task2);
            
            return Math.max(task1.join(), task2.join());
        }
    }
    
    private Integer findMaxSequential()
    {
        int iMax = iNums[0];
        
        for(int i = 0 ; i<iNums.length ; i++)
            if(iNums[i] > iMax)
                iMax = iNums[i];
        
        return iMax;
    }
}
