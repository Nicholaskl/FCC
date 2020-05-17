/*
 * File: test.java
 * File Created: Wednesday, 13th May 2020
 * Author: Nicholas Klvana-Hooper
 * -----
 * Last Modified: Friday, 15th May 2020
 * Modified By: Nicholas Klvana-Hooper
 * -----
 * Purpose: A test program showing that the Extended Euclidean algorithm works in the object
 * Reference: 
 */
import java.util.*;

public class test 
{
    public static void main(String[] args) 
    {
        if(args.length != 2) //must submit two numbers
        {
            System.out.println("Please execute with two numbers to test!");
        }
        else
        {
            Euclidean e = new Euclidean();

            //Print gcd passing the two values and also intialising s and t as 1
            System.out.println("gcd: " + e.extEuc(Integer.parseInt(args[0]), Integer.parseInt(args[1]), 1, 1));
            //print s and t by getting them from the class fied of Euc
            System.out.println("s: " + e.getS() + " t: " + e.getT()); 
        }
    }
}