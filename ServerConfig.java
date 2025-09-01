public class ServerConfig {
    private static String os;
    private static char pathSeperator;
    private static Integer threadCount = 10;
    private static Integer maxThreadCount = 30;
    private static Integer minThreadCount = 10;
    private static Float upperThreadLoad = .75f;
    private static Float lowerThreadLoad = .25f;

    public static void configureServerProps(Integer newThreadCount, Integer newMaxThreadCount, Float newUpperThreadLoad, Float newLowerThreadLoad, Integer newMinThreadCount){
        os = System.getProperty("os.name");
        
        if(os.contains(("Win"))){
            pathSeperator = '\\';
        }
        else{
            pathSeperator = '/';
        }

        setThreadCount(newThreadCount);
        setMaxThreadCount(newMaxThreadCount);
        setUpperThreadLoad(newUpperThreadLoad);
        setLowerThreadLoad(newLowerThreadLoad);
        setMinThreadCount(newMinThreadCount);
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

    public static Integer getMinThreadCount(){
        return maxThreadCount;
    }

    public static void setMinThreadCount(Integer newMaxThreadCount){
        maxThreadCount = newMaxThreadCount;
    }

    public static Float getUpperThreadLoad(){
        return upperThreadLoad;
    }

    public static void setUpperThreadLoad(Float newThreadLoad){
        upperThreadLoad = newThreadLoad;
    }

    public static Float getLowerThreadLoad(){
        return lowerThreadLoad;
    }

    public static void setLowerThreadLoad(Float newThreadLoad){
        lowerThreadLoad = newThreadLoad;
    }
}
