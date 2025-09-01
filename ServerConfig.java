public class ServerConfig {
    private static String os;
    private static char pathSeperator;
    private static Integer threadCount = 10;

    public static void configureServerProps(Integer newThreadCount){
        os = System.getProperty("os.name");
        
        if(os.contains(("Win"))){
            pathSeperator = '\\';
        }
        else{
            pathSeperator = '/';
        }

        setThreadCount(newThreadCount);
    }

    public static String getOS(){
        return os;
    }

    public static void setOS(String newOS){
        os = newOS;    
    }

    public static Integer getThreadCount(){
        return threadCount;
    }

    public static void setThreadCount(Integer newThreadCount){
        threadCount = newThreadCount;
    }

    public static char getPathSeperator(){
        return pathSeperator;
    }

    public static void setPathSeperator(char newPathSeperator){
        pathSeperator = newPathSeperator;
    }
}
