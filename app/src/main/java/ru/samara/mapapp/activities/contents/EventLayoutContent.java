package ru.samara.mapapp.activities.contents;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
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
import ru.samara.mapapp.dialogs.DateTimePickerDialog;
import ru.samara.mapapp.events.Event;
import ru.samara.mapapp.events.EventType;
import ru.samara.mapapp.server.Connect;
import ru.samara.mapapp.utils.DateUtils;


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
                comment.loadProfile(getParent(), adapter);
                addComment(comment);
            }
        } catch (JSONException ignored) {
        }
    }

    private void setLayoutEvent(final Event event) {
        if (event == null)
            return;
        int type = event.getTypeId();
        EventType t = EventType.getById(type);
        ((ImageView) findViewById(R.id.icon_event)).setImageResource(t.getIcon());
        @IdRes
        int[] ids = {
                R.id.name_switcher, R.id.description_switcher, R.id.full_descripion_switcher
        };
        String[][] texts = {
                {event.getName(), "Название"},
                {event.getShortDescription(), ""},
                {event.getLongDescription(), ""}
        };
        for (int i = 0; i < ids.length; i++) {
            ViewSwitcher switcher = (ViewSwitcher) findViewById(ids[i]);
            ((TextView) switcher.findViewById(R.id.tv_switcher)).setText(texts[i][0]);
            ((EditText) switcher.findViewById(R.id.et_switcher)).setText(texts[i][1]);
            switcher.findViewById(R.id.bt_switcher).setOnClickListener(v -> {
                EditText et = switcher.findViewById(R.id.et_switcher);
                String stringEt = et.getText().toString();
                ((TextView) switcher.findViewById(R.id.tv_switcher)).setText(stringEt);
                et.setText("");
                switcher.showNext();
                switch (switcher.getId()) {
                    case R.id.name_switcher:
                        event.setName(stringEt);
                        break;
                    case R.id.description_switcher:
                        event.setShortDescription(stringEt);
                        break;
                    case R.id.full_descripion_switcher:
                        event.setLongDescription(stringEt);
                        break;
                }
                updateEvent(event);
            });
            switcher.setOnLongClickListener(longClickListener);
        }

    }

    View.OnLongClickListener longClickListener = v -> {
        ViewSwitcher switcher = (ViewSwitcher) findViewById(v.getId());
        TextView tw = switcher.findViewById(R.id.tv_switcher);
        EditText et = switcher.findViewById(R.id.et_switcher);
        String textTW = tw.getText().toString();
        switcher.showNext();
        et.setText(textTW);
        return true;
    };

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
                comment.loadProfile(getParent(), adapter);
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

        TextView tvDate = (TextView) findViewById(R.id.event_tv_date);
        String dateString = "Дата: " + event.getStringDate();
        tvDate.setText(dateString);
        tvDate.setOnLongClickListener(v -> {
            new DateTimePickerDialog(getParent(), timeUNIX -> {
                String dateString0 = "Дата: " + DateUtils.dateToString(timeUNIX);
                tvDate.setText(dateString0);
            }).show();
            return false;
        });
    }


    private void addComment(Comment comment) {
        adapter.addComment(comment);
        adapter.notifyDataSetChanged();
    }

    private void updateEvent(Event event) {

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
