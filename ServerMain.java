import java.net.*;
import java.io.*;

public class ServerMain {

    public static void main(String[] args) {
        Integer currentThreadCount = 10;
        Integer maxThreadCount = 30;
        Integer minThreadCount = 10;
        Float upperThreadLoad = .75f;
        Float lowerThreadLoad = .25f;
        // TODO: Integrate server configuration
        // TODO: Create Thread pool
        ServerConfig.configureServerProps(currentThreadCount, maxThreadCount, upperThreadLoad, lowerThreadLoad, minThreadCount);

        ThreadPool tp = new ThreadPool(ServerConfig.getThreadCount(), ServerConfig.getMaxThreadCount(), ServerConfig.getMinThreadCount(), ServerConfig.getUpperThreadLoad(), ServerConfig.getLowerThreadLoad());

        final int PORT = 8080;

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);

            while (true) {
                Socket clientSocket = null;
                try {
                    clientSocket = serverSocket.accept();
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    OutputStream out = clientSocket.getOutputStream();

                    System.out.println("Client connected");

                    tp.useThread(in, out, clientSocket);

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
