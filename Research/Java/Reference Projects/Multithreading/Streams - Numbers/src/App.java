import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class App {

    public static void main(String[] args)
    {
        int iSum;
        int[] iNumbers  = {1, 67, 23, -2, 35, 10};
        String[] sNames = {"Paul", "Bob", "John", "Sam", "Darren", "Fred"};
        
        //Local lambda expression used to output the elements within array
        Arrays.stream(iNumbers).forEach(x->System.out.println(x));
        
        //Quick convenient method of calculating the sum (rather than verbose for loop)
        iSum = Arrays.stream(iNumbers).sum();
        
        System.out.println(iSum);
        
        IntStream.range(0, 10).forEach(x->System.out.print(x + " "));
        IntStream.range(0, 10).filter(x->x>4).forEach(x->System.out.print(x + " "));
        
        
        //Strings
        Stream.of(sNames).sorted().forEach(x->System.out.println(x));
        
        //Here the static method reverseOrder() of interface Comparator returns an anonymous object which implements interface Comparator 
        Stream.of(sNames).sorted(Comparator.reverseOrder()).forEach(x->System.out.println(x));
        
        //Here the x->x.startsWith() is a Predicate 'functional interface'  
        Stream.of(sNames).filter(x->x.startsWith("B")).forEach(x->System.out.println(x));
    }
}
