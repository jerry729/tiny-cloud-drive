package operation;

import filesystem.user.AbstractUser;

public class DeleteUserRequest extends AbstractRequest{
    private AbstractUser userToDelete;

    public DeleteUserRequest(AbstractUser userToDelete) {
        this.userToDelete = userToDelete;
    }


    public AbstractUser  getUserToDelete(){
        return userToDelete;
    }
    @Override
    public RequestOperation getType() {
        return RequestOperation.USER_DELETE_OPERATION;
    }
}
