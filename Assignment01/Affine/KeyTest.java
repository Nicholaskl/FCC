/*
 * File: KeyTest.java
 * File Created: Tuesday, 14th April 2020
 * Author: Nicholas Klvana-Hooper
 * -----
 * Last Modified: Sunday, 29th April 2020
 * Modified By: Nicholas Klvana-Hooper
 * -----
 * Purpose: To determine if two keys are suitable for affine cipher encryption
 * Reference:
 */
import java.util.*;

public class KeyTest
{
    public static void main(String[] args) 
    {
        if(args.length != 2) //Program needs two values to run if not print help dialogue
        {
            System.out.println("Please use program by java Question1 <a> <b>");
            System.out.println("  where a and b are the two keys you want to test");
        }
        else
        {
            try
            {
                if((Integer.parseInt(args[0]) > 0) && (Integer.parseInt(args[0]) < 27)) //checks if a is between 1 and 26
                {
                    if(coPrime(Integer.parseInt(args[0]), 27) == 1) //if a is coprime with 27 it is appropriate
                    {
                        System.out.println("a is an appropriate key");
                    }
                    else //otherwise not
                    {
                        System.out.println("a is NOT appropriate key");
                    }
                }
                else
                {
                    System.out.println("a is NOT appropriate key");
                }

                if((Integer.parseInt(args[1]) > 0) && (Integer.parseInt(args[1]) < 27))//checks if b is between 1 and 26
                {
                    System.out.println("b is an appropriate key"); //if it is, it is appropriate key
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

    /*
     * SUBMODULE: coPrime
     * IMPORT: a (int), b(int)
     * EXPORT: greatestFactor(int)
     * ASSERTION: Checks whether two numbers are coprime
     */
    private static int coPrime(int a, int b)
    {
        int greatestFactor = 0;

        if (a==0 || b==0)
        {
            greatestFactor = 0;
        }
        else if(a > b)
        {
            greatestFactor = coPrime(a-b, b); //if a is bigger than check smaller potential factor
        }
        else if(a < b)
        {
            greatestFactor = coPrime(a, b-a);//if b is bigger than check smaller potential factor
        }
        else if(a == b) //a is the largest common factor
        {
            greatestFactor = a;
        }
        return greatestFactor;
    }
}