package httpsever.concurrency;

import java.util.Queue;

import httpsever.Router;

public class ThreadPool {
    private int numThreads = 10;
    private int activeThreads = 0;
    private Queue requestQueue;
    private Semaphore sem;
    private Router router;

    public ThreadPool(int threadCount, Queue queue, Router router){
        this.requestQueue = queue;
        this.numThreads = threadCount;
        this.router = router;
        this.sem = new Semaphore();
    }

    public void initialize(){
        for(int i = 0; i < numThreads; i++){
            PooledClientThread pooledClientThread = new PooledClientThread(router, requestQueue, sem);
            pooledClientThread.run();
        }
    }
}
