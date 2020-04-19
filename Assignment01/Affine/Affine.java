/*
 * File: Affine.java
 * File Created: Tuesday, 14th April 2020
 * Author: Nicholas Klvana-Hooper
 * -----
 * Last Modified: Sunday, 29th April 2020
 * Modified By: Nicholas Klvana-Hooper
 * -----
 * Purpose: Implements the affine encryption/decryption algorithm in java with file reading and output
 * Reference: Algorithm for Affine taken from lec01 slides
 *            Accessed on the 14th April 2020
 *            Read and writing algorithms based on DSA Assignment 2019 written by Nicholas Klvana-Hooper(me)
 *            Accessed on the 4th April 2020
 */

import java.io.*;
import java.util.*;

public class Affine
{
    public static void main(String[] args)
    {
        String fileName = "";
        char menuType = 'a';
        String inputText = "";
        
        if(args.length != 5) //need all 5 arguments to run program if not display help message
        {
            System.out.println("Please use program by java Question1 <fileName> <encrypt/decrypt> <display graph> <a> <b>");
            System.out.println("  Type can be e for encrypt, or d for decrypt!");
            System.out.println("  To display graph use d or n to not");
        }
        else
        {
            fileName = args[0]; //file to read from
            menuType = args[1].charAt(0); //either encrypt or decrypt
            if((menuType == 'e') || (menuType == 'd'))
            {
                inputText = readFile(fileName); //read from file

                if(args[2].charAt(0) == 'd') //if told do display, execute method to graph chartacter frequency
                {
                    graphFreq(inputText);
                }
                else if (args[2].charAt(0) != 'n') //print if invalid argument
                {
                    System.out.println("Incorrect display graph character");
                }

                if(menuType == 'e') //encrypt if input is e
                {
                    inputText = encryptString(inputText, Integer.parseInt(args[3]), Integer.parseInt(args[4]));
                }
                else if(menuType == 'd') //decrpt if input is d
                {
                    inputText = decryptString(inputText, Integer.parseInt(args[3]), Integer.parseInt(args[4]));
                }
                else //or print error if invalid character
                {
                    System.out.println("Incorrect encrypt/decrypt character");
                }
                writeOneRow("output", inputText); //export text to file
                System.out.println("   File exported to file called 'output'");
            }
            else
            {
                System.out.println("Incorrect menu option given. Please use e or d!");
            }
        }
    }

    /*
     * SUBMODULE: graphFreq
     * IMPORT: text(String)
     * EXPORT: 
     * ASSERTION: Taken in string, outputs a graph of frequency each alphabet appears
     */
    public static void graphFreq(String text)
    {
        int[] numFreq = new int[26];
        int temp = 0;

        text.toLowerCase(); //doesn't count capatalised alphabet characters differently
        for(int i=0; i < 26; i++) //initalise count for every character to 0
        {
            numFreq[i] = 0;
        }
        for(int i=0; i < text.length(); i++) //iterates through string
        {
            temp = (int)text.charAt(i) - 97; //a becomes 0 and so on
            if((temp >= 0) && (temp <= 25)) //if it is an alphabet character add one to its count
            {
                numFreq[temp] += 1; 
            }
        }

        for(int i=0; i < 26; i++) //iterates through array printing the graph
        {
            System.out.print((char)(i+97) + ": ");
            for(int j=1; j <= numFreq[i]; j++)
            {
                System.out.print("*");
            }
            System.out.print("\n");
        }
    }

    /*
     * SUBMODULE: encryptString
     * IMPORT: plaintext(String) , a(int), b(int)
     * EXPORT: ciphertext(String)
     * ASSERTION: Encrypts a string with the affine cipher using inputs a and b
     */
    public static String encryptString(String plaintext, int a, int b)
    {
        String cipherText = "";
	    int[] temp = new int[plaintext.length()];

        for (int i=0; i < plaintext.length(); i++)
        {
            if((((int)plaintext.charAt(i)) >= 97) && (((int)plaintext.charAt(i)) <= 122)) //if the character is a lower case encrypt using 97 to make a = 0
            {
                temp[i] = encrypt(plaintext.charAt(i), 97, a, b);
            }
            else if((((int)plaintext.charAt(i)) >= 65) && (((int)plaintext.charAt(i)) <= 90))//if the character is a upper case encrypt using 65 to make A = 0
            {
                temp[i] = encrypt(plaintext.charAt(i), 65, a, b);
            }
            else //if not an alphabet do not encrypt it
            {
                temp[i] = (int)plaintext.charAt(i);
            }
        }

        for (int i=0; i < temp.length; i++) //turn ints back into alphabet characters
        {
            cipherText += (char)temp[i];
        }

        return cipherText;
    }

    /*
     * SUBMODULE: encrypt
     * IMPORT: currChar(int), alphaStart(int), a(int), b (int)
     * EXPORT: temp(int)
     * ASSERTION: Takes a character and uses affine algorithm to encrypt it
     */
    public static int encrypt(int currChar, int alphaStart, int a, int b)
    {
        int temp;
        
        temp = currChar - alphaStart; //takes away to make a = 0 and map all alphabet to ints between 0 and 25
        temp = ((a*temp + b) % 27) + alphaStart; //then apply affine algorithm

        return temp;
    }

    /*
     * SUBMODULE: decryptString
     * IMPORT: ciphertext(String) , a(int), b(int)
     * EXPORT: plaintext(String)
     * ASSERTION: Decrypts a string with the affine cipher using inputs a and b
     */
    public static String decryptString(String ciphertext, int a, int b)
    {
        String plaintext = "";
        int[] temp = new int[ciphertext.length()];
        int aInv = 0; //inverse of a

        for(int j=0; j < 27; j++) // find inverse of a
        {
            if(((a*j)%27)==1) //inverse function to find inverse of a
            {
                aInv = j;
            }
        }

        for (int i=0; i < ciphertext.length(); i++) //traverse ciphertext, decrypting it
        {
            if((((int)ciphertext.charAt(i)) >= 97) && (((int)ciphertext.charAt(i)) <= 123)) //if the character is a lower case decrypt using 97 to make a = 0
            {
                temp[i] = decrypt(ciphertext.charAt(i), 97, aInv, b);
            }
            else if((((int)ciphertext.charAt(i)) >= 65) && (((int)ciphertext.charAt(i)) <= 91)) //if the character is a lower case decrypt using 65 to make A = 0
            {
                temp[i] = decrypt(ciphertext.charAt(i), 65, aInv, b);
            }
            else //if not an alphabet do not encrypt it
            {
                temp[i] = (int)ciphertext.charAt(i);
            }
        }

        for (int i=0; i < temp.length; i++) //turn ints back into alphabet characters
        {
            plaintext += (char)temp[i];
        }

        return plaintext;
    }

    /*
     * SUBMODULE: decrypt
     * IMPORT: currChar(int), alphaStart(int), a(int), b (int)
     * EXPORT: temp(int)
     * ASSERTION: Takes a character and uses affine algorithm to encrypt it
     */
    public static int decrypt(int currChar,int alphaStart, int aInv, int b)
    {
        int temp;

        temp = currChar - alphaStart + 27; //takes away to make a = 0 and map all alphabet to ints between 0 and 25
        //the 27 is to ensure the value is above 0 to avoid java modding negative numbers incorrectly
        temp = (aInv * (temp - b)) % 27 + alphaStart; //then apply affine algorithm to decrypt

        return temp;
    }

    /*
     * SUBMODULE: readFile
     * IMPORT: inFilename(String)
     * EXPORT: plainText(String)
     * ASSERTION: Reads file and exports text
     */
    private static String readFile(String inFilename)
    {
        FileInputStream fileStrm = null;
        InputStreamReader rdr;
        BufferedReader bufRdr;
        String line;
        String plainText = "";

        try
        {
            fileStrm = new FileInputStream(inFilename);
            rdr = new InputStreamReader(fileStrm);
            bufRdr = new BufferedReader(rdr);

            line = bufRdr.readLine(); //Read line by line
            while (line != null && !(line.equals("")))
            {
                plainText = processLine(line, plainText); //processes the line
                line = bufRdr.readLine();
            }
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
        return plainText;
    }

    /*
     * SUBMODULE: processLine
     * IMPORT: line(String) , plainText(String)
     * EXPORT: plainText(String)
     * ASSERTION: Processes line and adds it to String 
     */
    private static String processLine(String line, String plainText) throws IllegalStateException
    {
        try
        {
            plainText += line;
        }
        catch(Exception e)
        {
            throw new IllegalStateException("Line has incorrect layout");
        }
        return plainText;
    }

    /*
     * SUBMODULE: writeOneRow
     * IMPORT: filename(String) , output(String)
     * EXPORT: 
     * ASSERTION: Writes text to file
     */
    private static void writeOneRow(String filename, String output)
    {
        FileOutputStream fileStrm = null;
        PrintWriter pw;

        try
        {
            fileStrm = new FileOutputStream(filename);
            pw = new PrintWriter(fileStrm);

            pw.println(output); //prints text to file

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

