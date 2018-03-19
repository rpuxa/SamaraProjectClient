package ru.samara.mapapp.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import org.json.JSONException;
import org.json.JSONObject;

import ru.samara.mapapp.R;
import ru.samara.mapapp.cache.Conservation;
import ru.samara.mapapp.cache.Conserved;
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
        setContentView(R.layout.login_activity);
        if (ProfileToken.instance.token != null)
            logIn(ProfileToken.instance.token);
        setDesign();
    }

    AlertDialog loading;

    public void showLoadingBar() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.loading_bar, null);
        dialog.setTitle("").setCancelable(false).setView(view);
        loading = dialog.create();
        loading.show();
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
        showLoadingBar();
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
            ProfileToken.instance.setToken(token);
            startActivity(intent);
            loading.dismiss();
        } catch (JSONException e) {
            ProfileToken.instance.token = null;
            Toast.makeText(this, "Не удалось войти, токен устарел, попробуйте еще раз.", Toast.LENGTH_LONG).show();
        }
    }

    ConstraintLayout authForm, layoutForm;
    Button buttonLogo;
    TextView textView;
    Space spaceCenter, spaceVertical;
    boolean isLogoBtnClick = false;

    private void setDesign() {
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.hide();
        }
        textView = findViewById(R.id.TextEnter);
        spaceVertical = findViewById(R.id.SpaceVertical);
        spaceCenter = findViewById(R.id.space);
        authForm = findViewById(R.id.AuthForm);
        layoutForm = findViewById(R.id.LinearForm);

        buttonLogo = findViewById(R.id.ButtonLogo);
        buttonLogo.post(() -> {
            buttonLogo.setLayoutParams(new ConstraintLayout.LayoutParams(buttonLogo.getWidth(), buttonLogo.getWidth()));
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(authForm);
            constraintSet.connect(buttonLogo.getId(), ConstraintSet.BOTTOM, authForm.getId(), ConstraintSet.BOTTOM, 0);
            constraintSet.connect(buttonLogo.getId(), ConstraintSet.TOP, authForm.getId(), ConstraintSet.TOP, 0);
            constraintSet.connect(buttonLogo.getId(), ConstraintSet.LEFT, authForm.getId(), ConstraintSet.LEFT, 0);
            constraintSet.connect(buttonLogo.getId(), ConstraintSet.END, authForm.getId(), ConstraintSet.END, 0);
            constraintSet.applyTo(authForm);
            buttonLogo.requestLayout();
        });
        buttonLogo.setOnClickListener(v -> {
            if (isLogoBtnClick)
                return;
            isLogoBtnClick = true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                TransitionManager.beginDelayedTransition(authForm);
            }
            buttonLogo.setLayoutParams(new ConstraintLayout.LayoutParams(buttonLogo.getWidth() / 3, buttonLogo.getWidth() / 3));


            layoutForm.setLayoutParams(new ConstraintLayout.LayoutParams(authForm.getWidth() * 4 / 6, authForm.getHeight() * 3 / 8));
            layoutForm.setBackgroundColor(Color.WHITE);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(authForm);
            constraintSet.connect(layoutForm.getId(), ConstraintSet.RIGHT, authForm.getId(), ConstraintSet.RIGHT, 0);
            constraintSet.connect(layoutForm.getId(), ConstraintSet.TOP, authForm.getId(), ConstraintSet.TOP, authForm.getHeight() / 4);
            constraintSet.connect(layoutForm.getId(), ConstraintSet.LEFT, authForm.getId(), ConstraintSet.LEFT, 0);

            constraintSet.connect(spaceVertical.getId(), ConstraintSet.TOP, layoutForm.getId(), ConstraintSet.TOP, buttonLogo.getWidth() / 6);

            constraintSet.connect(buttonLogo.getId(), ConstraintSet.RIGHT, authForm.getId(), ConstraintSet.RIGHT, 0);
            constraintSet.connect(buttonLogo.getId(), ConstraintSet.BOTTOM, spaceVertical.getId(), ConstraintSet.TOP, 0);
            constraintSet.connect(buttonLogo.getId(), ConstraintSet.LEFT, authForm.getId(), ConstraintSet.LEFT, 0);
            constraintSet.applyTo(authForm);

            constraintSet = new ConstraintSet();
            constraintSet.clone(layoutForm);
            constraintSet.connect(textView.getId(), ConstraintSet.TOP, layoutForm.getId(), ConstraintSet.TOP, buttonLogo.getWidth() / 6 + 16);
            constraintSet.connect(textView.getId(), ConstraintSet.LEFT, layoutForm.getId(), ConstraintSet.LEFT, 0);
            constraintSet.connect(textView.getId(), ConstraintSet.END, layoutForm.getId(), ConstraintSet.END, 0);
            constraintSet.applyTo(layoutForm);

            buttonLogo.requestLayout();

            buttonLogo.requestLayout();
        });
        findViewById(R.id.vk_login).setOnClickListener(view -> VKSdk.login(this, VKScope.OFFLINE));
    }
}

class ProfileToken implements Conserved {

    public static ProfileToken instance = new ProfileToken();

    static {
        Conservation.addConservedClass(ProfileToken.class);
    }

    String token;

    public void setToken(String token) {
        this.token = token;
    }
}



