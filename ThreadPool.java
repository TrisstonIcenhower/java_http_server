import java.io.BufferedReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

public class ThreadPool {
    private Integer threadCount;
    private Integer maxThreadCount;
    private Integer minThreadCount;
    private float upperThreadLoad;
    private float lowerThreadLoad;
    private Integer numActiveThreads;
    private Queue<ClientHandler> threadQueue;
    private boolean poolUnderLoad;
    private boolean poolOverLoad;

    public ThreadPool(Integer threadCount, Integer maxThreadCount, Integer minThreadCount, float upperThreadLoad, float lowerThreadLoad){
        this.threadCount = threadCount;
        this.maxThreadCount = maxThreadCount;
        this.minThreadCount = minThreadCount;
        this.upperThreadLoad = upperThreadLoad;
        this.numActiveThreads = 0;
        this.lowerThreadLoad = lowerThreadLoad;

        initializePool();
    }

    public int getActiveThreadCount(){
        return this.numActiveThreads;
    }

    public void useThread(BufferedReader in, OutputStream out, Socket soc){
        ClientHandler ch = grabThread();
        ch.assignClient(in, out, soc);
    }

    public ClientHandler grabThread(){
        try{
            this.numActiveThreads++;
            return this.threadQueue.remove();
        }
        catch(NoSuchElementException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            this.numActiveThreads--;
            return null;
        }
    }

    public void createThread(){
        try{
            ClientHandler ch = new ClientHandler();
            this.threadQueue.add(ch);
            System.out.println("Thread Created");
        }
        catch(Error e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void freeThread(ClientHandler ch){
        try{
            ch.clear();
            if(!this.poolOverLoad){
                this.threadQueue.add(ch);
                isOverThreadLoad();
            }

            this.numActiveThreads--;
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean isOverThreadLoad(){
        if(this.numActiveThreads.floatValue() / this.threadCount.floatValue() > this.upperThreadLoad && this.threadCount < this.maxThreadCount){
            this.poolOverLoad = true;
            return this.poolOverLoad;
        }
        else{
            this.poolOverLoad = false;
            return this.poolOverLoad;
        }
    }

    public boolean isUnderThreadLoad(){
        if(this.numActiveThreads.floatValue() / this.threadCount.floatValue() < this.lowerThreadLoad && this.threadCount > this.minThreadCount){
            this.poolUnderLoad = true;
            return this.poolUnderLoad;
        }
        else{
            this.poolUnderLoad = false;
            return this.poolUnderLoad;
        }
    }

    public void adjustLoadBalance(){
        if(isOverThreadLoad()){
            this.threadCount += (this.threadCount) / 2;
            if(this.threadCount > this.maxThreadCount){
                this.threadCount = this.maxThreadCount;
            }
        }
        else if(isUnderThreadLoad()){
            int tempCount = this.threadCount;
            this.threadCount -= (this.threadCount) / 2;
            if(this.threadCount < this.minThreadCount){
                this.threadCount = this.minThreadCount;
            }

            for(int i = 0; i < this.threadCount - tempCount; i++){
                createThread();
            }
        }
    }

    public void initializePool(){
        this.threadQueue = new LinkedList<>();

        for(int i = 0; i < this.threadCount; i++){
            createThread();
        }

        System.out.println("Thread Pool Initialized");
    }
}
