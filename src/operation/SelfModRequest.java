package operation;

import filesystem.user.AbstractUser;

public class SelfModRequest extends AbstractRequest{
    public String nPassword;
    public SelfModRequest(AbstractUser user, String nPassword) {
        super(user);
        this.nPassword = nPassword;
    }

    public String getnPassword(){
        return nPassword;
    }

    @Override
    public RequestOperation getType() {
        return RequestOperation.SELF_MOD_OPERATION;
    }
}
