import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.ArrayList;
import java.security.MessageDigest;

public class PasswordCracker {
    private static final String wordsPath = "/usr/share/dict/words";
    private ArrayList<String> hashes;
    private ArrayList<String> passwds;
    public PasswordCracker()
    {
        hashes = new ArrayList<String>();
        passwds= new ArrayList<String>();
    }

    public String getWordsPath()
    {
        return wordsPath;
    }

    public void crack(String passFilePath)
    {
        File passFile = new File(passFilePath);
        try ( Scanner passSc = new Scanner(passFile) ) {

            while (passSc.hasNextLine()) {
                String line = passSc.nextLine();
                //System.out.println(line);
                String delims = "[:]";
                String[] tokens = line.split(delims);
                //System.out.print(tokens);
                hashes.add(tokens[1]);
                //System.out.println(hashes);
            }
            passSc.close();
            File wordsFile = new File(wordsPath);
            Scanner wordsSc = new Scanner(wordsFile);
            MessageDigest md = MessageDigest.getInstance("MD5");
            for ( int i = 0 ; i <  hashes.size() ; i ++ )
            {
                while ( wordsSc.hasNextLine() ) {

                    String hash = hashes.get(i);
                    String word = wordsSc.nextLine();
                    System.out.println(word);

                    byte[] provider = word.getBytes();
                    md.update(provider);
                    byte[] digest = md.digest();

                    String hashedWord = DatatypeConverter.printHexBinary(digest);
                    System.out.println(hashedWord);

                    if ( hash.compareTo(hashedWord) == 1) {
                        passwds.add(word);
                    }
                }
            }


        }
        catch ( FileNotFoundException e )
        {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[])
    {
        PasswordCracker passCracker = new PasswordCracker();
        passCracker.crack("/home/ethan/IdeaProjects/PasswordCracker/passFile.txt");
    }
}
