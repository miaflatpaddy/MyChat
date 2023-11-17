package com.example.mychat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class RegistrationActivity extends AppCompatActivity {
    private UserInfoFragment registrationFragment = null;
    private LoginFragment loginFragment = null;
    private Fragment                currentFragment = null;
    private boolean     mIsDynamic = true;
    private Button registerButton;
    private Button      loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        FragmentManager fragmentManager = getSupportFragmentManager();

        registerButton = (Button) findViewById(R.id.register_btn);
        loginButton = (Button) findViewById(R.id.login_btn);
        if (mIsDynamic) {
            registrationFragment = new UserInfoFragment();
            loginFragment = new LoginFragment();

            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.add(R.id.container, registrationFragment, "");
            ft.commit();

            currentFragment = registrationFragment;
        }
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

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = null;

        if (view == registerButton) {
            fragment = registrationFragment;
        } else
        if (view == loginButton) {
            fragment = loginFragment;
        } else

        if (fragment == null)
        {
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
}