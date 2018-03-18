package ru.samara.mapapp.activities.contents;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ru.samara.mapapp.R;
import ru.samara.mapapp.activities.Content;
import ru.samara.mapapp.activities.MainActivity;
import ru.samara.mapapp.events.EventSearchFilter;
import ru.samara.mapapp.events.EventType;
import ru.samara.mapapp.events.EventsList;
import ru.samara.mapapp.server.Connect;
import ru.samara.mapapp.utils.DateUtils;


public class EventSearchContent extends Content {
    private AlertDialog dialog;
    private EventsList list;

    @Override
    public void onCreate(MainActivity parent, Intent intent) {
        getParent().getToolbar().setSubtitle("Найти мероприятие");
        list = new EventsList(getParent());
        dialog = createDialog();
        setListeners();
        list.setMainEventList((ListView) findViewById(R.id.mainEventList));
        updateEvents();
    }

    @Override
    public int layout() {
        return R.layout.event_list;
    }

    private void setListeners() {
        findViewById(R.id.settings).setOnClickListener(v -> dialog.show());
        findViewById(R.id.search).setOnClickListener(v -> list.notifyDataSetChanged());
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresher);
        swipeRefreshLayout.setColorSchemeResources(R.color.mainColor);
        swipeRefreshLayout.setColorSchemeColors(Color.LTGRAY, Color.GRAY);
        swipeRefreshLayout.setOnRefreshListener(() -> new Handler().postDelayed(() -> {
            swipeRefreshLayout.setRefreshing(true);
            updateEvents();
            swipeRefreshLayout.setRefreshing(false);
        }, 0));
    }

    private AlertDialog createDialog() {
        AlertDialog.Builder ad = new AlertDialog.Builder(getParent());
        View view = getParent().getLayoutInflater().inflate(R.layout.filters_layout, null);
        ad.setTitle("Фильтры")
                .setView(view)
                .setCancelable(true)
                .setMessage("Выберите сортировку мероприятий:")
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.cancel();
                    list.notifyDataSetChanged();
                });

        Filters filters = new Filters();
        filters.setEventTypeSpinner(view);
        filters.setPaidSpinner(view);
        filters.setNameFilter();
        filters.setIsMyEventFilter(view);
        dialog = ad.create();
        return dialog;
    }


    private void updateEvents() {
        try {
            list.removeAllEvents();
            JSONArray array = (JSONArray) Connect.sendToJSONObject("showallevents").get("events");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                list.addEvent(
                        object.getInt("id"),
                        object.getInt("type"),
                        new LatLng(
                                object.getDouble("latitude"),
                                object.getDouble("longitude")
                        ),
                        object.getString("name"),
                        object.getString("s_description"),
                        object.getString("l_description"),
                        DateUtils.timeToCalendar(object.getLong("time")),
                        object.getInt("cost"),
                        object.getInt("main_id")
                );
            }
            list.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private final class Filters {

        private void setIsMyEventFilter(View view) {
            CheckBox box = view.findViewById(R.id.event_search_isMyEvent);
            EventSearchFilter filter = event -> !box.isChecked() || (getParent().myProfile.getId() == event.getOwnerId());
            list.addFilter(filter);
        }

        private void setEventTypeSpinner(View view) {
            Spinner spinner = view.findViewById(R.id.eventTypeSpinner);
            ArrayList<String> typesNames = new ArrayList<>(EventType.types.size());
            typesNames.add("Любой");
            for (EventType type : EventType.types.values())
                typesNames.add(type.getName());
            spinner.setAdapter(new ArrayAdapter<>(getParent(), R.layout.support_simple_spinner_dropdown_item, typesNames));
            final int[] type = {-1};
            EventSearchFilter filter = event -> type[0] == -1 || type[0] == event.getTypeId();
            list.addFilter(filter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (i == 0) {
                        type[0] = -1;
                        return;
                    }
                    type[0] = EventType.getByName(typesNames.get(i)).getId();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        }

        private void setPaidSpinner(View view) {
            Spinner spinner = view.findViewById(R.id.event_search_isPaid);
            String names[] = {
                    "Любая", "Бесплатно", "Платно"
            };
            spinner.setAdapter(new ArrayAdapter<>(getParent(), R.layout.support_simple_spinner_dropdown_item, names));
            final int[] type = {0};
            EventSearchFilter filter = event -> type[0] == 0 || ((type[0] == 2) == (event.isPaid()));
            list.addFilter(filter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    type[0] = i;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        }

        private void setNameFilter() {
            EventSearchFilter filter = event -> {
                String text = ((EditText) findViewById(R.id.searchBar)).getText().toString();
                return text.isEmpty() || event.getName().toLowerCase().lastIndexOf(text.toLowerCase()) != -1;
            };
            list.addFilter(filter);
        }
    }

}
