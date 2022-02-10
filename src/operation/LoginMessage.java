package operation;

public class LoginMessage extends AbstractMessage{

    public LoginMessage(boolean isRequestSuccessful) {
        super(isRequestSuccessful);
    }

    @Override
    public RequestOperation getType() {
        return RequestOperation.LOGIN_OPERATION;
    }
}
