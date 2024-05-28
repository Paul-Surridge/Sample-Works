import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {

    static Book Books[] = new Book[Constants.NUMBER_OF_BOOKS];
    
    public static void main(String[] args)
    {
        Student Students[] = new Student[Constants.NUMBER_OF_STUDENTS];
        
        ExecutorService objExecutorService = Executors.newFixedThreadPool(Constants.NUMBER_OF_STUDENTS);

        try {
            for(int i = 0 ; i < Constants.NUMBER_OF_BOOKS ; i++)
                Books[i] = new Book(i);
            
            for(int i = 0 ; i < Constants.NUMBER_OF_STUDENTS ; i++) {
                Students[i] = new Student(i);
                objExecutorService.execute(Students[i]);
            }
        } catch (Exception e) {
            objExecutorService.shutdown();
        } finally {
            objExecutorService.shutdown();
        }
    }   
}
