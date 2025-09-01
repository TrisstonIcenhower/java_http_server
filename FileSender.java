import java.io.*;
import java.util.Scanner;

public class FileSender {
	// TEMP FILE PATHS
	// TODO: Replace with configurable static file pathing.
	static final String INDEX = "index.html";
	static final String VIEW_PATH = "views" + ServerConfig.getPathSeperator();
	static final String STYLE_PATH = "styles" + ServerConfig.getPathSeperator();
	static final String SCRIPT_PATH = "scripts" + ServerConfig.getPathSeperator();
	static public String getFile(String filePathString) {
		return fileRouter(filePathString.replace("/", ""));
	}

	static public String fileRouter(String filePathString) {
		if (filePathString.contains(".html")) {
			return fileToString(VIEW_PATH + filePathString);
		} else if (filePathString == "") {
			return fileToString(VIEW_PATH + INDEX);
		} else if (filePathString.contains(".css")) {
			return fileToString(STYLE_PATH + filePathString);
		} else if (filePathString.contains(".js")) {
			return fileToString(SCRIPT_PATH + filePathString);
		}

		return fileToString(VIEW_PATH + "404.html");
	}

	static public String fileToString(String filePathString) {
		String contents = "";
		InputStream file;
		try {
			file = new FileInputStream(new File(filePathString));

			Scanner scan = new Scanner(file);

			while (scan.hasNextLine()) {
				contents += scan.nextLine();
			}

			scan.close();

			try {
				file.close();
			} catch (IOException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}

			return contents;
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return "404";
		}
	}
}
