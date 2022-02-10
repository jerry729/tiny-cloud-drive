package operation;

import filesystem.user.AbstractUser;

public class AddUserRequest extends AbstractRequest{
    private AbstractUser userToInsert;
    public AddUserRequest(AbstractUser userToInsert) {
        this.userToInsert = userToInsert;
    }

    public AbstractUser getUserToInsert(){
        return userToInsert;
    }

    @Override
    public RequestOperation getType() {
        return RequestOperation.USER_ADD_OPERATION;
    }
}
