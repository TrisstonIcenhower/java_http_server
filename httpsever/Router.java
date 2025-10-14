package httpsever;

import java.util.HashMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import helper.ErrorPrinter;

public class Router {
    private HashMap<String, String> routeHashMap = new HashMap<>();
    private String staticFileDirectory;

    public Router(){
        FileServer.configurePathSeperator();
    }

    public void routePath(String urlPath, String filePath) {
        routeHashMap.putIfAbsent(urlPath, filePath);
    }

    public void routeStaticFiles(String directoryPath){
        this.staticFileDirectory = directoryPath;
    }

    public byte[] get(String urlPath, String mimeType){
        if(mimeType.contains("html")){
            return FileServer.getFile(routeHashMap.get(urlPath));
        }
        else{
            return FileServer.getFile(staticFileDirectory + urlPath);
        }
    }

    private static class FileServer {
        private static String os = System.getProperty("os.name");
        private static char pathSeperator;

        private static void configurePathSeperator() {
            if (os.contains(("Win"))) {
                pathSeperator = '\\';
            } else {
                pathSeperator = '/';
            }
        }

        private static byte[] getFile(String filePath) {
            if(filePath == null){
                return null;
            }

            if(pathSeperator == '\\'){
                filePath.replace('/', '\\');
            }
            else{
                filePath.replace('\\', '/');
            }

            return getFileBytes(filePath);
        }

        static public byte[] getFileBytes(String filePathString) {
            InputStream file;
            byte[] fileBytes = null;
            try {
                file = new FileInputStream(new File(filePathString));

                try {
                    fileBytes = file.readAllBytes();
                    file.close();
                } catch (IOException e) {
                    ErrorPrinter.logError(e);
                }

                return fileBytes;
            } catch (FileNotFoundException e) {
                ErrorPrinter.logError(e);
                return null;
            }
        }
    }
}
