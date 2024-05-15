import java.util.Random;

public class Philosopher implements Runnable{
    
    private Chopstick chopstickLeft, chopstickRight;
    private Random objRandom;
    
    private int iID, iEatCounter;
    private volatile boolean bFull = false;     //'volatile' ensures that the variable is stored in main memory (which all threads have access to rather than a cache associated with the thread.

    public Philosopher(int iID, Chopstick chopstickLeft, Chopstick chopstickRight) {
        
        this.iID = iID;
        this.chopstickLeft = chopstickLeft;
        this.chopstickRight = chopstickRight;
        
        objRandom = new Random();
    }

    @Override
    public void run() {
        
        try {
            while(!bFull) {
                
                think();
                
                if (chopstickLeft.pickUp(this, State.LEFT)) {
                    if (chopstickRight.pickUp(this, State.RIGHT)) {
                        eat();
                        chopstickRight.putDown(this, State.RIGHT);
                    }
                    
                    chopstickLeft.putDown(this, State.LEFT);
                }
            }
        } catch (Exception e) {
        }
        
    }
    
    private void think() throws InterruptedException {
        System.out.println(this + " is thinking...");
        Thread.sleep(objRandom.nextInt(1000));
    }
    
    private void eat() throws InterruptedException {
        System.out.println(this + " is eating...");
        Thread.sleep(objRandom.nextInt(1000));
        
        iEatCounter++;
    }
            
    public void setFull(boolean bState) {
        bFull = bState;
    }
    
    public int getEatCounter() {
        return iEatCounter;
    }

    @Override
    public String toString() {
        return "Philosopher " + iID;
    }   
}
