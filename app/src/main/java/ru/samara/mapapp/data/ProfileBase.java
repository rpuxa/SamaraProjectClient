package ru.samara.mapapp.data;

import android.content.Context;

import java.util.HashMap;

import ru.samara.mapapp.cache.Conservation;
import ru.samara.mapapp.cache.Conserved;
import ru.samara.mapapp.cache.Singleton;
import ru.samara.mapapp.utils.DownloadImageTask;

public class ProfileBase extends HashMap<Integer, Profile> implements Conserved {

    @Singleton
    public static ProfileBase base = new ProfileBase();

    static {
        Conservation.addConservedClass(ProfileBase.class);
    }

    public Profile get(Integer id, Context context, DownloadImageTask.DownloadProgressListener listener) {
        Profile profile = super.get(id);
        if (profile != null)
            return profile;
        profile = Profile.getProfileById(context, id, listener);
        put(id, profile);
        return profile;
    }
}
