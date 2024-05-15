import java.util.ArrayList;
import java.util.List;

//Single Unbounded
interface Interface1<T>
{
    /*
        Compile Error:      No Type Variable Instantiation

            T objTypeT = new T;
    
        - Interfaces only contain Static memory (except Abstract/Default Methods).
        - Therefore any Field is automatically placed in Static memory
        - Type Variables are implicitly non-Static, therefore Type Variables T cannot be created.
        - T is not known at runtime.
    */

    int method1(T t);
    T method2(T t);
    T method2(int i);
    <S, T extends Number> T method3(int i, S s, T t);
    <S> T method4(int i, S s, T t);
    <S> T method5(int i, S s, List<? extends Double> t);
    
    
    
    
    /*
        Static Methods
    
            - Permitted Type Parameters:        Local Only
    
                Compile Error:
    
                static int method6(T objT)
                {
                    return 1;
                }
                static T method6(T objT)
                {
                    return objT;
                }
    
            - Class Type Parameter are implicitly non-Static, therefore only Local Type Parameters maybe used in Static Methods..
    */
    public static <K, V extends Number> int method6(int i, K k , V v)
    {
        return 1;
    }
    


    /*
        Type Arguments:

            - Type Arguments utilise Wildcards to impose restrictions on the Type that can be assigned to the Type Argument.
            - Therefore specifying the range of Parameterised Type that can be assigned to the variable. 

                1. Class:       Fields                              List<? extends Number>  objList1;
                2. Methods:     Type Parameter                      void method(List<? extends Number>  objList1)
                3. Methods:     Local Variable                      List<? extends Number>  objList1;
                4. Methods:     Return Type (NB. Bad Practice)
    */
    
    //Upper Bounded:    ? extends
    public static <K> int method7(int i, K k , List<? extends Number> l)
    {
        return 1;
    }
    
    public static <K, U extends Number> int method8(int i, K k , List<U> l)
    {
        return 1;
    }
    
    //Lower Bounded:    ? super
    public static <K> int method9(int i, K k , List<? super Number> l)
    {
        return 1;
    }
    //Unbounded:        ?
    public static <K> int method10(int i, K k , List<?> l)
    {
        return 1;
    }
    
    
    
    
    
    
    
    
    
    
    /*
        Default Methods
    
            - Permitted Type Parameters:        Interface + Local
    */
    default <P, Q extends Integer> int method11(T objT, P p, Q q)
    {
        return 1;
    }
    default T method12(T objT)
    {
        return objT;
    }
    default T method13(int i)
    {
        return method2(i);
    }
    /*
        Compile Error:          No Generic Type Instance Variables.
    
            default T method6(int i)
            {
                T objT = new T();
                return T;
            }
    
        - T cannot be created/instaniated via new.
        - Unless T can be derived from another function which ultimately can create a new T e.g. method3(i).
    */
}
interface Interface2<V> extends Interface1<V>
{
    //Type Parameter passed to super inetrafec:     V           (Pass-Thru)
}
interface Interface3 extends Interface1<Integer>
{
    //Type Parameter passed to super inetrafec:     Integer     (Hardcoded)
}
interface Interface4 extends Interface1
{
    //Type Parameter passed to super inetrafec:     Object      (Default - Not Type Safe)
}



//Type Argument:   Pass-Thru
class Class1<T> implements Interface1<T>
{
    T objT;
    
    List<? extends Number> listNumber;
    ArrayList<? extends Number> arraylistNumber = new ArrayList<Double>();
    
    Class1(T t)
    {
        objT = t;
    }
    
    @Override
    public int method1(T t) {
        return 1;
    }

    @Override
    public T method2(T t) {
        return t;
    }

    @Override
    public T method2(int i) {
        return objT;
    }

    @Override
    public <S, T extends Number> T method3(int i, S s, T t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <S> T method4(int i, S s, T t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <S> T method5(int i, S s, List<? extends Double> t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
//Type Argument:   Hardcoded (Integer)
class Class2<T> implements Interface1<Integer>
{
    T objT;
    
    Class2(T t)
    {
        objT = t;
    }
    
    //Instances of T must be replaced with hardcoded 'Integer'
    @Override
    public int method1(Integer t) {
        return 1;
    }

    @Override
    public Integer method2(Integer t) {
        return t;
    }

    @Override
    public Integer method2(int i) {
        return i;
    }

    @Override
    public <S, Integer extends Number> Integer method3(int i, S s, Integer t) {
        throw new UnsupportedOperationException("Not supported yet."); //Integero change body of generated methods, choose Integerools | Integeremplates.
    }

    @Override
    public <S> Integer method4(int i, S s, Integer t) {
        throw new UnsupportedOperationException("Not supported yet."); //Integero change body of generated methods, choose Integerools | Integeremplates.
    }

    @Override
    public <S> Integer method5(int i, S s, List<? extends Double> t) {
        throw new UnsupportedOperationException("Not supported yet."); //Integero change body of generated methods, choose Integerools | Integeremplates.
    }
}
//Type Argument:   None/Blank (Object)
class Class3<T> implements Interface1<Object>
{
    T objT;
    
    Class3(T t)
    {
        objT = t;
    }
    
    //Instances of T must be replaced with hardcoded 'Object'
    @Override
    public int method1(Object t) {
        return 1;
    }

    @Override
    public Object method2(Object t) {
        return t;
    }

    @Override
    public Object method2(int i) {
        return new Object();
    }

    @Override
    public <S, Object extends Number> Object method3(int i, S s, Object t) {
        throw new UnsupportedOperationException("Not supported yet."); //Objecto change body of generated methods, choose Objectools | Objectemplates.
    }

    @Override
    public <S> Object method4(int i, S s, Object t) {
        throw new UnsupportedOperationException("Not supported yet."); //Objecto change body of generated methods, choose Objectools | Objectemplates.
    }

    @Override
    public <S> Object method5(int i, S s, List<? extends Double> t) {
        throw new UnsupportedOperationException("Not supported yet."); //Objecto change body of generated methods, choose Objectools | Objectemplates.
    }
    
    //NB:   If the Type Argument <Object> is removed, strangely causes the IDE to report an Erasure issue for the below generic method?
    //      Even though <Object> and blank should effectively be the same i.e. if Type Argument is not declared then Object is default.
    //      Possibly a bug in IDE and/or compiler?
    public <S, T extends Number> T method6(int i, S s, T t) {
        return t;
    }
}












//Type Parameter List:  Multiple Unbounded
interface Interface5<T, V>
{
    int method1(T t);
    int method1(T t, V v);
    T method1(int i);

    /*
    
    Compile Error:          Erasure Abiguity.

            int method1(T t);
            int method1(V v);
            String method1(V v);
    
        - Given that both the T and V become Object due to erasure.
        - Method signatures become identical.
        - Even if the return type is different, the JVM will not know which to call.
    */
}
class Class4<T, V> implements Interface5<T, V>
{
    T objT;
        
    @Override
    public int method1(T t) {
        return 1;
    }

    @Override
    public T method1(int i) {
        return objT;
    }

    @Override
    public int method1(T t, V v) {
        return 1;
    } 
}
class Class5<T, V> implements Interface5<T, Integer>
{
    T objT;
        
    @Override
    public int method1(T t) {
        return 1;
    }

    @Override
    public T method1(int i) {
        return objT;
    }

    @Override
    public int method1(T t, Integer i) {
        return 1;
    } 
}



/*
    Upper Bounded

    Class6: When Type Parameter T is an interface, T can be one of x4 options:

        1. An interface of Comparable<>
        2. A subinterface of Comparable<>
        3. Any object T that implements Comparable<T> interface.
        4. Any object T that implements a subinterface of Comparable<T> interface.

    There is no need to declare the upper bound in both the class and interface (only the class):

        class Class6<T extends Comparable<T>> implements Comparable<T extends Comparable<T>>         //Compile Error
*/
interface Interface6<T extends Comparable<T>>
{
    
}
class Class6<T extends Comparable<T>> implements Interface6<T>
{
    
}



//Combination
interface Interface7<T extends Comparable<T>, V, C>
{
    T method1();
    V method2(int i);
    C method3(T t);
}
class Class7<T extends Comparable<T>, V> implements Interface7<T, V, Integer>
{
    T objT;
    V objV;
    Integer objC;
    
    @Override
    public T method1() {
        return objT;
    }

    @Override
    public V method2(int i) {
        return objV;
    }

    @Override
    public Integer method3(T t) {
        return objC;
    }
}









/*
    Multiple Inheritance

        Generic interface is the same as non-generic interface except:

            Type Parameters must be passed to any super interface(s) via:

                i.      Local Declaration
                ii.     Hardcode
                iii.    None/Blank (Object)

        General:

            1. Subinterface can inherit from single or multiple superinterface(s).
            2. Subinterface can inherit from both generic and non-generic superinterface.
            3. Subinterface must supply any Type Arguments to generic superinterfaces.
            4. Subinterface aggregates all non-static members:		Abstract and default methods only.
            5. Subinterface may override:				Default methods only.
            6. Subinterface may overload:				Abstract and default methods only.
            7. Subinterface must override:				Any ambiguous members if extending multiple interface(s).
            8. Subinterface may override/overload:			Where it 'makes sense'.
*/
interface Interface8
{
    int IINTEGER1 = 1;
    int IINTEGER2 = 2;
    
    int method1(int i);
    
    static int staticMethod1()
    {
        return 1;
    }
    static int staticMethod2()
    {
        return 2;
    }
    
    default int defaultMethod1(int i)
    {
        return 3;
    }
}
interface Interface9<T>
{
    int IINTEGER1 = 1;
    int IINTEGER2 = 2;
    
    T method1(T t);
    
    static int staticMethod1()
    {
        return 1;
    }
    static int staticMethod2()
    {
        return 2;
    }
    
    default int defaultMethod1(int i)
    {
        return 3;
    }
}
interface Interface10<T>
{
    T method2(T t);
}
interface Interface11<T> extends Interface8, Interface9<Integer>, Interface10
{    
    int IINTEGER1 = 1;
    int IINTEGER2 = 2;
    
    /*
        Compile Error:      Erasure Ambiguity
    
            T method1(T t);
    
        - Duplicates method T method1(T t) in Interface9.
        - Compiler does not know which one to resolve to, would need to override to distinguish.
    */
    
    //Abstract Method
    T method2(int i);
    
    //Static Method
    static int staticMethod1(int i)
    {
        return 3;
    }
    
    //Default Method
    @Override
    default int defaultMethod1(int i)
    {
        return 3;
    }
}
class Class8<T> implements Interface11<T>
{
    Interface8 itfInterface8;
    
    int i = Interface8.IINTEGER1;
    int j = Interface8.IINTEGER2;
    
    int k = Interface8.staticMethod1(); 
    int l = Interface11.staticMethod1(1);
        
    int n = itfInterface8.defaultMethod1(i);
    
    @Override
    public Integer method1(Integer t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int method1(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T method2(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object method2(Object t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}