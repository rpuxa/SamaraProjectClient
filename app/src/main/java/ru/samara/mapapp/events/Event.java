package ru.samara.mapapp.events;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.GregorianCalendar;

import ru.samara.mapapp.R;
import ru.samara.mapapp.activities.MainActivity;
import ru.samara.mapapp.activities.MapActivity;
import ru.samara.mapapp.activities.contents.EventLayoutContent;
import ru.samara.mapapp.utils.DateUtils;

public class Event implements Serializable {
    private int id;
    private Integer typeId;
    private EventType type;
    private LatLng location;
    private String name, shortDescription, longDescription;
    private GregorianCalendar date;
    private int cost;

    private View view;
    private int ownerId;

    public Event(int id, Integer typeId, LatLng location, String name, String shortDescription, String longDescription,
                 GregorianCalendar date, int cost, int ownerId, ViewGroup parent, MainActivity activity) {
        this.id = id;
        this.typeId = typeId;
        this.location = location;
        this.name = name;
        this.shortDescription = shortDescription;
        this.date = date;
        this.cost = cost;
        this.longDescription = longDescription;
        this.ownerId = ownerId;
        type = EventType.getById(typeId);
        view = makeView(parent, activity);
    }

    private View makeView(ViewGroup parent, MainActivity activity) {
        View view = activity.getLayoutInflater().inflate(R.layout.event_example, parent, false);
        ((ImageView) view.findViewById(R.id.eventIcon)).setImageResource(type.getIcon());
        ((TextView) view.findViewById(R.id.eventName)).setText(name);
        String dateSt = "Дата проведения: " + getStringDate();
        ((TextView) view.findViewById(R.id.eventDate)).setText(dateSt);
        ((TextView) view.findViewById(R.id.eventShortDescription)).setText(shortDescription);
        view.findViewById(R.id.eventOpenMap).setOnClickListener(v -> MapActivity.gotoLocation(activity, location));
        view.findViewById(R.id.event_touch_layout).setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra(EventLayoutContent.EVENT, this);
            activity.startContent(intent, EventLayoutContent.class);
        });

        return view;
    }

    public String getStringDate() {
        return DateUtils.dateToString(date);
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public GregorianCalendar getDate() {
        return date;
    }

    public void setDate(GregorianCalendar date) {
        this.date = date;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getOwnerId() {
        return ownerId;
    }
}
