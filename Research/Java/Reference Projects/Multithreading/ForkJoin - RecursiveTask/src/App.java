import java.util.concurrent.ForkJoinPool;

public class App {

    /*
        SimpleRecursiveAction/SimpleRecursiveTask
    
            .fork()
                - This can be called on any RecursiveAction of RecursiveTask<T>
                - Upon encountering:

                    - The invoking object is inserted into the ForkJoinPool
                    - The invoking object is allocated another thread if available
                    - The invoking object is run asynchronously

            .join()
                - Returns the result of compute() once complete.
                - The return type is as defined in the generic type in the 'extended' RecursiveTask<T>.
                - Upon encountering:

                    - The current thread is halted.
                    - The current thread waits for the new thread allocated to the invoking object to complete and return its value
    
        ForkJoinPool.invoke()
            - Insert the passed SimpleRecursiveAction/SimpleRecursiveTask into the ForkJoinPool.
            - The passed object will now run its overridden .compute()
     */
    public static void main(String[] args)
    {
        ForkJoinPool            objForkJoinPool                 = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
        SimpleRecursiveTask     objSimpleRecursiveTask          = new SimpleRecursiveTask(120);
        /*
            - The ForkJoinPool is created.
            - The ForkJoinPool is allocated x8 threads given x8 cores on laptop.
            - Create an object which extended RecursiveTask.
            - Initially:
        
                - Pass this object into ForkJoinPool via .invoke().
                - The overridden compute() will run and return
        
            - The overridden compute() itself may recursively create more objects of itself and add these to the ForkJoinPool via .fork().
            - The compute() within new objects is run recursively using any available threads within the ForkJoinPool.
        
            - RecursiveTask is suitable when need to return any value.
        */
        objForkJoinPool.invoke(objSimpleRecursiveTask);
        
        System.out.println(objForkJoinPool.invoke(objSimpleRecursiveTask));
    }
}
