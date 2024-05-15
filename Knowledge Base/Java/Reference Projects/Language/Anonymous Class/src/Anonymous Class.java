class ClassA
{
    int iClassA;
    
    ClassA(int i)
    {
        iClassA = i;
    }
    
    void method()
    {}
}
class ClassB
{
    void method(ClassA objClassA)
    {}
}
class ClassC
{
    boolean         bTopLevelClass;
    
    int             iTopLevelClass;
    static int      iTopLevelClassStatic;
    
    ClassB          objClassB   = new ClassB();
    static ClassB   sobjClassB  = new ClassB();
    
    void nonStaticMethod(int iMethodParameter)
    {
        //Local Variables
        int                     iMethod = 0;                        //Effectively Final Field
        final int               iMethodConstant = 0;                //Constant Field
        //static int            iMethodStatic = 0;                  //Fail      No static member within method - Static members are only for Class Forms.
        //static final int      iMethodStaticConstant = 0;          //Fail      No static member within method - Static members are only for Class Forms.
        
        objClassB.method(new ClassA(0)                              //Extended class must be passed any necessary constructor arguments.
        {
            //Anonymous Class Fields                                //Additional fields can be added to the Anonymous Class
            int                 iLocal = 0;
            //static int        iLocalStatic = 0;                   //Fail      No static member of any Anonymous Class.
            final int           iLocalConstant = 0;
            static final int    iLocalStaticConstant = 1;
            
            //Anonymous Class Methods                               Additional methods can be added to the Anonymous Class
            void localMethod()
            {
                //TopLevel Class
                iTopLevelClass = 10;                                //OK
                iTopLevelClassStatic = 10;                          //OK
                
                //TopLevel Non-Static Method
                iLocal = iMethodParameter;                          //OK        As long as iTopLevelBlockParameter remains 'effectively final'
                iLocal = iMethod;                                   //OK        As long as iLocal remains 'effectively final'
                iLocal = iMethodConstant;                           //OK
                iLocal = iClassA;                                   //OK        Anonymous Class simply treated as a subclass having access to ClassA fields.
                
                //Anonymous Class
                iLocal = 10;                                        //OK
                iLocal = iLocalConstant;                            //OK
                iLocal = iLocalStaticConstant;                      //OK
            }
            
            @Override
            void method()
            {
                //Override method from ClassA as required
            }
        });
    }
    
    
    /*
        Static Memory:
    
            - A method or anonymous class are identical regardless if is non-Static or Static memory.
            - Except:
    
                1. A method or anonymous class in Static Memory only has access to Static Memory in Parental Chain and Top Level.
    */
    static int StaticMethod(int iMethodParameter)   
    {
        //Local Variables
        int                     iMethod = 0;                        //Effectively Final Field
        final int               iMethodConstant = 0;                //Constant Field
        //static int            iMethodStatic = 0;                  //Fail      No static member within method - Static members are only for Class Forms.
        //static final int      iMethodStaticConstant = 0;          //Fail      No static member within method - Static members are only for Class Forms.
        
        sobjClassB.method(new ClassA(0)                             //Extended class must be passed any necessary constructor arguments.
        {
            //Anonymous Class Fields                                //Additional fields can be added to the Anonymous Class
            int                 iLocal = 0;
            //static int        iLocalStatic = 0;                   //Fail      No static member of any Anonymous Class.
            final int           iLocalConstant = 0;
            static final int    iLocalStaticConstant = 1;
            
            //Anonymous Class Methods                               Additional methods can be added to the Anonymous Class
            void localMethod()
            {
                //TopLevel Class
                //iTopLevelClass = 10;                              //Fail      Static Memory may not access non-Static memory
                iTopLevelClassStatic = 10;                          //OK
                
                //TopLevel Non-Static Method
                iLocal = iMethodParameter;                          //OK        As long as iTopLevelBlockParameter remains 'effectively final'
                iLocal = iMethod;                                   //OK        As long as iLocal remains 'effectively final'
                iLocal = iMethodConstant;                           //OK
                iLocal = iClassA;                                   //OK        Anonymous Class simply treated as a subclass having access to ClassA fields.
                
                //Anonymous Class
                iLocal = 10;                                        //OK
                iLocal = iLocalConstant;                            //OK
                iLocal = iLocalStaticConstant;                      //OK
            }
            
            @Override
            void method()
            {
                //Override method from ClassA as required
            }
        });
        
        return 0;
    }
}





//Examples
interface InterfaceA
{
    void method1(int i);
    int method2(int i);
}
class ClassD
{
    /*
        Anonymous Class:    May be used in non-Static or Static memory.
    */
    //Instance Variable Initialisation
    ClassA objInstanceVariable = (new ClassA(10)
    {
        @Override
        public void method()
        {
            System.out.println("Hello World!");
        }
    });
    
    //Class Variable Initialisation
    static ClassA objClassVariable = (new ClassA(10)
    {
        @Override
        public void method()
        {
            System.out.println("Hello World!");
        }
    });
    
    
    
    
    
    
    /*
        Interface Variable Initialisation
    
            - InterfaceA initialised with arbitrary object which implements InterfaceA.
    */
    InterfaceA itfInterfaceA = (new InterfaceA()
    {
        @Override
        public void method1(int i)
        {
            System.out.println("Hello World!");
        }

        @Override
        public int method2(int i)
        {
            return 0;
        }
    });
    
    void method1()
    {
        //Local Variable Initialisation
        ClassA objInstanceVariable = (new ClassA(10)
        {
            @Override
            public void method()
            {
                System.out.println("Hello World!");
            }
        });
        
        //Local Interface Variable Initialisation
        InterfaceA itfInterfaceA = (new InterfaceA()
        {
            @Override
            public void method1(int i)
            {
                System.out.println("Hello World!");
            }

            @Override
            public int method2(int i)
            {
                return 0;
            }
        });
    }
    
    void method2(ClassA objClassA, InterfaceA itfInterfaceA)
    {
        
    }
    
    void method3()
    {
        method2
        (
            (
                new ClassA(10)             //Expression:   Returning anonymous instantiation of Subclass of ClassA as an Argument
                {
                    @Override
                    public void method()
                    {
                        System.out.println("Hello World!");
                    }
                }
            ),
            (
                new InterfaceA()           //Expression:   Returning anonymous instantiation of InterfaceA as an Argument
                {
                    @Override
                    public void method1(int i)
                    {
                        System.out.println("Hello World!");
                    }

                    @Override
                    public int method2(int i)
                    {
                        return 0;
                    }
                }
            )
        );
    }
}




class ClassE
{
    /*
        - x3 Objects assigned to x3 Interface Variables.
    
            English:    via Local Class         (Extended Local Class)
            French:     via Interface:          (Direct Implementation of the Interface)
            Spanish:    via Interface:          (Direct Implementation of the Interface)
    */

    //Nested Interface
    interface Interface 
    {
        public void greet();
        public void greetSomeone(String someone);
    }
  
    public void sayHelloIn3Languages()
    {
        
        /*
            Local Class: English Greeting
        
                - Extended Local Class
            _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _
        */
        class LocalClass implements Interface
        {
            int i;
            
            LocalClass(int i)
            {
                this.i = i;
            }
            
            @Override
            public void greet()
            {
                greetSomeone("world");
            }
            
            @Override
            public void greetSomeone(String sName)
            {
                System.out.println("Hello " + sName);
            }
        }
      
        Interface greetingEnglish = new LocalClass(0);
        
        /*
            Anonymous Class: French Greeting
        
                - Direct implementation of the Interface using an Arbitrary object.
            _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _
        */
        Interface greetingFrench = new Interface()
        {
            @Override
            public void greet() 
            {
                greetSomeone("tout le monde");
            }
            
            @Override
            public void greetSomeone(String sName) 
            {
                System.out.println("Salut " + sName);
            }
        };
        
        /*
            Anonymous Class: Spanish Greeting
        
                - Direct implementation of the Interface using an Arbitrary object.
            _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _
        */
        Interface greetingSpanish = new Interface()
        {
            @Override
            public void greet() 
            {
                greetSomeone("mundo");
            }
            
            @Override
            public void greetSomeone(String sName) 
            {
                System.out.println("Hola, " + sName);
            }
        };
        
        greetingEnglish.greet();
        greetingFrench.greetSomeone("Fred");
        greetingSpanish.greet();
    }

    public static void main(String... args)
    {
        ClassE objClassE = new ClassE();
        
        objClassE.sayHelloIn3Languages();
    }            
}