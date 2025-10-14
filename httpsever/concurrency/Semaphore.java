package httpsever.concurrency;

public class Semaphore {
    private int busy = 0;

    public boolean acquire(){
        if(busy == 0){
            busy++;
            return true;
        }
        else{
            return false;
        }
    }

    public boolean release(){
        if(busy == 1){
            busy--;
            return true;
        }
        else{
            return false;
        }
    }
}
