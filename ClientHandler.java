import java.io.*;
import java.net.*;
import java.util.Hashtable;
import java.lang.Thread;

public class ClientHandler extends Thread {
	final String VIEW_PATH = "/views";

	final BufferedReader in;
	final OutputStream out;
	final Socket soc;

	ClientHandler(BufferedReader bufIn, OutputStream outStream, Socket s) {
		this.in = bufIn;
		this.out = outStream;
		this.soc = s;
	}

	@Override
	public void run() {
		String reqString = "";
		String line;
		try {
			while ((line = in.readLine()) != null && !line.isEmpty()) {
				reqString += line + "\n";
			}
		} catch (IOException e) {
			System.out.println("Failed to read request:\n" + e.getMessage());
			e.printStackTrace();
		}

		byte[] headerBytes;
		byte[] bodyBytes;

		try {
			// TODO: Digest request, customize response based on request.
			HTTPRequest httpReq = new HTTPRequest(reqString.split("\n"));
			String body = FileSender.fileToString("views/index/index.html");

			bodyBytes = body.getBytes("UTF-8");

			String headers = "HTTP/1.1 200 OK\r\n" +
					"Date: " + HelperFunctions.getRfc1123Format() + "\r\n" +
					"Connection: keep-alive\r\n" +
					"Server: MyServer v1.0\r\n" +
					"Content-Length: " + bodyBytes.length + "\r\n" +
					"Content-Type: text/html\r\n" +
					"\r\n";

			headerBytes = headers.getBytes("UTF-8");

			System.out.println("Attempt to write response.");
			try {
				out.write(headerBytes);
				out.write(bodyBytes);
				out.flush();
			} catch (IOException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		} catch (UnsupportedEncodingException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		System.out.println("Response written successfully.");

		try {
			this.in.close();
			this.out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class HTTPRequest {
	public Hashtable<String, String> headers = new Hashtable<String, String>();
	public Hashtable<String, String> parameters = new Hashtable<String, String>();

	HTTPRequest(String[] reqHeader) {
		parseRequest(reqHeader);
	}

	private void parseRequest(String[] reqHeader) {
		String[] firstLine = reqHeader[0].split(" ");
		for (int i = 0; i < firstLine.length; i++) {
			headers.put("METHOD", firstLine[0]);
			if (!firstLine[1].contains("?")) {
				headers.put("PATH", firstLine[1]);
			} else {
				parseParameters(firstLine);
			}
			headers.put("HTTP_VERSION", firstLine[2]);

			for (int x = 1; x < reqHeader.length; x++) {
				String[] headerLine = reqHeader[x].split(":", 2);
				headers.put(headerLine[0], headerLine[1].trim());
			}
		}
	}

	private void parseParameters(String[] reqPath) {
		String[] path = reqPath[1].split("\\?");
		String[] params = path[1].split("&");

		for (int j = 0; j < params.length; j++) {
			String[] paramTemp = params[j].split("=");
			parameters.put(paramTemp[0], paramTemp[1]);
		}

		headers.put("PATH", path[0]);
	}
}
