package ru.samara.mapapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import ru.samara.mapapp.R;
import ru.samara.mapapp.events.Event;
import ru.samara.mapapp.events.EventType;


public class CreateEventActivity extends AppCompatActivity {

    Integer typeSelected = null;

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

    private void setListeners() {
        findViewById(R.id.createButton).setOnClickListener(v -> createEvent());
    }

    private void createEvent() {
        String name = ((EditText) findViewById(R.id.newEventName)).getText().toString();
        String shortDescription = ((EditText) findViewById(R.id.newEventShortDescription)).getText().toString();
        String longDescription = ((EditText) findViewById(R.id.newEventLongDescription)).getText().toString();
        Integer type = typeSelected;
        int cost = Integer.parseInt(((EditText) findViewById(R.id.newEventLongDescription)).getText().toString());
        Event event = new Event(1, type, null, name, shortDescription, longDescription, null, cost);
    }
}
