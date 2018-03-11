package ru.samara.mapapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import ru.samara.mapapp.R;
import ru.samara.mapapp.events.EventType;


public class CreateEventActivity extends AppCompatActivity {

    Integer typeSelected = null;
    LatLng locationSelected = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);
        setTypeSpinner();
        setListeners();
    }

    private void setTypeSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.newEventType);
        ArrayList<String> typesNames = new ArrayList<>(EventType.types.size());
        for (EventType type : EventType.types.values())
            typesNames.add(type.getName());
        spinner.setAdapter(new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, typesNames));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MapActivity.REQUEST_CODE_MAP_CHOSE_LOCATION && resultCode == RESULT_OK) {
            findViewById(R.id.newEventShowLocation).setEnabled(true);
            locationSelected = (LatLng) data.getExtras().get(MapActivity.MAP_CHOSE_LOCATION);
        }
    }

    private void setListeners() {
        findViewById(R.id.createButton).setOnClickListener(v -> createEvent());
        findViewById(R.id.newEventShowLocation).setOnClickListener(v -> MapActivity.gotoLocation(this, locationSelected));
        findViewById(R.id.newEventChoseLocation).setOnClickListener(v -> MapActivity.getLocation(this));
    }

    private void createEvent() {
        String name = ((EditText) findViewById(R.id.newEventName)).getText().toString();
        String shortDescription = ((EditText) findViewById(R.id.newEventShortDescription)).getText().toString();
        String longDescription = ((EditText) findViewById(R.id.newEventLongDescription)).getText().toString();
        Integer type = typeSelected;
        int cost = Integer.parseInt(((EditText) findViewById(R.id.newEventLongDescription)).getText().toString());
      //  Event event = new Event(1, type, null, name, shortDescription, longDescription, null, cost);
    }
}
