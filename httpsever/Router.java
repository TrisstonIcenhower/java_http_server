package httpsever;

import java.util.HashMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import helper.ErrorPrinter;

public class Router {
    private static final String defaultErrorString = "<h1>404: File not found.</h1>";
    private static HashMap<String, String> routeHashMap = new HashMap<>();
    private static String staticFileDirectory;
    private static byte[] errorBytes = defaultErrorString.getBytes();

    public Router() {
        FileServer.configurePathSeperator();
    }

    public void routePath(String urlPath, String filePath) {
        routeHashMap.putIfAbsent(urlPath, filePath);
    }

    public void routeUndefinedPath(String filePath) {
        routeHashMap.putIfAbsent("404", filePath);
    }

    public void routeStaticFiles(String directoryPath) {
        staticFileDirectory = directoryPath;
    }

    public HttpResponse get(String urlPath, String mimeType) {
        return generateResponse(urlPath, mimeType);
    }

    private static HttpResponse generateResponse(String urlPath, String mimeType) {
        HttpResponse res = new HttpResponse();
        if (mimeType != null && mimeType.contains("html")) {
            res.setBody(FileServer.getFile(routeHashMap.get(urlPath)));
        } else {
            res.setBody(FileServer.getFile(staticFileDirectory + urlPath));
            ;
        }
        if (res.getBody() == errorBytes) {
            res.setCode("404");
            res.setMessage("Not found");
        }
        else{
            res.setCode("200");
            res.setMessage("OK");
        }
        return res;
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
            if (filePath == null) {
                return get404();
            }

            if (pathSeperator == '\\') {
                filePath.replace('/', '\\');
            } else {
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
                return get404();
            }
        }

        private static byte[] get404() {
            if (routeHashMap.get("404") != null) {
                return getFile(routeHashMap.get("404"));
            } else {
                return errorBytes;
            }
        }
    }
}
