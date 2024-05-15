class ClassTemplate
{
    //Fields
    int          iTopLevel                   = 0;
    final int    iTopLevelFinal              = 0;
   
    static int   iTopLevelStatic             = 0;
    static final int iTopLevelStaticFinal    = 0;
   
    //Initialisation Blocks
    static
    {
        
    }
    {


    }
    
    //Methods
    int method()
    {
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
abstract class AbstractClass
{
    //Fields
    int          iTopLevel                   = 0;
    final int    iTopLevelFinal              = 0;
   
    static int   iTopLevelStatic             = 0;
    static final int iTopLevelStaticFinal    = 0;
    
    //Initialisation Blocks
    static
    {
        iTopLevelStatic = staticMethod();
    }
    {
       
    }
    
    //Concrete Methods
    int method()
    {        
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
    
    //Abstract Methods
    abstract void moveTo(double deltaX, double deltaY);
    
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
interface Interface
{

}
enum Enum
{

}