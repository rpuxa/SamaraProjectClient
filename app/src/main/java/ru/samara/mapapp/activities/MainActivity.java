package ru.samara.mapapp.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import ru.samara.mapapp.R;
import ru.samara.mapapp.activities.contents.CreateEventContent;
import ru.samara.mapapp.activities.contents.EventSearchContent;
import ru.samara.mapapp.data.MyProfile;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String ACTION = "action";
    public static final String LOG_IN = "login";

    public static final String NAME_LAST_NAME = "namelastname";
    public static final String AVATAR = "avtr";
    public static final String TOKEN = "token";
    public static final String MAIN_ID = "mainid";

    private Content activeContent;

    Toast toast;

    public MyProfile myProfile = new MyProfile();
    Toolbar toolbar;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        drawer.setDrawerListener(toggle);
        drawer.closeDrawers();
        toggle.syncState();
        handleBundle(getIntent().getExtras());
        ((NavigationView) findViewById(R.id.navigation)).setNavigationItemSelectedListener(this);
        toast = new Toast(this);
    }

    public void startContent(Class<? extends Content> clazz) {
        startContent(new Intent(), clazz);
    }

    public void startContent(Intent intent, Class<? extends Content> clazz) {
        Content content = null;
        try {
            content = clazz.newInstance();
        } catch (InstantiationException ignored) {
        } catch (IllegalAccessException ignored) {
        }
        assert content != null;
        activeContent = content;
        content.setLayout(content.layout());
        content.setParent(this);
        LinearLayout layout = findViewById(R.id.contentView);
        layout.removeAllViews();
        ViewGroup mainGroup = (ViewGroup) getLayoutInflater().inflate(content.getLayout(), layout, false);
        layout.addView(mainGroup);
        content.setMainGroup(mainGroup);
        content.onCreate(this, intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        activeContent.onActivityResult(requestCode, resultCode, data);
    }

    private void handleBundle(Bundle bundle) {
        if (bundle != null) {
            String action = (String) bundle.get(ACTION);
            assert action != null;
            switch (action) {
                case LOG_IN:
                    logIn(bundle);
                    break;
            }
        }
    }

    private void logIn(Bundle bundle) {
        String[] nameAndLastName = (String[]) bundle.get(NAME_LAST_NAME);
        assert nameAndLastName != null;
        myProfile = new MyProfile(
                nameAndLastName[0],
                nameAndLastName[1],
                (Bitmap) bundle.get(AVATAR),
                bundle.getString(TOKEN),
                bundle.getInt(MAIN_ID)
        );
        View header = ((NavigationView) findViewById(R.id.navigation)).getHeaderView(0);
        ((ImageView) header.findViewById(R.id.headerIcon)).setImageBitmap(myProfile.getAvatar());
        ((TextView) header.findViewById(R.id.headerName)).setText(myProfile.getFullName());
        startContent(EventSearchContent.class);
    }

    @Override
    public void onBackPressed() {
        if (activeContent instanceof EventSearchContent)
            return;
        startContent(EventSearchContent.class);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuCreateNewEvent:
                startContent(CreateEventContent.class);
                break;
            case R.id.menuFoundEvents:
                startContent(EventSearchContent.class);
                break;
        }
        drawer.closeDrawers();
        return true;
    }


    public void sendToast(String massage, boolean isShort) {
        toast.cancel();
        toast = Toast.makeText(this, massage, (isShort) ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG);
        toast.show();
    }

    public Toolbar getToolbar() {
        return toolbar;
    }
}

