package operation;

import filesystem.document.Doc;

public class DeleteDocRequest extends AbstractRequest{
    private Doc docToDelete;
    public DeleteDocRequest(Doc docToDelete) {
        this.docToDelete = docToDelete;
    }

    public Doc getDocToDelete() {
        return docToDelete;
    }

    @Override
    public RequestOperation getType() {
        return RequestOperation.DELETE_DOC_OPERATION;
    }
}
