class TopLevelClass
{
    /*
        Nested Class - Instantiation:
    
            Within Top Level/Enclosing Form:

                Top Level		Nested
                Memory          Class
                _______________________________________________

                non-Static		non-Static Only
                Static          Static Only

                Within the top level/enclosing form, nested classes may only be instantiated within their respective memory locations where they are declared.

            Within External Form:

                Top Level		Nested
                Memory          Class
                _______________________________________________

                non-Static		non-Static + Static
                Static          non-Static + Static

                An External Class, may place a nested class from either non-Static/Static memory into either its own non-Static/Static memory.
    
            1 or 2-Step Process:
    
                - If a nested class is within non-Static, an instance of the top level class is needed prior in order to instantiate the nested class.
                - This is so that any dependencies within the nested class have been initialised.
                - A nested class within Static memory (which does not have any dependencies on the top level class) may of course be instantiated directly.
    
                Top Level               Require                 Process
                Memory                  Top Level Instance      Steps
                __________________________________________________________

                non-Static              Y                       2
                Static                  -                       1
    */
    TopLevelClass                                                       obj1 = new TopLevelClass();
    TopLevelClass.nestedClass0                                          obj2 = new TopLevelClass().new nestedClass0();
    TopLevelClass.nestedClass0.nestedClass1                             obj3 = new TopLevelClass().new nestedClass0().new nestedClass1();
 
    static staticNestedClass0                                           obj4 = new staticNestedClass0();
    static staticNestedClass0.staticNestedClass1                        obj5 = new staticNestedClass0.staticNestedClass1();
    static staticNestedClass0.staticNestedClass1.staticNestedClass2     obj6 = new staticNestedClass0.staticNestedClass1.staticNestedClass2();
    
    static staticNestedClass0                                           obj7 = new staticNestedClass0();
    static staticNestedClass0.nestedClass1                              obj8 = obj7.new nestedClass1();
    
    int method()
    {
        return 0;
    }
    static int staticMethod()
    {
        return 0;
    }
    
    class nestedClass0
    {
        class nestedClass1
        {
            int i;
            
            int method()
            {
                return 0;
            }
        }
    }
    
    static class staticNestedClass0
    {
        class nestedClass1
        {
            int i;
            
            int method()
            {
                return 0;
            }
        }
                
        static class staticNestedClass1
        {
            static class staticNestedClass2
            {
                int i;
                static int iStatic;
            
                int method()
                {
                    return 0;
                }
                
                static int staticMethod()
                {
                    return 0;
                }
            }
        }
    }
}

class External
{
    TopLevelClass                                                                   obj1 = new TopLevelClass();
    TopLevelClass.nestedClass0                                                      obj2 = new TopLevelClass().new nestedClass0();
    TopLevelClass.nestedClass0.nestedClass1                                         obj3 = new TopLevelClass().new nestedClass0().new nestedClass1();
 
    static TopLevelClass                                                            obj4 = new TopLevelClass();
    static TopLevelClass.nestedClass0                                               obj5 = new TopLevelClass().new nestedClass0();
    static TopLevelClass.nestedClass0.nestedClass1                                  obj6 = new TopLevelClass().new nestedClass0().new nestedClass1();
    
    
    
    TopLevelClass.staticNestedClass0                                                obj7 = new TopLevelClass.staticNestedClass0();
    TopLevelClass.staticNestedClass0.staticNestedClass1                             obj8 = new TopLevelClass.staticNestedClass0.staticNestedClass1();
    TopLevelClass.staticNestedClass0.staticNestedClass1.staticNestedClass2          obj9 = new TopLevelClass.staticNestedClass0.staticNestedClass1.staticNestedClass2();
    
    TopLevelClass.staticNestedClass0                                                obj10 = new TopLevelClass.staticNestedClass0();
    TopLevelClass.staticNestedClass0.nestedClass1                                   obj11 = obj10.new nestedClass1();
    
    
    
    static TopLevelClass.staticNestedClass0                                         obj12 = new TopLevelClass.staticNestedClass0();
    static TopLevelClass.staticNestedClass0.staticNestedClass1                      obj13 = new TopLevelClass.staticNestedClass0.staticNestedClass1();
    static TopLevelClass.staticNestedClass0.staticNestedClass1.staticNestedClass2   obj14 = new TopLevelClass.staticNestedClass0.staticNestedClass1.staticNestedClass2();
    
    static TopLevelClass.staticNestedClass0                                         obj15 = new TopLevelClass.staticNestedClass0();
    static TopLevelClass.staticNestedClass0.nestedClass1                            obj16 = obj15.new nestedClass1();
    
    
    //x2-Step
    TopLevelClass                                                                   obj17   = new TopLevelClass();
    TopLevelClass.nestedClass0                                                      obj18   = obj17.new nestedClass0();
    
    //x1-Step
    TopLevelClass.nestedClass0                                                      obj19   = new TopLevelClass().new nestedClass0();
    
    
    
    //x1-Step
    static TopLevelClass.staticNestedClass0                                         obj20      = new TopLevelClass.staticNestedClass0();
    
    
    /*
        Top Level       Access to Nested Class
        Memory          Memory
        ______________________________________
    
        non-Static      non-Static + Static
        Static          non-Static + Static
        
        From the perspective of the External Class:
    
            1. If a nested class had its own non-Static or Static memory is irrlevent.
            2. A nested class (regardless if defined within non-Static or Static) is 'just a class' and can be considered just as any other Top Level Class.
            2. External Class has access to all members of the nested class that it was permitted to declare.
            4. Nested Class members do not get 'blocked' depending on which memory the instantiation of the nested class was placed in the External class.
    
        The only consideration is where the instantiation of a nested class has been placed within the External Class i.e. only static memory may access static memory, non-Static to both.
    */
    
    int i;
    static int iStatic;
    
    int method()
    {
        //Top Level:    non-Static Nested Class
        //External:     non-Static
        
            //Access non-Static memory via object
            i = obj3.i;
            i = obj3.method();
            
        //Top Level:    non-Static Nested Class
        //External:     Static
        
            //Access non-Static memory via object
            i = obj6.i;
            i = obj6.method();
            
            
            
            
            
            
        //Top Level:    Static Nested Class
        //External:     non-Static
        
            //Access non-Static memory via object
            i = obj9.i;
            i = obj9.method();

            //Access Static memory via object
            i = obj9.iStatic;
            i = obj9.staticMethod();
            
        //Top Level:    Static Nested Class
        //External:     Static
        
            //Access non-Static memory via object
            i = obj14.i;
            i = obj14.method();

            //Access Static memory via object
            i = obj14.iStatic;
            i = obj14.staticMethod();

            
            
            
        //Access Static memory via class
            i = TopLevelClass.staticNestedClass0.staticNestedClass1.staticNestedClass2.iStatic;
            i = TopLevelClass.staticNestedClass0.staticNestedClass1.staticNestedClass2.staticMethod();
        
        return 0;
    }
    
    
    
    
    
    static int staticMethod()
    {
        //Only possible to access static memory from static memory, therefore obj3 and obj9 not accessible.
        
        //Top Level:    non-Static Nested Class
        //External:     Static
        
            //Access non-Static memory via object
            iStatic = obj6.i;
            iStatic = obj6.method();
            
            

            
        //Top Level:    Static Nested Class
        //External:     Static
        
            //Access non-Static memory via object
            iStatic = obj14.i;
            iStatic = obj14.method();

            //Access Static memory via object
            iStatic = obj14.iStatic;
            iStatic = obj14.staticMethod();

            //Access Static memory via class
            iStatic = TopLevelClass.staticNestedClass0.staticNestedClass1.staticNestedClass2.iStatic;
            iStatic = TopLevelClass.staticNestedClass0.staticNestedClass1.staticNestedClass2.staticMethod();
        
        return 0;
    }
}

class TopLevelClass1
{
    class nestedClass0
    {
        class nestedClass1
        {
            class nestedClass2
            {
                //...indefinitely
            }
        }
    }
    
    static class staticNestedClass0
    {
        class nestedClass1
        {
            class nestedClass2
            {
                class nestedClass3
                {
                    //...indefinitely
                }
            }
        }
        
        static class staticNestedClass1
        {
            static class staticNestedClass2
            {
                static class staticNestedClass3
                {
                    //...indefinitely
                }
            }
        }
    }
}