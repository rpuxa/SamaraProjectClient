package ru.samara.mapapp.chat;

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


public class ChatListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<Comment> chatList;
    MainActivity activity;

    public ChatListAdapter(MainActivity activity) {
        this.activity = activity;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        chatList = new ArrayList<>();
    }

    public void setChatList(List<Comment> chatList) {
        this.chatList = chatList;
    }

    public void removeAll() {
        chatList.clear();
    }

    public void addComment(Comment comment) {
        chatList.add(0, comment);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return chatList.size();
    }

    @Override
    public Object getItem(int position) {
        return chatList.get(position);
    }

    public Comment getComment(int position) {
        return (Comment) getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.item_coment, parent, false);
        }
        Comment comment = getComment(position);
        ((TextView) view.findViewById(R.id.tv_user_name)).setText(comment.getUserName());
        ((TextView) view.findViewById(R.id.tv_chat_text)).setText(comment.getText());
        ((TextView) view.findViewById(R.id.comment_date)).setText(comment.getStringTime());
        ImageView imageView = view.findViewById(R.id.iv_avatar_user);
        imageView.setImageBitmap(comment.getAuthor().getAvatar());
        imageView.setOnClickListener(v -> {
            if (activity.myProfile.getId() != comment.getAuthor().getId()) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.vk.com/id" + comment.getAuthor().getVkId()));
                activity.startActivity(browserIntent);
            }
        });
        return view;
    }

}
