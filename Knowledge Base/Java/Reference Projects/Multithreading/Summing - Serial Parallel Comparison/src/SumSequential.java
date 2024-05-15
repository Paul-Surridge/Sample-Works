public class SumSequential {

    int iTotal;
    int[] iNumbers;
    
    public SumSequential() {
        iTotal = 0;
    }
    
    public int findTotal(int[] iNumbers)
    {
        this.iNumbers = iNumbers;
        
        for(int i = 0 ; i < iNumbers.length ; i++)
            iTotal = iTotal + iNumbers[i];
        
        return iTotal;
    }
}
