package CSCI_345_HW1;

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
    private static final String wordsPath = "C:\\Users\\patri\\Documents\\Password Cracker\\wordList.txt";
    private static final String passFilePath = "C:\\Users\\patri\\Documents\\Password Cracker\\examplePasses.txt";
    private ArrayList<String> passHashes;
    private ArrayList<String> passwds;
    public PasswordCracker()
    {
        passHashes = new ArrayList<String>();
        passwds= new ArrayList<String>();
    }

    public String getWordsPath()
    {
        return wordsPath;
    }
    public String getPassPath() 
    {
    	return passFilePath;
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
                passHashes.add(tokens[1]);
//                System.out.println(hashes);
            }
            passSc.close();
            File wordsFile = new File(wordsPath);
            Scanner wordsSc = new Scanner(wordsFile);
            MessageDigest md = MessageDigest.getInstance("MD5");
            for ( int i = 0 ; i <  passHashes.size() ; i ++ )
            {
                while ( wordsSc.hasNextLine() ) {

                    String hash = passHashes.get(i);
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
//            System.out.println(passwds);
                
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
        passCracker.crack("C:\\Users\\patri\\Documents\\Password Cracker\\passFile.txt");
    }
}

