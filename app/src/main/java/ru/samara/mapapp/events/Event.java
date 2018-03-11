package ru.samara.mapapp.events;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;
import java.util.GregorianCalendar;

import ru.samara.mapapp.R;
import ru.samara.mapapp.activities.MapActivity;

public class Event {
    private int id;
    private Integer typeId;
    private EventType type;
    private LatLng location;
    private String name, shortDescription, longDescription;
    private GregorianCalendar date;
    private int cost;

    private View view;

    public Event(int id, Integer typeId, LatLng location, String name, String shortDescription, String longDescription,
                 GregorianCalendar date, int cost, ViewGroup parent, Activity activity) {
        this.id = id;
        this.typeId = typeId;
        this.location = location;
        this.name = name;
        this.shortDescription = shortDescription;
        this.date = date;
        this.cost = cost;
        this.longDescription = longDescription;
        type = EventType.getType(typeId);
        view = makeView(parent, activity);
    }

    private View makeView(ViewGroup parent, Activity activity) {
        View view = activity.getLayoutInflater().inflate(R.layout.event_example, parent, false);
        ((ImageView) view.findViewById(R.id.eventIcon)).setImageResource(type.getIcon());
        ((TextView) view.findViewById(R.id.eventName)).setText(name);
        String dateSt = "Дата проведения: " + this.date.get(Calendar.HOUR) + ":" + ((this.date.get(Calendar.MINUTE) < 10) ? "0" : "")
                + this.date.get(Calendar.MINUTE) + " " + this.date.get(Calendar.DAY_OF_MONTH) + "." + this.date.get(Calendar.MONTH) + "." +
                this.date.get(Calendar.YEAR);
        ((TextView) view.findViewById(R.id.eventDate)).setText(dateSt);
        ((TextView) view.findViewById(R.id.eventShortDescription)).setText(shortDescription);
        view.findViewById(R.id.eventOpenMap).setOnClickListener(v -> MapActivity.gotoLocation(activity, location));

        return view;
    }

    public View getView() {
        return view;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public boolean isPaid() {
        return cost > 0;
    }
}
