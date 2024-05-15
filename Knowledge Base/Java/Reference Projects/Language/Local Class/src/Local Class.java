class Class1
{
    boolean     bOuterClass;
    
    int         iOuterClass;
    static int  iOuterClassStatic;
    
    //Static Initialisation Block
    static
    {
        class LocalClass
        {
            
        }
    }
    
    //Non-Static Initialisation Block
    {
        class LocalClass
        {
            
        }
    }
    
    Class1(int i)
    {
        
    }
    
    int nonStaticMethod(int i)
    {
        //Local Variables
        int                 iLocalMethod1 = 0;                  //Effectively Final Field
        final int           iLocalMethod2 = 0;                  //Constant Field
        //static int        iLocalMethodStatic = 0;             //Fail      No static member within block - Static members are only for Classes.
        //static final int  iLocalMethodConstant = 0;           //Fail      No static member within block - Static members are only for Classes.
            
        //private class LocalClass                              //Fail      No Access Modifiers may be applied to Local Class.
        class LocalClass
        {
            int                 iLocal = 0;
            //static int        iLocalStatic = 0;               //Fail      No static member of any Local Class.
            final int           iLocalConstant1 = 0;
            static final int    iLocalConstant2 = 1;
            
            void localMethod()
            {
                //Outer Class
                iOuterClass = 10;                               //OK
                iOuterClassStatic = 10;                         //OK
                
                //Outer Non-Static Method
                iLocal = iLocalMethod1;                         //OK        As long as iLocalMethod remains 'effectively final'
                iLocal = iLocalMethod2;                         //OK
                iLocal = i;                                     //OK
                
                //Local Class
                iLocal = 10;                                    //OK
                iLocal = iLocalConstant1;                       //OK
                iLocal = iLocalConstant2;                       //OK
            }
        }
        
        LocalClass objLocalClass = new LocalClass();
        //static LocalClass objLocalClass = new LocalClass();   //Fail      No static member within block - Static members are only for Classes.
        
        if(bOuterClass)
        {
            //class LocalClass                                  //Fail      Local Class may not have same name/identifer as another Local Class in Outer Method.
            //{}
        }
        
        return 0;
    }
    static int StaticMethod(int i)   
    {
        //Local Variables
        int                 iLocalMethod1 = 0;                  //Effectively Final Field
        final int           iLocalMethod2 = 0;                  //Constant Field
        //static int        iLocalMethodStatic = 0;             //Fail      No static member within block - Static members are only for Classes.
        //static final int  iLocalMethodConstant = 0;           //Fail      No static member within block - Static members are only for Classes.
            
        class LocalClass
        {
            int                 iLocal = 0;
            //static int        iLocalStatic = 0;               //Fail      No static member of any Local Class.
            final int           iLocalConstant1 = 0;
            static final int    iLocalConstant2 = 1;
            
            void localMethod()
            {
                //Outer Class
                //iOuterClass = 10;                             //Fail      Local Class within Static Method/Context cannot access Instance Variable of Outer Class
                iOuterClassStatic = 10;                         //OK
                
                //Outer Non-Static Method
                iLocal = iLocalMethod1;                         //OK        As long as iLocalMethod remains 'effectively final'
                iLocal = iLocalMethod2;                         //OK
                iLocal = i;                                     //OK
                
                //Local Class
                iLocal = 10;                                    //OK
                iLocal = iLocalConstant1;                       //OK
                iLocal = iLocalConstant2;                       //OK
            }
        }
        
        LocalClass objLocalClass = new LocalClass();
        //static LocalClass objLocalClass = new LocalClass();   //Fail      No static member within block - Static members are only for Classes.
        
        return 0;
    }  
}
//Nesting
class Class2
{
    int i = 0;
    
    void method()
    {
        int j = 0;
        
        class LocalClass1
        {
            int k = 0;
            
            void method()
            {
                int l = 0;
                
                class LocalClass2
                {
                    int m = 0;
                    
                    void method()
                    {
                        i = 1;
                        //j = 1;
                        k = 1;
                        //l = 1;
                        m = 1;
                        boolean bTest = false;
                        
                        if(bTest)
                        {
                            class LocalClass3
                            {
                                void method()
                                {

                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    static void staticMethod()
    {
        class LocalClass1
        {
            void method()
            {
                class LocalClass2
                {
                    void method()
                    {
                        class LocalClass3
                        {
                            void method()
                            {

                            }
                        }
                    }
                }
            }
        }
    }
}