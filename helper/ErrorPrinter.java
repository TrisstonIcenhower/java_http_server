package helper;

public class ErrorPrinter {
    public static void logError(Error e){
        System.out.println(e.getMessage());
        System.out.println(e.getStackTrace());
    }

    public static void logError(Exception e){
        System.out.println(e.getMessage());
        System.out.println(e.getStackTrace());
    }

    public static void logError(Throwable e){
        System.out.println(e.getMessage());
        System.out.println(e.getStackTrace());
    }
}
