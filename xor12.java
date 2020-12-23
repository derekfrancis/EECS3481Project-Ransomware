


import java.io.File;
	import java.io.FileInputStream;
	import java.io.FileNotFoundException;
	import java.io.FileOutputStream;
	import java.io.IOException;
	import java.io.InputStream;
	import java.io.OutputStream;
	
	public class xor12 {
		public static void main (String myKey, File file) {
			String key = myKey;
			File encFile = file;
						
			try {
				encryptdecrypt(encFile, key);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public static void encryptdecrypt(File f1, String k) throws IOException {
			String path = f1.getParent() + "temp.txt";
			File tempFile = new File(path);	//temp file to be renamed
			
			try {
				tempFile.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			InputStream inrt = null;
			OutputStream out = null;
			inrt = new FileInputStream(f1);
			out = new FileOutputStream(tempFile);
			
			int read=-1;
			int ki=0;
				while((read=inrt.read())!=-1) {
					read= read ^ k.charAt(ki);
					out.write(read);
					ki++;
					if (ki >=k.length()) {
						ki=0;
					}
				}
				inrt.close();
				out.close();
				String name= f1.getAbsolutePath();//gets name of original file
				f1.delete();//deletes original file so that temp file can be renamed
				File f2= new File(name);
				tempFile.renameTo(f2);
		}
	}