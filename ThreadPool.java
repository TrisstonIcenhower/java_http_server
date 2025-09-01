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
    }

    public int getActiveThreadCount(){
        return this.numActiveThreads;
    }

    public ClientHandler useThread(){
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
            return true;
        }
        else{
            this.poolOverLoad = false;
            return false;
        }
    }

    public boolean isUnderThreadLoad(){
        if(this.numActiveThreads.floatValue() / this.threadCount.floatValue() < this.lowerThreadLoad && this.threadCount > this.minThreadCount){
            this.poolUnderLoad = true;
            return true;
        }
        else{
            this.poolUnderLoad = false;
            return false;
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
            
            this.threadCount -= (this.threadCount) / 2;
            if(this.threadCount < this.minThreadCount){
                this.threadCount = this.minThreadCount;
            }
        }
    }

    public void initializePool(){
        for(int i = 0; i < this.threadCount; i++){
            createThread();
        }
    }
}
