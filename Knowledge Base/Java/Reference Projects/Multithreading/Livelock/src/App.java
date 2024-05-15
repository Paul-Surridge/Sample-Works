import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {

    private Lock objLock1 = new ReentrantLock(true);
    private Lock objLock2 = new ReentrantLock(true);

    //The below results in a Livelock situation where both threads get in a cyclic dependency between the locks.
    public void process1()
    {
        while(true)
        {
            try {
                objLock1.tryLock(500, TimeUnit.MILLISECONDS);
            } catch (InterruptedException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            System.out.println("Process 1 acquires Lock 1");
            System.out.println("Process 1 tries to acquire Lock 2");
            
            if(objLock2.tryLock())
            {
                System.out.println("Process 1 acquires Lock 2");
                objLock2.unlock();
            }
            else
            {
                System.out.println("Process 1 fails to acquire Lock 2");
                continue;
            }
            
            break;
        }
    }
    public void process2()
    {
        while(true)
        {
            try {
                objLock2.tryLock(500, TimeUnit.MILLISECONDS);
            } catch (InterruptedException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            System.out.println("Process 2 acquires Lock 2");
            System.out.println("Process 2 tries to acquire Lock 1");
            
            if(objLock1.tryLock())
            {
                System.out.println("Process 2 acquires Lock 1");
                objLock1.unlock();
            }
            else
            {
                System.out.println("Process 2 fails to acquire Lock 1");
                continue;
            }
            
            break;
        }
    }
    public static void main(String[] args)
    {
        //It s necessary to create an additional instance of the App class in order to allow calling process1() and process2() from within main() (which is static context)
        App objApp = new App();

        Thread t1 = new Thread(new Runnable(){

            @Override
            public void run() {
                objApp.process1();
            }
        });
        Thread t2 = new Thread(new Runnable(){

            @Override
            public void run() {
                objApp.process2();
            }
        });

        t1.start();
        t2.start();
    }
}
