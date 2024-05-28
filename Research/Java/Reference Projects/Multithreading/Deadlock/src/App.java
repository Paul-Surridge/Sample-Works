import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {

    private Lock objLock1 = new ReentrantLock(true);
    private Lock objLock2 = new ReentrantLock(true);

    //In order to prevent cyclic dependency ensure that locks are acquired using the same ordering i.e. in this case swap line 71 and 80 to resolve deadlock.
    public void process1()
    {
        objLock1.lock();
        System.out.println("Process 1 acquires Lock 1");

        try {
            Thread.sleep(300);
        } catch (InterruptedException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }

        objLock2.lock();
        System.out.println("Process 1 acquires Lock 2");

        objLock1.unlock();
        objLock2.unlock();
    }

    public void process2()
    {
        objLock2.lock();
        System.out.println("Process 2 acquires Lock 2");

        try {
            Thread.sleep(300);
        } catch (InterruptedException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }

        objLock1.lock();
        System.out.println("Process 2 acquires Lock 1");

        objLock1.unlock();
        objLock2.unlock();
    }

    public static void main(String[] args)
    {
        //It's necessary to create an additional instance of the App class in order to allow calling process1() and process2() from within main() (which is static context)
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
        
        //Both process1() and process2() acquire the respective locks such that execution hangs as the other method owns the locks of those
        //other lock objects, overall leading to permanent deadlock.
        t1.start();
        t2.start();
    }
}
