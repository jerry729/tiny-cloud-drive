package operation;

import filesystem.document.Doc;

import java.util.List;

public class ListDocMessage extends AbstractMessage{
    private List<Doc> docList;

    public ListDocMessage(List<Doc> docList){
        super(true);
        this.docList = docList;
    }

    public List<Doc> getDocList() {
        return docList;
    }

    @Override
    public RequestOperation getType() {
        return RequestOperation.LIST_ALL_DOC_OPERATION;
    }
}
