class ClassA
{
    ClassExt        objClassExt     = new ClassExt();
    static ClassExt sobjClassExt    = new ClassExt();

    int i;
    static int iStatic;
    
    int method()
    {
        //static int iStatic = 0;                               Fail:   Not possible for non-Static memory to have own Static Members
        
        //Top Level: non-Static members
        i = objClassExt.i;
        i = objClassExt.method();
        
        i = objClassExt.iStatic;
        i = objClassExt.staticMethod();
        
        
        //Top Level: Static members
        i = sobjClassExt.i;
        i = sobjClassExt.method();
        
        i = sobjClassExt.iStatic;
        i = sobjClassExt.staticMethod();
        
        
        //Direct Referencing via Class 
        i = ClassExt.iStatic;
        i = ClassExt.staticMethod();
        
        InnerClass          objInnerClass           = new InnerClass();
        StaticNestedClass   objStaticNestedClass    = new StaticNestedClass();
        
        return 0;
    }
    static int staticMethod()
    {
        //Instantiation:    Non-Static
        //iStatic = objClassExt.i;                              Fail:       Not possible to reference (non-static) objClassExt in static memory
        //iStatic = objClassExt.method();                       Fail:       Not possible to reference (non-static) objClassExt in static memory
        
        //iStatic = objClassExt.iStatic;                        Fail:       Not possible to reference (non-static) objClassExt in static memory
        //iStatic = objClassExt.staticMethod();                 Fail:       Not possible to reference (non-static) objClassExt in static memory
        
        
        //Top Level: Static
        iStatic = sobjClassExt.i;
        iStatic = sobjClassExt.method();
        
        iStatic = sobjClassExt.iStatic;
        iStatic = sobjClassExt.staticMethod();
        
        
        //Direct Referencing via Class
        iStatic = ClassExt.iStatic;
        iStatic = ClassExt.staticMethod();
        
//        InnerClass          objInnerClass           = new InnerClass();           Fail:       Not possible to reference (non-static) InnerClass in static memory
        StaticNestedClass   objStaticNestedClass    = new StaticNestedClass();
        
        return 0;
    }
    
    class InnerClass
    {
        InnerClass()
        {
            i = 10;
            iStatic = 10;
        }
        
//        static class StaticNestedClass()        //Fail:       Inner Class composition is entirely non-Static therefore not possible to place anything in static memory (as this memory does not exist).
//        {}
    }
    
    static class StaticNestedClass
    {
        StaticNestedClass()
        {
//            i = 10;                             //Fail        Not possible to reference (non-static) i in static memory
            iStatic = 10;
        }
    }
}
class ClassExt
{
    int         i = 10;
    static int  iStatic = 10;
    
    int method()
    {
        return 0;
    }
    
    static int staticMethod()
    {
        return 0;
    }
}