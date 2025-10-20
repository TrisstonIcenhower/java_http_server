package httpsever.concurrency;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

import httpsever.Request;
import helper.ErrorPrinter;

public class ReceiverThread implements Runnable{
    private int PORT;
    private BlockingQueue<Request> requestQueue;

    public ReceiverThread(int portNum, BlockingQueue<Request> bQueue){
        PORT = portNum;
        requestQueue = bQueue;
    }

    @Override
    public void run() {
        try(ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);

            while(true){
                Socket clientSocket = null;

                try{
                    clientSocket = serverSocket.accept();
                    requestQueue.put(createRequest(clientSocket));
                    System.out.println("Client connected");
                }
                catch(InterruptedException e){
                    ErrorPrinter.logError(e);
                    clientSocket.close();
                }
            }
        } catch (IOException e) {
            ErrorPrinter.logError(e);
        }
    }

    private Request createRequest(Socket clientSocket){
        Request newRequest = null;
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            OutputStream out = clientSocket.getOutputStream();
            newRequest = new Request(in, out, clientSocket);
        }
        catch(IOException e){
            ErrorPrinter.logError(e);
        }

        return newRequest;
    }
}
