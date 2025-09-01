public class ServerConfig {
    private static String os;
    private static char pathSeperator;
    private static Integer threadCount = 10;
    private static Integer maxThreadCount = 30;
    private static Float threadLoad = .75f;

    public static void configureServerProps(Integer newThreadCount, Integer newMaxThreadCount, Float newThreadLoad){
        os = System.getProperty("os.name");
        
        if(os.contains(("Win"))){
            pathSeperator = '\\';
        }
        else{
            pathSeperator = '/';
        }

        setThreadCount(newThreadCount);
        setMaxThreadCount(newMaxThreadCount);
        setThreadLoad(newThreadLoad);
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

    public static Integer getMaxThreadCount(){
        return maxThreadCount;
    }

    public static void setMaxThreadCount(Integer newMaxThreadCount){
        maxThreadCount = newMaxThreadCount;
    }

    public static Float getThreadLoad(){
        return threadLoad;
    }

    public static void setThreadLoad(Float newThreadLoad){
        threadLoad = newThreadLoad;
    }
}
