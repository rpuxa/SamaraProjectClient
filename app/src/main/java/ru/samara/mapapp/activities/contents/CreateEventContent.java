package ru.samara.mapapp.activities.contents;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.util.ArrayList;

import ru.samara.mapapp.R;
import ru.samara.mapapp.activities.Content;
import ru.samara.mapapp.activities.MainActivity;
import ru.samara.mapapp.activities.MapActivity;
import ru.samara.mapapp.events.EventType;
import ru.samara.mapapp.server.Connect;

import static android.app.Activity.RESULT_OK;

public class CreateEventContent extends Content {

    private Integer typeSelected = null;
    private LatLng locationSelected = null;

    @Override
    public void onCreate(MainActivity parent, Intent intent) {
        getParent().getToolbar().setSubtitle("Создать мероприятие");
        setTypeSpinner();
        setListeners();
    }

    @Override
    public int layout() {
        return R.layout.create_event;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MapActivity.REQUEST_CODE_MAP_CHOSE_LOCATION && resultCode == RESULT_OK) {
            findViewById(R.id.newEventShowLocation).setEnabled(true);
            locationSelected = (LatLng) data.getExtras().get(MapActivity.MAP_CHOSE_LOCATION);
        }
    }

    private void setTypeSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.newEventType);
        ArrayList<String> typesNames = new ArrayList<>(EventType.types.size());
        for (EventType type : EventType.types.values())
            typesNames.add(type.getName());
        spinner.setAdapter(new ArrayAdapter<>(getParent(), R.layout.support_simple_spinner_dropdown_item, typesNames));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                typeSelected = EventType.getByName(typesNames.get(i)).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setListeners() {
        findViewById(R.id.createButton).setOnClickListener(v -> createEvent());
        findViewById(R.id.newEventShowLocation).setOnClickListener(v -> MapActivity.gotoLocation(getParent(), locationSelected));
        findViewById(R.id.newEventChoseLocation).setOnClickListener(v -> MapActivity.getLocation(getParent()));
    }

    private void createEvent() {
        String name = ((EditText) findViewById(R.id.newEventName)).getText().toString();
        String shortDescription = ((EditText) findViewById(R.id.newEventShortDescription)).getText().toString();
        String longDescription = ((EditText) findViewById(R.id.newEventLongDescription)).getText().toString();
        Integer type = typeSelected;
        int cost = Integer.parseInt(((EditText) findViewById(R.id.newEventCost)).getText().toString());
        JSONObject answer = Connect.send("addevent",
                "main_id", String.valueOf(getParent().myProfile.getId()),
                "token", getParent().myProfile.getToken(),
                "request", "{" +
                        "\"name\":\"" + name + "\"," +
                        "\"time\":" + 1520899999 + "," +
                        "\"longitude\":\"" + ((float) locationSelected.longitude) + "\"," +
                        "\"latitude\":\"" + ((float) locationSelected.latitude) + "\"," +
                        "\"ShortDescription\":\"" + shortDescription + "\"," +
                        "\"l_description\":\"" + longDescription + "\"," +
                        "\"cost\":" + cost + "}"
        );
        System.out.println();
    }


}