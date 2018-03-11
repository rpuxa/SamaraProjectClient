package ru.samara.mapapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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

import java.io.InputStream;
import java.util.concurrent.ExecutionException;

import ru.samara.mapapp.server.Connect;

import static ru.samara.mapapp.activities.MainActivity.ACTION;
import static ru.samara.mapapp.activities.MainActivity.AVATAR;
import static ru.samara.mapapp.activities.MainActivity.LOG_IN;
import static ru.samara.mapapp.activities.MainActivity.NAME_LAST_NAME;

public class LoginActivity extends AppCompatActivity {
    Activity instant = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VKSdk.login(this, VKScope.OFFLINE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                Intent intent = new Intent(instant, MainActivity.class);
                AsyncTask<String, Void, JSONObject> task = new Connect().execute("auth",
                        "token", res.accessToken
                );
                JSONObject object = null;
                try {
                    object = task.get();
                } catch (InterruptedException | ExecutionException ignored) {
                }
                assert object != null;
                try {
                    intent.putExtra(ACTION, LOG_IN);
                    intent.putExtra(NAME_LAST_NAME, new String[]{
                            object.getString("first_name"),
                            object.getString("last_name"),
                    });
                    intent.putExtra(AVATAR, toBitmap(object.getString("photo_50")));
                    startActivity(intent);
                } catch (JSONException e) {
                }
            }

            @Override
            public void onError(VKError error) {
            }
        });
    }

    private Bitmap toBitmap(String url) {
        try {
            return new DownloadImageTask().execute(url).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}

class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception ignored) {
        }
        return mIcon11;
    }

}
