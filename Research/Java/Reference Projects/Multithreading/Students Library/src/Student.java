import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Student implements Runnable{

    private int iID;
    private Random objRandom = new Random();
    
    public Student(int iID) {
        this.iID = iID;
    }

    @Override
    public void run() {
        
        while(true) {
            try {
                App.Books[objRandom.nextInt(7)].read(this);
            } catch (InterruptedException ex) {
                Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
            }
        }          
    }

    @Override
    public String toString() {
        return "Student ID " + iID;
    }
}
