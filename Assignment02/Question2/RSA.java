/*
 * File: RSA.java
 * File Created: Thursday, 14th May 2020
 * Author: Nicholas Klvana-Hooper
 * -----
 * Last Modified: Friday, 15th May 2020
 * Modified By: Nicholas Klvana-Hooper
 * -----
 * Purpose: Class for an object that implements the Lehmann Algorithm to check if a number is prime.
 * Reference: RSA Algorithm based on assignment hints Google Drive on RSA 
 *            (https://drive.google.com/open?id=1CDDTnTTnFAVFbQHtFcX4QJ93mAawJLft)
 *            Accessed on the 14th May 2020
 * 
 *            Fast mod algorith based on pseudocode from Cryptography and Network Security: Principles
 *            and Practice, Sixth Edition (ISBN 13: 978-0-13-335469-0, page 268, figure 9.8)
 *            Accessed on the 14th May 2020
 */
import java.util.*;
import java.io.*;

public class RSA 
{
    public static void main(String[] args) 
    {
        Random rand = new Random();
        Euclidean euc = new Euclidean();
        int p, q, n, d, phiN;
        int e = 0;
        String plain, cipher, input, fileIn;
        Boolean done = false;
        char encDec = 'a';

        if(args.length != 5) //Must be 5 arguments entered
        {
            System.out.println("Please use code like java RSA <p> <q> <e> <either e or d> <file to read>!");
            System.out.println("e for encrypt, d for decrypt!");
        }
        else
        {
            p = Integer.parseInt(args[0]);
            q = Integer.parseInt(args[1]);
            e = Integer.parseInt(args[2]);
            encDec = args[3].charAt(0);
            fileIn = args[4];

            n = p * q; //n is equal to p*q
            phiN = (p - 1) * (q - 1); //phi of N

            //the gcd of e and phi of N must be equal to 1, if entered number isn't
            // find new number
            if(euc.method(e, phiN, 1, 1) != 1) 
            {
                while(!done) //keep going until correct number found
                {
                    if(euc.method(e, phiN, 1, 1) != 1)
                    {
                        //select a random number between 0 and phiN -1
                        e = rand.nextInt(phiN-1) + 1; 
                    }
                    else
                    {
                        //if gcd of new e and PhiN is 1, finished searching
                        done =true; 
                    }
                }

                //let user know we haven't used their value
                System.out.println("Given e is not coprime with phi of N");
                System.out.println("Using " + e +" instead");
            }

            if(encDec == 'e') //if e, encrypt message
            {
                plain = readFile(fileIn); //read from input file
                cipher = encrypt(plain, e, n); //perform RSA encryption on it
                writeOneRow("output", cipher); //Write it to file called output
                System.out.println("Successfully Encrypted!");
            }
            else if(encDec == 'd') //if d, decrypt
            {
                input = readFile(fileIn); //read from input file
                d = euc.getS(); //d is equal to the inverse mod of 'e' above.
                plain = decrypt(input, d, n); //decrypt using the inverse of the key
                writeOneRow("output", plain); //output to file called output
                System.out.println("Successfully Decrypted!");
            }
            else //otherwise incorrect value entered
            {
                System.out.println("You're 4th argument is incorrect Please enter:");
                System.out.println("  e for encryption, or d for decryption!");
            }
        }
        
    }

    /*
     * SUBMODULE: encrypt
     * IMPORT: plain(String), e(int), n(int)
     * EXPORT: cipher(String)
     * ASSERTION: Implements the RSA encrption based on google drive link at top of file.
     *            Decrypts plain with key of e modded by n
     */
    public static String encrypt(String plain, int e, int n)
    {
        String cipher = "";

        //go through every character in file, encrypting one by one
        for(int i = 0; i < plain.length(); i++) 
        {
            int currChar = 0;

            currChar = (int)plain.charAt(i); //convert char to int value
            //encrypt value using key e, and modded by n
            currChar = (int)fastMod(currChar, e, n);
            //add new encrypted character to cipher text String
            cipher += currChar;

            //If it isn't last character add "," at end to seperate each encrypted 
            //character for output file
            if(i+1 != plain.length())
            {
                cipher += ",";
            }
        }

        return cipher;
    }

    /*
     * SUBMODULE: decrypt
     * IMPORT: cipher(String), d(int), n(int)
     * EXPORT: plain(String)
    * ASSERTION: Implements the RSA decryption based on google drive link at top of file.
     *            Decrypts cipher with key of d modded by n
     */
    public static String decrypt(String cipher, int d, int n) //d is inverse of e
    {
        String plain = "";
        String[] str;
        
        //split ciphertext on the commas, getting each individual value
        str = cipher.split(",");

        //go through all characters in ciphertext
        for(int i = 0; i < str.length; i++)
        {
            int currChar = 0;

            //Convert character to int value
            currChar = Integer.parseInt(str[i]);
            //decrypt char value using d key modded by n
            currChar = (int)fastMod(currChar, d, n);
            //add decrypted character to plain text string
            plain += (char)currChar;
        }

        return plain;
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

    /*
     * SUBMODULE: readFile
     * IMPORT: inFilename(String)
     * EXPORT: input(String)
     * ASSERTION: reads from the file line by line
     */
    private static String readFile(String inFilename)
    {
        FileInputStream fileStrm = null;
        InputStreamReader rdr;
        BufferedReader bufRdr;
        String line;
        String input = "";

        try
        {
            fileStrm = new FileInputStream(inFilename);
            rdr = new InputStreamReader(fileStrm);
            bufRdr = new BufferedReader(rdr);

            line = bufRdr.readLine();
            while (line != null) //read line by line unless line is null
            {
                input += line + "\n"; //process the line and add new line to keep format
                line = bufRdr.readLine();
            }
            input = input.substring(0, input.length()-1);
            fileStrm.close();
        }
        catch(IOException e)
        {
            if(fileStrm != null)
            {
                try
                {
                    fileStrm.close();
                }
                catch(IOException ex2)
                {
                }
            }
            System.out.println("Error in file processing: " + e.getMessage());
        }
        return input;
    }

        /*
     * SUBMODULE: writeOneRow
     * IMPORT: filename(String), output(String)
     * EXPORT: 
     * ASSERTION: Writes String to file
     */
    private static void writeOneRow(String filename, String output)
    {
        FileOutputStream fileStrm = null;
        PrintWriter pw;

        try
        {
            fileStrm = new FileOutputStream(filename);
            pw = new PrintWriter(fileStrm);

            pw.print(output); //print string

            pw.close();
        }
        catch (IOException e)
        {
            if(fileStrm != null)
            {
                try
                {
                    fileStrm.close();
                }
                catch(IOException ex2)
                {
                }
            }
            System.out.println("Error in writing to file: " + e.getMessage());
        }
    }
}