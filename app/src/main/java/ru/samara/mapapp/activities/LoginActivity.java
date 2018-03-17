package ru.samara.mapapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import org.json.JSONException;
import org.json.JSONObject;

import ru.samara.mapapp.cache.Conservation;
import ru.samara.mapapp.cache.Conserved;
import ru.samara.mapapp.cache.Singleton;
import ru.samara.mapapp.server.Connect;
import ru.samara.mapapp.utils.DownloadImageTask;

import static ru.samara.mapapp.activities.MainActivity.ACTION;
import static ru.samara.mapapp.activities.MainActivity.AVATAR;
import static ru.samara.mapapp.activities.MainActivity.LOG_IN;
import static ru.samara.mapapp.activities.MainActivity.MAIN_ID;
import static ru.samara.mapapp.activities.MainActivity.NAME_LAST_NAME;
import static ru.samara.mapapp.activities.MainActivity.TOKEN;

public class LoginActivity extends AppCompatActivity {
    Activity instant = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ProfileToken.profileToken.token == null)
            VKSdk.login(this, VKScope.OFFLINE);
        else {
            logIn(ProfileToken.profileToken.token);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                logIn(res.accessToken);
            }

            @Override
            public void onError(VKError error) {
            }
        });
    }

    private void logIn(String token) {
        Intent intent = new Intent(instant, MainActivity.class);
        JSONObject object = Connect.sendToJSONObject("auth_vk",
                "token", token
        );
        try {
            intent.putExtra(ACTION, LOG_IN);
            intent.putExtra(NAME_LAST_NAME, new String[]{
                    object.getString("first_name"),
                    object.getString("last_name"),
            });
            intent.putExtra(AVATAR, DownloadImageTask.getImage(object.getString("photo_200")));
            intent.putExtra(TOKEN, object.getString("token"));
            String id = object.getString("main_id");
            intent.putExtra(MAIN_ID, Integer.parseInt(id));
            ProfileToken.profileToken.setToken(token);
            startActivity(intent);
        } catch (JSONException ignored) {
        }
    }


    static class ProfileToken implements Conserved {
        @Singleton
        public static ProfileToken profileToken = new ProfileToken();

        static {
            Conservation.addConservedClass(ProfileToken.class);
        }

        String token;

        public void setToken(String token) {
            this.token = token;
        }
    }

}


