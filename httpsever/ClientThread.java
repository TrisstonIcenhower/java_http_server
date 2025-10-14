package httpsever;

import java.io.*;
import java.net.*;
import java.util.Objects;

import helper.ErrorPrinter;
import helper.DateHelper;

public class ClientThread {
    private BufferedReader in;
	private OutputStream out;
	private Socket soc;
	private Router router;

    ClientThread(BufferedReader in, OutputStream out, Socket soc, Router router){
        this.in = in;
        this.out = out;
        this.soc = soc;
		this.router = router;
    }

    public void run(){
        while(true){
            String reqString = "";
            String line;

            try{
                    while((line = in.readLine()) != null && !line.isEmpty()){
                        reqString += line + "\n";
                    }

                    byte[] headerBytes;

                    try {
						HttpRequest httpReq = new HttpRequest(reqString.split("\n"));
						byte[] body = router.get(httpReq.headers.get("PATH"), httpReq.headers.get("Accept"));
						String headers;
						// TODO: fix temp error handler for server error
						if (Objects.equals(body, null)) {
							headers = "HTTP/1.1 404 Not Found\r\n" +
									"Date: " + DateHelper.getRfc1123Format() + "\r\n" +
									"Connection: close\r\n" +
									"Server: MyServer v1.0\r\n" +
									"Content-Length: " + body.length + "\r\n" +
									String.format("Content-Type: %s\r\n", httpReq.headers.get("Content-Type")) +
									"\r\n";
						} else {
							headers = "HTTP/1.1 200 OK\r\n" +
									"Date: " + DateHelper.getRfc1123Format() + "\r\n" +
									"Connection: keep-alive\r\n" +
									"Server: MyServer v1.0\r\n" +
									"Content-Length: " + body.length + "\r\n" +
									String.format("Content-Type: %s\r\n", httpReq.headers.get("Accept")) +
									"\r\n";
						}

						headerBytes = headers.getBytes("UTF-8");

						System.out.println("Attempt to write response.");
						try {
							out.write(headerBytes);
							out.write(body);
							out.flush();
							reqString = null;
						} catch (IOException e) {
							ErrorPrinter.logError(e);
						}
					} catch (UnsupportedEncodingException e) {
						ErrorPrinter.logError(e);
					}
					System.out.println("Response written successfully.");

                    this.in.close();
			        this.out.close();
			        this.soc.close();
                    return;
            }
            catch(IOException e){
                ErrorPrinter.logError(e);
            }
        }
    }
}
