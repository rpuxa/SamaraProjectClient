package ru.samara.mapapp.events;

import android.support.annotation.IdRes;

import java.util.HashMap;
import java.util.Map;

import ru.samara.mapapp.R;

import static ru.samara.mapapp.events.EventType.Types.CINEMA;
import static ru.samara.mapapp.events.EventType.Types.MUSEUM;
import static ru.samara.mapapp.events.EventType.Types.OTHER;
import static ru.samara.mapapp.events.EventType.Types.SALES;
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
                new EventType(MUSEUM, "Музей", R.drawable.musem),
                new EventType(OTHER, "Прочее", R.drawable.other),
                new EventType(SALES, "Скидки", R.drawable.sales),
                new EventType(CINEMA, "Кино", R.drawable.cinema)
        };
        for (EventType type : array)
            types.put(type.id, type);
    }


    public static EventType getById(int id) {
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
        int CINEMA = 2;
        int SALES = 3;
        int OTHER = 4;
    }
}

