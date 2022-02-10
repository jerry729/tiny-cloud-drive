package operation;

import filesystem.document.Doc;

public class UploadRequest extends AbstractRequest{
    private Doc docToUpload;
    private long docSize;

    public UploadRequest(Doc docToUpload, long docSize) {
        this.docToUpload = docToUpload;
        this.docSize = docSize;
    }

    public long getDocSize() {
        return docSize;
    }

    public Doc getDocToUpload() {
        return docToUpload;
    }

    @Override
    public RequestOperation getType() {
        return RequestOperation.UPLOAD_OPERATION;
    }
}
