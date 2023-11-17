package com.example.mychat;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mychat.helpers.DBHelper;
import com.example.mychat.models.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UserInfoFragment extends Fragment {
    private static String MSG = "myLogs: LoginFragment";
    private static TextView email;
    private static TextView name;
    private static TextView nickname;
    private static TextView pass;
    private TextView confirmPass;
    DBHelper dbHelper;
    SQLiteDatabase db;
    Button register;
    public UserInfoFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        dbHelper = new DBHelper(getContext());
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_info, container, false);
        email = (TextView) rootView.findViewById(R.id.emailEditText);
        name = (TextView) rootView.findViewById(R.id.nameEditText);
        nickname = (TextView) rootView.findViewById(R.id.nicknameEditText);
        pass = (TextView) rootView.findViewById(R.id.passEditText);
        confirmPass = (TextView) rootView.findViewById(R.id.confirmPassEditText);
        db = dbHelper.getWritableDatabase();
        register = (Button) rootView.findViewById(R.id.register_btn1);
        register.setOnClickListener(registerClickListener);

        return rootView;
    }
    View.OnClickListener registerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ContentValues info = getInfo();
            if(info.get(DBHelper.KEY_PASS).equals(confirmPass.getText().toString())){
                new HTTPReqTask().execute();
                Toast toast =  Toast.makeText(getContext(),"Registration succesfully",Toast.LENGTH_SHORT);
                toast.show();
            } else {
                Toast toast =  Toast.makeText(getContext(),"Passwords not match",Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    };
    private static ContentValues getInfo(){
        ContentValues info = new ContentValues();
        info.put(DBHelper.KEY_MAIL,email.getText().toString());
        info.put(DBHelper.KEY_NAME,name.getText().toString());
        info.put(DBHelper.KEY_NICKNAME,nickname.getText().toString());
        info.put(DBHelper.KEY_PASS,pass.getText().toString());
        return info;
    }
    private static class HTTPReqTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            ContentValues info = getInfo();
            Log.d(MSG, "rquest test");
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL("http://10.0.2.2:3000/userRegistration?name=" + info.get(DBHelper.KEY_NAME).toString() + "&nickname=" +
                        info.get(DBHelper.KEY_NICKNAME).toString() + "&email=" + info.get(DBHelper.KEY_MAIL).toString() + "&password=" + info.get(DBHelper.KEY_PASS).toString());
                Log.d(MSG, "rquest test1");
                urlConnection = (HttpURLConnection) url.openConnection();
                Log.d(MSG, "rquest test2");
                int code = urlConnection.getResponseCode();
                Log.d(MSG, Integer.toString(code));
                if (code !=  200) {
                    Log.d(MSG, "invalid");
                    throw new IOException("Invalid response from server: " + code);

                }
                Log.d(MSG, "rquest test");

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