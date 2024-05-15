import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Book {
    
    private int     iID;
    private Lock    objLock;

    public Book(int iID) {
        this.iID = iID;
        this.objLock = new ReentrantLock();
    }
    
    public void read(Student objStudent) throws InterruptedException {
        
        try {
            objLock.lock();
            System.out.println("Student " + objStudent + " reading book " + iID);
            Thread.sleep(500);
        } finally {
            System.out.println("Student " + objStudent + " finished book " + iID);
            objLock.unlock();
        }
    }

    @Override
    public String toString() {
        return "Book " + iID;
    }
}
