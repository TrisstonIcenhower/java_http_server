import httpsever.Router;
import httpsever.Server;

public class Main {
    public static void main(String args[]){
        Server server = new Server(8080);
        Router router = new Router();
        router.routePath("/", "pages/index.html");
        router.routePath("/about", "pages/about.html");
        router.routeStaticFiles("public");
        server.setRouter(router);
        server.run();
    }
}
