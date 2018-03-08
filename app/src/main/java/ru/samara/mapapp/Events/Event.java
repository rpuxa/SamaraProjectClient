package ru.samara.mapapp.Events;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;
import java.util.GregorianCalendar;

import ru.samara.mapapp.R;

public class Event {
    int id;
    Integer typeId;
    EventType type;
    LatLng location;
    String name, shortDescription;
    GregorianCalendar date;

    public Event(int id, Integer typeId, LatLng location, String name, String shortDescription, GregorianCalendar date) {
        this.id = id;
        this.typeId = typeId;
        this.location = location;
        this.name = name;
        this.shortDescription = shortDescription;
        this.date = date;
        type = EventType.getType(typeId);
    }

    public LinearLayout getVisual(Context context) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        LinearLayout mainLayout = new LinearLayout(context);
        mainLayout.setOrientation(LinearLayout.HORIZONTAL);
        mainLayout.setLayoutParams(params);

        ImageView icon = new ImageView(context);
        icon.setImageResource(type.getIcon());
        mainLayout.addView(icon);

        LinearLayout layout0 = new LinearLayout(context);
        layout0.setLayoutParams(params);
        layout0.setOrientation(LinearLayout.VERTICAL);
        mainLayout.addView(layout0);

        TextView name = new TextView(context);
        name.setText(this.name);
        name.setLayoutParams(params);
        name.setTextSize(17);
        name.setTextColor(context.getResources().getColor(R.color.common_google_signin_btn_text_dark_focused));
        layout0.addView(name);

        LinearLayout layout1 = new LinearLayout(context);
        layout1.setLayoutParams(params);
        layout1.setOrientation(LinearLayout.HORIZONTAL);
        layout0.addView(layout1);

        LinearLayout layout2 = new LinearLayout(context);
        layout2.setLayoutParams(params);
        layout2.setOrientation(LinearLayout.VERTICAL);
        layout1.addView(layout2);

        TextView date = new TextView(context);
        date.setLayoutParams(params);
        String dateSt = "Дата проведения: " + this.date.get(Calendar.HOUR) + ":" + ((this.date.get(Calendar.MINUTE) < 10) ? "0" : "")
                + this.date.get(Calendar.MINUTE) + " " + this.date.get(Calendar.DAY_OF_MONTH) + "." + this.date.get(Calendar.MONTH) + "." +
                this.date.get(Calendar.YEAR);
        date.setText(dateSt);
        layout2.addView(date);

        TextView description = new TextView(context);
        description.setLayoutParams(params);
        description.setText(this.shortDescription);
        layout2.addView(description);

        ImageButton button = new ImageButton(context);
        button.setImageResource(R.drawable.google_maps_icon);
        button.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));
        mainLayout.addView(button);

        return mainLayout;
    }

    public Integer getTypeId() {
        return typeId;
    }
}
