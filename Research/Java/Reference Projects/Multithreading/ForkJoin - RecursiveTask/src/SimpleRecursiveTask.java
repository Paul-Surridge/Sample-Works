import java.util.concurrent.RecursiveTask;

public class SimpleRecursiveTask extends RecursiveTask<Integer>{
    
    private int iSimulateWork;

    public SimpleRecursiveTask(int iSimulateWork)
    {
        this.iSimulateWork = iSimulateWork;
    }

    @Override
    protected Integer compute()
    {
        /*
            - Within this method create more instances of the objects which extend RecursiveAction/RecursiveTask
            - Run recursively via .fork()
        */
        int iTotal;
        
        if(iSimulateWork > 100)
        {
            System.out.println("Parallel execution required and split task..." + iSimulateWork);
            
            SimpleRecursiveTask objSimpleRecursiveTask1 = new SimpleRecursiveTask(iSimulateWork/2);
            SimpleRecursiveTask objSimpleRecursiveTask2 = new SimpleRecursiveTask(iSimulateWork/2);
            
            objSimpleRecursiveTask1.fork();
            objSimpleRecursiveTask2.fork();
            
            iTotal = 0;
            
            //As normal execution of this thread is halted at the .join() and waits for the thread to finish and return a value.
            iTotal += objSimpleRecursiveTask1.join();
            iTotal += objSimpleRecursiveTask2.join();
            
            return iTotal;
        }
        else
        {
            System.out.println("No need for parallel execution, sequential algorithm suitable." + iSimulateWork);
            
            return (2*iSimulateWork);
        }
    }
}