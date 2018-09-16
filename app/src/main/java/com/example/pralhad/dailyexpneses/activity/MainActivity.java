package com.example.pralhad.dailyexpneses.activity;

import android.content.Intent;
import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.design.widget.BottomNavigationView;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v7.app.ActionBar;
//import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;

import com.example.pralhad.dailyexpneses.R;
import com.example.pralhad.dailyexpneses.extra.BottomNavigationViewBehavior;
import com.example.pralhad.dailyexpneses.fragment.Account;
import com.example.pralhad.dailyexpneses.fragment.Dashboard;
import com.example.pralhad.dailyexpneses.fragment.Expenses;
import com.example.pralhad.dailyexpneses.fragment.Profile;
import com.example.pralhad.dailyexpneses.project_db.MainDataSource;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


public class MainActivity extends AppCompatActivity {
    public static MainDataSource dataSource = null;
    private ActionBar actionBar;
    public static BottomNavigationView navigation;
    private byte TAG_DASHBOAR = 1, TAG_TRANSACTION = 2, TAG_EXPENSES = 3, TAG_PROFILE = 4;

    public BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            String dashboard, expenses, account, profile;
            dashboard = getResources().getString(R.string.title_dashboard);
            expenses = getResources().getString(R.string.title_expenses);
            account = getResources().getString(R.string.title_account);
            profile = getResources().getString(R.string.title_profile);
            Bundle bundle = new Bundle();
            switch (item.getItemId()) {

                case R.id.navigation_dashboard:
                    bundle.putByte("tag_type", TAG_DASHBOAR);
                    actionBar.setTitle(Html.fromHtml("<b><font color='#FFFFFF'>" + dataSource.sPref.getUserName() + "</b></font>"));
                    Fragment fragment = new Dashboard();
                    fragment.setArguments(bundle);
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_expenses:
                    bundle.putByte("tag_type",TAG_TRANSACTION);
                    actionBar.setTitle(Html.fromHtml("<font color='#FFFFFF'>" + expenses + "</font>"));
                    fragment = new Expenses();
                    fragment.setArguments(bundle);
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_account:
                    bundle.putByte("tag_type", TAG_EXPENSES);
                    actionBar.setTitle(Html.fromHtml("<font color='#FFFFFF'>" + account + "</font>"));
                    fragment = new Account();
                    fragment.setArguments(bundle);
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_profile:
                    bundle.putByte("tag_type", TAG_PROFILE);
                    actionBar.setTitle(Html.fromHtml("<font color='#FFFFFF'>" + profile + "</font>"));
                    fragment = new Profile();
                    fragment.setArguments(bundle);
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataSource = new MainDataSource(this);
        dataSource.open();

        if (dataSource.sPref.getUserContact() == null && dataSource.sPref.getUserPassword() == null) {
            startActivity(new Intent(MainActivity.this, UserSignUp.class));
            finish();
            return;
        } else
            setContentView(R.layout.activity_main);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationViewBehavior());

        if (savedInstanceState == null)
            loadFragment(new Dashboard());

        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(Html.fromHtml("<b><font color='#FFFFFF'>" + dataSource.sPref.getUserName() + "</b></font>"));
            actionBar.setDisplayShowHomeEnabled(true);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        invalidateOptionsMenu();
        menu.findItem(R.id.action_settings).setVisible(true);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }

}
