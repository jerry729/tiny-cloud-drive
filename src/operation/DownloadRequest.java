package operation;

public class DownloadRequest extends AbstractRequest{
    private int ID;

    public DownloadRequest(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    @Override
    public RequestOperation getType() {
        return RequestOperation.DOWNLOAD_OPERATION;
    }
}
