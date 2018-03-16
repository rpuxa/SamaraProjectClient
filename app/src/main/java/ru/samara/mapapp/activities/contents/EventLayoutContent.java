package ru.samara.mapapp.activities.contents;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private TabHost tabHost;

    @Override
    public void onCreate(MainActivity parent, Intent intent) {
        getParent().getToolbar().setSubtitle("Инфо о мероприятии");
        commentEdit = (EditText) findViewById(R.id.et_coment);
        adapter = new ChatListAdapter(getParent());
        ListView listView = (ListView) findViewById(R.id.chat_list);
        tabHost = (TabHost) findViewById(R.id.tabhost);
        createTabHost();
        listView.setAdapter(adapter);
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        Event event = (Event) bundle.get(EVENT);
        setLayoutEvent(event);
        setListeners(event);
        updateComments(0, 100, event);
    }

    private void createTabHost() {
        tabHost.setup();
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setContent(R.id.tab_event);
        tabSpec.setIndicator("Мероприятие");
        tabHost.addTab(tabSpec);
        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setContent(R.id.tab_chat);
        tabSpec.setIndicator("Чат");
        tabHost.addTab(tabSpec);
        tabHost.setCurrentTab(0);
    }


    private void updateComments(int from, int to, Event event) {
        try {
            JSONObject object = Connect.sendToJSONObject("get_comments",
                    "id", String.valueOf(event.getId()),
                    "from", String.valueOf(from),
                    "to", String.valueOf(to)
            );
            JSONArray comments = object.getJSONArray("comments");
            for (int i = 0; i < comments.length(); i++) {
                JSONObject commentObject = comments.getJSONObject(i);
                Comment comment = new Comment(
                        commentObject.getInt("author_id"),
                        commentObject.getString("author_name"),
                        commentObject.getString("text"),
                        commentObject.getLong("time")
                );
                addComment(comment);
            }
        } catch (JSONException ignored) {
        }
    }

    private void setLayoutEvent(Event event) {
        if (event == null)
            return;
        int type = event.getTypeId();
        EventType t = EventType.getById(type);
        ((ImageView) findViewById(R.id.icon_event)).setImageResource(t.getIcon());
        String dateString = "Дата: " + event.getStringDate();
        ((TextView) findViewById(R.id.tv_data_event)).setText(dateString);
        ((TextView) findViewById(R.id.tv_short_description)).setText(event.getShortDescription());
        ((TextView) findViewById(R.id.tv_full_description)).setText(event.getLongDescription());
    }

    private void setListeners(final Event event) {
        findViewById(R.id.button_send_coment).setOnClickListener(v -> {
            String commentString = commentEdit.getText().toString();
            if (commentString.length() > 0) {
                Comment comment = new Comment(
                        getParent().myProfile.getId(),
                        getParent().myProfile.getName(),
                        commentString,
                        System.currentTimeMillis() / 1000
                );
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
                "token", getParent().myProfile.getToken(),
                "text", comment.getText()
        );
    }

    @Override
    public int layout() {
        return R.layout.event_layout;
    }

}
