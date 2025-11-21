package httpsever.concurrency;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.BlockingQueue;

import httpsever.*;
import helper.DateHelper;
import helper.ErrorPrinter;

public class ConsumerThreadPool {
    private int numThreads = 10;
    private Router router;
    private BlockingQueue<Request> requestQueue;
    private volatile boolean shutdown = false;
    private Thread pool[];
    private final int SOCKET_TIMEOUT = 30000;

    public ConsumerThreadPool(int threadCount, Router pathRouter, BlockingQueue<Request> bQueue) {
        numThreads = threadCount;
        router = pathRouter;
        requestQueue = bQueue;
        pool = new Thread[numThreads];
    }

    public void initializePool() {
        for (int i = 0; i < numThreads - 1; i++) {
            Thread consumer = new Thread(new ConsumerThread());
            pool[i] = consumer;
            consumer.start();
        }
    }

    public void shutdownPool() {
        shutdown = true;
        for (int i = 0; i < pool.length; i++) {
            pool[i].interrupt();
        }
    }

    class ConsumerThread implements Runnable {
        @Override
        public void run() {
            while (!shutdown) {
                try {
                    consumeRequest(requestQueue.take());
                    System.out.println("Request served");
                } catch (InterruptedException e) {
                    if (shutdown) {
                        return;
                    }
                    ErrorPrinter.logError(e);
                } catch (Throwable t) {
                    ErrorPrinter.logError(t);
                }
            }
        }

        private void consumeRequest(Request request) {
            Request clientRequest = request;
            if (!shutdown) {
                String reqString = "";
                String line;
                try {
                    clientRequest.clientSocket.setSoTimeout(SOCKET_TIMEOUT);
                    while ((line = clientRequest.in.readLine()) != null && !line.isEmpty() && !shutdown) {
                        reqString += line + "\n";
                    }

                    byte[] headerBytes;
                    try {
                        HttpRequest httpReq = new HttpRequest(reqString.split("\n"));
                        String acceptType = httpReq.headers.getOrDefault("Accept", "text/plain");
                        String contentType = httpReq.headers.getOrDefault("Content-Type", "text/plain");
                        HttpResponse res = router.get(httpReq.headers.get("PATH"), acceptType);
                        System.out.println(reqString);
                        System.out.println(httpReq.parameters.get("test"));
                        String headers;
                        if (res.getStatus().equals("404")) {
                            headers = "HTTP/1.1 " + res.getMessage() + "\r\n" +
                                    "Date: " + DateHelper.getRfc1123Format() + "\r\n" +
                                    "Connection: close\r\n" +
                                    "Server: MyServer v1.0\r\n" +
                                    "Content-Length: " + res.getBody().length + "\r\n" +
                                    String.format("Content-Type: %s\r\n", contentType) +
                                    "\r\n";
                        } else {
                            headers = "HTTP/1.1 " + res.getMessage() + "\r\n" +
                                    "Date: " + DateHelper.getRfc1123Format() + "\r\n" +
                                    "Connection: " + httpReq.headers.get("Connection") + "\r\n" +
                                    "Server: MyServer v1.0\r\n" +
                                    "Content-Length: " + res.getBody().length + "\r\n" +
                                    String.format("Content-Type: %s\r\n", acceptType) +
                                    "\r\n";
                        }

                        headerBytes = headers.getBytes("UTF-8");

                        System.out.println("Attempt to write response.");
                        try {
                            clientRequest.out.write(headerBytes);
                            clientRequest.out.write(res.getBody());
                            clientRequest.out.flush();
                            reqString = null;
                        } catch (IOException e) {
                            ErrorPrinter.logError(e);
                        }
                    } catch (UnsupportedEncodingException e) {
                        ErrorPrinter.logError(e);
                    } finally {
                        System.out.println("Response written successfully.");

                        clientRequest.in.close();
                        clientRequest.out.close();
                        clientRequest.clientSocket.close();
                    }
                } catch (IOException e) {
                    ErrorPrinter.logError(e);
                }
            }
        }
    }
}
