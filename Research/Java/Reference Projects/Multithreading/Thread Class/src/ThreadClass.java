import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadClass {

    public static void main(String[] args)
    {
        Counter t1 = new Counter("Counter 1:");
        Counter t2 = new Counter("Counter 2:");
        
        t1.start();
        t2.start();
        
        //This will wait for t1 and t2 to finish and 'die' before continuing.
        //Upon encountering these statements the execution is suspended and waits for the invoking threads to 're-join' main Application Thread
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(ThreadClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //NB This is printed out before the lines in the Counter objects given that it runs on the Application Thread (if not joined)
        System.out.println("Finished Threads");
    } 
}
