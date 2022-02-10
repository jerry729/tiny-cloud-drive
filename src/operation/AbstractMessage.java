package operation;


import java.io.Serializable;

public abstract class AbstractMessage implements Serializable {
    private boolean isRequestSuccessful;

    public AbstractMessage(boolean isRequestSuccessful){
        this.isRequestSuccessful = isRequestSuccessful;
    }

    public boolean isRequestSuccessful() {
        return isRequestSuccessful;
    }

    public abstract RequestOperation getType();
}
