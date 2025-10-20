package httpsever;

import java.io.BufferedReader;
import java.io.OutputStream;
import java.net.Socket;

public class Request {
    public BufferedReader in;
    public OutputStream out;
    public Socket clientSocket;

    public Request(BufferedReader buffIn, OutputStream osOut, Socket cSocket){
        in = buffIn;
        out = osOut;
        clientSocket = cSocket;
    }
}
