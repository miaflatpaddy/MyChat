package com.example.mychat;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;


public class LoginFragment extends Fragment {
    private static TextView nickname;
    private static TextView pass;
    DBHelper dbHelper;
    SQLiteDatabase db;
    Button login;
    private static String JSONstring;
    private static String MSG = "myLogs: LoginFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        dbHelper = new DBHelper(getContext());
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        nickname = (TextView) rootView.findViewById(R.id.nicknameEditText1);
        pass = (TextView) rootView.findViewById(R.id.passEditText1);
        db = dbHelper.getWritableDatabase();
        login = (Button) rootView.findViewById(R.id.login_btn1);
        login.setOnClickListener(loginClickListener);
        return rootView;
    }
    View.OnClickListener loginClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            long time = 1;

            new HTTPReqTask().execute();
            try {
                TimeUnit.SECONDS.sleep(time);
                if(MainActivity.logedIn){
                    Toast toast =  Toast.makeText(getContext(),"You successfully logged in.",Toast.LENGTH_SHORT);
                    toast.show();
                    new HTTPReqStatusTask().execute();
                } else {
                    Toast toast =  Toast.makeText(getContext(),"Password or nickname is incorrect.",Toast.LENGTH_SHORT);
                    toast.show();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
//

        }
    };
    private static ContentValues getInfo(){
        ContentValues info = new ContentValues();
        info.put(DBHelper.KEY_NICKNAME,nickname.getText().toString());
        info.put(DBHelper.KEY_PASS,pass.getText().toString());
        return info;
    }
    private static class HTTPReqTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Log.d(MSG, "rquest test");
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL("http://10.0.2.2:3000/getUsers");
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
                BufferedReader rd = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream()));
                String line;
                while ((line = rd.readLine()) != null) {
                    Log.i("data", line);
                    Log.d(MSG, line);
                    ContentValues info = getInfo();
                    JSONArray arr = null;
                    JSONArray arr1 = null;

                    arr = new JSONArray(line);

                    arr1 = new JSONArray();
                    for(int i = 0; i < arr.length();i++){
                        if(info.get(DBHelper.KEY_NICKNAME).toString().equals(arr.getJSONObject(i).getString("nickname"))){
                            arr1.put(arr.getJSONObject(i));
                        }
                    }
                    for(int i = 0; i<arr1.length();i++){
                        if(info.get(DBHelper.KEY_PASS).toString().equals(arr1.getJSONObject(i).getString("password"))){
                            MainActivity.LogIn(arr1.getJSONObject(i).getInt("id"), arr1.getJSONObject(i).getString("nickname"));
                        }
                    }
                }

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
    private static class HTTPReqStatusTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Log.d(MSG, "rquest test");
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL("http://10.0.2.2:3000/updateStatus?isOnline=" + 1 + "&id=" + MainActivity.userID);
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