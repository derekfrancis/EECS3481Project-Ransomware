// Author: Rachel Tam
// EECS 3481 Group Project - Ransomware
// This class implements RSA algorithm

//package project;

import java.lang.Math;
import java.math.BigInteger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;


public class RSA {
	public static int[] publicKey = new int[2];		// int array of size 2 to store public key
	public static int[] privateKey = new int[2];	// int array of size 2 to store private key
	public static  byte[] key; 

	// this method can be used for testing purposes
	// Main class required a main method in order to run
	public static void main(String[] args) throws IOException {
		
		String test = "testfdfdfdddfd" ; 
		key = test.getBytes() ; 
		
		generateKey(17, 11);
		
	
		
		System.out.println(publicKey[0]); 
		System.out.println(publicKey[1]); 
		
		System.out.println(privateKey[0]); 
		System.out.println(privateKey[1]); 
	
		

	}
	
	// generate a pair of keys (public and private)
	public static void generateKey(int p, int q) {
		if (isPrime(p) == true && isPrime(q) == true) {
			int n = p * q;
			
			// there are 127 ASCII values, n must be larger than 127 or the encryption/decryption formula fails
			if (n < 128) {
				System.out.println("Error: The product of the prime numbers must be greater than 127.");
			}
			else {
				int z = (p - 1) * (q - 1);
				String nBinary = Integer.toBinaryString(n);
				
				if (nBinary.length() > 1024) {
					System.out.println("RSA laboratories recommends that the product of p and q be 1024 bits long.");
				}
				
				int e = eGenerate(n, z);
				int d = dGenerate(e, z);
				
				publicKey[0] = e;		// assign public key value e
				publicKey[1] = n;		// assign public key value n
				
				privateKey[0] = d;		// assign private key value d
				privateKey[1] = n;		// assign private key value n
			}
		}
		else {
			System.out.println("Please try again. Numbers must be prime.");
		}	
	}
	
	// generates an e value such that 1< e < z < n and greatest common divisor of e and z is 1
	public static int eGenerate(int n, int z) {
		int e;
		
		while (true) {
			// to generate a random number between 2 and n (max - min + 1) + min
			e = (int) (Math.random() * (n - 2 + 1) + 1);
			
			if (e < z && gcd(e, z) == 1) {
				return e;
			}
		}	
	}
	
	// generates the smallest d value (not including 1) such that ed % z = 1
	public static int dGenerate(int e, int z) {
		int d = 1;
		
		while (true) {
			if ((e * d % z) == 1) {
				break;
			}
			
			d++;
		}
		
		return d;
	}
	
	// method to find the greatest common divisor of integers a and b
	public static int gcd(int a, int b) {
		int gcd = 1;
		
		// check if a and b are divisible by i and reassign the value of gcd as larger numbers are found
		for (int i = 1; i <= a && i <= b; i++) {
			if (a % i == 0 && b % i == 0) {
				gcd = i;
			}
		}
		
		return gcd;
	}
	
	// method to check if a number is prime
	public static Boolean isPrime(int p) {
		// base cases
		if (p == 0 || p == 1) {
			return false;
		}
		
		for (int i = 2; i <= Math.sqrt(p); i++) {
			if (p % i == 0) {
				return false;
			}
		}
		return true;
	}
	
	// returns the public key
	public static int[] getPublicKey() {
		return publicKey;
	}
	
	// returns the private key
	public static int[] getPrivateKey() {
		return privateKey;
	}
	
	// encrypt file with public key
	/*
	public static void encrypt(File f, int[] pubK) throws IOException {
		// Encryption Algorithm:
		// C = P^e mod n
		// where C is the ciphertext, P is the plaintext, e and n are from the public key

		String path = f.getParent() + "temp.txt";	// get the path of the file
		File newFile = new File(path);				// new file to be the encrypted version of f
		
		// tries to create a new file
		try {
			newFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		InputStream in = new FileInputStream(f);			// creates input file stream
		OutputStream out = new FileOutputStream(newFile);	// creates output file stream
		
		int inRead;						// int value of byte read from file
		byte[] cByte = new byte[4];		// will be writing 4 bytes at a time, this is because the
										// value of c is sometimes larger than 1 byte, which causes
										// inaccurate value when writing to the file, and therefore
										// incorrect decryption
		
		BigInteger p;									// plaintext
		BigInteger pubE = BigInteger.valueOf(pubK[0]);	// convert e to BigInteger
		BigInteger pubN = BigInteger.valueOf(pubK[1]);	// convert n to BigInteger
		BigInteger c;									// ciphertext
		
		// read input file one byte at a time, and for each byte (read as ASCII int), applying the RSA
		// algorithm (C = P^e mod n) then turning C into a 4 byte array to then be written back to the file
		// loop continues until all bytes of the file of been read
		while (true) {
			inRead = in.read();		// reads a byte of data
			
			// if the input stream .read() method returns -1, it is at the end of the file
			if (inRead == -1) {
				break;
			}
			
			p = BigInteger.valueOf(inRead);				// convert the plaintext int to BigInteger
			c = p.modPow(pubE, pubN);					// RSA encryption (C = P^e mod n)
			
			cByte = ByteBuffer.allocate(4).putInt(c.intValue()).array();	// converts C into 4 byte array			
			
			out.write(cByte, 0, 4);						// write the 4 bytes of C back to the file
			
		}
		in.close();							// close the input stream
		out.close();						// close the output stream
		
		String name = f.getAbsolutePath();	// gets name of original file
		f.delete();							// deletes inputed file (so that it can be replaced)
		File file = new File(name);			// replaces deleted
		newFile.renameTo(file);				// renames the newly encrypted file to the original name
		
	}
	*/
	
	
	public static BigInteger encrypt (byte[] arr, int[] pubK) {
		BigInteger p = new BigInteger(arr);									// plaintext
		BigInteger pubE = BigInteger.valueOf(pubK[0]);	// convert e to BigInteger
		BigInteger pubN = BigInteger.valueOf(pubK[1]);	// convert n to BigInteger
		BigInteger c;									// ciphertext
		
		
		return p.modPow(pubE, pubN);
	}
	
	
	public static BigInteger decrypt (byte[] arr, int[] privK) {
		BigInteger c = new BigInteger(arr);										// ciphertext
		BigInteger privD = BigInteger.valueOf(privK[0]);	// convert d to BigInteger
		BigInteger publicN = BigInteger.valueOf(privK[1]);	// convert n to BigInteger
		BigInteger p;
		
		
		return c.modPow(privD, publicN);
		
	}
	
	// decrypt file with private key
	/*
	public static void decrypt(File f, int[] privK) throws IOException {
		// Decryption Algorithm:
		// P = C^d mod n
		// where P is the plaintext, c is the ciphertext, d and n are from the private key
		
		String path = f.getParent() + "temp.txt";	// get the path of the file
		File newFile = new File(path);				// new file to be the encrypted version of f
		
		// tries to create a new file
		try {
			newFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		InputStream in = new FileInputStream(f);			// creates input file stream
		OutputStream out = new FileOutputStream(newFile);	// creates output file stream
		
		int inRead;						// int value of byte read from file
		int cInt;
		byte[] data = new byte[4];		// will be reading 4 bytes at a time, this is because the
										// encrypted file was written 4 bytes at a time, so to successfully
										// decrypt the file, we must read 4 bytes at a time, then write
										// back to the file one byte at a time
		
		BigInteger c;										// ciphertext
		BigInteger privD = BigInteger.valueOf(privK[0]);	// convert d to BigInteger
		BigInteger privN = BigInteger.valueOf(privK[1]);	// convert n to BigInteger
		BigInteger p;
		
		// read input file 4 bytes at a time, and for every 4 bytes, applying the RSA algorithm
		// (P = C^d mod n) then P is written back to the file
		// loop continues until all bytes of the file of been read
		while (true) {
			inRead = in.read(data, 0, 4);	// reads 4 bytes of data and stores it in the byte array "data"
			
			// if the input stream .read() method returns -1, it is at the end of the file
			if (inRead == -1) {
				break;
			}
			cInt = ByteBuffer.wrap(data).getInt();			// converts ciphertext bytes to int
			c = BigInteger.valueOf(cInt);					// convert the ciphertext int to BigInteger
			
			p = c.modPow(privD, privN);						// RSA decryption (P = C^d mod n)

			// write method doesn't take BigInteger as input, so convert to int before writing to file
			out.write(p.intValue());
		}
		in.close();							// close input stream
		out.close();						// close output stream
		
		String name = f.getAbsolutePath();	// gets name of original file
		f.delete();							// deletes inputed file (so it can be replaced)
		File file = new File(name);			// replaces deleted
		newFile.renameTo(file);				// renames the newly encrypted file to the original name
	}
	
	*/
}
