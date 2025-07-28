package TCP_Server;

import java.io.*;
import java.util.Scanner;

public class FileSender {
	
	static public String fileToString(String filePathString) {
		String contents = "";
		InputStream file;
		try {
			file = new FileInputStream(new File(filePathString));
			
			Scanner scan = new Scanner(file);
			
			while(scan.hasNextLine()) {
				contents += scan.nextLine();
			}

			scan.close();
			
			try {
				file.close();
			}
			catch(IOException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			
			return contents;
		}
		catch(FileNotFoundException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return "";
		}
	}
}
