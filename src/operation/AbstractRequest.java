package operation;

import filesystem.user.AbstractUser;

import java.io.Serializable;

public abstract class AbstractRequest implements Serializable {
    private AbstractUser user;

    public AbstractRequest(AbstractUser user) {
        this.user = user;
    }

    public AbstractRequest() {
    }

    public AbstractUser getUser(){
        return user;
    }

    public abstract RequestOperation getType();

    @Override
    public String toString(){
        return "AbstractRequest{" + "user=" + user + "}";
    }
}
