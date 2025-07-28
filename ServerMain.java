package TCP_Server;

import java.net.*;
import java.io.*;

public class ServerMain {

    public static void main(String[] args) {
        final int PORT = 8080;

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);

            while (true) {
            	Socket clientSocket = null;
                try
                {
                	clientSocket = serverSocket.accept();
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    OutputStream out = clientSocket.getOutputStream();
                    
                    System.out.println("Client connected");
                    
                    Thread clientThread = new ClientHandler(in, out, clientSocket);
                    
                    clientThread.start(); 
                    
                } catch (Exception e) {
                    System.out.println("Client error:\n" + e.getMessage());
                    clientSocket.close();
                }
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }
}
