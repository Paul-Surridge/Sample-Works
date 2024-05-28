import java.util.logging.Level;
import java.util.logging.Logger;

public class SumParallel {
    
    private SumParallelThread[] threadPool;
    private int                 iNumberOfThreads;

    public SumParallel() {
        
        iNumberOfThreads = Runtime.getRuntime().availableProcessors();
        
        threadPool = new SumParallelThread[iNumberOfThreads];
    }
    
    public int findTotal(int[] iNumbers)
    {
        int iSegment, iTotal;
        
        iTotal = 0;
        
        iSegment = (int) Math.ceil( (iNumbers.length * 1.0) / iNumberOfThreads );
        
        for(int i = 0 ; i < iNumberOfThreads ; i++)
        {
            threadPool[i] = new SumParallelThread(iNumbers, (i*iSegment), ((i+1)*iSegment));
            threadPool[i].start();
        }
        
        try {
            for(int i = 0 ; i < iNumberOfThreads ; i++)
                threadPool[i].join();
        } catch (InterruptedException ex) {
            Logger.getLogger(SumParallel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for(int i = 0 ; i < iNumberOfThreads ; i++)
            iTotal = iTotal + threadPool[i].getSum();
        
        return iTotal;
    }
}
