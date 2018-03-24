package ru.samara.mapapp.events;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;

import ru.samara.mapapp.activities.MainActivity;

public class EventsList extends BaseAdapter {
    private ArrayList<Event> events;
    private Event[] filteredEvents;
    private ArrayList<EventSearchFilter> filters;
    private MainActivity activity;
    private ListView mainEventList;

    public EventsList(MainActivity activity) {
        events = new ArrayList<>();
        filters = new ArrayList<>();
        this.activity = activity;
    }

    public void addEvent(int id, Integer typeId, LatLng location, String name, String shortDescription, String longDescription,
                         GregorianCalendar date, int cost, int ownerId, int rating, int peopleCount) {
        events.add(new Event(
                id, typeId, location, name, shortDescription, longDescription, date, cost, ownerId, peopleCount, rating, mainEventList, activity
        ));

        Collections.sort(events, (o1, o2) -> {
            GregorianCalendar date1 = o1.getDate();
            GregorianCalendar date2 = o2.getDate();
            return date1.compareTo(date2);
        });
    }

    public void setMainEventList(ListView mainEventList) {
        this.mainEventList = mainEventList;
        mainEventList.setAdapter(this);
    }

    private Event[] filterEvents() {
        ArrayList<Event> filteredEvents = new ArrayList<>();
        label:
        for (Event event : events) {
            for (EventSearchFilter filter : filters) {
                boolean isSuited = filter.suit(event);
                if (!isSuited)
                    continue label;
            }
            filteredEvents.add(event);
        }
        return filteredEvents.toArray(new Event[0]);
    }

    public void addFilter(EventSearchFilter filter) {
        filters.add(filter);
    }

    @Override
    public int getCount() {
        filteredEvents = filterEvents();
        return filteredEvents.length;
    }

    @Override
    public Object getItem(int i) {
        return filteredEvents[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return filteredEvents[i].getView();
    }

    public void removeAllEvents() {
        events.clear();
    }
}
