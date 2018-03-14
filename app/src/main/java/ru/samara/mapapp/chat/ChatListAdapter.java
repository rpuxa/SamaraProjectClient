package ru.samara.mapapp.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.samara.mapapp.R;


public class ChatListAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    List<Coment> chatList;

    public ChatListAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        chatList = new ArrayList<>();
    }

    public void setChatList(List<Coment> chatList) {
        this.chatList = chatList;
    }

    public void addComent(Coment coment) {
        chatList.add(coment);
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

    public Coment getComent(int position) {
        return (Coment) getItem(position);
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
        Coment coment = getComent(position);
        ((TextView) view.findViewById(R.id.tv_user_name)).setText(coment.getUserName());
        ((TextView) view.findViewById(R.id.tv_chat_text)).setText(coment.getText());
        ((ImageView) view.findViewById(R.id.iv_avatar_user)).setImageResource(R.drawable.sport);
        return view;
    }
}
