public class RunnableInterface {

    public static void main(String[] args)
    {
        Thread t1 = new Thread(new Counter("Counter 1"));
        Thread t2 = new Thread(new Counter("Counter 2"));
        Thread t3 = new Thread(new Counter("Counter 3"));
        Thread t4 = new Thread(new Counter("Counter 4"));
        Thread t5 = new Thread(new Counter("Counter 5"));
        
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
    }
}
