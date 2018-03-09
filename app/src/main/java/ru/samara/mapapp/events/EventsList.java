package ru.samara.mapapp.events;

import java.util.ArrayList;

public class EventsList {
    ArrayList<Event> events;
    ArrayList<EventSearchFilter> filters;

    public EventsList() {
        events = new ArrayList<>();
        filters = new ArrayList<>();
    }

    public Event[] getEvents() {
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
}
