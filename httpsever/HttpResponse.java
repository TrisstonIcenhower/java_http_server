package httpsever;

public class HttpResponse {
    private byte[] body;
    private String status;
    private String message;

    public void setBody(byte[] resBody){
        body = resBody;
    }

    public void setCode(String resCode){
        status = resCode;
    }

    public void setMessage(String resMessage){
        message = resMessage;
    }

    public byte[] getBody(){
        return body;
    }

    public String getStatus(){
        return status;
    }

    public String getMessage(){
        return message;
    }
}
