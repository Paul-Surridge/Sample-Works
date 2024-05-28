import java.util.ArrayList;
import java.util.Collection;

class Class1
{
    Collection<?>                   c1 = new ArrayList<>();
    Collection<? extends Number>    c2 = new ArrayList<>();
    Collection<? super Integer>     c3 = new ArrayList<>();
    Collection<Integer>             c4 = new ArrayList<>();
    
    void method()
    {
        c1 = c2;                            //Wildcards within Type Arguments allow for Parameterised Types with related Type Arguments to become compatible.
        c1 = c3;
        c3 = c4;                            //Upper and Lower Bounds are inclusive of the bound (not exclusive when Lower Bounded).
    }
}
class Class2
{
    Collection<?>                   c1 = new ArrayList<>();
    Collection<? extends Number>    c2 = new ArrayList<>();
    Collection<? super Integer>     c3 = new ArrayList<>();
    
    void method()
    {
        c1 = new ArrayList<String>();       //c1 may be assigned any type
        c1 = new ArrayList<Double>();
        c1 = new ArrayList<Float>();
        
        //c2 = new ArrayList<String>();     //c2 may be assigned any type which is Number or an Extension
        c2 = new ArrayList<Double>();
        c2 = new ArrayList<Float>();
        
        //c3 = new ArrayList<String>();     //c3 may be assigned any type which is parent of Integer
        c3 = new ArrayList<Number>();
        c3 = new ArrayList<Object>();
    }
}
class Class3<E>
{
    E e;
    
    void method1(E e)
    {
        this.e = e;
    }
    void method2(E e)
    {
        this.e = e;
    }
    int set(E e)
    {
        this.e = e;
        
        return 0;
    }
    E get()
    {
        return e;
    }
}
class Class4
{
    Class3<? extends String> c1 = new Class3<>();
    
    void method()
    {
        //c1.set("Test");         //Only possible to 'get' from a Parameterised Type whose Type Argument is undefined.
        c1.get();
    }
}
