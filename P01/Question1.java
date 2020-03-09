import java.util.*;

public class Question1
{
   private static final int C_VALUE = 3;

    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        String plaintext = "";
	String cipherText = "";

        System.out.println("Please enter text");
        plaintext = sc.next();

	plaintext = plaintext.toLowerCase();	

	cipherText = EncryptString(plaintext);
        System.out.println("Encrypted: " + cipherText);
	
	plaintext = DecryptString(plaintext);
	System.out.println("Decrypted: " + plaintext);
    }

    public static String EncryptString(String plaintext)
    {
        String cipherText = "";
	int[] temp = new int[plaintext.length()];

        for (int i=0; i < plaintext.length(); i++)
        {
            temp[i] = (int)(plaintext.charAt(i)) - 97;
            temp[i] = ((temp[i] + C_VALUE) % 26) + 97;
        }

	for (int i=0; i < temp.length; i++)
	{
		cipherText += (char)temp[i];
	}

        return cipherText;
    }

    public static String DecryptString(String plaintext)
    {
        String cipherText = "";
	int[] temp = new int[plaintext.length()];

        for (int i=0; i < plaintext.length(); i++)
        {
            temp[i] = (int)(plaintext.charAt(i)) - 97;
	    if ((temp[i]-3) < 0)
	    {
		temp[i] = temp[i] + 120;
	    }
	    else
	    {
		temp[i] = ((temp[i] - C_VALUE) % 26) + 97;
	    }
        }

	for (int i=0; i < temp.length; i++)
	{
		cipherText += (char)temp[i];
	}

        return cipherText;
    }
}

