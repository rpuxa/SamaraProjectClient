package ru.samara.mapapp.activities.contents;

import android.content.Context;
import android.content.Intent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import ru.samara.mapapp.R;
import ru.samara.mapapp.activities.Content;
import ru.samara.mapapp.activities.MainActivity;
import ru.samara.mapapp.chat.ChatListAdapter;
import ru.samara.mapapp.chat.Coment;
import ru.samara.mapapp.events.Event;
import ru.samara.mapapp.events.EventType;


public class EventLayoutContent extends Content {
    private EditText comentEdit;
    Context context;
    ChatListAdapter adapter;
    ListView listView;
    public static final String EVENT = "event";

    @Override
    public void onCreate(MainActivity parent, Intent intent) {
        setListeners();
        comentEdit = (EditText) findViewById(R.id.et_coment);
        context = parent.getApplicationContext();
        adapter = new ChatListAdapter(context);
        listView = (ListView) findViewById(R.id.chat_list);
        listView.setAdapter(adapter);
        setLayoutEvent((Event) intent.getExtras().getSerializable(EVENT));
    }

    void setLayoutEvent(Event event) {
        if (event == null) return;
        int type = event.getTypeId();
        EventType t = EventType.getById(type);
        ((ImageView) findViewById(R.id.icon_event)).setImageResource(t.getIcon());
        ((TextView) findViewById(R.id.name_event)).setText(event.getName());
        ((TextView) findViewById(R.id.tv_data_event)).setText("ДАТА");
        ((TextView) findViewById(R.id.tv_short_description)).setText(event.getShortDescription());
        ((TextView) findViewById(R.id.tv_full_description)).setText(event.getLongDescription());
    }

    void setListeners() {
        findViewById(R.id.button_send_coment).setOnClickListener(v -> {
            String comentStr = comentEdit.getText().toString();
            if (comentStr.length() > 0) {
                addComent(new Coment(0, "Name", comentStr));
                comentEdit.setText("");
            } else {
                Toast.makeText(context, "СНачала введите потом кликайте!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    void addComent(Coment coment) {
        adapter.addComent(coment);
        adapter.notifyDataSetChanged();
    }

    @Override
    public int layout() {
        return R.layout.event_layout;
    }
}
