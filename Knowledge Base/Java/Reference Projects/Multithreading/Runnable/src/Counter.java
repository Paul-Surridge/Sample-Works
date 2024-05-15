public class Counter implements Runnable{
    
    private final String sName;
    
    Counter(String sName)
    {
        this.sName = sName;
    }
    
    @Override
    public void run(){
        for(int i = 0 ; i<10 ; i++){
            System.out.println(sName + ": " + i);
        }
    }
}
