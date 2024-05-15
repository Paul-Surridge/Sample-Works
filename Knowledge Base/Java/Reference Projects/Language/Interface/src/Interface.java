/*
    Access Modifier

        None/Blank:     Package Only
        Public:         Global (must be name of file)
*/
public interface Interface
{
    /*
        Fields
        
        - All implicit 'public' 'static' 'final' (can be omitted).
        - All Class Variables - No Instance Variables.
        - Must be initialised when declared.
        - All fields are specific to this interface.
        - Fields with the same name in super interface are hidden (not specifically overriden).
    */
    int                     ISUPER = 10;
    
    int                     INTEGER1 = 1;
    final int               INTEGER2 = 2;
    static int              INTEGER3 = 3;
    final static int        INTEGER4 = 4;
    
    String                  STRING1 = "Test 1";
    final String            STRING2 = "Test 2";
    static String           STRING3 = "Test 3";
    final static String     STRING4 = "Test 4";
    
    Class1                  objClass1 = new Class1(3);
    final Class1            objClass2 = new Class1(2);
    static Class1           objClass3 = new Class1(3);
    final static Class1     objClass4 = new Class1(4);   
    
    

    /*
        Methods:
    
        - All methods:
    
            1. Implicit 'public'
            2. Not permitted 'final'
    
        - Overload:     Permitted for all types.
        - Override:     Permitted Abstract and Default.
    */
    
    //Abstract Methods
    int abstractMethod1(String i);
    int abstractMethod2(int i);
    int abstractMethod3(int i) throws IllegalArgumentException;
    
    
    
    /*
        Static Methods

        - Specific to interface.
        - Utility method.
        - Accessible via interface direct: 	Interface.staticMethod();
    */
    static int staticMethod(int i, int j)
    {
        return i + 1;
    }
    static int staticMethod(int i)
    {
        return i + 2;
    }

    
    
    
    /*
        Default Methods

        - Core method.
        - Accessible via interface variable: 	InterfaceVariable.defaultMethod();
    */
    default int defaultMethod(int i)
    {
        return i + 2;
    }
    
    
    
    //Nested Class
    class                   NestedClass1
    {

    }
    static class            NestedClass2
    {

    }
    final class             NestedClass3
    {
        
    }
    final static class      NestedClass4
    {
        
    }
    
    //Nested Abstract Class
    abstract class          NestedClass5
    {
        
    }
    abstract static class   NestedClass6
    {
        
    }
    //abstract final class        Class7                    Abstract and Final not permitted    (given that Abstract Classes are intended to be extended)

    //abstract final static class Class8                    Abstract and Final not permitted    (given that Abstract Classes are intended to be extended)

    
    //Nested Interface
    interface               NestedInterface1
    {
        
    }
    static interface        NestedInterface2
    {
        
    }
    //final interface         Interface1_3                  Final and Interface not permitted

    //final static interface  Interface1_4                  Final and Interface not permitted
    
    
    //Nested Enum
    enum                    NestedEnum1
    {
        
    }
    static enum             NestedEnum2
    {
        
    }
    //final enum              Enum3                         Final and Enum not permitted

    //final static enum       Enum4                         Final and Enum not permitted
}



/*
    Class Implementation:

    1. Override:        Class must implement all abstract methods declared in the interface(s) and naturlly any superinterface(s).
    2. @Override:       Class must add @Override annotation to all implemented methods.
    3. 'public'         Class must declare all implemented methods as 'public'.
    4. Ambiguity:       Class must override any ambiguous members if implementing multiple interface(s).
*/
class Class1 implements Interface
{
    private static int          iStatic;
    private static Interface    itfInterfaceStatic;
            
    private int                 i, iNonStatic;
    private Interface           itfInterfaceNonStatic;
    
    public Class1(int i) {
    
        this.i = i;
    }


    
    
    //All implementing classes must override all abstract methods in the interface and any super interfaces.
    @Override
    public int abstractMethod1(String j) {
        return 1;
    }
    
    @Override
    public int abstractMethod2(int sTest) {
        return i;
    }

    @Override
    public int abstractMethod3(int i) {
        return i;
    }

    
    
    
    
    
    
    //Non-Static context can contain static and non-static variables.    
    public void method1()
    {
        iNonStatic  = itfInterfaceNonStatic.defaultMethod(1);
        iStatic     = itfInterfaceNonStatic.defaultMethod(1);
        
        iNonStatic  = itfInterfaceStatic.defaultMethod(1);
        iStatic     = itfInterfaceStatic.defaultMethod(1);
        
        iNonStatic  = Interface.staticMethod(1);
        iStatic     = Interface.staticMethod(1);
    }

    
    
    
    
    /*
        - Static context can only contain static variables.
        
            - Compiles:     If both called and return variable are static.
            - Fail:         If either the called or return variable is non-static.
    */
    public static void staticMethod1()
    {
        // iStatic      = itfInterfaceNonStatic.defaultMethod(1);       //Compile Error:        itfInterfaceNonStatic   is non-static
        // iNonStatic   = itfInterfaceStatic.defaultMethod(1);          //Compile Error:        iNonStatic              is non-static
        iStatic = itfInterfaceStatic.defaultMethod(1);                  //Only compiles if both called and return variable are static
        
        // iNonStatic   = Interface1.staticMethod(1);                   //Compile Error:        iNonStatic              is non-static
        iStatic = Interface.staticMethod(1);                            //Only compiles if both called and return variable are static
    }
}










/*
    Interface Variables
*/
interface Interface1
{
    int ISUPER = 1;

    int abstractMethod(int i);

    static int staticMethod(int i)
    {
        return i + 1;
    }
    default int defaultMethod(int i)
    {
        return i + 3;
    }
}
interface Interface2 extends Interface1
{
    int ISUPER = 2;
    
    @Override
    int abstractMethod(int i);
    
    static int staticMethod(int i)
    {
        return i + 1;
    }
    @Override
    default int defaultMethod(int i)
    {
        return 2;
    }
}
class Class2 implements Interface1
{
    @Override
    public int abstractMethod(int i)
    {
        return 1;
    }
}
class Class3 extends Class2
{
    
}
class Class4 implements Interface1
{
    @Override
    public int abstractMethod(int i)
    {
        return 1;
    }
}
class Class5
{
    Interface1      infInterface1;
    Interface2      infInterface2;
    
    Class2 objClass2 = new Class2();
    Class3 objClass3 = new Class3();
    Class4 objClass4 = new Class4();
    
    void method1()
    {
        //Interface Variable may be assigned any value which ultimately implements its interface.
        infInterface1 = objClass2;
        infInterface1 = objClass3;
        infInterface1 = objClass4;
    }  
}





interface Relatable{}

class Class6
{
    
}
class Class7 extends Class6 implements Relatable
{
    
}
class Class8 extends Class7
{
    
}
class Class10
{
    int j =10;
    static String sTest;
    
    Class6 objClass6 = new Class6();
    Class7 objClass7 = new Class7();
    Class8 objClass8 = new Class8();
    
    Relatable itfRelatable1;
    Relatable itfRelatable2;
    
    Class10(int i)
    {
        this.j = i;
    }
    
    void method()
    {
        j = 20;
        itfRelatable1 = objClass8;
        //itfRelatable = objClass6;   //Compile Fail given Class6 does not implement Relatable
        itfRelatable1 = itfRelatable2;
    }
    
    static void methodStatic()
    {
        
    }
}