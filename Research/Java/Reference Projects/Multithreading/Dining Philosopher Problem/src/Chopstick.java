import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Chopstick {
    
    private int     iID;
    private Lock    objLock;

    public Chopstick(int iID) {
        this.iID = iID;
        this.objLock = new ReentrantLock();
    }
    
    public boolean pickUp(Philosopher objPhilosopher, State objState) throws InterruptedException {
        
        if( objLock.tryLock(10, TimeUnit.MILLISECONDS) ) {
            System.out.println(objPhilosopher + " picked up " + objState.toString() + " " + this);
            return true;
        }
        
        return false;
    }
    
    public void putDown(Philosopher objPhilosopher, State objState) {
        
        objLock.unlock();
        System.out.println(objPhilosopher + " put down " + this);
    }

    @Override
    public String toString() {
        return " Chopstick " + iID;
    }
}
