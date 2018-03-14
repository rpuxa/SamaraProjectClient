package ru.samara.mapapp.activities.contents;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import ru.samara.mapapp.R;
import ru.samara.mapapp.activities.Content;
import ru.samara.mapapp.activities.MainActivity;
import ru.samara.mapapp.chat.ChatListAdapter;
import ru.samara.mapapp.chat.Comment;
import ru.samara.mapapp.events.Event;
import ru.samara.mapapp.events.EventType;


public class EventLayoutContent extends Content {
    private EditText commentEdit;
    private ChatListAdapter adapter;
    public static final String EVENT = "event";

    @Override
    public void onCreate(MainActivity parent, Intent intent) {
        setListeners();
        commentEdit = (EditText) findViewById(R.id.et_coment);
        adapter = new ChatListAdapter(getParent());
        ListView listView = (ListView) findViewById(R.id.chat_list);
        listView.setAdapter(adapter);
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        setLayoutEvent((Event) bundle.get(EVENT));
    }

    private void setLayoutEvent(Event event) {
        if (event == null)
            return;
        int type = event.getTypeId();
        EventType t = EventType.getById(type);
        ((ImageView) findViewById(R.id.icon_event)).setImageResource(t.getIcon());
        ((TextView) findViewById(R.id.name_event)).setText(event.getName());
        ((TextView) findViewById(R.id.tv_data_event)).setText("ДАТА");
        ((TextView) findViewById(R.id.tv_short_description)).setText(event.getShortDescription());
        ((TextView) findViewById(R.id.tv_full_description)).setText(event.getLongDescription());
    }

    private void setListeners() {
        findViewById(R.id.button_send_coment).setOnClickListener(v -> {
            String commentString = commentEdit.getText().toString();
            if (commentString.length() > 0) {
                addComment(new Comment(0, "Name", commentString));
                commentEdit.setText("");
            } else {
                getParent().sendToast("Сначала введите потом кликайте!", true);
            }
        });
    }


    private void addComment(Comment comment) {
        adapter.addComment(comment);
        adapter.notifyDataSetChanged();
    }

    @Override
    public int layout() {
        return R.layout.event_layout;
    }
}
