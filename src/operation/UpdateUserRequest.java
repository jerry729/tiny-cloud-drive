package operation;

import filesystem.user.AbstractUser;

public class UpdateUserRequest extends AbstractRequest{
    private AbstractUser userToUpdate;
    private String nPassword;
    private String nRole;

    public UpdateUserRequest(AbstractUser userToUpdate,
                             String nPassword, String nRole) {
        this.userToUpdate = userToUpdate;
        this.nPassword = nPassword;
        this.nRole = nRole;
    }

    public AbstractUser getUserToUpdate() {
        return userToUpdate;
    }

    public String getnPassword() {
        return nPassword;
    }

    public String getnRole() {
        return nRole;
    }

    @Override
    public RequestOperation getType() {
        return RequestOperation.USER_UPDATE_OPERATION;
    }
}
