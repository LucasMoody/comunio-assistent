package de.lucaspradel.comunioassistent.dailytransfermarket.manager;

import android.content.Context;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import de.lucaspradel.comunioassistent.dailytransfermarket.helper.UserInfo;

/**
 * Created by lucas on 08.07.15.
 */
public class UserInfoManger {

    private static final String COMUNIO_USER_INFOS_FILENAME = "comunioUserInfos";

    private final Context context;

    public UserInfoManger(Context context) {
        this.context = context;
    }

    public List<UserInfo> loadUserInfos() {
        List<UserInfo> userInfos = new ArrayList<>();
        try {
            ObjectInputStream ois = new ObjectInputStream(context.openFileInput(COMUNIO_USER_INFOS_FILENAME));
            userInfos = (List<UserInfo>) ois.readObject();
            ois.close();
        } catch (IOException | ClassNotFoundException e) {

        }
        return userInfos;
    }

    public void saveUserInfos(List<UserInfo> userInfos) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(context.openFileOutput(COMUNIO_USER_INFOS_FILENAME, Context.MODE_PRIVATE));
            oos.writeObject(userInfos);
            oos.close();
        } catch (IOException e) {
            //TODO use common way to solve with exceptions
            Toast.makeText(context, "Saving of comunio user was not succesful. Please contact the vendor.", Toast.LENGTH_LONG).show();
        }
    }


}
