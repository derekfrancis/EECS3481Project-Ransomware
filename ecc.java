import java.io.IOException;
import java.nio.file.Files;
import java.io.File;
import java.io.FileOutputStream;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;


public class ecc {
	/*public static void main(String[] args) throws Exception {//main method for testing
		Security.addProvider(new BouncyCastleProvider());

        KeyPairGenerator ecKeyGen = KeyPairGenerator.getInstance("ECIES");
        ecKeyGen.initialize(new ECGenParameterSpec("secp256r1"));//secp256r1 is a type of elliptic curve

        KeyPair kp = ecKeyGen.generateKeyPair();//makes key pairs
        
        PublicKey publicKey= kp.getPublic();//puts into public key
        PrivateKey privateKey= kp.getPrivate();//put into private key

        Cipher iesCipher = Cipher.getInstance("ECIES", "BC");// get ECIES cipher for encryptions
        Cipher iesDecipher = Cipher.getInstance("ECIES", "BC");// get ECIES cipher for decryption
        
        iesCipher.init(Cipher.ENCRYPT_MODE, publicKey);

        String message = "Hello World";//test message
        System.out.println("Public Key: " + new String(kp.getPublic().getEncoded()) + "\nPrivate Key: " + new String(kp.getPrivate().getEncoded()));// for testing

        byte[] ciphertext = iesCipher.doFinal(message.getBytes());//encrypting
        
        System.out.println("Public Key base64: " + new String( Base64.encode(publicKey.getEncoded())));//Keys in base64
        System.out.println("Private Key base64: " + new String(Base64.encode(privateKey.getEncoded())));
        
        File file= new File("/ecctest/test.txt");//testing file
        
        //encrypt(base64toPublicKey("MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEPtzLmDSPjzvoroBl8SHeLdju9XBSqw4JxzPYZFHhQXZzWEJFimp8zyAF0pyNFH5BXAiHRcFxkMmo2pO9MgPBfw=="), file);//testing with known key.
        

        iesDecipher.init(Cipher.DECRYPT_MODE, privateKey);//decrypting
        byte[] plaintext = iesDecipher.doFinal(ciphertext);
        
        decrypt(base64toPrivateKey("MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQgsb43zRmBzfF7FtHOu8HcPqFTvAFxGcXAADMfvGmK9tGgCgYIKoZIzj0DAQehRANCAAQ+3MuYNI+PO+iugGXxId4t2O71cFKrDgnHM9hkUeFBdnNYQkWKanzPIAXSnI0UfkFcCIdFwXGQyajak70yA8F/"), file);
        
        System.out.println(new String(plaintext));
	}*/
	
	public static void encrypt(Key key, File file) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, NoSuchProviderException, IllegalBlockSizeException, BadPaddingException, IOException{
		Security.addProvider(new BouncyCastleProvider());
		Cipher iesCipher = Cipher.getInstance("ECIES", "BC");//algorithm type and provider, BC for bouncy castle
		iesCipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] ciphertext = iesCipher.doFinal(Files.readAllBytes(file.toPath()));
		
		FileOutputStream fos= new FileOutputStream(file);
		fos.write(ciphertext);
		fos.close();
		
	}
	
	public static void decrypt(Key key, File file) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException{
		Security.addProvider(new BouncyCastleProvider());
		Cipher iesDecipher = Cipher.getInstance("ECIES", "BC");//algorithm type and provider, BC for bouncy castle
		iesDecipher.init(Cipher.DECRYPT_MODE, key);
		byte[] plaintext ;
		
		plaintext = iesDecipher.doFinal(Files.readAllBytes(file.toPath())); //gets bytes from file
		
		FileOutputStream fos= new FileOutputStream(file);
		fos.write(plaintext);
		fos.close();
	}
	
	public static KeyPair generateECKeyPair() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
		Security.addProvider(new BouncyCastleProvider());
        KeyPairGenerator ecKeyGen = KeyPairGenerator.getInstance("ECIES");
        ecKeyGen.initialize(new ECGenParameterSpec("secp256r1"));//secp256r1 is a type of elliptic curve
        KeyPair ecKeyPair = ecKeyGen.generateKeyPair();
        PublicKey publicKey= ecKeyPair.getPublic();
        PrivateKey privateKey= ecKeyPair.getPrivate();
        System.out.println("Public Key base64: " + new String( Base64.encode(publicKey.getEncoded())));
        System.out.println("Private Key base64: " + new String(Base64.encode(privateKey.getEncoded())));
        return ecKeyPair;
	}
	
	public static Key base64toPublicKey(String base) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException {
		Security.addProvider(new BouncyCastleProvider());
		return KeyFactory.getInstance("EC", "BC").generatePublic(new X509EncodedKeySpec(Base64.decode(base)));// x509Encoded is keyspec for public keys
	}
	
	public static Key base64toPrivateKey(String base) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException {
		Security.addProvider(new BouncyCastleProvider());
		return KeyFactory.getInstance("EC", "BC").generatePrivate(new PKCS8EncodedKeySpec(Base64.decode(base)));// PKCS8encoded is keyspec for private keys, this was the error causing decryption problems it was set as x509
	}
	
	
	//these dont work and arent necessary
	
	/*public static Key base64toPublicKey(String base) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException {
		Security.addProvider(new BouncyCastleProvider());
		byte[] decodedPublic= Base64.decode(base);
		PKCS8EncodedKeySpec publicKeyspec = new PKCS8EncodedKeySpec(decodedPublic);
		KeyFactory kf = KeyFactory.getInstance("EC", "BC");
	    PublicKey pubkey = kf.generatePublic(publicKeyspec);
	    return pubkey;
	}
	
	public static Key base64toPrivateKey(String base) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException {
		Security.addProvider(new BouncyCastleProvider());
		byte[] decodedPrivate= Base64.decode(base);
		PKCS8EncodedKeySpec privateKeyspec = new PKCS8EncodedKeySpec(decodedPrivate);
		KeyFactory kf = KeyFactory.getInstance("EC", "BC");
	    PrivateKey privKey = kf.generatePrivate(privateKeyspec);
	    return privKey;
	}*/
}

