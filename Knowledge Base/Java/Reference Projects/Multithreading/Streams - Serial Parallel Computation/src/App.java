import java.util.stream.IntStream;

public class App
{
    public static boolean isPrime(long lNumber)
    {
        long lMaximumDivsor;

        if(lNumber <= 1)    return false;
        if(lNumber == 2)    return true;
        if(lNumber%2 == 0)  return false;

        lMaximumDivsor = (long) Math.sqrt(lNumber);

        for(int i = 3 ; i<lMaximumDivsor ; i+=2)
            if(lNumber%i==0)
                return false;

        return true;
    }
    public static void main(String[] args)
    {
        long lInitialTime, lNumberOfPrimes;
                
        //Sequential processing of stream
        lInitialTime = System.currentTimeMillis();
        lNumberOfPrimes = IntStream.rangeClosed(2, Integer.MAX_VALUE/100).filter(x->isPrime(x)).count();
        System.out.println("Sequential: Number of Primes Found: " + lNumberOfPrimes);
        System.out.println("Sequential: Duration: " + (System.currentTimeMillis() - lInitialTime) + "mS");
        
        //Parallel processing of stream
        lInitialTime = System.currentTimeMillis();
        lNumberOfPrimes = IntStream.rangeClosed(2, Integer.MAX_VALUE/100).parallel().filter(x->isPrime(x)).count();
        System.out.println("Parallel: Number of Primes Found: " + lNumberOfPrimes);
        System.out.println("Parallel: Duration: " + (System.currentTimeMillis() - lInitialTime)+ "mS");
        
        //Parallel processing is very convenient to apply when using streams, just need to insert the .parallel() intermediate operation
    }
}
