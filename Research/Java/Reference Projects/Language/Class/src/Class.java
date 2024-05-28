class ClassA
{
    //Fields
    int                     iSuper = 10;
    
    /*
        Non-Static Fields:

            - Placed within the 'non-static' memory of ClassA.
            - Associated with Instances of Class.
            - Default value until assigned otherwise.
    
                0, '', false, null
    
        Non-Static Constants:
    
            - Constants must be initialised when class instantiated i.e.
    
                1. Declared
                2. Constructor(s).
    
            - Constants once initialised cannot be assigned an alternate value.
    */
    int                     iInteger1;
    String                  sString1;
    final int               iInteger2;
    final String            sString2     = "Test 2";
    
    /*
        Static Fields:

            - Placed within the 'static' memory of ClassA.
            - Associated with Class.
            - Default value until assigned otherwise.
    
                0, '', false, null
    
        - Static Constants:
    
            - Static Constants must be initialised when class loaded i.e.
    
                1. Declared
                2. Static Initialise Block.
    
            - Static Constants once initialised cannot be assigned an alternate value.
    */
    static int              iInteger3;
    static String           sString3     = "Test 3";
    static final int        iInteger4;
    static final String     sString4     = "Test 4";
    
    //Initialisation Block:         Copied to all Constructors, significantly reducing amount of code.
    {
        iInteger1 = 10;
        sString1 = "Test";
    }
    
    //Static Initialisation Block:  Ran when Class is loaded, to allow complex logic to initialise static fields.
    static
    {
        iInteger4 = 4;
    }
    
    //Constructors
    ClassA()
    {
        iInteger2 = 2;
    }
    ClassA(int i)
    {
        iInteger2 = 2;
    }
    ClassA(ClassA objClassToBeCopied)
    {
        iInteger2 = 2;
    }

    //  Non-Static Memory:
    //
    //      Access:                     non-Static + Static members of outer nested scopes and top level form
    //      Local Variable/Parameter:   Must be 'final' or 'effectively final' i.e. does not change value once initialised.

    //  Method Overloading:             Method Signature (Identifier, Parameter List) must be different (not just return type).
    void method1()
    {
        System.out.println("Class A: Method 1");
    }
    int method1(int i)
    {
        System.out.println("Class A: Method 1 (Overloaded)");
        
        return 0;
    }
    
    //Nested Class(es)
    class                   InnerClass
    {
        
    }
    //Nested Abstract Class
    abstract class          AbstractClass
    {
        
    }
    
    
    //  Static Memory:
    //
    //      Access:       Static members of outer nested scopes and top level form
    
    static void staticMethod1()
    {
        System.out.println("Class A: Static Method 1");
    }
    static int staticMethod1(int i)
    {
        System.out.println("Class A: Static Method 1 (Overloaded)");
            
        return 1;
    }
    
    
    static class            StaticNestedClass
    {
        
    }
    abstract static class   StaticAbstractClass
    {
        
    }
    
    //Nested Interface(s)
    interface               NonStaticNestedInterface    //Implicitly/automatically placed in Static memory
    {
        
    }
    static interface        StaticNestedInterface
    {
        
    }
    
    //Nested Enum(s)
    enum                    NonStaticNestedEnum         //Implicitly/automatically placed in Static memory
    {
        
    }
    static enum             StaticNestedEnum
    {
        
    }
}
class ClassB extends ClassA
{
    int     iInteger1    = 1; 
    String  sString1     = "Test 5";

    ClassA  objClassA   = new ClassA(1);

    public ClassB(int i) {
    
        //super() must be first statement in contructor
        //It does not need to be called, in which case the default constructor of ClassA is called.
        super(i);
    }

    @Override
    void method1() {
        System.out.println("Class B: Method 1");
    }

    @Override
    int method1(int i) {
        System.out.println("Class B: Method 1 (Overridden and Overloaded)");
        return 0;
    }
    
    //Static methods not overridable given that they are associated and referenced directly to the class rather than any instances.
}

//Empty Top Level Class Template
class ClassC
{
    //Fields
    int                 iTopLevel               = 0;
    final int           iTopLevelFinal          = 0;
   
    static int          iTopLevelStatic         = 0;
    static final int    iTopLevelStaticFinal    = 0;
   
    //Initialisation Blocks
    static
    {
        
    }
    {


    }
    
    ClassC()
    {
        
    }
    
    //Methods
    int method()
    {
        class LocalClass
        {
            
        }
        
        return 0; 
    }
    static int staticMethod()
    {        
        return 0;
    }
    final int finalMethod()
    {
        return 0; 
    }
    final static int finalStaticMethod()
    {
        return 0;
    }
    
    //Nested Types
    
    //Nested Class
    class                   Class1
    {

    }
    static class            Class2
    {

    }
    final class             Class3
    {
        
    }
    final static class      Class4
    {
        
    }
    
    //Nested Abstract Class
    abstract class          Class5
    {
        
    }
    abstract static class   Class6
    {
        
    }
    //abstract final class        Class7                    Abstract and Final not permitted    (given that Abstract Classes are intended to be extended)

    //abstract final static class Class8                    Abstract and Final not permitted    (given that Abstract Classes are intended to be extended)

    
    //Nested Interface
    interface               Interface1
    {
        
    }
    static interface        Interface2
    {
        
    }
    //final interface         Interface3                    Final and Interface not permitted

    //final static interface  Interface4                    Final and Interface not permitted
    
    
    //Nested Enum
    enum                    Enum1
    {
        
    }
    static enum             Enum2
    {
        
    }
    //final enum              Enum3                         Final and Enum not permitted

    //final static enum       Enum4                         Final and Enum not permitted

}
public class Class {

    public static void main(String[] args) {
        
        ClassA  objClassA = new ClassA(1);
        ClassB  objClassB = new ClassB(1);
        
        objClassA.method1();
        objClassA = objClassB;
        objClassA.method1();
    }
}
