package httpsever;

import java.util.Hashtable;

public class HttpRequest {
	public Hashtable<String, String> headers = new Hashtable<String, String>();
	public Hashtable<String, String> parameters = new Hashtable<String, String>();
	public Hashtable<String, String> body = new Hashtable<String, String>();

	public HttpRequest(String[] reqHeader, String reqType, String[] reqBody) {
		if(reqType == "GET"){
			parseGetRequest(reqHeader);
		}
		else{
			parsePostRequest(reqHeader, reqBody);
		}
	}

	private void parseGetRequest(String[] reqHeader) {
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

	private void parsePostRequest(String[] reqHeader, String[] reqBody) {
		try{
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

			if(headers.get("Content-Type").equals("application/x-www-form-urlencoded")){
				headers.put("Content-Type", "text/plain");
			}
		}

		for(int j = 0; j < reqBody.length; j++){
			String[] bodyLine = reqBody[j].split("=", 2);
			System.out.println("Body Line: " + bodyLine[0] + " : " + bodyLine[1]);
			body.put(bodyLine[0], bodyLine[1].trim());
		}
		}
		catch(Exception e){
			System.out.println("Error parsing POST request body.");
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
