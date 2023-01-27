package com.example.pralhad.dailyexpneses.activity

import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.pralhad.dailyexpneses.R
import android.os.Bundle
import com.example.pralhad.dailyexpneses.backend_sync_service.AdapterDataAsyncTask
import android.text.Html
import com.example.pralhad.dailyexpneses.activity.MainActivity
import com.example.pralhad.dailyexpneses.fragment.Dashboard
import com.example.pralhad.dailyexpneses.data_source.MainDataSource
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import com.example.pralhad.dailyexpneses.activity.UserSignUp
import com.example.pralhad.dailyexpneses.fragment.Profile
import com.example.pralhad.dailyexpneses.fragment.Tab
import com.example.pralhad.dailyexpneses.general.Constants

//import android.support.annotation.NonNull;
//import android.support.design.widget.BottomNavigationView;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v7.app.ActionBar;
//import android.support.v7.app.AppCompatActivity;
class MainActivity : AppCompatActivity() {
    private var actionBar: ActionBar? = null
    var mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val dashboard: String
        val expenses: String
        val account: String
        val profile: String
        dashboard = resources.getString(R.string.title_dashboard)
        expenses = resources.getString(R.string.title_expenses)
        account = resources.getString(R.string.title_account)
        profile = resources.getString(R.string.title_profile)
        val args = Bundle()
        var fragment: Fragment = Dashboard()
        when (item.itemId) {
            R.id.navigation_dashboard -> {
                args.putByte(Tab.TAB_TYPE_KEY, AdapterDataAsyncTask.DATA_TYPE_DASHBOARD)
                actionBar!!.setTitle(Html.fromHtml("<b><font color='#FFFFFF'>" + dataSource!!.sPref.userName + "</b></font>"))
                fragment.arguments = args
                loadFragment(fragment, Constants.FRAGMENT_DASHBOARD)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_expenses -> {
                args.putByte(Tab.TAB_TYPE_KEY, AdapterDataAsyncTask.DATA_TYPE_EXPENSES)
                actionBar!!.setTitle(Html.fromHtml("<font color='#FFFFFF'>$expenses</font>"))
                fragment = Tab()
                fragment.setArguments(args)
                loadFragment(fragment, Constants.FRAGMENT_EXPENSES)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_account -> {
                args.putByte(Tab.TAB_TYPE_KEY, AdapterDataAsyncTask.DATA_TYPE_TRANSACTION)
                actionBar!!.setTitle(Html.fromHtml("<font color='#FFFFFF'>$account</font>"))
                fragment = Tab()
                fragment.setArguments(args)
                loadFragment(fragment, Constants.FRAGMENT_ACCOUNT)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                args.putByte(Tab.TAB_TYPE_KEY, AdapterDataAsyncTask.DATA_TYPE_PROFILE)
                actionBar!!.setTitle(Html.fromHtml("<font color='#FFFFFF'>$profile</font>"))
                fragment = Profile()
                fragment.setArguments(args)
                loadFragment(fragment, Constants.FRAGMENT_PROFILE)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataSource = MainDataSource(this)
        dataSource!!.open()
        if (dataSource!!.sPref.userContact == null && dataSource!!.sPref.userPassword == null) {
            startActivity(Intent(this@MainActivity, UserSignUp::class.java))
            finish()
            return
        } else setContentView(R.layout.activity_main)
        navigation = findViewById<View>(R.id.navigation) as BottomNavigationView
        navigation!!.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        if (savedInstanceState == null) loadFragment(Dashboard(), Constants.FRAGMENT_DASHBOARD)
        actionBar = supportActionBar
        if (actionBar != null) {
            actionBar!!.title = Html.fromHtml("<b><font color='#FFFFFF'>" + dataSource!!.sPref.userName + "</b></font>")
            actionBar!!.setDisplayShowHomeEnabled(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        invalidateOptionsMenu()
        menu.findItem(R.id.action_settings).isVisible = true
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }

    private fun loadFragment(fragment: Fragment, FRAGMENT_TAG: String) {
        // load fragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container, fragment, FRAGMENT_TAG)
        transaction.commit()
    }

    companion object {
        @JvmField
        var dataSource: MainDataSource? = null
        @JvmField
        var navigation: BottomNavigationView? = null
    }
}