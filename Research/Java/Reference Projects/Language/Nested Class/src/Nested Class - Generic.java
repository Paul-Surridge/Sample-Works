interface FunctionalInterface
{
    void abstractMethod();
}
class Class1
{
    Class1(int i)
    {
        
    }
}
class Class2
{
    void method(Class1 objClass1)
    {}
}
class Class3<T>
{    
    T objT;
    //static T    onjStaticT;           //T is inherently non-Static (given unknown at compile time) therefore cannot be made static.
    
    Class2          objClass2   = new Class2();
    static Class2   sobjClass2  = new Class2();
    
    //Static Initialisation Block
    static
    {
        //T objT;                       //T is inherently non-Static (given unknown at compile time) therefore cannot be made static.
    }
    
    //Non-Static Initialisation Block
    {
        T objT;                         //T may be invoked but is of limited use.
    }
    
    Class3(int i, T objT)
    {
        this.objT = objT;
    }
    Class3(float i)
    {

    }
    
    
    
    
    /*
        non-Static Memory:
    
            - Type Parameters declared within the non-Static memory of the Top Level shadow those within any outer layer/scope.
            - Compiler checks immediate class and then recursively out until it finds the matching/applicable Type Parameter. 
    */
    int nonStaticMethod(int i)
    {
        class LocalClass1<A>
        {
            T   objT;                       //T is Top Level Class Type Parameter, T is not declared locally therefore compiler refers to the next 'outer' layer/scope.
            A   objA;                       //A is Local Type Parameter.
        }
        class LocalClass2<T, A>
        {
            T   objT;                       //T is Local Type Parameter, shadowing the T declared within any outer/Top Level class(es).
            A   objA;                       //A is Local Type Parameter.
        }
        
        return 0;
    }
    <T> int nonStaticGenericMethod(int i)
    {
        class LocalClass1<A>
        {
            T   objT;                       //T is Local Type Parameter declared by the enclosing method i.e. T is defined when the generic method is invoked, shadowing the T declared within any outer scopes.
            A   objA;                       //A is Local Type Parameter.
        }
        class LocalClass2<T, A>
        {
            T   objT;                       //T is Local Type Parameter, shadowing the T declared within any outer scopes i.e. shadowing both the T declared by the Generic Method and the Top Level Class.
            A   objA;                       //A is Local Type Parameter.
        }
        
        return 0;
    }
    class InnerClass1<A>
    {
        T   objT;                       //T is Top Level Class Type Parameter, T is not declared locally therefore compiler refers to the next 'outer' layer/scope.
        A   objA;                       //A is Local Type Parameter.
    }
    class InnerClass2<T, A>
    {
        T   objT;                       //T is Local Type Parameter, shadowing the T declared within any outer/Top Level class(es).
        A   objA;                       //A is Local Type Parameter.
    }
    
    
    
    
    
    
    
    
    
    /*
        Static Memory:
    
            - Class Type Parameters are inherently non-Static, therefore may not be placed within the static memory of the Top Level Class they are declared.
            - Local Type Parameters are the only Type Parameters which may be declared within the Top Level static memory.
    */
    static int staticMethod(int i)   
    {
        class LocalClass1<A>
        {
            //T   objT;                     //Type Parameters are inherently non-Static, therefore may not be placed within the static memory of the Top Level Class.
            A   objA;                       //A is Local Type Parameter.
        }
        class LocalClass2<T, A>
        {
            T   objT;                       //T is Local Type Parameter.
            A   objA;                       //A is Local Type Parameter.
        }
        
        return 0;
    }
    static <T> int StaticGenericMethod(int i)   
    {
        class LocalClass1<A>
        {
            T   objT;                       //T is Local Type Parameter declared by the enclosing method i.e. T is defined when the generic method is invoked, shadowing the T declared within any outer scopes.
            A   objA;                       //A is Local Type Parameter.
        }
        class LocalClass2<T, A>
        {
            T   objT;                       //T is Local Type Parameter, shadowing the T declared within any outer scopes i.e. shadowing both the T declared by the Generic Method and the Top Level Class.
            A   objA;                       //A is Local Type Parameter.
        }
        
        return 0;
    }
    static class StaticNestedClass1<A>
    {
        //T   objT;                     //Type Parameters are inherently non-Static, therefore may not be placed within the 'static' memory of the Top Level Class.
        A   objA;                       //A is Local Type Parameter.
    }
    static class StaticNestedClass2<T, A>
    {
        T   objT;                       //T is Local Type Parameter.
        A   objA;                       //A is Local Type Parameter.
    }
    
    
    
    
    
    
    
    
    
    
    /*
        Anonymous Class:
    
            - Type Parameters declared within Anonymous Class shadow those within any outer layer/scope.
    */
    void anonymousClass()
    {
        objClass2.method(new Class1(0)
        {
            T                       objT;
            //static int            objStaticT;             //Fail      No static member of any Anonymous Class nor no static Type Parameter
            //final T               objFinalT;              //Fail      No constructor in order to initialise the constant.
            //static final T        objStaticFinalT;        //Fail      No static member of any Anonymous Class nor no static Type Parameter
        });
    }
    
    
    
    
    
    /*
        Lambda Expression:
    
            - Type Parameters declared within Lambda Expression shadow those within any outer layer/scope.
    */
    void lamdaExpression()
    {
        FunctionalInterface itfFunctionalInterface = () ->
        {
            T       objT;
            final T objFinalT;
        };
    }
    
    
    
    
    
    /*
        Lambda Expression:
    
            - Type Parameters declared within Lambda Expression shadow those within any outer layer/scope.
    */
    void block()
    {
        boolean bTest = false;
        
        if(bTest)
        {
            T       objT;
            final T objFinalT;
        }
    }
}