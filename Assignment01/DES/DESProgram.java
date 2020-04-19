/*
 * File: DESProgram.java
 * File Created: Wednesday, 15th April 2020
 * Author: Nicholas Klvana-Hooper
 * -----
 * Last Modified: Sunday, 29th April 2020
 * Modified By: Nicholas Klvana-Hooper
 * -----
 * Purpose: Implements the DES encryption/decryption algorithm in java using file reading and writing
 * Reference: Read and writing algorithms based on DSA Assignment 2019 written by Nicholas Klvana-Hooper(me)
 *            Accessed on the 4th April 2020
 */
import java.io.*;
import java.util.*;

public class DESProgram
{
    public static void main(String[] args) 
    {
        String tempKey = "";
        DES des1 = new DES();

        if(args.length != 4) //need all 5 arguments to run program if not display help message
        {
            System.out.println("Please use program by java DESProgram <fileName to read> <output filename> <encrypt/decrypt> <8 character key>");
            System.out.println("  Type can be e for encrypt, or d for decrypt!");
        }
        else
        {
            if(args[3].length() == 8) //key needs to be exactly 8 characters
            {
                for(int i=0; i <8; i++)
                {
                    tempKey += charToHex(args[3].charAt(i)); //change characters to hex
                }
                des1.keyGen(tempKey);

                if(args[2].charAt(0) == 'e') //if e then encrypt file
                {
                    //writeOneRow(args[1], readFile(args[0], des1, true));
                    writeOneRow(args[1], readFile(args[0], des1, true));
                }
                else if(args[2].charAt(0) == 'd') //if d then decrypt file
                {
                    //writeOneRow(args[1], readFile(args[0], des1, false));
                }
                else //otherwise incorrect character
                {
                    System.out.println("Non correct encrypt/decrypt chracter given");
                }
            }
            else //otherwise key is wrong size
            {
                System.out.println("Key is wrong size!");
            }
        }
    }

    /*
     * SUBMODULE: readFile
     * IMPORT: inFilename(String), des (DES), doEncrypt(Boolean)
     * EXPORT: plainText(String)
     * ASSERTION: reads from the file line by line
     */
    private static String readFile(String inFilename, DES des1, Boolean doEncrypt)
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

            line = bufRdr.readLine();
            while (line != null) //read line by line unless line is null
            {
                plainText = processLine(line, plainText, des1, doEncrypt); //process the line and encrypt it
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
     * IMPORT: line(String) plainText(String), des1(DES), doEncrypt(Boolean)
     * EXPORT: temp(String)
     * ASSERTION: Processes line and encrypts/decrypts it
     */
    private static String processLine(String line, String plainText, DES des1, Boolean doEncrypt) throws IllegalStateException
    {
        String temp = "";
        if(doEncrypt) //if doEncrypt is true then encrypt it or decrypt it
        {
            temp = processEncrypt(line, plainText, des1);       
        }
        else
        {
            temp = processDecrypt(line, plainText, des1); 
        }
        
        return temp;
    }

    /*
     * SUBMODULE: processEncrypt
     * IMPORT: line(String) ciphertext(String), des1(DES)
     * EXPORT: ciphertext(String)
     * ASSERTION: Takes in line and encrypts it
     */
    private static String processEncrypt(String line, String ciphertext, DES des1)
    {
        Boolean ended = false;
        String temp;

        for(int i=0; i < (int)Math.ceil(line.length()/8.0); i++) //Go through text by 8 bit
        {
            temp = "";
            for(int j=0; j < 8; j++) //Go through each 8 bit character block
            {
                if((i*8+j) < line.length()) //if have not traversed through entire of text in 8 bit block add next character
                {
                    temp += charToHex(line.charAt(i*8+j));
                }
                else //otherwise either add a padding or a newline character at end
                {
                    if(j == 7)
                    {
                        temp += "0A";
                        ended = true; //if end line character is placed, line is finished
                    }
                    else
                    {
                        temp += "00";
                    }
                }
            }
            ciphertext += des1.encrypt(temp); //encrypt 8 bit block
        }
        if(!ended) //if no new line character fit in the 8 bit blocks of the line then add a block for it
        {
            temp = "000000000000000A";
            ciphertext += des1.encrypt(temp);
        }

        return ciphertext  + '\n'; //add new line for formatting to remain in tact
    }

    /*
     * SUBMODULE: processDecrypt
     * IMPORT: line(String), plaintext(String), des1(DES)
     * EXPORT: plaintext(String)
     * ASSERTION: Taken in string, outputs a graph of frequency each alphabet appears
     */
    private static String processDecrypt(String line, String plaintext, DES des1)
    {
        String temp;

        for(int i =0; i < line.length()/16; i++) //go through in 16 bit hex blocks
        {
            temp = "";
            for(int j = 0; j < 16; j++) //iterate through 16 bit hex block
            {
                temp += line.charAt(i*16+j);
            }
            temp = des1.decrypt(temp); //decrypt the block

            for(int j = 0; j < 8; j++) //go through in 8 bit chunks, 2 at a time changing hex back to characters
            {
                plaintext += binToChar(des1.hexToBin("" + temp.charAt(2*j) + temp.charAt(2*j+1)));
            }
        }

        return plaintext;
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

            pw.print(output.replaceAll("[\u0000]","")); //prints file but gets riff of any character with an ascii value of 0000
            //This is to avoid having the weird characters print

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

    /*
     * SUBMODULE: charToHex
     * IMPORT: inChar(char)
     * EXPORT: hexStr(String)
     * ASSERTION: converts character to hex
     */
    public static String charToHex(char inChar)
    {
        int decimal;
        String hexStr;
        String output = "";
        int dec = inChar; //change char to integer value
        
        for(int i=7; i >= 0; i--) //iterates making a 8 bit binary string
        {
            if(dec >= Math.pow(2, i)) //converts to binary string
            {
                output += "1";
                dec -= Math.pow(2,i);
            }
            else
            {
                output += "0";
            }
        }

        decimal = Integer.parseInt(output,2); //convert binary to decimal
        hexStr = Integer.toString(decimal,16); //convert into hex String

        return hexStr;
    }

    /*
     * SUBMODULE: binToChar
     * IMPORT: inBin(String)
     * EXPORT:  output(char)
     * ASSERTION: Takes in a binary String and converts it into a character
     */
    public static char binToChar(String inBin)
    {
        char output = ' ';
        int dec = 0;

        for(int i=0; i < inBin.length(); i++) //iterates through binary string converting it to decimal
        {
            if(inBin.charAt(i) == '1')
            {
                dec += Math.pow(2, inBin.length()-i-1);
            }
        }

        output = (char)dec; //converts the integer value into a character

        return output;
    }
}