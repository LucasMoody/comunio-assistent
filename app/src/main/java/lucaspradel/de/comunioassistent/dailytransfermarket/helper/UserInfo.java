package lucaspradel.de.comunioassistent.dailytransfermarket.helper;

import java.io.Serializable;

/**
 * Created by lucas on 17.04.15.
 */
public class UserInfo implements Serializable {

    private final String userName;
    private final String comunioName;
    private final int id;

    public UserInfo(String userName, String comunioName, int id) {
        this.userName = userName;
        this.comunioName = comunioName;
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public String getComunioName() {
        return comunioName;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return comunioName;
    }
}
