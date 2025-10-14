package httpsever.concurrency;

import java.io.BufferedReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Queue;
import httpsever.Router;

public class PooledClientThread {
    private BufferedReader in;
	private OutputStream out;
	private Socket soc;
	private Router router;
    private Semaphore sem;

    /*
     * TODO: Do these when not as sleepy
     * 1. Set up semaphore and pass to thread with aquire and release
     * 2. Set up requestQueue (and request object) - TODO: Create this request object
     * 3. If aquired, then check for queue len > 0, then grab a request and release the semaphore
     * 4. Set up temp thread system
     */
    PooledClientThread(Router router, Queue rQueue, Semaphore sem){
		this.router = router;
        this.sem = sem;
    }

    public void run(){
        System.out.println("Thread Running with: semaphore - "  + sem);
    }
}
