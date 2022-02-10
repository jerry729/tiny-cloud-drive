package operation;

public class DeleteUserMessage extends AbstractMessage{
    public DeleteUserMessage(boolean isRequestSuccessful) {
        super(isRequestSuccessful);
    }

    @Override
    public RequestOperation getType() {
        return RequestOperation.DELETE_DOC_OPERATION;
    }
}
