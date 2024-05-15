import java.util.concurrent.RecursiveAction;

public class SimpleRecursiveAction extends RecursiveAction{
    
    private int iSimulateWork;

    public SimpleRecursiveAction(int iSimulateWork) {
        this.iSimulateWork = iSimulateWork;
    }

    @Override
    protected void compute() {
        
        /*
            - Within this method create more instances of the objects which extend RecursiveAction/RecursiveTask
            - Run recursively via .fork()
        */
        if(iSimulateWork > 100)
        {
            System.out.println("Parallel execution and split task..." + iSimulateWork);
            
            SimpleRecursiveAction objSimpleRecursiveAction1 = new SimpleRecursiveAction(iSimulateWork/2);
            SimpleRecursiveAction objSimpleRecursiveAction2 = new SimpleRecursiveAction(iSimulateWork/2);
            
            objSimpleRecursiveAction1.fork();
            objSimpleRecursiveAction2.fork();
        }
        else
        {
            System.out.println("No need for parallel execution, sequential algorithm suitable." + iSimulateWork);
        }
    }
}
