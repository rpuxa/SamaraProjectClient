package ru.samara.mapapp.events;

import android.support.annotation.IdRes;

import java.util.HashMap;
import java.util.Map;

import ru.samara.mapapp.R;

import static ru.samara.mapapp.events.EventType.Types.MUSEUM;
import static ru.samara.mapapp.events.EventType.Types.SPORT;

public class EventType {
    private int id;
    private String name;
    @IdRes
    private int icon;

    public EventType(int id, String name, int icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    public static final Map<Integer, EventType> types = new HashMap<>();

    static {
        EventType array[] = {
                new EventType(SPORT, "Спорт", R.drawable.sport),
                new EventType(MUSEUM, "Музей", R.drawable.sport)
        };
        for (EventType type : array)
            types.put(type.id, type);
    }


    public static EventType getType(int id) {
        return types.get(id);
    }

    public static EventType getByName(String name) {
        for (EventType type : types.values()) {
            if (type.name.equals(name))
                return type;
        }
        throw new RuntimeException("Not Found");
    }


    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getIcon() {
        return icon;
    }


    public interface Types {
        int SPORT = 0;
        int MUSEUM = 1;
    }
}

