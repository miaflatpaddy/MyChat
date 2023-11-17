package com.example.mychat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SupportActivity extends AppCompatActivity {
    private String email = "miaflatpaddy33@gmail.com";
    private TextView subject;
    private TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        subject = (TextView) findViewById(R.id.subjectEditText);
        message = (TextView) findViewById(R.id.msgEditText);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.home:
                Intent intentMain = new Intent(this, MainActivity.class);
                intentMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentMain);
                return true;
            case R.id.logout:
                MainActivity.userID = 0;
                MainActivity.logedIn = false;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void onClick(View view) {

        openEmailClient(email, subject.getText().toString(), message.getText().toString());
    }
    public final void openEmailClient(final String address, final String subject, final String text)
    {
        String URI;
        try {
            URI = ("mailto:" + (address == null ? "" : URLEncoder.encode(address, "utf-8").replace("+", "%20")));
            URI += ("?subject=" + (subject == null ? "" : URLEncoder.encode(subject, "utf-8").replace("+", "%20")));

            URI += ("&body=" + (text == null ? "" : URLEncoder.encode(text, "utf-8").replace("+", "%20")));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        Uri data = Uri.parse(URI);
        intent.setData(data);
        startActivity(intent);
    }
}