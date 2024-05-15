interface IntegerMath
{
    int operation(int a, int b);
    //void operation(int a, float b);
}
interface Predicate1<T, P>
{
    boolean test(T t, P p, int i);
}
interface Predicate2
{
    boolean test(int i);
}

class ClassA
{
    IntegerMath itfIntegerMath = (m, n) -> m - n;         //OK:         Initialise Functional Interface with an Anonymous Object via Lambda Expression
    //ClassB objClassB = (m, n) -> m - n;                   Fail:       ClassB not a Functional Interface
    //ClassC objClassC = (m, n) -> m - n;                   Fail:       ClassC not a Functional Interface
    Predicate2 itfPredicate2;
    
    void method1(IntegerMath iMath, Predicate1<String, Boolean> predicate)
    {
        
    }
    void method2(IntegerMath iMath, ClassB classB)
    {
        
    }
    void method3()
    {
        //Array Initialisation
        IntegerMath[] arrIntegerMath = {(m, n) -> m - n + 1, (m, n) -> m - n + 2};
        
        //Assignment
        itfIntegerMath = (m, n) -> m - n + 1;
        itfIntegerMath = (m, n) -> m - n + 2;
        itfIntegerMath = (m, n) -> m - n + 2;
        
        //Without parenthesis if only x1 parameter within Abstract Method.
        itfPredicate2 = p -> p>10;
        
        //Explicit Declaration
        itfIntegerMath = (int m, int n) ->
        {
            //Lambda Expression Body
            IntegerMath itfIntegerMathLocal = (a, b) -> a - b + 1;
            
            return m - n + 2;
        };
    }
    IntegerMath method4()
    {
        //Return Statement
        return (m, n) -> m - n + 3;
    }
    
    void methodInvoke()
    {
        method1(
            (i, j) -> i + j,
            (sName, sAddress, iNumber) ->
            {
                String sLocal = sName + " " + sAddress;
                
                return (sLocal.length() > 10);
            });
        
        /*
        method2(
            (i, j) -> i + j,
            (m, n) -> n + m                                 Fail:       Only possible to initialise Functional Interface not a Class via Lambda Expression
            );
        */
    }
}

class ClassB implements IntegerMath
{
    @Override
    public int operation(int a, int b) {
        return 0;
    }
}
class ClassC
{
    public int method(int a, int b) {
        return 0;
    }
}

//Scoping
/*
    Summary:

        - Lambda Expressions has x2 possible immediate enclosing environments:

            1. Class:       Variable Initialisation
            2. Method:      In non-Static memory.
            3. Method:      In Static memory.

        - Access of enclosing scope:

            1. Class

                - All:                  Everything at Class Level
                - Non-Static:           Members
                - Static:               Members

            2. Non-Static:  Method/Context

                - All:                  Everything at Class and Method Level
                - Class:

                    - Non-Static:       Members
                    - Static:           Members

                - Method:

                    - Parameters:       (Final or Effectively Final)
                    - Local Variables:  (Final or Effectively Final)

            3. Static:      Method/Context

                - Class:

                    - Static:           Members Only (No Non-Static Members)

                - Method:

                    - Parameters:       (Final or Effectively Final)
                    - Local Variables:  (Final or Effectively Final)

    Lambda Expression has access to everything at Class and Method level except when contained within a Static memory whereby only having access to Static Class members.
*/
class ClassD
{
    int i;
    static int iStatic;
    
    final int iFinal = 0;
    static final int iStaticFinal = 0;
    
    IntegerMath itfIntegerMath = (m, n) ->
    {
        //Class Members - Fields
        i = 10;
        iStatic = 10;
        
        i = iFinal;
        iStatic = iStaticFinal;
        
        //Class Members - Methods
        method1(10);
        methodStatic1(10);
        
        return m - n;
    };
    
    void method1(int a)
    {
        int iLocal = 0;
        final int iTest = 0;
        
        IntegerMath itfIntegerMath1 = (m, n) ->
        {
            int iLambda = 0;
            final int iStaticLambda = 10;

            //Class Members - Fields
            i = 10;
            iStatic = 10;
            
            i = iFinal;
            iStatic = iStaticFinal;
            
            //Class Members - Methods
            method2(10);
            methodStatic2(10);
        
            //Local Variables
            i = iLocal;
            i = iTest;
            
            //Local Parameters
            i = a;

            //Of course methods of methods not possible.
            
            return m - n;
        };
    }
    void method2(int a)
    {
        
    }
    
    static void methodStatic1(int a)
    {
        int iLocal = 0;
        final int iTest = 0;
        
        IntegerMath itfIntegerMath1 = (m, n) ->
        {
            int iLambda = 0;
            final int iStaticLambda = 10;
            
            //Class Members - Fields
            //i = 10;                                       Fail:           Non-Static Members not permitted in Static Context
            iStatic = 10;
            
            //iStatic = iFinal;                             Fail:           Non-Static Members not permitted in Static Contex
            iStatic = iStaticFinal;
            
            //Class Members - Methods
            //method2(10);                                  Fail:           Non-Static Members not permitted in Static Context
            methodStatic2(10);
        
            //Local Variables
            iStatic = iLocal;
            iStatic = iTest;
            
            //Local Parameters
            iStatic = a;

            //Of course methods of methods not possible.
            
            return m - n;
        };
    }
    static void methodStatic2(int a)
    {
        
    }
}