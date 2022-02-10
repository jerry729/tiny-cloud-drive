package operation;

import filesystem.document.Doc;

public class DownloadMessage extends AbstractMessage{
    private Doc doc = null;

    public DownloadMessage(boolean isRequestSuccessful, Doc doc) {
        super(isRequestSuccessful);
        this.doc = doc;
    }

    public Doc getDoc() {
        return doc;
    }

    @Override
    public RequestOperation getType() {
        return RequestOperation.DOWNLOAD_OPERATION;
    }
}
