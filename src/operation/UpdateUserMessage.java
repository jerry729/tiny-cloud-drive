package operation;

public class UpdateUserMessage extends AbstractMessage{
    public UpdateUserMessage(boolean isRequestSuccessful) {
        super(isRequestSuccessful);
    }

    @Override
    public RequestOperation getType() {
        return null;
    }
}
