import java.util.*;

public class Question1
{
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        String plaintext = "";

        System.out.println("Please enter text");
        plaintext = sc.next();

        System.out.println(EncryptString(plaintext));

        System.out.println(plaintext);
    }

    public static String EncryptString(String plaintext)
    {
        int currInt = 0;
        char currChar = 'a';
        String cipherText = "";

        for (int i=0; i < plaintext.length(); i++)
        {
            currChar = plaintext.charAt(i);
            currInt = Integer.parseInt((String)currChar);
            currInt = (currInt + 3) % 26;
            currChar = (char)String.valueOf(currInt);
            ciphertext += currChar;
        }

        return cipherText;
    }
}

