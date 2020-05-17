/*
 * File: Lehmann.java
 * File Created: Wednesday, 13th May 2020
 * Author: Nicholas Klvana-Hooper
 * -----
 * Last Modified: Friday, 15th May 2020
 * Modified By: Nicholas Klvana-Hooper
 * -----
 * Purpose: Class for an object that implements the Lehmann Algorithm to check if a number is prime.
 * Reference: Lehmann Algorithm based on lab 2 Q. 3 of Fundamentals of Cryptography
 *            Accessed on the 13th May 2020
 * 
 *            Fast mod algorith based on pseudocode from Cryptography and Network Security: Principles
*             and Practice, Sixth Edition (ISBN 13: 978-0-13-335469-0, page 268, figure 9.8)
 *            Accessed on the 13th May 2020
 */
import java.util.*;

public class Lehmann 
{
    public static void main(String[] args)
    {
        Random rand = new Random();
        int a, p;
        long r = 1;
        int count = 0;
        int abs = 0;

        if(args.length != 1) //Ensure a number is entered to test
        {
            System.out.println("Please use code like java Lehmann <num to test>!");
        }
        else
        {
            p = Integer.parseInt(args[0]); //set p to the integer to test

            for(int i=0 ; i < 50; i++) //We do 50 runs so that there is a 99% chance of primarity
            {
                a = rand.nextInt(p-1) + 1; //pick a number between 0 and p-1
                
                r = fastMod(a, ((p - 1) / 2), p); //a to the power of (p-1)/2 modded by p.

                if ((r % p != 1) && (r % p != (p-1))) //if r%p is not 1 or -1, then it cannot be prime
                {
                }
                else
                {
                    count += r; /* count is an addition of r. so if a -1 occurs, 
                                 * it reduces. used to make sure its not 1 everytime */
                    abs ++; //absolute is just a count of how many 1s or -1s occur
                }
            }

            // -1 or 1 has to occur evertime, but not 1 everytime
            if(abs==50 && count!=50)
            {
                System.out.println("Has a 99% chance of being a prime");
            }
            else //otherwise its not a prime
            {
                System.out.println("Not a prime");
            }
        }
    }  

    /*
     * SUBMODULE: fastMod
     * IMPORT: a(int), b(int), n(int)
     * EXPORT: f(long)
     * ASSERTION: Implements the fast mod algorith from Cryptography and Network Security: Principles
     *            and Practice, Sixth Edition (ISBN 13: 978-0-13-335469-0, page 268, figure 9.8)
     */
    public static long fastMod(int a, int b, int n) // a to the power of b modded by n
    {   
        String bStr;
        long f = 1; //f is the output. has to be long as numbers can get big

        bStr = Integer.toBinaryString(b); //convert the exponent to binary

        for(int i = bStr.length()-1; i >= 0; i--) //go from right to left in the string
        {
            f = (f * f) % n; //perform this equation everytime

            //if character at this point of the string is one, perform this equation
            if(bStr.charAt(bStr.length() -1 - i) == '1') 
            {
                f = (f * a) % n;
            }
        }

        return f;
    }
}