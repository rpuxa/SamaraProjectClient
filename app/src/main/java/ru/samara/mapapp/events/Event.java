package ru.samara.mapapp.events;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.GregorianCalendar;

import ru.samara.mapapp.R;
import ru.samara.mapapp.activities.LoginActivity;
import ru.samara.mapapp.activities.MainActivity;
import ru.samara.mapapp.activities.MapActivity;
import ru.samara.mapapp.activities.contents.EventLayoutContent;
import ru.samara.mapapp.utils.DateUtils;

public class Event implements Serializable {
    private static final int NOTIFY_ID = 101;
    private int id;
    private Integer typeId;
    private EventType type;
    private LatLng location;
    private String name, shortDescription, longDescription;
    private GregorianCalendar date;
    private int cost;
    private View view;
    private int ownerId;
    private int rating;
    private int peopleCount;
    private MainActivity activity;

    public Event(int id, Integer typeId, LatLng location, String name, String shortDescription, String longDescription,
                 GregorianCalendar date, int cost, int ownerId, int peopleCount, int rating, ViewGroup parent, MainActivity activity) {
        this.id = id;
        this.typeId = typeId;
        this.location = location;
        this.name = name;
        this.shortDescription = shortDescription;
        this.date = date;
        this.cost = cost;
        this.longDescription = longDescription;
        this.ownerId = ownerId;
        this.activity = activity;
        this.rating = rating;
        this.peopleCount = peopleCount;
        type = EventType.getById(typeId);
        view = makeView(parent, activity);
        createNotification(activity);
    }


    private void createNotification(MainActivity activity) {
        Intent notificationIntent = new Intent(activity, LoginActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(activity,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(activity);

        builder.setContentIntent(contentIntent)
                .setSmallIcon(EventType.getById(getTypeId()).getIcon())
                .setContentTitle("Напоминание")
                .setContentText("Скоро будет '" + getName() + "'") // Текст уведомления
                .setWhen(getTime() * 1000)
                .setAutoCancel(true);

        NotificationManager notificationManager =
                (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.notify(NOTIFY_ID, builder.build());
    }

    private View makeView(ViewGroup parent, MainActivity activity) {
        View view = activity.getLayoutInflater().inflate(R.layout.event_example, parent, false);
        ((ImageView) view.findViewById(R.id.eventIcon)).setImageResource(type.getIcon());
        TextView twName = view.findViewById(R.id.eventName);
        twName.setText(name);
        twName.setAllCaps(true);
        String dateSt = "Дата: " + getStringDate();
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

    public long getTime() {
        return date.getTimeInMillis() / 1000;
    }

    public boolean checkEvent() {
        if (name.length() < 5 || name.length() > 20) {
            activity.sendToast("Неверное кол-во символов у названия", true);
            return false;
        }
        if (shortDescription.length() < 0 || shortDescription.length() > 100) {
            activity.sendToast("Неверное кол-во символов у краткого описания", true);
            return false;
        }
        if (longDescription.length() < 0 || longDescription.length() > 1000) {
            activity.sendToast("Неверное кол-во символов у полного описания", true);
            return false;
        }

        if (date == null) {
            activity.sendToast("Установите дату проведения", true);
            return false;
        }

        if (getTime() <= System.currentTimeMillis() / 1000) {
            activity.sendToast("Дата проведения должна быть позже настоящего времени", true);
            return false;
        }

        if (location == null) {
            activity.sendToast("Установите место проведения", true);
            return false;
        }

        return true;
    }

    public int getRating() {
        return rating;
    }

    public int getPeopleCount() {
        return peopleCount;
    }
}
