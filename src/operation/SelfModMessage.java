package operation;

public class SelfModMessage extends AbstractMessage{
    public SelfModMessage(boolean isRequestSuccessful) {
        super(isRequestSuccessful);
    }

    @Override
    public RequestOperation getType() {
        return RequestOperation.SELF_MOD_OPERATION;
    }
}
