package httpsever.concurrency;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import httpsever.*;

public class ThreadedServer {
    private int PORT = 8080;
    private int requestQueueMax = 10;
    private int numPoolThreads = 10;
    private Router router;
    private Thread requestListener;
    private ConsumerThreadPool cThreadPool;
    private BlockingQueue<Request> requestQueue = new ArrayBlockingQueue<Request>(requestQueueMax);
    

    public ThreadedServer (int portNum, int requestQueueMaxVal, int poolThreadCount, Router pathRouter){
        PORT = portNum;
        requestQueueMax = requestQueueMaxVal;
        numPoolThreads = poolThreadCount;
        router = pathRouter;
        requestListener = new Thread(new ReceiverThread(PORT, requestQueue));
        cThreadPool = new ConsumerThreadPool(numPoolThreads, router, requestQueue);
    }

    public void runServer(){
        startConsumerPool();
        startListener();
    }

    private void startListener(){
        requestListener.start();
    }

    private void startConsumerPool(){
        cThreadPool.initializePool();
    }

    public void stopServer(){
        cThreadPool.shutdownPool();
        requestListener.interrupt();
    }
} 
