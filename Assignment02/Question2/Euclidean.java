/*
 * File: Euclidean.java
 * File Created: Wednesday, 13th May 2020
 * Author: Nicholas Klvana-Hooper
 * -----
 * Last Modified: Friday, 15th May 2020
 * Modified By: Nicholas Klvana-Hooper
 * -----
 * Purpose: Class for an object that does all of the Extended Euclidean algorithm to find gcd as well as s and t for two values
 * Reference: Extended Euclidean algorithm based on https://www.geeksforgeeks.org/euclidean-algorithms-basic-and-extended/
 *            Accessed on the 13th May 2020
 */
import java.util.*;

public class Euclidean
{
    private  int s, t;

    /*
     * SUBMODULE: Euclidean Default constructor
     * IMPORT:
     * EXPORT:
     * ASSERTION: Default Constuctor for the Euclidean algorithm
     */
    public void Euclidean()
    {
        s = 0;
        t = 0;
    }

    /*
     * SUBMODULE: DES Default Constructor 
     * IMPORT: front(int), mod(int), inS(int), inT(int)
     * EXPORT: gcd(int)
     * ASSERTION: Implements the extended euclidean algorithm
     */
    public int extEuc(int front, int mod, int inS, int inT)
    {
        int gcd;

        // Base Case 
        if (front == 0) 
        { 
            s = 0; 
            t = 1; 
            gcd = mod; 
        }
        else
        {
             // not base case so call function again
            gcd = extEuc(mod%front, front, inS, inT); 
      
            inS = t - (mod/front) * s;
            inT = s; //T is equal to previous s stored in class field
            s = inS; //store value in object's class field of t
            t = inT; //store value in object's class field of t
        } 

        return gcd;
    }


    /*
     * SUBMODULE: getS
     * IMPORT: 
     * EXPORT: s(int)
     * ASSERTION: Basic getter
     */
    int getS()
    {
        return s;
    }

    
    /*
     * SUBMODULE: getT
     * IMPORT: 
     * EXPORT: t(int)
     * ASSERTION: Basic getter
     */
    int getT()
    {
        return t;
    }
}