public class App{

    public static void main(String[] args) {

        long lStartTimeMs, lEndTimeMs, lStartTimeNs, lEndTimeNs;
        long lTotal = 0;
        
        lStartTimeMs    = System.currentTimeMillis();
        lStartTimeNs    = System.nanoTime();
        
        for(long l = 0 ; l < 1000000000 ; l++)
            lTotal++;
        
        lEndTimeMs      = System.currentTimeMillis();
        lEndTimeNs      = System.nanoTime();
        
        System.out.println("Duration: " + (lEndTimeMs - lStartTimeMs) + "mS");
        //Process
        System.out.println("Duration: " + (lEndTimeNs - lStartTimeNs) + "nS");
    }
}
