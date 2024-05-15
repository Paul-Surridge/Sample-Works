/*
Local Variables

    In all circumstances:

        Top Level:
            Static Nested Class:

                int iLocal = 0;                             //Field
                final int iLocalFinal = 0;                  //Constant
                static int iLocalStatic = 0;                //Static Field
                static final int iLocalStaticFinal = 0;     //Static Constant

            Method:

                int iLocal = 0;                             //Field
                final int iLocalFinal = 0;                  //Constant

        Local Class:
            non-Static Nested Class:

                int iLocal = 0;                             //Field
                final int iLocalFinal = 0;                  //Constant
                static final int iLocalStaticFinal = 0;     //Static Constant

Shadowing:

    Only the following Shadow:

        1. Extended Class/Interface		Shadow/hide fields in superclass			super.iField;
        2. Init Block [non-Static]		Shadow/hide fields in enclosing class			this.iField;
        3. Init Block [Static]			Shadow/hide static fields in enclosing class		classname.iStaticField
        4. Constructor				Shadow/hide fields in enclosing class			this.iField;
        5. Method [non-Static]			Shadow/hide fields in enclosing class			this.iField;

        Not nested or local classes.
*/
class TopLevelClass2
{
    int iLocal = 0;
    final int iLocalFinal = 0;
    static int iLocalStatic = 0;
    static final int iLocalStaticFinal = 0;
    
    //non-Static Memory _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _
    void method()
    {
        int iLocal = 0;
        final int iLocalFinal = 0;
        //static int iLocalStatic = 0;
        //static final int iLocalStaticFinal = 0;
        
        class localClass0
        {
            int iLocal = 0;
            final int iLocalFinal = 0;
            //static int iLocalStatic = 0;
            static final int iLocalStaticFinal = 0;
    
            class nestedClass0
            {
                int iLocal = 0;
                final int iLocalFinal = 0;
                //static int iLocalStatic = 0;
                static final int iLocalStaticFinal = 0;
                
                class nestedClass1
                {
                    int iLocal = 0;
                    final int iLocalFinal = 0;
                    //static int iLocalStatic = 0;
                    static final int iLocalStaticFinal = 0;
                    
                    class nestedClass2
                    {
                        class InnerClass3
                        {
                            //...indefinitely InnerClassN
                        }
                    }
                }
            }
        }
    }
    
    class nestedClass0
    {
        int iLocal = 0;
        final int iLocalFinal = 0;
        //static int iLocalStatic = 0;
        static final int iLocalStaticFinal = 0;
        
        class nestedClass1
        {
            int iLocal = 0;
            final int iLocalFinal = 0;
            //static int iLocalStatic = 0;
            static final int iLocalStaticFinal = 0;

            class nestedClass2
            {
                //...indefinitely nestedClassN
            }
        }
    }
    
    
    //Static Memory _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _
    static void staticMethod()
    {
        int iLocal = 0;
        final int iLocalFinal = 0;
        //static int iLocalStatic = 0;
        //static final int iLocalStaticFinal = 0;
        
        class localClass0
        {
            int iLocal = 0;
            final int iLocalFinal = 0;
            //static int iLocalStatic = 0;
            static final int iLocalStaticFinal = 0;
    
            class nestedClass0
            {
                int iLocal = 0;
                final int iLocalFinal = 0;
                //static int iLocalStatic = 0;
                static final int iLocalStaticFinal = 0;
                
                class nestedClass1
                {
                    int iLocal = 0;
                    final int iLocalFinal = 0;
                    //static int iLocalStatic = 0;
                    static final int iLocalStaticFinal = 0;
                    
                    class nestedClass2
                    {
                        class InnerClass3
                        {
                            //...indefinitely InnerClassN
                        }
                    }
                }
            }
        }
    }
    
    static class staticNestedClass0
    {
        int iLocal = 0;
        final int iLocalFinal = 0;
        static int iLocalStatic = 0;
        static final int iLocalStaticFinal = 0;
    
        class nestedClass0
        {
            int iLocal = 0;
            final int iLocalFinal = 0;
            //static int iLocalStatic = 0;
            static final int iLocalStaticFinal = 0;

            class nestedClass1
            {
                int iLocal = 0;
                final int iLocalFinal = 0;
                //static int iLocalStatic = 0;
                static final int iLocalStaticFinal = 0;

                class nestedClass2
                {
                    //...indefinitely nestedClassN
                }
            }
        }
        
        static class staticNestedClass1
        {
            int iLocal = 0;
            final int iLocalFinal = 0;
            static int iLocalStatic = 0;
            static final int iLocalStaticFinal = 0;

            static class staticNestedClass3
            {
                //...indefinitely StaticNestedClassN 
            }
        }
    }
}