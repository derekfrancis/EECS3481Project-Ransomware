import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;

public class RSAProject {
	
	public static  byte[] plaintext; //  stores the aes key
	public static  byte[] ciphertext; //  stores the encrypted/decrypted aes key
	public static PublicKey publicKey  ; // public key required for RSA encryption
	public static PrivateKey privateKey ; // Private Key required for RSA encryption
	public static byte[] key ; // stores the AES key as a byte array
	public static SecretKeySpec secretKey; // secret key generated for the AES encryption
	public static byte[] fileContent; // byte array to read in content from the files to be encrypted.
	//public  static String location ;
	public  static String secret; //  stores the secret needed to generate the AES key
	
	
	// reads the aes key generated from the file and stores in the plaintext variable.
	 public static byte[] readFile (File file) {
	    	
				try {
					
					plaintext = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
					//ct = new byte [plaintext.length];
					//originalString = Base64.getEncoder().encodeToString(plaintext);
					
				}catch (IOException e) {
		            e.printStackTrace();
		        }
				
				return plaintext;
				
	    }
	 
	 // This method write backs bytes to the file
	 public static void fileWriteBack(byte[] encryptedText, File file) {
		 
		
			try {
	   			Files.write(file.toPath(), encryptedText);

	   		} catch (IOException e) {
	   			// TODO Auto-generated catch block
	   			e.printStackTrace();
	   		}
	        catch (Exception e) 
	        {
	            System.out.println("Error while encrypting: " + e.toString());
	        }
	    
	 }
	 
	 
	 // This method generates public and private keys for the RSA encryption algortihm
	 public static void keyPairGenerator() throws Exception { 
		 
		  KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
	      kpg.initialize(2048);
	      KeyPair kp = kpg.genKeyPair();
	      publicKey = kp.getPublic();
	      privateKey = kp.getPrivate();
	      
	      System.out.println(publicKey.toString()) ;
	      System.out.println(privateKey.toString()) ;
	      
	      
	 }
	 
	 // NOTE: Only used for RSA ! 
	 // Encryption using the RSA algorithm 
	 public static void encrypt(String location)  {
		
		 try
	      {
	    	  Cipher encryptCipher = Cipher.getInstance("RSA");///ECB/OAEPWithSHA-256AndMGF1Padding");
	          encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

	          ciphertext = encryptCipher.doFinal(ciphertext) ;
	          key = ciphertext ; 
	          setBackAESKey(ciphertext); 
	         
	      } catch (Exception e) 
	      {
	          System.out.println("Error while decrypting: " + e.toString());
	      }
		 
		  // We now create a file and the store the encrypted aes key into it. 
	       File myObj = null ;
			  try {
	 	       myObj = new File(location + "\\AESKeyEncrypted.txt");
	 	      if (myObj.createNewFile()) {
	 	       
	 	      } else {
	 	        System.out.println("File already exists.");
	 	      }
	 	    } catch (IOException e) {
	 	      System.out.println("An error occurred.");
	 	      e.printStackTrace();
	 	    }
			  
			  
			fileWriteBack(key, myObj ) ; 
	      
	 }
	 
	 // NOTE: Only used for RSA ! 
	 // DEcryption using the RSA algorithm 
	 public static void decrypt(String location)  {
			
		 try
	      {
	    	  Cipher decryptCipher = Cipher.getInstance("RSA");
	          decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);

	          ciphertext = decryptCipher.doFinal(ciphertext) ;
	          key = ciphertext ; 
	          setBackAESKey(ciphertext); 
	          
	      } catch (Exception e) 
	      {
	          System.out.println("Error while decrypting: " + e.toString());
	      }
		 
		 File myObj = null ;
		  try {
	       myObj = new File(location + "\\AESKeyDecrypted.txt");
	      if (myObj.createNewFile()) {
	       
	      } else {
	        System.out.println("File already exists.");
	      }
	    } catch (IOException e) {
	      System.out.println("An error occurred.");
	      e.printStackTrace();
	    }
		  
		  
		fileWriteBack(key, myObj ) ; 
	      
	 }

	 
	 // This main method is mainly used for testing purposes. 
   public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException{

      /*
      String FilePath = "C:\\Users\\produ\\Desktop\\AES.txt"; // For the AES key
    // String files = "C:\\Users\\produ\\Desktop\\assignment\\HarryPotter_Paragraph.txt" ; // txt files
      String files = "C:\\Users\\produ\\Desktop\\assignment\\arduino.exe" ; // exe test files 
      
      File aesfile = new File (FilePath) ; // We create a file only to store the encrypted/decrypted aes key. 
      File test = new File (files); // File object 
      
      RSAProject.generateAESKeys() ; // We first generate the AES keys
     // RSAProject.fileWriteBack(secretKey.getEncoded(), aesfile) ; // Then we store this key in a file
      
      
      plaintext = RSAProject.readFile(aesfile) ; // reads the newly generated AES key 
      ciphertext = plaintext ; // sets ciphertext to the aes key as well 
      
      fileContent = RSAProject.readFile(test) ; // reads all the file content from the sample file to be encrypted, not from the aes file
      
      // We generate the RSA private/public keys. 
      try {
        RSAProject.keyPairGenerator();
      }catch (Exception E) {
    	  
      }
     
      
      
      /*
       * Steps in the overall encryption/decryption process. 
       * NOTE: This is just for demonstration/testing, the main code integration will be a bit different. 
       */
	   
	 /*
     RSAProject.encryptAES(fileContent ,  secretKey, test); // We first encrypt the files using the aes algorithm and the aes keys
     

     RSAProject.encrypt(); // Then, we encrypt the aes key that was generated earlier
     RSAProject.fileWriteBack(ciphertext, aesfile) ; // Now we write back the aes key to a file. 
     
     
     // The decryption process starts from here 
     
     RSAProject.decrypt(); //  Then we decrypt the aes key
     RSAProject.fileWriteBack(ciphertext, aesfile) ; // And write it back to the file  
    
      
      RSAProject.setBackAESKey(ciphertext); //  Now we set the newly decrypted key back into SecretKey object used for decryption
    
      fileContent = RSAProject.readFile(test) ; // reads in the encrypted file
      RSAProject.decryptAES(fileContent ,  secretKey, test);// decrypts the file using the newly decrypted key. 
      
      */
	   
	   
	   File[] filesArray = null;
	   
	   location = "C:\\Users\\produ\\Desktop\\assignment" ; 
       File Folder = new File(location);
       filesArray = Folder.listFiles();
       
       System.out.print("Enter a secret: ") ;
       Scanner in =  new Scanner (System.in) ; 
       String secret  = in.nextLine() ; 
        
       
      // RSAProject.generateAESKeys(secret);  //generates the AES keys and creates the file AESKey.txt that stores the AES key. 
     


       for(int i = 0 ; i < filesArray.length; i++ ) {
    	   
    	   if(!filesArray[i].getPath().contains("AES")) {
    		   File j = filesArray[i];
               byte[] content = AESClass.readFile(j);
    		   encryptAES(content,  secretKey, j ) ; 
    		   System.out.println(filesArray[i] + "\t is encrypted") ; 
    		   
    	   }
    	   
       }

       try {
			RSAProject.keyPairGenerator();
		} catch (Exception E) {

		}

		encrypt();
		//decrypt();

		for (int i = 0; i < filesArray.length; i++) {

			if (!filesArray[i].getPath().contains("AES")) {
				File j = filesArray[i];
				byte[] content = AESClass.readFile(j);
				decryptAES(content, secretKey, j);
				System.out.println(filesArray[i] + "\t is decrypted");

			}

		}
     
   }
   
   
   
   // This Whole section below, is solely AES, just how the section above the main was solely RSA
   
   public static void generateAESKeys(String secret, String location)  {
	   MessageDigest sha = null;
       try {
           key = secret.getBytes("UTF-8");
           sha = MessageDigest.getInstance("SHA-1");
           key = sha.digest(key);
           key = Arrays.copyOf(key, 16); 
           
           
           plaintext = key; 
           ciphertext = plaintext ; // cipherText corresponds to the plaintext that stores the aes key ;
           
           secretKey = new SecretKeySpec(key, "AES");
       } 
       catch (NoSuchAlgorithmException e) {
           e.printStackTrace();
       } 
       catch (UnsupportedEncodingException e) {
           e.printStackTrace();
       }
       
       // We now create a file and the store the encrypted aes key into it. 
       File myObj = null ;
		  try {
 	       myObj = new File(location + "\\AESKey.txt");
 	      if (myObj.createNewFile()) {
 	       
 	      } else {
 	        System.out.println("File already exists.");
 	      }
 	    } catch (IOException e) {
 	      System.out.println("An error occurred.");
 	      e.printStackTrace();
 	    }
		  
		  
		fileWriteBack(key, myObj ) ; 
   }
   
   
   // Takes the decrypted key and constructs another secretKey object
   public static void setBackAESKey(byte[] ciphertext)  {

           key = ciphertext ; 
           secretKey = new SecretKeySpec(key, "AES");

   }
   
   
   // encryption using the AES algorihtm
   public static void encryptAES(byte[] strToEncrypt, SecretKeySpec secretKey, File file ) 
   {
   	
   	byte[] encryptedText; 
       try
       {
           
           Cipher cipherAES = Cipher.getInstance("AES/ECB/PKCS5Padding");
         //  Cipher cipherAES = Cipher.getInstance("AES/ECB/PKCS5Padding");
           cipherAES.init(Cipher.ENCRYPT_MODE, secretKey);
           
        // encryptedText =  Base64.getEncoder().encodeToString(cipherAES.doFinal(strToEncrypt));//strToEncrypt.getBytes("UTF-8"))); 
          encryptedText = cipherAES.doFinal(strToEncrypt) ;
          
          fileWriteBack(encryptedText, file);

     }catch (Exception e) 
	      {
         System.out.println("Error while encrypting: " + e.toString());
     }
   }
   
   
   // Decryption using the AES algorithm 
   public static void decryptAES(byte[] strToEncrypt, SecretKeySpec secretKey, File file ) 
   {
   	
   	byte[] decryptedText; 
       try
       {
           
           Cipher cipherAES = Cipher.getInstance("AES/ECB/PKCS5Padding");
         //  Cipher cipherAES = Cipher.getInstance("AES/ECB/PKCS5Padding");
           cipherAES.init(Cipher.DECRYPT_MODE, secretKey);
           
        // encryptedText =  Base64.getEncoder().encodeToString(cipherAES.doFinal(strToEncrypt));//strToEncrypt.getBytes("UTF-8"))); 
          decryptedText = cipherAES.doFinal(strToEncrypt) ;
          
          fileWriteBack(decryptedText, file);

     }catch (Exception e) 
	      {
         System.out.println("Error while decrypting: " + e.toString());
     }
       
       
       
   }
 
}