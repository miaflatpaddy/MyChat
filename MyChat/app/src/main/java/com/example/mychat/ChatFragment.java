package com.example.mychat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mychat.adapters.MessageAdapter;
import com.example.mychat.adapters.UserAdapter;
import com.example.mychat.models.Msg;
import com.example.mychat.models.User;
import com.example.mychat.services.MessageService;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ChatFragment  extends Fragment{
    final static String LOG_TAG = "myLogs: ChatFragment";
    private TextView mTitleTextView;
    public final static String PARAM_RESULT = "result";
    private Button      sendButton;
    private TextView    mesTextView;
    private TextView username;
    public final static String BROADCAST_ACTION = "r.com.MyChat.servicechat";
    private String jstring;
    ArrayList<Msg> msgs;
    ListView msglist;
    static View rootView;
    BroadcastReceiver br;
    private boolean notifyed = false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        username = rootView.findViewById(R.id.userNameStr);
        if(MainActivity.nickname!=null){
            username.setText(MainActivity.nickname);
        }
        sendButton = (Button) rootView.findViewById(R.id.sendChatButton);
        sendButton.setOnClickListener(sendMessageClickListener);

        mesTextView = (TextView) rootView.findViewById(R.id.messageEditText);
        mesTextView.setText("");
        if(MainActivity.logedIn){
            br = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    jstring = intent.getStringExtra(PARAM_RESULT);
                    Log.d(LOG_TAG, "onReceive: result = " + jstring);
                    displayChat();
                }
            };
            IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION);
            getContext().registerReceiver(br, intFilt);
            MessageService.runned = true;
            Intent intent = new Intent(getContext(), MessageService.class);
            getContext().startService(intent);
        }
        return rootView;
    }

    View.OnClickListener sendMessageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(MainActivity.logedIn){
                onSendMsgClick();
            } else {
                Toast toast =  Toast.makeText(getContext(),"You must loged in at first.",Toast.LENGTH_SHORT);
                toast.show();
            }

        }
    };

    public void onSendMsgClick()
    {
        // выводим сообщение
        new HTTPReqMsgTask().execute();
        mesTextView.setText("");
    }


    public void onDestroyView(){
        MessageService.runned = false;
        super.onDestroyView();
    }
    public void onPause() {
        MessageService.runned = false;
        super.onPause();
    }

//    @Override
//    public void onClick(View view) {
//
//        OnSelectedButtonListener listener = (OnSelectedButtonListener) getActivity();
//        listener.onButtonSelected(buttonIndex, mesTextView.getText().toString());
//        mesTextView.setText("");
//
//        // Временный код для получения индекса нажатой кнопки
//        Toast.makeText(getActivity(), String.valueOf(buttonIndex),
//                Toast.LENGTH_SHORT).show();
//    }

    public interface OnSelectedButtonListener {
        void onButtonSelected(String userID, String messageStr);
    }
    public void displayChat(){

        msglist = rootView.findViewById(R.id.msgView);

        msgs = new ArrayList<Msg>();
        try {
            JSONArray arr = new JSONArray(jstring);
            for(int i = 0; i < arr.length();i++){
                msgs.add(new Msg(arr.getJSONObject(i).getString("author"),arr.getJSONObject(i).getString("message")));
                if(arr.getJSONObject(i).getInt("isnew")==1&&!notifyed){
                    OnSelectedButtonListener listener = (OnSelectedButtonListener) getActivity();
                    listener.onButtonSelected(arr.getJSONObject(i).getString("author"), arr.getJSONObject(i).getString("message"));
                    notifyed = true;
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        Log.d(LOG_TAG,"test1");
        MessageAdapter msgAdapter = new MessageAdapter(getContext(),R.layout.msg_item,msgs);
        msglist.setAdapter(msgAdapter);
        notifyed = false;
    }
    private static class HTTPReqMsgTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Log.d(LOG_TAG, "rquest test");
            HttpURLConnection urlConnection = null;
            TextView msgView = rootView.findViewById(R.id.messageEditText);
            try {
                URL url = new URL("http://10.0.2.2:3000/addMessage?author=" + MainActivity.nickname + "&msg=" + msgView.getText().toString());
                Log.d(LOG_TAG, "rquest test1");
                urlConnection = (HttpURLConnection) url.openConnection();
                Log.d(LOG_TAG, "rquest test2");
                int code = urlConnection.getResponseCode();
                Log.d(LOG_TAG, Integer.toString(code));
                if (code !=  200) {
                    Log.d(LOG_TAG, "invalid");
                    throw new IOException("Invalid response from server: " + code);

                }
                Log.d(LOG_TAG, "rquest test");

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }
    }

}