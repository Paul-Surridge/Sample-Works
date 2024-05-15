public enum Planet
{
    //Enum Constants
    MERCURY (3.303e+23, 2.4397e6),
    VENUS   (4.869e+24, 6.0518e6),
    EARTH   (5.976e+24, 6.37814e6),
    MARS    (6.421e+23, 3.3972e6),
    JUPITER (1.9e+27,   7.1492e7),
    SATURN  (5.688e+26, 6.0268e7),
    URANUS  (8.686e+25, 2.5559e7),
    NEPTUNE (1.024e+26, 2.4746e7);

    //Fields
    int i;
    final int iConstant = 0;
    
    //Constants
    static int iStatic;
    static final int iStaticFinal = 0;
    
    private final double mass;   // in kilograms
    private final double radius; // in meters
    
    //Constants
    public static final double G = 6.67300E-11;
    
    //Initialisation Blocks
    static
    {
        
    }
    {
        
    }
    
    //Constructor:  Called by Enum Constants
    Planet(double mass, double radius) {
        this.mass = mass;
        this.radius = radius;
    }
    
    
    
    
    
    //Methods
    private double mass()
    {
        class InnerClass
        {

        }

        return mass;
    }
    private double radius()
    {
        return radius;
    }
    double surfaceGravity()
    {
        return G * mass / (radius * radius);
    }
    double surfaceWeight(double otherMass)
    {
        return otherMass * surfaceGravity();
    }
    static void staticMethod()
    {
        
    }
    
    
    
    
    //Nested Class
    class InnerClass
    {
        
    }
    static class StaticNestedClass
    {

    }
    
    
    //Nested Interface
    interface InnerInterface
    {
        
    }
    static interface StaticNestedInterface
    {

    }
    
    
    
    //Nested Enum
    enum InnerEnum
    {
        
    }
    static enum StaticNestedEnum
    {

    }
    
    
    
    
    //Nested Abstract Class
    abstract class InnerAbstractClass
    {
        
    }
    static abstract class StaticNestedAbstractClass
    {
        
    }
    
    
    public static void main(String[] args)
    {
        if (args.length != 1) {
            System.err.println("Usage: java Planet <earth_weight>");
            System.exit(-1);
        }
        
        double earthWeight      = Double.parseDouble(args[0]);
        double mass             = earthWeight/EARTH.surfaceGravity();
        
        for (Planet p : Planet.values())
           System.out.printf("Your weight on %s is %f%n", p, p.surfaceWeight(mass));
    }
}

class Class
{
    //1. Enum declared
    Planet enPlanet;
    
    void method()
    {
        //2. Enum initialised via indirect reference to 'static' enum constant
        enPlanet = Planet.EARTH;
        
        switch(enPlanet)
        {
            case MERCURY:       System.err.println("Planet: " + enPlanet);    break;
            case VENUS:         System.err.println("Planet: " + enPlanet);    break;
            case EARTH:         System.err.println("Planet: " + enPlanet);    break;
        }
        
        //Return the fields of the enum constant currently assigned to the enum enPlanet
        enPlanet.surfaceGravity();
    }
}
