package operation;

public class ListUserRequest extends AbstractRequest{

    @Override
    public RequestOperation getType() {
        return  RequestOperation.LIST_ALL_USER_OPERATION;
    }
}
