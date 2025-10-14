import httpsever.Router;
import httpsever.Server;
import httpsever.concurrency.ThreadPool;

public class Main {
    public static void main(String args[]){
        Server server = new Server(8080);
        Router router = new Router();
        ThreadPool threadPool = new ThreadPool(10, null, router);
        threadPool.initialize();
        router.routePath("/", "pages/index.html");
        router.routePath("/about", "pages/about.html");
        router.routeStaticFiles("public");
        server.setRouter(router);
        server.run();
    }
}
