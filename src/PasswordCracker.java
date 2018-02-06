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
            File wordsFile = new File(wordsPath);
            Scanner wordsSc = new Scanner(wordsFile);
            MessageDigest md = MessageDigest.getInstance("MD5");
            for ( int i = 0 ; i <  hashes.size() ; i ++ )
            {
                String hash = hashes.get(i);
                while ( wordsSc.hasNextLine() ) {


                    String word = wordsSc.nextLine();
                    //System.out.println(word);

                    byte[] provider = word.getBytes();
                    md.update(provider);
                    byte[] digest = md.digest();

                    String hashedWord = DatatypeConverter.printHexBinary(digest);
                    //System.out.println(hashedWord);

                    if (hash.compareTo(hashedWord) == 0) {
                        passwds.add(word);
                    }
                    /*
                    * A four char word from /u sr/share/dict/words (Linux or Mac)
                    * which gets the first letter  capitalized and a 1 - digit number appended
                    * */
                    else if (word.length() == 4) {
                        String cap = Character.toUpperCase(word.charAt(0)) + word.substring(i) ;//+ "!";
                        provider = cap.getBytes();
                        md.update(provider);
                        digest = md.digest();
                        hashedWord = DatatypeConverter.printHexBinary(digest);
                        if (hash.compareTo(hashedWord) == 0) {
                            passwds.add(cap);
                        }
                        else {
                            String punct = cap + "!";
                            provider = punct.getBytes();
                            md.update(provider);
                            digest = md.digest();
                            hashedWord = DatatypeConverter.printHexBinary(digest);
                            if (hash.compareTo(hashedWord) == 0) {
                                passwds.add(punct);
                            }
                        }
                    }

                    /*
                    A five char word from /usr/share/dict/words with the letter 'e' in it which gets replaced
                    with the digit 3. (words with 2 2's treat as two separate words with e's, eg. sleep -> sl3ep
                    and sle3p, but not sl33p
                    */
                    else if ( word.length() == 5 && word.contains("e")) {
                        char[] chars =  word.toCharArray();
                        int size = word.length();
                        ArrayList<String> combinations = new ArrayList<String>;
                        StringBuilder conv = new StringBuilder();
                        for ( int i ; i < size ; i ++ ) {
                            if ( chars[i] == 'e' ) {
                                chars[i] = '3';
                            }
                            conv.append(chars[i]);
                            if ( chars[i] == '3') {
                                chars[i] = 'e';
                                combinations.add(conv.toString());
                            }
                        }
                    }
                    else {
                        for ( Integer j = 0 ; j <= 999999 ; j ++ ) {
                            //System.out.println(j);
                            String jString = Integer.toString(j);
                            byte[] provider = jString.getBytes();
                            md.update(provider);
                            byte[] digest = md.digest();

                            String hashedInt = DatatypeConverter.printHexBinary(digest);
                            //System.out.println(hashedInt);

                            if(hash.compareTo(hashedInt) == 0) {
                                passwds.add(Integer.toString(j));
                            }

                        }
                    }
                }
            }
        }



        catch ( FileNotFoundException e )
        {
            e.printStackTrace();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[])
    {
        PasswordCracker passCracker = new PasswordCracker();
        passCracker.crack("/home/ethan/IdeaProjects/PasswordCracker/passFile.txt");
        ArrayList<String> passwds = passCracker.getPasswds();
        System.out.println(passwds);
    }
}
