package ru.samara.mapapp.events;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.samara.mapapp.R;
import ru.samara.mapapp.activities.MainActivity;
import ru.samara.mapapp.chat.Comment;


public class EventTypeAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<EventType> typeList;
    MainActivity activity;

    public EventTypeAdapter(MainActivity activity) {
        this.activity = activity;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        typeList = new ArrayList<>();
    }


    public void removeAll() {
        typeList.clear();
    }

    public void addType(EventType type) {
        typeList.add(0, type);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return typeList.size();
    }

    @Override
    public Object getItem(int position) {
        return typeList.get(position);
    }

    public EventType getType(int position) {
        return (EventType) getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.item_type_layout, parent, false);
        }
        EventType type = getType(position);

        ((TextView) view.findViewById(R.id.event_type_name)).setText(type.getName());
        ((ImageView) view.findViewById(R.id.event_type_icon)).setImageResource(type.getIcon());
        return view;
    }
}
