package operation;

public class UploadMessage extends AbstractMessage{
    public UploadMessage(boolean isRequestSuccessful) {
        super(isRequestSuccessful);
    }

    @Override
    public RequestOperation getType() {
        return RequestOperation.UPLOAD_OPERATION;
    }
}
