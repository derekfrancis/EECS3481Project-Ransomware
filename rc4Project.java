import java.util.Base64 ;
import java.nio.charset.StandardCharsets;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class rc4Project {
	
	public static int[] s = new int[256]; 
	public static byte[] KTemp  ; // 
	public static byte[] K = new byte[256] ;
	public static byte[] plaintext ; 
	public static byte[]  ks ;
	public static byte[] ct;
	
	
	 public static void main(String[] args) {
				 
	 }
	 
	 // Sets the key to be used.
	 
		public static void setKey(String key) {
			KTemp = key.getBytes() ; 
		}

		public static void readFile(File file) {

			
			try {
				plaintext = Files.readAllBytes(Paths.get(file.getAbsolutePath()));	
				ks = new byte [plaintext.length]  ;
				ct = new byte [plaintext.length] ;
				
			}catch (IOException e) {
	            e.printStackTrace();
	        }
		}
	
/*
 * This method performs encryption and decryption on the plaintext array by xor-ing all the bytes of the plaintext with the keystream
 * The same method is also used for decryption. when the file is read, the ecnrypted contents are stored
 * in the plaintext array and are xored with the key stream to get back the regular plaintext.
 */
	
public static void encryptDecrypt(File file)  {
	
	for (int i = 0 ; i < plaintext.length; i ++ ) {
		ct[i] = (byte) ( plaintext[i] ^ ks[i] );
	}
	
	try {
   		   Files.write(file.toPath(), ct);
   		   
   		} catch (IOException e) {
   			// TODO Auto-generated catch block
   			e.printStackTrace();
		} 

}
/*
 * This method initializes the state array . each element stores the value of its index
 * eg. element 1 will have value 1, element 2 will have 2 .....
 */


/*
 * Initializes the STATE and the key array to store the contents of the key in a256 byte array
 * each element in the state array stores the value of its index eg. element 1 will have value 1, element 2 will have 2 .....
 */
	
public static void initializeStateANDKeyArray() {
	// initializing key and state array . 
	
			for (int i = 0 ; i < 256; i ++ ) {
				K[i] = KTemp[i % KTemp.length];
			    s[i] =   i ; 
			}
	     
}

/*
 * 
the first stage is the Key Scheduling Algorithm which uses the key matrix to swap or permutate
the bytes of the state vector. The result of the KSA is a permutated state vector.
 */
	
	
	public static void keyScheduling()  {
		int j = 0 ;
		
		for (int i = 0 ; i < 256; i ++) {
			j =  (int) (j+ (int) s[i] + (int) K[i]) % 256 ; 
			
			int temp = s[i] ; 
			s[i] = s[j] ;
			s[j] = temp;

		}
	}

/*
 * The second stage (Pseudo random generation algorithm ) then takes the permutated state vector and performs a second round of permutation
on the state vector to generate a completely pseudo randomized stream called the keystream of the same length as the plaintext.
 */
	

	public static void pseudoRandomStreamGeneration() {
		int i = 0, j = 0, t = 0 ;
		
		for (int x = 0 ; x < plaintext.length; x ++) {
			i = (i + 1) % 256 ;
			j = (j + s[i])% 256 ; 
			
			int temp = s[i] ; 
			s[i] = s[j] ;
			s[j] = temp;
			
			t = (s[i] + s[j])% 256;
			ks[x] =  (byte) s[t] ; 
		}
		
	}

	
}
