package com.example.mychat.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mychat.R;
import com.example.mychat.models.User;

import java.util.List;
public class UserAdapter extends ArrayAdapter<User> {
    private LayoutInflater inflater;
    private int layout;
    private List<User> users;
    public UserAdapter(@NonNull Context context, int resource, List<User> users) {
        super(context, resource, users);
        this.users = users;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }
    public View getView(int position,View convertView, ViewGroup parent){
        View view = inflater.inflate(this.layout,parent,false);
        TextView nameView = view.findViewById(R.id.name1);
        TextView statusView = view.findViewById(R.id.status1);
        User user= users.get(position);
        nameView.setText(user.getNickname());
        if(user.getIsonline()==1){
            statusView.setText("Online");
        } else{
            statusView.setText("Offline");
        }
        return view;
    }
}
