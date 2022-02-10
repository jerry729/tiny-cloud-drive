package operation;

public class ListDocRequest extends AbstractRequest {

    @Override
    public RequestOperation getType() {
        return RequestOperation.LIST_ALL_DOC_OPERATION;
    }
}
