public class SumParallelThread extends Thread{
    
    private int iLow, iHigh, iSum;
    private int[] iNumbers;

    public SumParallelThread(int[] iNumbers, int iLow, int iHigh) {
        this.iNumbers   = iNumbers;
        this.iLow       = iLow;
        this.iHigh      = iHigh;
    }
    
    public int getSum()
    {
        return iSum;
    }

    @Override
    public void run() {
        
        iSum = 0;
        
        for(int i = iLow ; i<iHigh ; i++)
            iSum = iSum + iNumbers[i];
    }
}
