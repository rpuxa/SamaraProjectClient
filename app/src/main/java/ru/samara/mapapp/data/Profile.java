package ru.samara.mapapp.data;

import android.content.Context;
import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

import ru.samara.mapapp.server.Connect;
import ru.samara.mapapp.utils.DownloadImageTask;

public class Profile {
    int id;
    String name, lastName;
    Bitmap avatar;
    int reputation;
    int vkId;

    public Profile(int id, String name, String lastName, Bitmap avatar, int reputation) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.avatar = avatar;
        this.reputation = reputation;
    }

    public Profile() {
    }

    public static Profile getProfileById(Context context, int id, DownloadImageTask.DownloadProgressListener listener) {
        try {
            JSONObject obj = Connect.sendToJSONObject("get_user",
                    "id", String.valueOf(id)
            );
            Bitmap avatar = DownloadImageTask.downloadInBackground(context, obj.getString("photo"), listener);
            return new Profile(
                    id,
                    obj.getString("first_name"),
                    obj.getString("last_name"),
                    avatar,
                    obj.getInt("reputation")
            );
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public String getFullName() {
        return name + " " + lastName;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
