package operation;

import filesystem.user.AbstractUser;

public class LoginRequest extends AbstractRequest{

    public LoginRequest(AbstractUser user) {
        super(user);
    }

    @Override
    public RequestOperation getType() {
        return RequestOperation.LOGIN_OPERATION;
    }
}
