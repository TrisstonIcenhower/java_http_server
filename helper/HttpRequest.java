package helper;

import java.util.Hashtable;

public class HttpRequest {
	public Hashtable<String, String> headers = new Hashtable<String, String>();
	public Hashtable<String, String> parameters = new Hashtable<String, String>();

	public HttpRequest(String[] reqHeader) {
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
