package httpsever.concurrency;

import java.io.IOException;
import java.io.OutputStream;
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
                String reqType;
                String[] bodyLine = new String[0];

                try {
                    clientRequest.clientSocket.setSoTimeout(SOCKET_TIMEOUT);
                    line = clientRequest.in.readLine();
                    reqString += line + "\n";

                    if (line.contains("POST")) {
                        reqType = "POST";
                        Integer contentLength = 0;
                        char[] contentBuffer;

                        while ((line = clientRequest.in.readLine()) != null && !line.isEmpty() && !shutdown) {
                            reqString += line + "\n";
                            if (line.startsWith("Content-Length:")) {
                                String[] parts = line.split(" ");
                                contentLength = Integer.parseInt(parts[1]);
                            }
                        }

                        contentBuffer = new char[contentLength];
                        clientRequest.in.read(contentBuffer, 0, contentLength);
                        String bodyString = new String(contentBuffer);
                        bodyLine = bodyString.split("&");

                    } else {
                        reqType = "GET";
                        while ((line = clientRequest.in.readLine()) != null && !line.isEmpty() && !shutdown) {
                            reqString += line + "\n";
                        }
                    }

                    HttpRequest httpReq = new HttpRequest(reqString.split("\n"), reqType, bodyLine);
                    String acceptType = httpReq.headers.getOrDefault("Accept", "text/plain");
                    String contentType = httpReq.headers.getOrDefault("Content-Type", "text/plain");

                    if (reqType.equals("GET")) {
                        handleGet(httpReq, acceptType, contentType, clientRequest, reqString);
                    } else {
                        handlePost(httpReq, acceptType, contentType, clientRequest, reqString);
                    }
                } catch (IOException e) {
                    ErrorPrinter.logError(e);
                }
            }
        }

        private void writeResponse(OutputStream out, byte[] headerBytes, byte[] bodyBytes) throws IOException {
            try {
                out.write(headerBytes);
                out.write(bodyBytes);
                out.flush();
            } catch (IOException e) {
                ErrorPrinter.logError(e);
            }
        }

        private void closeSocket(Request clientRequest) throws IOException {
            clientRequest.in.close();
            clientRequest.out.close();
            clientRequest.clientSocket.close();
        }

        private void handleGet(HttpRequest req, String acceptType, String contentType,
                Request clientRequest, String reqString) {
            HttpResponse res;
            byte[] headerBytes;
            String headers;
            try {
                System.out.println("Processing GET request.");

                res = router.get(req.headers.get("PATH"), acceptType);
                if (res.getStatus().equals("404")) {
                    headers = "HTTP/1.1 " + res.getMessage() + "\r\n" +
                            "Date: " + DateHelper.getRfc1123Format() + "\r\n" +
                            "Connection: close\r\n" +
                            "Server: MyServer v3.0\r\n" +
                            "Content-Length: " + res.getBody().length + "\r\n" +
                            String.format("Content-Type: %s\r\n", contentType) +
                            "\r\n";
                } else {
                    headers = "HTTP/1.1 " + res.getMessage() + "\r\n" +
                            "Date: " + DateHelper.getRfc1123Format() + "\r\n" +
                            "Connection: " + req.headers.get("Connection") + "\r\n" +
                            "Server: MyServer v3.0\r\n" +
                            "Content-Length: " + res.getBody().length + "\r\n" +
                            String.format("Content-Type: %s\r\n", acceptType) +
                            "\r\n";
                }

                headerBytes = headers.getBytes("UTF-8");

                System.out.println("Attempt to write response.");
                try {
                    writeResponse(clientRequest.out, headerBytes, res.getBody());
                    reqString = null;
                } catch (IOException e) {
                    ErrorPrinter.logError(e);
                }
            } catch (UnsupportedEncodingException e) {
                ErrorPrinter.logError(e);
            } finally {

                try {
                    closeSocket(clientRequest);
                    System.out.println("Response written successfully.");
                } catch (IOException e) {
                    ErrorPrinter.logError(e);
                }
            }
        }

        private void handlePost(HttpRequest req, String acceptType, String contentType,
                Request clientRequest, String reqString) {
            HttpResponse res;
            byte[] headerBytes;
            String headers;
            try {
                System.out.println("Processing POST request.");
                res = router.post(req.body, req.headers.get("PATH"));
                if (res.getStatus().equals("500")) {
                    headers = "HTTP/1.1 " + res.getMessage() + "\r\n" +
                            "Date: " + DateHelper.getRfc1123Format() + "\r\n" +
                            "Connection: close\r\n" +
                            "Server: MyServer v3.0\r\n" +
                            "Content-Length: " + res.getBody().length + "\r\n" +
                            String.format("Content-Type: %s\r\n", contentType) +
                            "\r\n";
                } else {
                    headers = "HTTP/1.1 " + res.getMessage() + "\r\n" +
                            "Date: " + DateHelper.getRfc1123Format() + "\r\n" +
                            "Connection: " + req.headers.get("Connection") + "\r\n" +
                            "Server: MyServer v3.0\r\n" +
                            "Content-Length: " + res.getBody().length + "\r\n" +
                            String.format("Content-Type: %s\r\n", acceptType) +
                            "\r\n";
                }

                headerBytes = headers.getBytes("UTF-8");

                System.out.println("Attempt to write response.");
                try {
                    writeResponse(clientRequest.out, headerBytes, res.getBody());
                    reqString = null;
                } catch (IOException e) {
                    ErrorPrinter.logError(e);
                }
            } catch (UnsupportedEncodingException e) {
                ErrorPrinter.logError(e);
            } finally {

                try {
                    closeSocket(clientRequest);
                    System.out.println("Response written successfully.");
                } catch (IOException e) {
                    ErrorPrinter.logError(e);
                }
            }
        }
    }
}
