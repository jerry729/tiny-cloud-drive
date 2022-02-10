package operation;

import filesystem.user.AbstractUser;

import java.util.List;

public class ListUserMessage extends AbstractMessage{
    private List<AbstractUser> userList;

    public ListUserMessage(List<AbstractUser> list) {
        super(true);
        userList = list;
    }

    public List<AbstractUser> getUserList() {
        return userList;
    }

    @Override
    public RequestOperation getType() {
        return RequestOperation.LIST_ALL_USER_OPERATION;
    }
}
