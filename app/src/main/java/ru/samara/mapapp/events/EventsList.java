package ru.samara.mapapp.events;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class EventsList extends BaseAdapter {
    private ArrayList<Event> events;
    private Event[] filteredEvents;
    private ArrayList<EventSearchFilter> filters;
    private Activity activity;

    public EventsList(Activity activity) {
        events = new ArrayList<>();
        filters = new ArrayList<>();
        this.activity = activity;
    }

    private Event[] getEvents() {
        ArrayList<Event> filteredEvents = new ArrayList<>();
        label: for (Event event : events) {
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

    public void addEvent(Event event) {
        events.add(event);
    }

    @Override
    public int getCount() {
        filteredEvents = getEvents();
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
}
