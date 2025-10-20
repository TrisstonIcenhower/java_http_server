package httpsever.concurrency;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;

import httpsever.*;
import helper.DateHelper;
import helper.ErrorPrinter;

public class ConsumerThreadPool {
    private int numThreads = 10;
    private Router router;
    private BlockingQueue<Request> requestQueue;

    public ConsumerThreadPool(int threadCount, Router pathRouter, BlockingQueue<Request> bQueue) {
        numThreads = threadCount;
        router = pathRouter;
        requestQueue = bQueue;
    }

    public void initializePool() {
        for (int i = 0; i < numThreads; i++) {
            Thread consumer = new Thread(new ConsumerThread());
            consumer.start();
        }
    }

    class ConsumerThread implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    consumeRequest(requestQueue.take());
                }
            } catch (InterruptedException e) {
                ErrorPrinter.logError(e);
            }
        }

        private void consumeRequest(Request request) {
            Request clientRequest = request;
            while (true) {
                String reqString = "";
                String line;

                try {
                    while ((line = clientRequest.in.readLine()) != null && !line.isEmpty()) {
                        reqString += line + "\n";
                        System.out.println(line);
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
                            clientRequest.out.write(headerBytes);
                            clientRequest.out.write(body);
                            clientRequest.out.flush();
                            reqString = null;
                        } catch (IOException e) {
                            ErrorPrinter.logError(e);
                        }
                    } catch (UnsupportedEncodingException e) {
                        ErrorPrinter.logError(e);
                    }
                    System.out.println("Response written successfully.");

                    clientRequest.in.close();
                    clientRequest.out.close();
                    clientRequest.clientSocket.close();
                    return;
                } catch (IOException e) {
                    ErrorPrinter.logError(e);
                }
            }
        }
    }
}
