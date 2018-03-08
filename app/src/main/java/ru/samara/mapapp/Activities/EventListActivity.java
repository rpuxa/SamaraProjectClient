package ru.samara.mapapp.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import ru.samara.mapapp.ActivityUtils;
import ru.samara.mapapp.Events.Event;
import ru.samara.mapapp.Events.EventSearchFilter;
import ru.samara.mapapp.Events.EventType;
import ru.samara.mapapp.Events.EventsList;
import ru.samara.mapapp.R;

public class EventListActivity extends AppCompatActivity {

    EventsList list = new EventsList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list);
        setEventTypeSpinner();
        setListeners();
        for (int i = 0; i < 100; i++)
            list.addEvent(new Event(123, 0, new LatLng(53, 54), "Name", "description verylongasasasasasasasasasasasdasdasdsaaaaaaaaaaaasdsdsdsdsd", new GregorianCalendar(2018, 3, 18, 9, 0)));

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
        findViewById(R.id.search).setOnClickListener(v -> {
            LinearLayout container = (LinearLayout) findViewById(R.id.mainList);
            container.removeAllViews();
            for (Event event : list.getEvents())
                container.addView(event.getVisual(this));
        });
    }

}

