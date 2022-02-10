package operation;

public class AddUserMessage extends AbstractMessage{

    public AddUserMessage(boolean isRequestSuccessful) {
        super(isRequestSuccessful);
    }

    @Override
    public RequestOperation getType() {
        return null;
    }
}
