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
import ru.samara.mapapp.activities.MapActivity;
import ru.samara.mapapp.chat.ChatListAdapter;
import ru.samara.mapapp.chat.Comment;
import ru.samara.mapapp.events.Event;
import ru.samara.mapapp.events.EventType;
import ru.samara.mapapp.server.Connect;


public class EventLayoutContent extends Content {
    private EditText commentEdit;
    private ChatListAdapter adapter;
    public static final String EVENT = "event";

    @Override
    public void onCreate(MainActivity parent, Intent intent) {
        getParent().getToolbar().setSubtitle("Инфо о мероприятии");
        commentEdit = (EditText) findViewById(R.id.et_coment);
        adapter = new ChatListAdapter(getParent());
        ListView listView = (ListView) findViewById(R.id.chat_list);
        listView.setAdapter(adapter);
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        Event event = (Event) bundle.get(EVENT);
        setLayoutEvent(event);
        setListeners(event);
        updateComments(0, 100);
    }

    private void updateComments(int from, int to) {

    }

    private void setLayoutEvent(Event event) {
        if (event == null)
            return;
        int type = event.getTypeId();
        EventType t = EventType.getById(type);
        ((ImageView) findViewById(R.id.icon_event)).setImageResource(t.getIcon());
        ((TextView) findViewById(R.id.name_event)).setText(event.getName());
        String dateString = "Дата: " + event.getStringDate();
        ((TextView) findViewById(R.id.tv_data_event)).setText(dateString);
        ((TextView) findViewById(R.id.tv_short_description)).setText(event.getShortDescription());
        ((TextView) findViewById(R.id.tv_full_description)).setText(event.getLongDescription());
    }

    private void setListeners(final Event event) {
        findViewById(R.id.button_send_coment).setOnClickListener(v -> {
            String commentString = commentEdit.getText().toString();
            if (commentString.length() > 0) {
                Comment comment = new Comment(0, "Name", commentString);
                sendCommentToServer(comment, event);
                addComment(comment);
                commentEdit.setText("");
            } else {
                getParent().sendToast("Сначала введите потом кликайте!", true);
            }
        });
        findViewById(R.id.event_bt_location).setOnClickListener(v ->
                MapActivity.gotoLocation(getParent(), event.getLocation())
        );
    }


    private void addComment(Comment comment) {
        adapter.addComment(comment);
        adapter.notifyDataSetChanged();
    }

    private void sendCommentToServer(Comment comment, Event event) {
        Connect.send("add_comment",
                "main_id", String.valueOf(getParent().myProfile.getId()),
                "event_id", String.valueOf(event.getId()),
                "token", "\"" + getParent().myProfile.getToken() + "\"",
                "text", "\"" + comment.getText() + "\""
        );
    }

    @Override
    public int layout() {
        return R.layout.event_layout;
    }
}
