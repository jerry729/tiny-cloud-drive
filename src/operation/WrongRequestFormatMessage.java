package operation;

public class WrongRequestFormatMessage extends AbstractMessage{


    public WrongRequestFormatMessage(boolean isRequestSuccessful) {
        super(isRequestSuccessful);
    }

    @Override
    public RequestOperation getType() {
        return RequestOperation.WRONG_REQUEST_OPERATION;
    }
}
