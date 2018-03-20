package ru.samara.mapapp.activities.contents;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ru.samara.mapapp.R;
import ru.samara.mapapp.activities.Content;
import ru.samara.mapapp.activities.MainActivity;
import ru.samara.mapapp.activities.MapActivity;
import ru.samara.mapapp.activities.QRActivity;
import ru.samara.mapapp.chat.ChatListAdapter;
import ru.samara.mapapp.chat.Comment;
import ru.samara.mapapp.data.Profile;
import ru.samara.mapapp.dialogs.DateTimePickerDialog;
import ru.samara.mapapp.events.Event;
import ru.samara.mapapp.events.EventType;
import ru.samara.mapapp.events.EventTypeAdapter;
import ru.samara.mapapp.qr.IntentIntegrator;
import ru.samara.mapapp.qr.IntentResult;
import ru.samara.mapapp.server.Connect;
import ru.samara.mapapp.utils.DateUtils;

import static android.app.Activity.RESULT_OK;


public class EventLayoutContent extends Content {
    private boolean canEdit;
    private EditText commentEdit;
    private ChatListAdapter adapter;
    public static final String EVENT = "event";
    private TabHost tabHost;
    private Event event;
    private AlertDialog dialog;

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
        event = (Event) bundle.get(EVENT);
        assert event != null;
        canEdit = getParent().myProfile.getId() == event.getOwnerId();
        if (canEdit) {
            findViewById(R.id.tw_organiztor).setVisibility(View.VISIBLE);
            findViewById(R.id.event_layout_qr_read).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.event_layout_qr_read).setVisibility(View.GONE);
        }
        setLayoutEvent();
        setListeners();
        updateComments(0, 100);
        dialog = createTypesDialog();
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

    private void updateComments(int from, int to) {
        try {
            JSONObject object = Connect.sendToJSONObject("get_comments",
                    "id", String.valueOf(event.getId()),
                    "from", String.valueOf(from),
                    "to", String.valueOf(to)
            );
            JSONArray comments = object.getJSONArray("comments");
            adapter.removeAll();
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

    private void setLayoutEvent() {
        if (event == null)
            return;
        int type = event.getTypeId();
        EventType t = EventType.getById(type);
        ((ImageView) findViewById(R.id.icon_event)).setImageResource(t.getIcon());
        ImageView organizerAvatar = ((ImageView) findViewById(R.id.org_avatar));
        Profile organizer = Profile.getProfileById(getParent(), event.getOwnerId(), bitmap -> {
            organizerAvatar.setImageBitmap(bitmap);
            getParent().runOnUiThread(adapter::notifyDataSetChanged);
        });
        assert organizer != null;
        ((TextView) findViewById(R.id.org_name)).setText(organizer.getFullName());
        findViewById(R.id.org_layout).setOnClickListener(v -> getParent().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.vk.com/id" + organizer.getVkId()))));

        @IdRes
        int[] ids = {
                R.id.name_switcher, R.id.cost_event, R.id.description_switcher, R.id.full_descripion_switcher
        };

        String[][] texts = {
                {event.getName(), "Название"},
                {event.getCost() == 0 ? "Бесплатно" : "Цена " + event.getCost(), ""},
                {event.getShortDescription(), ""},
                {event.getLongDescription(), ""}
        };

        for (int i = 0; i < ids.length; i++) {
            ViewSwitcher switcher = (ViewSwitcher) findViewById(ids[i]);
            TextView textView = switcher.findViewById(R.id.tv_switcher);
            EditText editText = switcher.findViewById(R.id.et_switcher);
            if (ids[i] == R.id.name_switcher) {
                textView.setTextSize(20);
                textView.setTypeface(null, Typeface.BOLD);
                textView.setTextColor(Color.BLACK);
                textView.setAllCaps(true);
            }
            if (ids[i] == R.id.cost_event) {
                editText.setInputType(InputType.TYPE_CLASS_PHONE);
            }
            textView.setText(texts[i][0]);
            editText.setText(texts[i][1]);
            switcher.findViewById(R.id.bt_switcher).setOnClickListener(v -> {

                String stringEt = editText.getText().toString();
                textView.setText(stringEt);
                editText.setText("");
                switcher.showNext();
                switch (switcher.getId()) {
                    case R.id.name_switcher:
                        textView.setText(stringEt);
                        event.setName(stringEt);
                        break;
                    case R.id.description_switcher:
                        textView.setText(stringEt);
                        event.setShortDescription(stringEt);
                        break;
                    case R.id.full_descripion_switcher:
                        textView.setText(stringEt);
                        event.setLongDescription(stringEt);
                        break;
                    case R.id.cost_event:
                        StringBuilder s = new StringBuilder();
                        for (char ch : stringEt.toCharArray()) {
                            if (Character.isDigit(ch)) s.append(ch);
                        }
                        int cost = s.length() > 0 ? Integer.parseInt(s.toString()) : 0;
                        event.setCost(cost);
                        if (cost == 0) {
                            s = new StringBuilder("Бесплатно");
                        } else {
                            s.insert(0, "Цена ");
                        }
                        textView.setText(s.toString());
                        updateEvent(event);
                        break;
                }
                updateEvent(event);
            });
            switcher.setOnLongClickListener(v -> {
                if (canEdit) {
                    String textTW = textView.getText().toString();
                    if (switcher.getId() == R.id.cost_event) {
                        String s = "";
                        for (char ch : textTW.toCharArray()) {
                            if (Character.isDigit(ch)) s = s + ch;
                        }
                        textTW = s;
                    }
                    switcher.showNext();


                    editText.setText(textTW);
                } else getParent().sendToast("Вы не можете редактировать это мероприятие!", true);
                return true;
            });
        }

    }

    private void setListeners() {
        findViewById(R.id.button_send_coment).setOnClickListener(v -> {
            String commentString = commentEdit.getText().toString();
            if (commentString.length() > 200) {
                getParent().sendToast("Комментарий не должен превышать 200 символов", false);
                return;
            }
            if (commentString.length() > 0) {
                Comment comment = new Comment(
                        getParent().myProfile.getId(),
                        getParent().myProfile.getName(),
                        commentString,
                        System.currentTimeMillis() / 1000
                );
                sendCommentToServer(comment, event);
                comment.loadProfile(getParent(), adapter);
                addComment(comment);
                commentEdit.setText("");
            }
        });
        View location = findViewById(R.id.event_bt_location);
        location.setOnClickListener(v ->
                MapActivity.gotoLocation(getParent(), event.getLocation())
        );
        if (canEdit) {
            location.setOnLongClickListener(view -> {
                MapActivity.getLocation(getParent());
                return true;
            });
            findViewById(R.id.icon_event).setOnLongClickListener(v -> {
                dialog.show();
                return false;
            });


            findViewById(R.id.event_delete).setVisibility(View.VISIBLE);
            findViewById(R.id.event_button_delete).setOnClickListener(view -> {
                createRemoveDialog().show();
            });
        }
        TextView tvDate = (TextView) findViewById(R.id.event_tv_date);
        String dateString = "Дата: " + event.getStringDate();
        tvDate.setText(dateString);
        tvDate.setOnLongClickListener(v -> {
            if (canEdit) {
                new DateTimePickerDialog(getParent(), timeUNIX -> {
                    String dateString0 = "Дата: " + DateUtils.dateToString(timeUNIX);
                    tvDate.setText(dateString0);
                }).show();
            } else getParent().sendToast("Вы не можете редактировать это мероприятие!", true);
            return false;
        });
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_chat);
        swipeRefreshLayout.setColorSchemeResources(R.color.mainColor);
        swipeRefreshLayout.setColorSchemeColors(Color.LTGRAY, Color.GRAY, Color.DKGRAY, Color.LTGRAY);
        swipeRefreshLayout.setOnRefreshListener(() -> new Handler().postDelayed(() -> {
            swipeRefreshLayout.setRefreshing(true);
            updateComments(0, 100);
            swipeRefreshLayout.setRefreshing(false);
        }, 0));
        findViewById(R.id.event_layout_qr_read_button).setOnClickListener(view -> {
            QRActivity.read(getParent());
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MapActivity.REQUEST_CODE_MAP_CHOSE_LOCATION && resultCode == RESULT_OK) {
            LatLng location = (LatLng) data.getExtras().get(MapActivity.MAP_CHOSE_LOCATION);
            event.setLocation(location);
            return;
        }
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanResult != null) {
            try {
                String code = scanResult.getContents();
                JSONObject obj = Connect.sendToJSONObject("check_qr",
                        "event_id", String.valueOf(event.getId()),
                        "token", getParent().myProfile.getToken(),
                        "qr_token", code
                );
                if (obj.getString("status").equalsIgnoreCase("ok")) {
                    getParent().sendToast("Пользователь зачекинен!", false);
                } else
                    getParent().sendToast("Ошибка чтения QR", false);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void addComment(Comment comment) {
        adapter.addComment(comment);
        adapter.notifyDataSetChanged();
    }

    private void updateEvent(Event event) {
        String name = event.getName();
        String shortDescription = event.getShortDescription();
        String longDescription = event.getLongDescription();
        if (name.length() < 5 || name.length() > 20) {
            getParent().sendToast("Неверное кол-во символов у названия", true);
            return;
        }
        if (shortDescription.length() < 0 || shortDescription.length() > 100) {
            getParent().sendToast("Неверное кол-во символов у краткого описания", true);
            return;
        }
        if (longDescription.length() < 0 || longDescription.length() > 1000) {
            getParent().sendToast("Неверное кол-во символов у полного описания", true);
            return;
        }
        Connect.send("event_edit",
                "id", String.valueOf(event.getId()),
                "name", name,
                "longitude", String.valueOf(event.getLocation().longitude),
                "latitude", String.valueOf(event.getLocation().latitude),
                "s_description", shortDescription,
                "l_description", longDescription,
                "type", String.valueOf(event.getTypeId()),
                "cost", String.valueOf(event.getCost()),
                "time", String.valueOf(event.getTime()),
                "token", getParent().myProfile.getToken()
        );
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


    private void removeEvent(Event event) {


    }

    private AlertDialog createTypesDialog() {
        AlertDialog.Builder ad = new AlertDialog.Builder(getParent());
        View mainView = getParent().getLayoutInflater().inflate(R.layout.event_types_layout, null);
        ListView listView = mainView.findViewById(R.id.list_types);
        EventTypeAdapter adapter = new EventTypeAdapter(getParent());
        listView.setAdapter(adapter);

        for (int id : EventType.ids) {
            EventType type = EventType.types.get(id);
            adapter.addType(type);
        }
        ad.setTitle("Виды меропприятий")
                .setView(mainView)
                .setCancelable(true);
        AlertDialog dialog = ad.create();
        listView.setOnItemClickListener((parent, view, position, id) -> {
            event.setTypeId(adapter.getType(position).getId());
            updateEvent(event);
            dialog.cancel();
            ((ImageView) findViewById(R.id.icon_event)).setImageResource(adapter.getType(position).getIcon());
        });
        return dialog;
    }

    private AlertDialog createRemoveDialog() {
        AlertDialog.Builder ad = new AlertDialog.Builder(getParent());
        ad.setTitle("Удаление")
                .setMessage("Вы дейтсвительно хоитите удалить мероприятия?")
                .setPositiveButton("ДА", (dialog, which) -> {
                    removeEvent(event);
                    dialog.cancel();
                })
                .setNegativeButton("Назад", (dialog, which) -> {
                    dialog.cancel();
                })
                .setCancelable(true);
        return ad.create();
    }

}
