package ru.samara.mapapp.activities.contents;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import ru.samara.mapapp.utils.ActivityUtils;
import ru.samara.mapapp.R;
import ru.samara.mapapp.activities.Content;
import ru.samara.mapapp.activities.MainActivity;
import ru.samara.mapapp.events.EventSearchFilter;
import ru.samara.mapapp.events.EventType;
import ru.samara.mapapp.events.EventsList;


public class EventSearchContent extends Content {

    private EventsList list;

    @Override
    public void onCreate(MainActivity parent, Intent intent) {
        getParent().getToolbar().setSubtitle("Найти мероприятие");
        list = new EventsList(getParent());
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

    @Override
    public int layout() {
        return R.layout.event_list;
    }

    private void setEventTypeSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.eventTypeSpinner);
        ArrayList<String> typesNames = new ArrayList<>(EventType.types.size());
        typesNames.add("Любой");
        for (EventType type : EventType.types.values())
            typesNames.add(type.getName());
        spinner.setAdapter(new ArrayAdapter<>(getParent(), R.layout.support_simple_spinner_dropdown_item, typesNames));
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

}
