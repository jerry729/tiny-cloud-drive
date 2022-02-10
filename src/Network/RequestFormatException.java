package Network;

public class RequestFormatException extends Exception{

    public RequestFormatException() {
    }
    public RequestFormatException(String message){
        super(message);
    }
    public RequestFormatException(String message, Throwable throwable){
        super(message,throwable);
    }
    public RequestFormatException(Throwable throwable){
        super(throwable);
    }
}
