/*
*  Authors: McCardle, Patrick; Hendrix, Ethan
*  Date: 02.05.18
*  Dr. X
*  CSCI 345
*
*  This project was created to test password cracking of the MD5 hashing algorithm
*  Provided a file in the MD5 password file format user:hash[:other:options]
*  this code can crack the hashes in that file using a standard common dictionary
*  for this project we have used the dictionary in /usr/share/dict/words
*  this is hard coded, but another dictionary could be used in the event that you wanted to
*  it would parse that file and use the words in it just as well
*
* */

import javax.xml.bind.DatatypeConverter;
import javax.xml.crypto.Data;
import java.security.NoSuchAlgorithmException;
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

    public ArrayList<String> getHashes() {
        return hashes;
    }

    public ArrayList<String> getPasswds() {
        return passwds;
    }
    private ArrayList<String> getPaswds(String hash) {
        return passwds;
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
                System.out.println(hashes);
            }
            passSc.close();
            //File wordsFile = new File(wordsPath);
            //Scanner wordsSc = new Scanner(wordsFile);
            //MessageDigest md = MessageDigest.getInstance("MD5");
            for ( int i = 0 ; i <  hashes.size() ; i ++ )
            {
                String passwd = check(hashes.get(i));
                passwds.add(passwd);
            }
        }



        catch ( FileNotFoundException e )
        {
            e.printStackTrace();
        }
        /*
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        */
    }

    private static String check(String hash){
        try
        {
            File wordsFile = new File(wordsPath);
            Scanner wordsSc = new Scanner(wordsFile);
            MessageDigest md = MessageDigest.getInstance("MD5");
            while ( wordsSc.hasNextLine() )
            {


                String word = wordsSc.nextLine();
                //System.out.println(word);

                byte[] provider = word.getBytes();
                md.update(provider);
                byte[] digest = md.digest();

                String hashedWord = DatatypeConverter.printHexBinary(digest);
                //System.out.println(hashedWord);

                if (hash.compareTo(hashedWord) == 0)
                {
                    //passwds.add(word);
                    return word;
                }
                /*
                 * A four char word from /u sr/share/dict/words (Linux or Mac)
                 * which gets the first letter  capitalized and a 1 - digit number appended
                 * */
                else if (word.length() == 4)
                {

                    String cap = Character.toUpperCase(word.charAt(0)) + word.substring(1) ;//+ "!";
                    provider = cap.getBytes();
                    md.update(provider);
                    digest = md.digest();
                    hashedWord = DatatypeConverter.printHexBinary(digest);
                    if (hash.compareTo(hashedWord) == 0)
                    {
                        //passwds.add(cap);
                        return cap;
                    }

                    else
                    {
                        ArrayList<String> capNums = new ArrayList<String>();
                        for( int i = 0 ; i < 10 ; i++)
                        {
                            capNums.add(cap+Integer.toString(i));
                        }
                        for( int i = 0 ; i < capNums.size() ; i++ )
                        {
                            provider = capNums.get(i).getBytes();
                            md.update(provider);
                            digest = md.digest();
                            String hashedCapNum = DatatypeConverter.printHexBinary(digest);
                            if( hash.compareTo(hashedCapNum) == 0 )
                            {
                                return capNums.get(i);
                            }
                        }
                        String punct = cap + "!";
                        provider = punct.getBytes();
                        md.update(provider);
                        digest = md.digest();
                        hashedWord = DatatypeConverter.printHexBinary(digest);
                        if (hash.compareTo(hashedWord) == 0) {
                            //passwds.add(punct);
                            return punct;
                        }
                    }
                }

                    /*
                    A five char word from /usr/share/dict/words with the letter 'e' in it which gets replaced
                    with the digit 3. (words with 2 2's treat as two separate words with e's, eg. sleep -> sl3ep
                    and sle3p, but not sl33p
                    */
                else if ( word.length() == 5 && word.contains("e"))
                {
                    char[] chars =  word.toCharArray();
                    int size = word.length();
                    ArrayList<String> combinations = new ArrayList<String>();
                    combinations.add(word.replaceAll("e","3"));
                    StringBuilder conv = new StringBuilder();
                    for ( int l = 0 ; l < size ; l ++ )
                    {
                        if ( chars[l] == 'e' )
                        {
                            chars[l] = '3';
                        }
                        conv.append(chars[l]);
                        if ( chars[l] == '3')
                        {
                            chars[l] = 'e';
                            combinations.add(conv.toString());
                        }
                    }

                    for (int l = 0 ; l < combinations.size() ; l++ )
                    {
                        provider = combinations.get(l).getBytes();
                        md.update(provider);
                        digest = md.digest();
                        String hashedComb = DatatypeConverter.printHexBinary(digest);

                        if(hash.compareTo(hashedComb) == 0)
                        {
                            //passwds.add(combinations.get(l));
                            return combinations.get(l);
                        }
                    }
                }
                /*
                else {

                }
                */
            }
            for ( Integer j = 0 ; j <= 999999 ; j ++ )
            {
                //System.out.println(j);
                String jString = Integer.toString(j);
                byte[] provider = jString.getBytes();
                md.update(provider);
                byte[] digest = md.digest();

                String hashedInt = DatatypeConverter.printHexBinary(digest);
                //System.out.println(hashedInt);

                if(hash.compareTo(hashedInt) == 0)
                {
                    //passwds.add(Integer.toString(j));
                    return Integer.toString(j);
                }

            }
        }

        catch(FileNotFoundException e) {
            e.printStackTrace();
            return hash;
        }
        catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();
            return hash;
        }
        return hash;
    }

    public static void main(String args[])
    {
        PasswordCracker passCracker = new PasswordCracker();
        passCracker.crack("passFile.txt");
        ArrayList<String> passwds = passCracker.getPasswds();
        System.out.println(passwds);
    }
}
