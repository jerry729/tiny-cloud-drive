package operation;

public class UnknownRequestMessage extends AbstractMessage{

    public UnknownRequestMessage() {
        super(false);
    }

    @Override
    public RequestOperation getType() {
        return RequestOperation.UNKNOWN_MESSAGE_OPERATION;
    }
}
