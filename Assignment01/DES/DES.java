/*
 * File: DES.java
 * File Created: Wednesday, 15th April 2020
 * Author: Nicholas Klvana-Hooper
 * -----
 * Last Modified: Sunday, 29th April 2020
 * Modified By: Nicholas Klvana-Hooper
 * -----
 * Purpose: Implements the DES encryption/decryption algorithm in java using file reading and writing
 * Reference: DES process based on http://page.math.tu-berlin.de/~kant/teaching/hess/krypto-ws2006/des.htm
 *            Accessed on the 15th April 2020
 */

import java.util.*;

public class DES 
{
    private String key;
    private String[] keys = new String[16];

    private static final int[] PC1 = {57, 49 , 41, 33, 25, 17, 9, 1, 58, 50, 42, 34, 26, 18, 10, 2, 59, 51, 43, 35, 27, 19, 11, 3, 60, 52, 44, 36, 63, 55, 47, 39, 31, 23, 15, 7, 62, 54, 46, 38, 30, 22, 14, 6, 61, 53, 45, 37, 29, 21, 13, 5, 28, 20, 12, 4};

    private static final int[] PC2 = {14, 17, 11, 24, 1, 5, 3, 28, 15, 6, 21, 10, 23, 19, 12 , 4, 26, 8, 16, 7, 27, 20, 13, 2, 41, 52, 31, 37, 47, 55, 30, 40, 51, 45, 33, 48, 44, 49, 39, 56, 34, 53, 46, 42, 50, 36, 29, 32};

    private static final int[] L_SHIFT = {1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};

    private static final int[] IP = {58, 50, 42, 34, 26, 18, 10 , 2, 60, 52, 44, 36, 28, 20, 12, 4, 62, 54, 46, 38, 30, 22, 14, 6, 64, 56, 48, 40, 32, 24, 16, 8, 57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43, 35, 27, 19, 11, 3, 61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7};

    private static final int[] E = {32, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8, 9, 8, 9, 10, 11, 12, 13, 12, 13, 14, 15, 16, 17, 16, 17, 18, 19, 20, 21, 20, 21, 22, 23, 24, 25, 24, 25, 26, 27, 28, 29, 28, 29, 30, 31, 32, 1};

    private static final int[][][] S = {
    { {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
    {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
    {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
    {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13} },
    { {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
    {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
    {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
    {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9} },
    { {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
    {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
    {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
    {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12} },
    { {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
    {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
    {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
    {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14} },
    { {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
    {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
    {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
    {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3} },
    { {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
    {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
    {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
    {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13} },
    { {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
    {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
    {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
    {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12} },
    { {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
    {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
    {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
    {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11} }
    };

    private static final int[] P = {16, 7, 20, 21, 29, 12, 28, 17, 1, 15, 23, 26, 5, 18, 31, 10, 2, 8, 24, 14, 32, 27, 3, 9, 19, 13, 30, 6, 22, 11, 4, 25};

    private static final int[] IP_I = {40, 8, 48, 16, 56, 24, 64, 32, 39, 7, 47, 15, 55, 23, 63, 31, 38, 6, 46, 14, 54, 22, 62, 30, 37, 5, 45, 13, 53, 21, 61, 29, 36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43, 11, 51, 19, 59, 27, 34, 2, 42, 10, 50, 18, 58, 26, 33, 1, 41, 9, 49, 17, 57, 25};

    /*
     * SUBMODULE: DES Default Constructor 
     * IMPORT:
     * EXPORT:
     * ASSERTION: Default Constuctor 
     */
    public DES()
    {
        key = "";
    }

    /*
     * SUBMODULE: keyGen
     * IMPORT: inKey(String)
     * EXPORT: 
     * ASSERTION: Generates keys from imported key
     */
    public void keyGen(String inKey)
    {
        String left, right;
        String temp = "";
        String[] c = new String[16];
        String[] d = new String[16];

        key = hexToBin(inKey.toUpperCase()); //converts character key to hex
        for(int i=0; i < (64-key.length()); i++) //pads the end of key with 0s
        {
            temp += "0";
        }
        key = temp + key; //adds padding to key
        key = permutate(key, PC1); //Original permutation
        left = key.substring(0, (key.length()/2)); //splits key into left and right
        right = key.substring(key.length()/2);
        
        for(int i=0; i < 16; i++) //16 iterations of shifting the left and right
        {
            if(i == 0) //original shift is based on the original left and right
            {
                c[0] = leftShift(left, L_SHIFT[0]);
                d[0] = leftShift(right, L_SHIFT[0]);
            }
            else
            {
                c[i] = leftShift(c[i-1], L_SHIFT[i]);
                d[i] = leftShift(d[i-1], L_SHIFT[i]);
            }
            keys[i] = permutate(c[i] + d[i], PC2);
        }
    }

    /*
     * SUBMODULE: encrypt
     * IMPORT: plaintext(String)
     * EXPORT: ciphertext(String)
     * ASSERTION: Encrypts plaintext with key
     */
    public String encrypt(String plaintext)
    {
        String ciphertext = "";
        String left, right, prevLeft;

        keyGen("0000000000000000");
        ciphertext = hexToBin(plaintext.toUpperCase());
        ciphertext = permutate(ciphertext, IP); //Original Permutation
        left = ciphertext.substring(0, 32); //Split into left
        right = ciphertext.substring(32); //Split into Right

        //16 iterations for L and R function XOR's
        for(int i=0; i < 16; i++)
        {
            prevLeft = left; //Save left to be xor-ed later
            left = right; //left equals previous right
            right = xor(rightFunc(right, i), prevLeft); //xor previous left with right function
        }
        ciphertext = permutate(switchS(right, left), IP_I); //final permutation, flip left and right
        ciphertext = binToHex(ciphertext); //convert binary to hex

        return ciphertext.toUpperCase();
    }

     /*
     * SUBMODULE: decrypt
     * IMPORT: ciphertext(String)
     * EXPORT: plaintext(String)
     * ASSERTION: Decrypts ciphertext with key
     */
    public String decrypt(String ciphertext)
    {
        String plaintext = "";
        String left, right, prevLeft;

        keyGen("0000000000000000");
        plaintext = hexToBin(ciphertext.toUpperCase());
        plaintext = permutate(plaintext, IP); //Original Permutation
        left = plaintext.substring(0, 32); //Split into left
        right = plaintext.substring(32); //Split into Right

        //16 iterations for L and R function XOR's
        for(int i=15; i > -1; i--)
        {
            prevLeft = left; //Save left to be xor-ed later
            left = right; //left equals previous right
            right = xor(rightFunc(right, i), prevLeft); //xor previous left with right function
        }
        plaintext = permutate(switchS(right, left), IP_I); //final permutation, flip left and right
        plaintext = binToHex(plaintext); //converts binary to hex

        return plaintext.toUpperCase(); //ensures uppercase for hex
    }

    /*
     * SUBMODULE: switchS
     * IMPORT: right(String), left(String)
     * EXPORT: right+left(String)
     * ASSERTION: Switches so left is on the right.
     */
    private String switchS(String right, String left)
    {
        return right + left;
    }

    /*
     * SUBMODULE: rightFunc
     * IMPORT: right(String), i(int)
     * EXPORT: right(String)
     * ASSERTION: Generates keys from imported key
     */
    private String rightFunc(String right, int i)
    {
        String temp, tempRight;
        int row, col;

        right = permutate(right, E); //original permutation of right side
        right = xor(right, keys[i]); //xor the key and permutated right

        tempRight = ""; //resets to blank
        
        //8 S_Box permutations
        for(int j=0; j < 8; j++)
        {
            temp = "" + right.charAt(j*6) + right.charAt(j*6+5); //row is based off first and last bits in String
            row = binToDec(temp); 
            temp = "" + right.charAt(j*6+1) + right.charAt(j*6+2) + right.charAt(j*6+3) + right.charAt(j*6+4); //column is based off the rest of the bits
            col = binToDec(temp);
            tempRight += decToBin(S[j][row][col]); //get binary of the S_box entries
        }
        right = permutate(tempRight, P); //Final permutation on right side

        return right;
    }

    /*
     * SUBMODULE: hexToBin
     * IMPORT: inKey(String)
     * EXPORT: tempKey(String)
     * ASSERTION: Converts hexadecimal to binary
     */
    public String hexToBin(String inKey)
    {
        int tempChar = 0;
        String tempKey = "";

        for(int i=1; i <= inKey.length(); i++) //go through string length
        {
            tempChar = (int)inKey.charAt(i-1); //Gets next character in key entered

            //FORMATS FROM ASCII
            if(tempChar >= 65)
            {
                tempChar -= 55;
            }
            else
            {
                tempChar -= 48;
            }

            for(int j=3; j >= 0; j--) //hexadecmial is based off 4 binary
            {
                if(tempChar >= Math.pow(2,j))
                {
                    tempKey += "1";
                    tempChar -= Math.pow(2,j);
                }
                else
                {
                    tempKey += "0";
                }
            }
        }
        return tempKey;
    }

    /*
     * SUBMODULE: binToHex
     * IMPORT: binary(String)
     * EXPORT: hex(String)
     * ASSERTION: converts binary to hex
     */
    public String binToHex(String binary)
    {
        String hex = "";
        int dec;

        for(int i=0; i < (binary.length()/4); i++) //traverses binary string in 4 bit blocks
        {
            dec = 0;
            for(int j=0; j < 4; j++) //traverses 4 bit blocks
            {
                if(binary.charAt(4*i+j) == '1') //if 1 adds the value from binary
                {
                    dec += Math.pow(2, 3-j);
                }
            }
            hex += Integer.toHexString(dec); //converts binary to hex
        }

        return hex;
    }

    /*
     * SUBMODULE: binToDec
     * IMPORT: binary(String)
     * EXPORT: dec(int)
     * ASSERTION: converts binary to decimal
     */
    public int binToDec(String binary)
    {
        int dec = 0;

        for(int i=0; i < binary.length(); i++) //iterates binary string
        {
            if(binary.charAt(i) == '1') //if one adds 2^ current location
            {
                dec += Math.pow(2, binary.length()-i-1);
            }
        }

        return dec;
    }

    /*
     * SUBMODULE: decToBin
     * IMPORT: dec(int)
     * EXPORT: output(String)
     * ASSERTION: Convets decimal to binary
     */
    public String decToBin(int dec)
    {
        String output = "";

        for(int i=3; i >= 0; i--) //makes 4 bit binary
        {
            if(dec >= Math.pow(2, i))
            {
                output += "1";
                dec -= Math.pow(2,i);
            }
            else
            {
                output += "0";
            }
        }
        
        return output;
    }

    /*
     * SUBMODULE: permutate
     * IMPORT: text(String) arr(int[])
     * EXPORT: temp(String)
     * ASSERTION: Permutatetes text based on value in array
     */
    private String permutate(String text, int[] arr)
    {
        String temp = "";
        
        for(int i=0; i < arr.length; i++) //goes through array and chooses specified bit
        {
            temp += text.charAt(arr[i]-1);
        }
        return temp;
    }

    /*
     * SUBMODULE: leftShift
     * IMPORT: currKey(String), numShift(int)
     * EXPORT: left shifted text(String)
     * ASSERTION: Left shifts the binary string by given number
     */
    private String leftShift(String currKey, int numShift)
    {
        int[] shiftBy = new int[currKey.length()]; //create array to enter permutation array
        for(int i=1; i <= shiftBy.length; i++) //enter values to new location value of permutated array
        {
            if((i+numShift) > currKey.length()) //if new value is above length of string mod it to bring it back to 0
            {
                shiftBy[i-1] = (i+numShift)%currKey.length();
            }
            else
            {
                shiftBy[i-1] = (i+numShift); //otherwise just change value
            }
        }
        return permutate(currKey, shiftBy); //now use array to permutate the array to left shift the data
    }

    /*
     * SUBMODULE: xor
     * IMPORT: one(String), two(String)
     * EXPORT: output(String)
     * ASSERTION: does a xor operation using the two strings
     */
    public String xor(String one, String two)
    {
        String output = "";

        for (int i=0; i < one.length(); i++) //go through bit by bit
        {
            output += ((int)one.charAt(i) + (int)two.charAt(i)) % 2; //do xor operation
        }

        return output;
    }
}