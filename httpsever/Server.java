package httpsever;
import java.net.*;
import java.io.*;

public class Server {
    private final int PORT;
    private Router router;

    public Server(int newPort){
        PORT = newPort;
    }

    public void setRouter(Router router){
        this.router = router;
    }

    public void run(){
        if(this.router == null){
            System.out.println("Error: Please configure a router. SEE (httpserver/Router.java)");
            return;
        }

        try(ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);

            while(true){
                Socket clientSocket = null;

                try{
                    clientSocket = serverSocket.accept();
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    OutputStream out = clientSocket.getOutputStream();

                    //TODO: Use thread pool
                    ClientThread cThread = new ClientThread(in, out, clientSocket, router);
                    cThread.run();

                    System.out.println("Client connected");
                }
                catch(Exception e){
                    System.out.println("Client error:\n" + e.getMessage());
                    clientSocket.close();
                }
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }

}
