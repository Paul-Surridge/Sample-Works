import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class App {

    public static void main(String[] args)
    {
        long lCount;
        String sStudentsLocal;
        
        List<Student> objStudents = new ArrayList<>();
        
        objStudents.add(new Student("Paul", true));
        objStudents.add(new Student("Bob", true));
        objStudents.add(new Student("John", false));
        objStudents.add(new Student("Sam", true));
        objStudents.add(new Student("Darren", false));
        objStudents.add(new Student("Fred", false));
        
        //Implement a stream from the ArrayList.
        
        //  objStudents.stream().                                   are "source operations"
        //  .map(x->x.getName()).filter(x->x.startsWith("P"))       are "intermediate operations"
        //  .forEach(x->System.out.println(x))                      are "termainl operations"
        objStudents.stream().map(x->x.getName()).filter(x->x.startsWith("P")).forEach(x->System.out.println(x));
        
        //  objStudents.stream().                                   are "source operations"
        //  .filter(x->x.isLocal())                                 are "intermediate operations"
        //  .forEach(x->System.out.println(x.getName()))            are "termainl operations"
        objStudents.stream().filter(x->x.isLocal()).forEach(x->System.out.println(x.getName()));
        
        //  objStudents.stream().                                   are "source operations"
        //  .filter(x->x.isLocal())                                 are "intermediate operations"
        //  .count();                                               are "termainl operations"
        lCount = objStudents.stream().filter(x->x.isLocal()).count();
        
        System.out.println(lCount);
        
        //The return name can be concatenated as follows:
        
        //  objStudents.stream().                                   are "source operations"
        //  .filter(x->x.isLocal()).map(x->x.getName())             are "intermediate operations"
        //  .collect(Collectors.joining(" "));                      are "termainl operations"
        sStudentsLocal = objStudents.stream().filter(x->x.isLocal()).map(x->x.getName()).collect(Collectors.joining(" "));
        
        System.out.println(sStudentsLocal);
    }
}
