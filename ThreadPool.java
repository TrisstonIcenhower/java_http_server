import java.util.Queue;

public class ThreadPool {
    private Integer threadCount;
    private Integer maxThreadCount;
    private Float threadLoad;
    private Integer numFreeThreads;
    private Queue<ClientHandler> ThreadQueue;

    public ThreadPool(Integer threadCount, Integer maxThreadCount, Float threadLoad){
        this.threadCount = threadCount;
        this.maxThreadCount = maxThreadCount;
        this.threadLoad = threadLoad;
        this.numFreeThreads = this.threadCount;
    }

    public Integer pollFreeThreads(){
        return this.numFreeThreads;
    }
}
