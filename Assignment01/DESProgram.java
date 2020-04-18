import java.io.*;
import java.util.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DESProgram
{
    public static void main(String[] args) 
    {
        String plaintext = "0123456789ABCDEF"; //0123456789ABCDEF
        String key = "133457799BBCDFF1";
        String ciphertext;
        LocalTime myDateObj = LocalTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("HH.mm.ss");

        String formattedDate = myDateObj.format(myFormatObj);

        DES des1 = new DES();
        
        des1.keyGen(key);
        //System.out.println("encrypted: " + des1.encrypt(plaintext)); //85e813540f0ab405
        //System.out.println("decrypted: " + des1.decrypt("85e813540f0ab405"));
        //System.out.println("decrypted: " + des1.decrypt("9FFEC45C707B32D9"));
        //writeOneRow("outputE" + formattedDate, readFile("test", des1, true));
        writeOneRow("outputD" + formattedDate, readFile("output", des1, false)); 
    }

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
            while (line != null && !(line.equals("")))
            {
                plainText = processLine(line, plainText, des1, doEncrypt);
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

    private static String processLine(String line, String plainText, DES des1, Boolean doEncrypt) throws IllegalStateException
    {
        String temp = "";
        if(doEncrypt)
        {
            temp = processEncrypt(line, plainText, des1);       
        }
        else
        {
            temp = processDecrypt(line, plainText, des1); 
        }
        
        return temp;
    }

    private static String processEncrypt(String line, String plainText, DES des1)
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
                    temp += charToHex(line.charAt(count));
                }
                else
                {
                    if(i == 8)
                    {
                        temp += "0A";
                        ended = true;
                    }
                    else
                    {
                        temp += "00";
                    }
                }
            }
            plainText += des1.encrypt(temp) + '\n';
        }
        if(!ended)
        {
            temp += "000000000000000A";
            plainText += des1.encrypt(temp) + '\n';
        }
        return plainText;
    }

    private static String processDecrypt(String line, String ciphertext, DES des1)
    {
        String temp;

        for(int i =0; i < line.length()/16; i++)
        {
            temp = "";
            for(int j = 0; j < 16; j++)
            {
                System.out.println(i + ", " + j);
                temp += line.charAt(i*16+j);
            }
            temp = des1.decrypt(temp);

            for(int j = 0; j < 8; j++)
            {
                ciphertext += binToChar(des1.hexToBin("" + temp.charAt(2*j) + temp.charAt(2*j+1)));
            }
        }

        return ciphertext;
    }

    private static void writeOneRow(String filename, String output)
    {
        FileOutputStream fileStrm = null;
        PrintWriter pw;

        try
        {
            fileStrm = new FileOutputStream(filename);
            pw = new PrintWriter(fileStrm);

            pw.print(output);

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

    
    public static String charToHex(char inChar)
    {
        int decimal;
        String hexStr;
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

        decimal = Integer.parseInt(output,2);
        hexStr = Integer.toString(decimal,16);

        return hexStr;
    }

    public static char binToChar(String inBin)
    {
        char output = ' ';
        int dec = 0;

        for(int i=0; i < inBin.length(); i++)
        {
            if(inBin.charAt(i) == '1')
            {
                dec += Math.pow(2, inBin.length()-i-1);
            }
        }

        output = (char)dec;

        return output;
    }
}