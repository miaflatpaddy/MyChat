package com.example.mychat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.example.mychat.services.UserService;

import org.json.JSONArray;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ChatFragment.OnSelectedButtonListener{
    private static final String MSG = "myLogs: MainActivity";
    private static final String CHANNEL_ID = "1001";
    public static boolean     logedIn = false;
    public static int userID = 0;
    public static String nickname;
    public static String jline;
    private boolean     mIsDynamic = true;
    private Button      usersButton;
    private Button      chatButton;
    private Button      preferencesButton;

    private Fragment                currentFragment = null;
    private UsersFragment           usersFragment = null;
    private ChatFragment            chatFragment = null;
    private PreferencesFragment     preferencesFragment = null;
    final String LOG_TAG_U = "myLogs: UserFragment";




    BroadcastReceiver br;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CreateImage();

        usersButton         = (Button) findViewById(R.id.user_btn);
        chatButton          = (Button) findViewById(R.id.chat_btn);
        preferencesButton   = (Button) findViewById(R.id.preferences_btn);

        usersButton.setOnClickListener(this);
        chatButton.setOnClickListener(this);
        preferencesButton.setOnClickListener(this);

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (mIsDynamic) {
            usersFragment       = new UsersFragment();
            chatFragment        = new ChatFragment();
            preferencesFragment = new PreferencesFragment();

            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.add(R.id.container, chatFragment, "");
            ft.commit();

            currentFragment = chatFragment;
        }

        //hideKeyboardFrom(this);
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }


    public static void LogIn(int _userID,String _nickname){
        logedIn = true;
        userID = _userID;
        nickname = _nickname;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.singin:
                Intent intentSingin = new Intent(this, RegistrationActivity.class);
                startActivity(intentSingin);
                return true;
            case R.id.logout:
                if(logedIn){
                    new HTTPReqStatusTask().execute();
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    userID = 0;
                    logedIn = false;

                }

                return true;
            case R.id.callus:
                Log.d(MSG, "call us");
                Intent intent= new Intent(Intent.ACTION_DIAL, Uri.parse("tel:54745744"));
                startActivity(intent);
                return true;
            case R.id.msgus:
                Log.d(MSG, "msg us");
                Intent intentMsgus = new Intent(this, SupportActivity.class);
                startActivity(intentMsgus);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void showNotification(String title, String message){
        //       Toast.makeText(this, "showNotification", Toast.LENGTH_SHORT).show();
        Log.d(MSG, "showNotification");

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "channel_id_01";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My_Notifications", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            nm.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_center_focus_weak_24)
                //TODO  .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_baseline_camera_alt_24))
                .setWhen(System.currentTimeMillis())
                .setContentTitle(title)
                .setContentText(message);

        Intent notificationIntent = new Intent(this, MainActivity.class);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(contentIntent);
        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        nm.notify(4, notification);
    }

//    private void showNotification1(String title, String message){
//        //       Toast.makeText(this, "showNotification", Toast.LENGTH_SHORT).show();
//        Log.d(MSG, "showNotification");
//
//        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        String NOTIFICATION_CHANNEL_ID = "channel_id_02";
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My_Notifications", NotificationManager.IMPORTANCE_HIGH);
//            notificationChannel.setDescription("Channel description");
//            notificationChannel.enableLights(true);
//            notificationChannel.setLightColor(Color.RED);
//            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
//            notificationChannel.enableVibration(true);
//            nm.createNotificationChannel(notificationChannel);
//        }
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setWhen(System.currentTimeMillis())
//                .setContentTitle(title)
//                .setContentText(message);
//
//        Intent notificationIntent = new Intent(this, AboutActivity.class);
//
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//        builder.setContentIntent(contentIntent);
//
//        Notification notification = builder.build();
//        notification.flags |= Notification.FLAG_AUTO_CANCEL;
//        nm.notify(45, notification);
//    }

//    private Fragment getFragmentByID(FragmentManager fragmentManager, int fragmentId){
//        Fragment fragment =fragmentManager .findFragmentById(fragmentId);
//        return fragment;
//    }

    @Override
    public void onClick(View view) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = null;

        if (view == usersButton) {
            fragment = usersFragment;
        } else
        if (view == chatButton) {
            fragment = chatFragment;
        } else
//        if (view == preferencesButton) {
//            fragment = preferencesFragment;
//        }

        if (fragment == null)
        {
            showNotification("Unknown", "ERROR!!!");
            return;
        }

        if (fragment == currentFragment)
            return;

        currentFragment = fragment;

        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.container, currentFragment, "");
        ft.addToBackStack(null);
        ft.setCustomAnimations(
                android.R.animator.fade_in, android.R.animator.fade_out);
        ft.commit();
    }

    private void SetFont(Button view){
        Typeface myCustomFontBold = Typeface.createFromAsset(getAssets(), "font/fontawesome_webfont.ttf");
        view.setTypeface(myCustomFontBold);
    }

    private void CreateImage(){
        ImageView imageView = (ImageView) findViewById(R.id.image) ;
        String filename = "android_001.png";
        InputStream inputStream = null;
        try{
            inputStream = getApplicationContext().getAssets().open(filename);
            if (inputStream != null) {
                try {
                    Drawable drawable = Drawable.createFromStream(inputStream, null);
                    imageView.setImageDrawable(drawable);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                } catch ( Exception e1){
                    Log.e(MSG, e1.getMessage());
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally {
            try{
                if(inputStream!=null)
                    inputStream.close();
            }
            catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }
    private static class HTTPReqStatusTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Log.d(MSG, "rquest test");
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL("http://10.0.2.2:3000/updateStatus?isOnline=" + 0 + "&id=" + userID);
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

    @Override
    public void onButtonSelected(String userID, String messageStr) {
        showNotification(userID, messageStr);
//        showNotification1("userID", messageStr);
    }

}