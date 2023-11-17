package com.example.mychat.adapters;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mychat.MainActivity;
import com.example.mychat.R;
import com.example.mychat.models.Msg;

import java.util.List;
public class MessageAdapter extends ArrayAdapter<Msg> {
    private LayoutInflater inflater;
    private int layout;
    private List<Msg> msgs;
    public MessageAdapter(@NonNull Context context, int resource, List<Msg> msgs) {
        super(context, resource, msgs);
        this.msgs = msgs;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }
    public View getView(int position,View convertView, ViewGroup parent){
        View view = inflater.inflate(this.layout,parent,false);
        LinearLayout ll = view.findViewById(R.id.msgitem);
        TextView authorView = view.findViewById(R.id.author);
        TextView msgView = view.findViewById(R.id.message1);
        Msg msg= msgs.get(position);
        authorView.setText(msg.getAuthor());
        msgView.setText(msg.getText());
        if(msg.getAuthor().equals(MainActivity.nickname)){
            msgView.setGravity(Gravity.END);
            authorView.setGravity(Gravity.END);
            ll.setGravity(Gravity.END);
        }

        return view;
    }

}
