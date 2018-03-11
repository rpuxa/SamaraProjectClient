package ru.samara.mapapp.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import ru.samara.mapapp.ActivityUtils;
import ru.samara.mapapp.R;
import ru.samara.mapapp.data.MyProfile;
import ru.samara.mapapp.events.EventSearchFilter;
import ru.samara.mapapp.events.EventType;
import ru.samara.mapapp.events.EventsList;

public class MainActivity extends AppCompatActivity {

    public static final String ACTION = "action";
    public static final String LOG_IN = "login";

    public static final String NAME_LAST_NAME = "namelastname";
    public static final String AVATAR = "avtr";

    EventsList list = new EventsList(this);
    MyProfile myProfile = new MyProfile();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        handleBundle(getIntent().getExtras());
        setEventTypeSpinner();
        setListeners();
        list.setMainEventList((ListView) findViewById(R.id.mainEventList));
        for (int i = 0; i < 10; i++)
            list.addEvent(123,
                    0,
                    new LatLng(53, 54),
                    "Name", "description",
                    "",
                    new GregorianCalendar(2018, 3, 18, 9, 0),
                    0
            );
    }

    private void handleBundle(Bundle bundle) {
        String action = (String) bundle.get(ACTION);
        assert action != null;
        switch (action) {
            case LOG_IN:
                logIn(bundle);
                break;
        }
    }

    private void logIn(Bundle bundle) {
        String[] nameAndLastName = (String[]) bundle.get(NAME_LAST_NAME);
        assert  nameAndLastName != null;
        myProfile = new MyProfile(
                nameAndLastName[0],
                nameAndLastName[1],
                (Bitmap) bundle.get(AVATAR),
                ""
        );
        View header = ((NavigationView) findViewById(R.id.navigation)).getHeaderView(0);

        ((ImageView) header.findViewById(R.id.headerIcon)).setImageBitmap(myProfile.getAvatar());
        ((TextView) header.findViewById(R.id.headerName)).setText(myProfile.getFullName());
    }

    private void setEventTypeSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.eventTypeSpinner);
        ArrayList<String> typesNames = new ArrayList<>(EventType.types.size());
        typesNames.add("Любой");
        for (EventType type : EventType.types.values())
            typesNames.add(type.getName());
        spinner.setAdapter(new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, typesNames));
        final int[] type = {-1};
        EventSearchFilter filter = event -> type[0] == -1 || type[0] == event.getTypeId();
        list.addFilter(filter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    type[0] = -1;
                    return;
                }
                type[0] = EventType.getByName(typesNames.get(i)).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void setListeners() {
        findViewById(R.id.settings).setOnClickListener(v -> ActivityUtils.changeVisible(findViewById(R.id.settingsLayout)));
        findViewById(R.id.search).setOnClickListener(v -> list.notifyDataSetChanged());
        final CheckBox checkBox = (CheckBox) findViewById(R.id.isPaid);
        list.addFilter(event -> checkBox.isChecked() == event.isPaid());
        //findViewById(R.id.headerLayout).setOnClickListener();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuCreateNewEvent:
                ActivityUtils.changeActivity(this, CreateEventActivity.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

