import java.io.*;
import java.util.*;

public class Affine
{
    public static void main(String[] args)
    {
        String fileName = "";
        char menuType = 'a';
        String inputText = "";
        String outputGraph = "";
        
        if(args.length != 4)
        {
            System.out.println("Please use program by java Question1 <fileName> <type> <a> <b>");
            System.out.println("  Type can be e for encrypt, or d for decrypt!");
        }
        else
        {
            fileName = args[0];
            menuType = args[1].charAt(0);
            if((menuType == 'e') || (menuType == 'd'))
            {
                inputText = readFile(fileName);

                graphFreq(inputText);

                if(menuType == 'e')
                {
                    inputText = EncryptString(inputText, Integer.parseInt(args[2]), Integer.parseInt(args[3]));
                }
                else
                {
                    inputText = DecryptString(inputText, Integer.parseInt(args[2]), Integer.parseInt(args[3]));
                }
                writeOneRow("output", inputText);
            }
            else
            {
                System.out.println("Incorrect menu option given. Please use e or d!");
            }
        }
    }

    public static void graphFreq(String text)
    {
        int[] numFreq = new int[26];
        int temp = 0;

        text.toLowerCase();
        for(int i=0; i < 26; i++)
        {
            numFreq[i] = 0;
        }
        for(int i=0; i < text.length(); i++)
        {
            temp = (int)text.charAt(i) - 97;
            if((temp >= 0) && (temp <= 25))
            {
                numFreq[temp] += 1;
            }
        }

        for(int i=0; i < 26; i++)
        {
            System.out.print((char)(i+97) + ": ");
            for(int j=1; j <= numFreq[i]; j++)
            {
                System.out.print("*");
            }
            System.out.print("\n");
        }
    }

    public static String EncryptString(String plaintext, int a, int b)
    {
        String cipherText = "";
	    int[] temp = new int[plaintext.length()];

        for (int i=0; i < plaintext.length(); i++)
        {
            if((((int)plaintext.charAt(i)) >= 97) && (((int)plaintext.charAt(i)) <= 122))
            {
                temp[i] = Encrypt(plaintext.charAt(i), 97, a, b);
            }
            else if((((int)plaintext.charAt(i)) >= 65) && (((int)plaintext.charAt(i)) <= 90))
            {
                temp[i] = Encrypt(plaintext.charAt(i), 65, a, b);
            }
            else
            {
                temp[i] = (int)plaintext.charAt(i);
            }
        }

        for (int i=0; i < temp.length; i++)
        {
            cipherText += (char)temp[i];
        }

        return cipherText;
    }

    public static int Encrypt(int currChar, int alphaStart, int a, int b)
    {
        int temp;
        
        temp = currChar - alphaStart;
        temp = ((a*temp + b) % 27) + alphaStart;

        return temp;
    }

    public static String DecryptString(String plaintext, int a, int b)
    {
        String cipherText = "";
        int[] temp = new int[plaintext.length()];
        int aInv = 0; //inverse of a

        for(int j=0; j < 27; j++) // find inverse of a
        {
            if(((a*j)%27)==1)
            {
                aInv = j;
            }
        }

        for (int i=0; i < plaintext.length(); i++)
        {
            if((((int)plaintext.charAt(i)) >= 97) && (((int)plaintext.charAt(i)) <= 123))
            {
                temp[i] = Decrypt(plaintext.charAt(i), 97, aInv, b);
            }
            else if((((int)plaintext.charAt(i)) >= 65) && (((int)plaintext.charAt(i)) <= 91))
            {
                temp[i] = Decrypt(plaintext.charAt(i), 65, aInv, b);
            }
            else
            {
                temp[i] = (int)plaintext.charAt(i);
            }
        }

        for (int i=0; i < temp.length; i++)
        {
            cipherText += (char)temp[i];
        }

        return cipherText;
    }

    public static int Decrypt(int currChar,int alphaStart, int aInv, int b)
    {
        int temp;

        temp = currChar - alphaStart + 27;
        temp = (aInv * (temp - b)) % 27 + alphaStart;

        return temp;
    }

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

            line = bufRdr.readLine();
            while (line != null && !(line.equals("")))
            {
                plainText = processLine(line, plainText);
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

    private static void writeOneRow(String filename, String output)
    {
        FileOutputStream fileStrm = null;
        PrintWriter pw;

        try
        {
            fileStrm = new FileOutputStream(filename);
            pw = new PrintWriter(fileStrm);

            pw.println(output);

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

