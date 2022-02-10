package operation;

public class DeleteDocMessage extends AbstractMessage{
    public DeleteDocMessage(boolean isRequestSuccessful) {
        super(isRequestSuccessful);
    }

    @Override
    public RequestOperation getType() {
        return RequestOperation.DELETE_DOC_OPERATION;
    }
}
