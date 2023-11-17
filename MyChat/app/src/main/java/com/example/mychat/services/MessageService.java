package com.example.mychat.services;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.mychat.ChatFragment;
import com.example.mychat.UsersFragment;

public class MessageService extends Service {
    final static String MSG = "myLogs";
    ExecutorService es;
    public static boolean runned = false;
    private static String jline;

    public void onCreate() {
        super.onCreate();
        Log.d(MSG, "MessageService onCreate");
        es = Executors.newFixedThreadPool(2);
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(MSG, "MessageService onDestroy");
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(MSG, "MessageService onStartCommand");


        MyRun mr = new MyRun(startId);
        es.execute(mr);

        return super.onStartCommand(intent, flags, startId);
    }
    private static class HTTPReqTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Log.d(MSG, "rquest test");
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL("http://10.0.2.2:3000/getMessages");
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
                line = rd.readLine();
                jline=line;

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
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(MSG, "onBind");
        return null;
    }
    class MyRun implements Runnable {
        int time = 3;
        int startId;
        public MyRun(int startId) {
            Log.d(MSG, "MyRun#" + startId + " create");
        }

        public void run() {
            Intent intent = new Intent(ChatFragment.BROADCAST_ACTION);
            Log.d(MSG, "MyRun#" + startId + " start");
            try {
                do {
                    TimeUnit.SECONDS.sleep(time);
                    new HTTPReqTask().execute();
                    TimeUnit.SECONDS.sleep(1);
                    intent.putExtra(UsersFragment.PARAM_RESULT, jline);
                    Log.d(MSG,jline);
                    sendBroadcast(intent);
                } while (runned);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            stop();
        }
        void stop() {
            Log.d(MSG, "MyRun#" + startId + " end, stopSelfResult("
                    + startId + ") = " + stopSelfResult(startId));
        }
    }

}