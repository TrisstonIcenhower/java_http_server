import httpsever.Router;
import httpsever.concurrency.ThreadedServer;

public class Main {
    public static void main(String args[]){
        Router router = new Router();
        router.routePath("/", "pages/index.html");
        router.routePath("/about", "pages/about.html");
        router.routeStaticFiles("public");
        router.routeUndefinedPath("pages/404.html");
        ThreadedServer threadedServer = new ThreadedServer(8080, 25, 15, router);
        threadedServer.runServer();
    }
}
