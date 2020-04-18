import java.io.*;
import java.util.*;

public class DESProgram
{
    public static void main(String[] args) 
    {
        String plaintext = "0123456789ABCDEF"; //0123456789ABCDEF
        String key = "133457799BBCDFF1";
        String ciphertext;

        DES des1 = new DES();
        
        des1.keyGen(key);
        //System.out.println("encrypted: " + des1.encrypt(plaintext)); //85e813540f0ab405
        //System.out.println("decrypted: " + des1.decrypt("85e813540f0ab405"));
        ciphertext = des1.encrypt(readFile("test"));
        System.out.println("encrypt: " + des1.encrypt(ciphertext));
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
        Boolean ended = false;
        int count = -1;
        String temp = "";
        while (count < line.length())
        {
            for(int i= 1; i < 9; i++)
            {
                count += 1;
                if(count < line.length())
                {
                    temp += charToBin(line.charAt(count));
                    System.out.println(temp);
                    System.out.println(line.charAt(count));
                }
                else
                {
                    if(i ==8)
                    {
                        temp += "00001010";
                        ended = true;
                    }
                    else
                    {
                        temp += "00000000";
                    }
                }
            }
        }
        if(!ended)
        {
            temp += "0000000000000000000000000000000000000000000000000000000000101010";
        }

        plainText += temp;
        System.out.println(plainText);

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

    
    public static String charToBin(char inChar)
    {
        String output = "";
        int dec = inChar;

        for(int i=7; i >= 0; i--)
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
}