package com.example.mychat;



import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Context;
import android.content.Intent;

import com.example.mychat.adapters.UserAdapter;
import com.example.mychat.models.User;
import com.example.mychat.services.UserService;
import android.app.PendingIntent;
import android.content.IntentFilter;
import android.util.Log;
import android.content.ContextWrapper;

import androidx.annotation.Nullable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class UsersFragment  extends Fragment {
    final String LOG_TAG = "myLogs: UserFragment";
    private TextView mTitleTextView;
    public final static String PARAM_RESULT = "result";
    public final static String BROADCAST_ACTION = "r.com.MyChat.serviceusers";
    private String jstring;
    ArrayList<User> users;
    ListView userlist;
    View rootView;
    BroadcastReceiver br;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_users, container, false);
        mTitleTextView = (TextView) rootView.findViewById(R.id.usersTitleTextView);
        br = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String result = intent.getStringExtra(PARAM_RESULT);
                Log.d(LOG_TAG, "onReceive: result = " + result);
                jstring=result;
                updateUsers();
            }
        };
        IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION);
        getContext().registerReceiver(br, intFilt);
        Intent intent = new Intent(getContext(), UserService.class);
        getContext().startService(intent);
        UserService.runned = true;
        return rootView;
    }
    public void updateUsers(){

        userlist = rootView.findViewById(R.id.userlist);

        users = new ArrayList<User>();
        try {
            JSONArray arr = new JSONArray(jstring);
            for(int i = 0; i < arr.length();i++){
                users.add(new User(arr.getJSONObject(i).getString("nickname"),arr.getJSONObject(i).getInt("isonline")));
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        UserAdapter userAdapter = new UserAdapter(getContext(),R.layout.user_item,users);
        userlist.setAdapter(userAdapter);
    }

    public void setTitleForFragment(String valueStr) {

        mTitleTextView.setText(valueStr);

    }
    public void onDestroyView(){
        UserService.runned = false;
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        UserService.runned = false;
        super.onPause();
    }

}