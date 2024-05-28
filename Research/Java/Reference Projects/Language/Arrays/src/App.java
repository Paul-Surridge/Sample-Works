import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class App{

    public static void main(String[] args)
    {
        //Single Dimension
        
            //x1 Step:

                int[] iResults1 = {1, 2, 3, 4, 5};	//Declare, allocate memory (implicitly) and bulk initialise.
                int[] iResults2 = new int[]{1, 2};	//Declare, allocate memory and bulk initialise, the new int[] is superfluous
        
            //x2 Step:
            
                //Form 1:   With Discrete Initialisation
                    int[] iResults3 = new int[10];      //Declare + Allocate
                    iResults3[0] = 10;                  //Discrete Initialise
                    iResults3[1] = 11;
                    iResults3[2] = 12;
                    //...
            
                //Form 2:   With Bulk Initialisation
                    int[] iResults4;                    //Declare
                    iResults4 = new int[]{1,2};         //Alocate + Bulk Initialise
                    
            //x3 Step:
            
                int[] iResults5;                        //Declare
                iResults5 = new int[10];		//Allocate
                iResults5[0] = 10;			//Discrete Initialise
                iResults5[1] = 11;
                //...
    
        //Multi Dimension
        
            //x1 Step:

                int[][] iResults6 = { {1, 2, 3, 4, 5},
                                      {6, 7, 8, 9, 10} };                   //Declare, allocate memory (implicitly) and bulk initialise.
                
                int[][] iResults7 = new int[][]{ {1, 2, 3, 4, 5},
                                      {6, 7, 8, 9, 10} };                   //Declare, allocate memory and initialise, the new int[][] is superfluous
                
                int[][][] iResults12 = { {{1,2}, {1,2}, {1,2}, {1,2}, {1,2}},
                                         {{1,2,3}, {1,2,3,4}, {1}, {1,2}, {1,2,3,4,5,6}, {1,2,3}, {1,2,3,4}} };
                
            //x2 Step:
            
                //Form 1:   Discrete Initialisation
                    int[][][] iResults8 = new int[10][][];                  //Declare + Allocate
                    iResults8[0][0][0] = 10;                                //Discrete Initialise
                    iResults8[0][0][1] = 11;
                    iResults8[0][0][2] = 12;
                    //...
                
                //Form 2:   With Bulk Initialisation
                    int[][] iResults9;                                      //Declare
                    iResults9 = new int[][]{ {1, 2, 3, 4, 5},
                                             {6, 7, 8, 9, 10} };            //Alocate + Bulk Initialise
                    
            //x3 Step:    
                int[][] iResults10;                                         //Declare
                iResults10 = new int[10][];                                 //Allocate
                iResults10[0][0] = 10;                                      //Discrete Initialise
                iResults10[0][1] = 11;
                iResults10[0][2] = 12;
                //...
    }

    private static void printlnBreak(String sTitle)
    {
        System.out.println("\n" + sTitle + ": - - - - - - - - - - - - - - - - - - - - - - - - -");
    }
    public static void printLn(String s)
    {
        System.out.println(s);
    }
    public static void printLn(char c)
    {
        System.out.println(c);
    }
    public static void printLn(int i)
    {
        System.out.println(i);
    }
    public static void printLn(boolean b)
    {
        System.out.println(b);
    }
    public static void printMap(Map<String, String> m)
    {
        Set<String> Keys = m.keySet();
        String sBuild;
        
        for(String s : Keys)
        {
            sBuild = "[" + s + ", " + (String)m.get(s) + "]";
            System.out.println(sBuild);
        }
    }
    public static void printBytes(byte[] b)
    {
        String sBuild = "[";
        for(int i = 0 ; i<b.length ; i++)
            sBuild += b[i] + ",";
        sBuild = sBuild.substring(0, sBuild.length()-1) + "]";
        
        System.out.println(sBuild);
    }
    public static void printInts(int[] a)
    {
        String sBuild = "[";
        for(int i = 0 ; i<a.length ; i++)
            sBuild += a[i] + ",";
        sBuild = sBuild.substring(0, sBuild.length()-1) + "]";
        
        System.out.println(sBuild);
    }
    public static void printChars(char[] c)
    {
        String sBuild = "[";
        for(int i = 0 ; i<c.length ; i++)
            sBuild += c[i] + ",";
        sBuild = sBuild.substring(0, sBuild.length()-1) + "]";
        
        System.out.println(sBuild);
    }
    public static void printStrings(String[] s)
    {
        String sBuild = "[";
        for(int i = 0 ; i<s.length ; i++)
            sBuild += s[i] + ",";
        sBuild = sBuild.substring(0, sBuild.length()-1) + "]";
        
        System.out.println(sBuild);
    }
    public static void printCharSequence(CharSequence cs)
    {
        String sBuild = "[";
        for(int i = 0 ; i<cs.length() ; i++)
            sBuild += cs.charAt(i) + ",";
        sBuild = sBuild.substring(0, sBuild.length()-1) + "]";
        
        System.out.println(sBuild);
    }
    public static void printTestObjects(TestObject[] o)
    {
        String sBuild = "[";
        for(int i = 0 ; i<o.length ; i++)
            if(o[i] != null)    sBuild += o[i].toString() + ",";
            else                sBuild += "0,";
        sBuild = sBuild.substring(0, sBuild.length()-1) + "]";
        
        System.out.println(sBuild);
    }
    private static char[][] copy2DArray(char[][] a)
    {
        char[][] newCopy = new char[a.length][];

        for(int iRow = 0 ; iRow<a.length ; iRow++)
            newCopy[iRow] = Arrays.copyOf(a[iRow],a[iRow].length);

        return newCopy;
    }
}
