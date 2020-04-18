import java.util.*;

public class KeyTest
{
    public static void main(String[] args) 
    {
        if(args.length != 2)
        {
            System.out.println("Please use program by java Question1 <a> <b>");
            System.out.println("  where a and b are the two keys you want to test");
        }
        else
        {
            try
            {
                if((Integer.parseInt(args[0]) > 0) && (Integer.parseInt(args[0]) < 27))
                {
                    if(coPrime(Integer.parseInt(args[0]), 27) == 1)
                    {
                        System.out.println("a is an appropriate key");
                    }
                    else
                    {
                        System.out.println("a is NOT appropriate key");
                    }
                }
                else
                {
                    System.out.println("a is NOT appropriate key");
                }

                if((Integer.parseInt(args[1]) > 0) && (Integer.parseInt(args[1]) < 27))
                {
                    System.out.println("b is an appropriate key");
                }
                else
                {
                    System.out.println("b is NOT appropriate key");
                }
            }
            catch (NumberFormatException e)
            {
                System.out.println("Error in type of data entered. Must be an integer! :  " + e.getMessage());
            }
        }
    }

    private static int coPrime(int a, int b)
    {
        int greatestFactor = 0;

        if (a==0 || b==0)
        {
            greatestFactor = 0;
        }
        else if(a > b)
        {
            greatestFactor = coPrime(a-b, b);
        }
        else if(a < b)
        {
            greatestFactor = coPrime(a, b-a);
        }
        else if(a == b)
        {
            greatestFactor = a;
        }
        return greatestFactor;
    }
}