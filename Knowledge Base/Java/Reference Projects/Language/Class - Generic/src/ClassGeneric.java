import java.util.ArrayList;
import java.util.List;

interface Interface1
{
    void method1();
    void method2();
}
interface Interface2
{
    void method3();
    void method4();
}
class Class1<T, V>
{
    //Empty/null Type Variables, cannot be instaniated using new, only initialisaed via passed Type Arguments in Constructor.
    T objT;
    V objV;
    
    final T             finalT;         //Must be initialised in the constructor
    final V             finalV;         //Must be initialised in the constructor
    //static T          staticT;        //Compile Error:    Static Type Variables not permitted given they are inherently non-Static and unknown at compile time.
    //final static T    finalstaticT;   //Compile Error:    Static Type Variables not permitted given they are inherently non-Static and unknown at compile time.
    
    ArrayList<Number>           arraylistNumber1 = new ArrayList<>();
    /*
        An ArrayList Variable where Type Argument only needs to be defined once and not supplied again within the diamond operator.
    */
    ArrayList<? extends Number> arraylistNumber2 = new ArrayList<Double>();
    /*
        An ArrayList Variable which may reference any ArrayList whose Type Argument is either:
    
            1. Number
            2. Extension of Number
    */
    ArrayList<? super Double>   arraylistNumber3 = new ArrayList<>();
    /*
        An ArrayList Variable which may reference any ArrayList whose Type Argument is either:
    
            1. Parent of Double
            2. But not including Double
    */
    ArrayList<?>                arraylistNumber4 = new ArrayList<>();
    /*
        An ArrayList Variable which may reference any ArrayList whose Type Argument is anything.
    */
    ArrayList<? extends Interface1> arraylistNumber5 = new ArrayList<>();
    /*
        An ArrayList Variable which may reference any ArrayList whose Type Argument implements either:
    
            1. A class which implements Interface1
            2. A class which implements an extension of Interface1 (the extension will inherently contain Interface1)
            3. An Interface Variable of Interface1
            4. An Interface Variable of an extension of Interface1 (the extension will inherently contain Interface1)
    */
    ArrayList<? super Interface1> arraylistNumber6 = new ArrayList<>();
    /*
        An ArrayList Variable which may reference any ArrayList whose Type Argument implements either:
    
            1. A class which implements a parent of Interface1 (but not including Interface1)
            2. An Interface Variable of a parent of Interface1 (but not including Interface1)
    */
    
    <K> Class1(T t, V v, K k)
    {
        //Type Variables can only be assigned/initialisaed objects that have been passed via Constructor/Method.
        objT = t;
        objV = v;
        
        finalT = t;
        finalV = v;
        
        //Constructors may declare Local Type Parameters for use if necessary.
    }
    
    /*
        Generic and Non-Generic Methods
    
            - Generic Class may contain Generic and Non-Generic methods.
            - Types Variables may be used as:
    
                1. Type Parameters
                2. Return Type Variables
    */
    void nonGenericMethod1()
    {
        System.out.println("Class1: Non Generic Method 1");
    }
    int genericMethod1(T t)
    {
        System.out.println("Class1: Generic Method 1");
        
        return 1;
    }
    T genericMethod1(T t, V v)
    {
        T objLocalT = t;
        
        System.out.println("Class1: Generic Method 1 (Overloaded)");
        
        return t;
    }
    final T genericMethod2(int i)
    {
        System.out.println("Class1: Generic Method 1 (Final)");
        
        return objT;
    }
}
class Class2<T extends Number, V extends String>
{
    //Empty/null Type Variable, cannot be instaniated using new.
    T objT;
    V objV;
}



/*
    'Passup':

        - Any extended class whose parent was generic, must 'pass up' and define the Type that the parent class must use, either via:

            1. Hardcode:		'Integer'
            2. Class Type Parameter:	Relay and pass up the Type that gets passed to the extended class.
            3. Blank/Object:            Leave blank <> and let Object be used by default.

    'super':

        - The constructor of the extended class must contain super(...) as the first statement.
        - 'super' must initialise any fields of the Parent Class in accordance with any Type passed up to Parent Class.
*/
class Class3<K, E> extends Class1<K, E>
{
    Class3(K k, E e)
    {
        //Generic Parent need to 'know' what type they are using during runtime.
        //All Generic Subclasses must contain 'super' in the constructor as the first statement.
        super(k, e, 0);
    }

    @Override
    int genericMethod1(K k) {
        return 1;
    }

    @Override
    K genericMethod1(K k, E e) {
        return k;
    }

    @Override
    void nonGenericMethod1() {
        
    }
}




/*
    Generic Subclass Declaration - Additional Type Parameters:

        - Generic Subclass can declare additional Type Parameters of its own, than those declared by the Generic Superclass.
        - The only requirment is that the correct number of Types Arguments are passed up to the Generic Superclass.

    Object Declaration:

        Class4<Integer, String, Double> objClass5 = new Class5<>(1, "Argument", 1.1);
*/
class Class4<K, E, V> extends Class1<K, E>
{
    Class4(K k, E e, V v)
    {
        super(k, e, 0);
    }
}




/*
    Generic Subclass Declaration - Hardcoded Type Arguments:

        - It is possible to 'hardcode' the Type Arguments passed to the Generic Superclass.
        - The corresponding values must be passed in the 'super' constructor of the Generic Superclass.
*/
class Class5<K, E> extends Class1<Integer, String>
{
    Class5(K k, E e)
    {
        super(1000, "Test", 0);
    }
}




/*
    Non-use of Type Parameters

        - A class may be declared generic but not actually contain/use any of its declared Type Parameters.
*/
class Class6<T, V>
{
    int i;
    
    Class6(int i)
    {
        this.i = i;
    }
}




/*
    Non-Generic Constructor

        - A generic class may contain a non-generic constructor.
        - Given Type Variables cannot be instaniated, any methods that return a Type Variable must either:

            - Return a Type Variable that was passed to the method as a parameter.
            - Return a Type Variable which may contain null or a value derived from another local method.

        - A Non-Generic Constructor containing a Non-Generic Argument are not declared differently:

            Generic Constructor:        Class7<Integer, String>  obj1 = new Class7<>(1);
            Non-Generic Constructor:    Class7<Integer, String>  obj2 = new Class7<>(1, "Argument");
*/
class Class7<T, V> 
{
    T objT;
    V objV;
    
    int i;
    
    Class7(T t, V v)
    {
        objT = t;
        objV = v;
    }
    Class7(int i)
    {
        this.i = i;
    }
    
    T methodReturnType1(T t)
    {
        return t;
    }
    
    V methodReturnType2(V v)
    {
        return v;
    }
    
    void methodReturnType3(T t)
    {
        objT = t;
    }
    
    T methodReturnType4()
    {
        return objT;
    }
}



/*
    Generic Methods:

        - Generic Method can use Class or Local Type Parameters.
        - Generic Method Local Type Parameters shadow Class Type Parameters.
        - Local Type Parameters shadow Class Type Parameters.
*/
class Class8<T extends Number, V> 
{
    int i;
    
    Class8(int i)
    {
        this.i = i;
    }
    
    T genericMethod1(T t, V v)
    {
        return t;
    }
    
    <K> String genericMethod2(K k)
    {
        return "Test";
    }
    
    <K> String genericMethod3(K k, T t)
    {
        return "Test";
    }

    <K> K genericMethod4(K k, T t)
    {
        return k;
    }
    
    <T, V> String genericMethod5(T t, V v)
    {
        return "Test";
    }
    
    
    /*
        Bounded Types:
    
            - Upper Bound:
   
                1. Classes:     Any object that belong to a class or subclass of that bound.
                2. Interface:   Any object that implements the interface or subinterface (subinterface is extended therefore naturally contains original interface).
    */
    <K extends Number> K genericMethod4(K k)
    {
        return k;
    }
    
    <K extends Interface1> K genericMethod4(K k)
    {
        return k;
    }
    
    /*
        Static Methods
    
            - Static Methods are the only Static memory which may use Generic syntax.
            - Static Methods are in Static memory therefore can not use Class Type Parameters but only Local Type Parameters.
            - Local Type Parameters are local only to the method itself.
            - Local Type Parameters are not determined or linked to the class level Class Type Parameters.
    
                - The static method could be used without instantiating the Generic Class within which it is defined.
                - In which case the JVM would not know the types to used.
                - Therefore the Local Type Parameters are completely unique and whose scope is to the static method only.
                - Local Type Parameters shadow Class Type Parameters, therefore they may even have the same placeholder 'letter' as one declared for the Generic Class.
                - Local Type Parameters may be bounded as per the Type Specification prior to the return type of the method.
                
            - Type Specification List:
    
                - The Type Specification List is declared and defined before the return type of the static method.
                - The Type Specification List defines any restrictions of the Local Type Parameters.
                - The methods Parameter List may only use the placeholders contained in the Local Type Specification List.
                - The arguments passed to the static method must use and follow the placeholders and any Type Specification they impose.
    
            - Type Arguments are passed when the static method is called (after the dot operator).
    
                boolean bReturn = Class9.<Integer, String>staticGenericMethod(obj1, obj2);
    */
    public static final <K, V> boolean staticGenericMethod1(K obj1, V obj2)
    {
        return true;
    }
    /*
        Compile Error:
    
            - Static Method can only use Local Type Parameters.
            - Static Method can not reference Class  Type Parameters.
    
    public static <K> boolean staticGenericMethod2(K obj1, V obj2)
    {
        return true;
    }
    */
    
    /*
        Compile Error:
    
            public static <K, V> boolean staticGenericMethod2(Class8<K, V> obj1, Class8<K, V> obj2)
    
            - Given that the Local Type Specification does not match that imposed by ClassB i.e. <T extends Number, V>.
            - It is not possible to declare Class8<K, V> parameters as shown above for it does not impose same restrictions where the first Type Parameter must be Number or extended Number.
            - Therefore it would have been possible to pass through postentially undesired types.
            - Only Class7 does not impose any restrictions.
            - staticGenericMethod4 compiles as required for its first Type Parameter matches the first Type Parameter of Class8.
    */
    public static <K, V> boolean staticGenericMethod2(Class7<K, V> obj1, Class7<K, V> obj2)
    {
        return true;
    }
    public static <K extends Number, V extends Integer> boolean staticGenericMethod4(Class8<K, V> obj1, Class8<K, V> obj2)
    {
        return true;
    }
}



/*
    Type Arguments:

        - Type Arguments utilise Wildcards to impose restrictions on the Type that can be assigned to the Type Argument.
        - Therefore specifying the range of Parameterised Type that can be assigned to the variable. 

            1. Class:       Fields                              List<? extends Number>  objList1;
            2. Methods:     Type Parameter                      void method(List<? extends Number>  objList1)
            3. Methods:     Local Variable                      { List<? extends Number>  objList1;
            4. Methods:     Return Type (NB. Bad Practice)
*/
class Class9
{
    int i;
    
    /*
        Fields
    
            - objList1 may hold any value which is a List of Number+
    
                List<Number>
                List<Double>
                List<Integer>
                ...
            
            - objList2 may hold any value which is a parent of List of Number (but not including Number)
    
                List<Object>
                ...
    */
    List<? extends Number>  objList1;
    List<? super Number>    objList2;
            
    Class9(int i)
    {
        this.i = i;
    }
    
    <T> int genericMethod1(T t)
    {
        T objT;
        
        objT = t;
        
        return 1;
    }
    
    /*
        Type Parameter:
    
            - listNumber maybe any List<> whose Type Argument is Number+
    
                List<Number>
                List<Double>
                List<Integer>
                ...
    */
    <T> int genericMethod2(List<? extends Number> listNumber, T t)
    {
        T objT;
        
        objT = t;
        
        return 1;
    }
    
    /*
        Type Parameter:
    
            - listNumber maybe any List<> whose Type Argument is a parent of Number (but not including Number)
    
                List<Object>
                ...
    */
    <T> int genericMethod3(List<? super Number> listNumber, T t)
    {
        T objT;
        
        objT = t;
        
        return 1;
    }
    
    //Local Variable
    int nonGenericMethod1(int i)
    {
        List<? extends Number>  objList3;
    
        return 1;
    }
}





/*
    Generic Class - Generic Method:

        - The same functionality can be achieved at following levels:

            1. Class:       Class is declared Generic.
            2. Method:      Method is declared to accept Generic arguments.
*/


/*
    - Class:        Generic
    - Methods:      Non-Generic
*/
class BoxType1<T>
{
    void add(T t)
    {
        //Add the object which is either a number or one of its subclasses.
    }
}
/*
    - Class:        Generic
    - Methods:      Non-Generic
*/
class BoxType2<T extends Number>
{
    void add(T t)
    {
        //Add the object which is either a number or one of its subclasses.
    }
}
/*
    - Class:        Non-Generic
    - Methods:      Generic
*/
class BoxType3
{
    <T extends Number> void add(T t)
    {
        //Add the object which is either a number or one of its subclasses.
    }
    <T extends Number, V extends T> void add(T t, int i, V v)
    {
        //Add the object which is either a number or one of its subclasses.
    }
}
class FillBox
{
    BoxType1            objBox1 = new BoxType1();           //T not declared therefore is Object, not typesafe.
    BoxType1            objBox2 = new BoxType1<>();         //T not declared therefore is Object, diamond operator <> can be omitted.
    BoxType1<Number>    objBox3 = new BoxType1<>();         //T declared therefore is bound to use only Number or subclass of Number.

    BoxType2<Integer>   objBox4 = new BoxType2<>();         //T can be any object that is of Number or its subclasses
    BoxType2<Double>    objBox5 = new BoxType2<>();         //T can be any object that is of Number or its subclasses

    /*
        Compatible Object Usage
    
            - A common technique of using a superclass type argument.
            - Enabling any subclass to be passed and used within that particular Type Varaiable.
    */
    FillBox()
    {
        //T is Object:              Therefore can be passed any class.
        objBox1.add(new String("Test 1"));
        objBox1.add(new Double(1.1));
        objBox1.add("Test 2");
        objBox1.add(1.1);
        
        //T is Bound by Number:     Therefore can be passed any object that is of Number or subclass.
        objBox2.add(new Integer(1));
        objBox2.add(new Double(1.1));
        objBox2.add(1);
        objBox2.add(1.1);
        
        //T is Bound by Integer:    Therefore can be passed any object that is of Integer or subclass.
        objBox3.add(new Integer(1));
        //objBox3.add(new Double(1.1));     //Compile Error
        objBox3.add(1);
        //objBox3.add(1.1);                 //Compile Error
    }
}



//Generic Class Relationships
class Class10<T>
{
    T t;
    
    Class10(T t)
    {
        this.t = t;
    }
}
class Class11<T, V>
{
    T t;
    
    Class11(T t, V v)
    {
        this.t = t;
    }
}
class ClassNone<T> extends Class10<T>
{
    ClassNone(T t)
    {
        super(t);
    }
}
class ClassInteger<T> extends Class10<Integer>
{
    ClassInteger(T t)
    {
        super(1);
    }
}
class ClassString<T> extends Class10<String>
{
    ClassString(T t)
    {
        super("Test");
    }
}
class ClassExtra<T, V> extends Class10<String>
{
    ClassExtra(T t, V v)
    {
        super("Test");
    }
}
class ClassMultiple<T extends Number & Comparable<T>> extends Class10<String>
{
    ClassMultiple(T t)
    {
        super("Test");
    }
}
class GenericClassRelationshipDemo
{
    Class10<Integer> objSuper1   = new Class10<>(5);
    Class10<Integer> objSuper2   = new Class10(5);
    Class11 objSuper3            = new Class11<>(5, "Test");
    Class10<Number> objSuper4    = new Class10<>(5);
    Class10<Integer> objSuper5   = new Class10<>(5);
    Class10<String> objSuper6    = new Class10<>("Test");
    
    ClassNone<Integer>            objSubNone      = new ClassNone<>(1);
    ClassInteger<Integer>         objSubInteger   = new ClassInteger<>(1);
    ClassString<String>           objSubString    = new ClassString<>("Test");
    ClassExtra<Integer, String>   objSubExtra     = new ClassExtra<>(1, "Test");
    
    void method1()
    {
        objSuper1 = objSubNone;
        objSuper1 = objSubInteger;
        
        //objSuper1 = objSubString;             Incompatible Type
        //objSuper1 = objSubExtra;              Incompatible Type
        //objSuper5 = objSubExtra;              Incompatible Type
    }
}



class ClassRaw {

    public static void main(String[] args) {
        
        boolean bReturn;
        
        /*
            Declaration:
        
                - It is the same as a Non-Generic object.
                - Except the Type Arguments are passed to the Generic Class.
        
            Instaniation:
        
                - It is the same a Non-Generic object.
                - Except that the Type Arguments are omitted in place of the diamond operator.
                - Constructor Arguments are passed as normal.
                
        */
        Class4<Integer, String, Double>   objClass5 = new Class4<>(1, "Argument", 1.1);
        Class5<Integer, String>           objClass6 = new Class5<>(1, "Argument");
        
        Class7<Integer, String>              obj1 = new Class7<>(1, "Argument");
        Class7<Integer, String>              obj2 = new Class7<>(2, "Argument");
        
        /*
            Static Methods:
        
                - The Type Arguments can be explicit or inferred.
        */
        bReturn = Class8.<Integer, String>staticGenericMethod1(1, "Argument");
        bReturn = Class8.staticGenericMethod1(1, "Argument");
        
        bReturn = Class8.<Integer, String>staticGenericMethod2(obj1, obj2);
        bReturn = Class8.<Integer, String>staticGenericMethod2(new Class7<>(1, "Argument - 1"), new Class7<>(2, "Argument - 2"));
        
        /*
            Raw Type:
        
                - No Type Arguments are supplied when the generic class is invoked and instantiated.
                - In this case Object is used in place.
                - This loses type saftey.
                - Used in legacy pre-generic code.
        */
        Class7 objRaw1 = new Class7<>(1, "Argument");
        Class7 objRaw2 = new Class7(1, "Argument");
    }
}
