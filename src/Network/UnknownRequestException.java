package Network;

public class UnknownRequestException extends Exception{

    public UnknownRequestException(String e) {
        super(e);
    }
    public UnknownRequestException(String e, Throwable throwable){
        super(e,throwable);
    }
    public UnknownRequestException(Throwable throwable){
        super(throwable);
    }
}
