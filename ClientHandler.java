package TCP_Server;

import java.io.*;
import java.net.*;

public class ClientHandler extends Thread{
	final BufferedReader in;
	final OutputStream out;
	final Socket soc;
	
	ClientHandler(BufferedReader bufIn, OutputStream outStream, Socket s){
		this.in = bufIn;
		this.out = outStream;
		this.soc = s;
	}
	
	@Override 
	public void run() {
		// Read the request headers until an empty line
        String line;
        try {
        	//TODO: Write a serializer for http request from website you found
			while ((line = in.readLine()) != null && !line.isEmpty()) {
			    System.out.println("Received: " + line);
			}
		} catch (IOException e) {
			System.out.println("Failed to read request:\n" + e.getMessage());
			e.printStackTrace();
		}
        
        byte[] headerBytes;
        byte[] bodyBytes;

        
        
		try {
			//TODO: Digest request, customize response based on request.
			String body = FileSender.fileToString("src/TCP_Server/views/index/index.html"); 
			
			bodyBytes = body.getBytes("UTF-8");
			
	        String headers =
	                "HTTP/1.1 200 OK\r\n" +
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
			}
			catch(IOException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		} catch (UnsupportedEncodingException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}      
		System.out.println("Response written successfully.");
		
		try
        {
            this.in.close();
            this.out.close();
            
        }catch(IOException e){
            e.printStackTrace();
        }
	}
}
