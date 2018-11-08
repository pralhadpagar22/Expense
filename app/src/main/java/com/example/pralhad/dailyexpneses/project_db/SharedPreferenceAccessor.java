package com.example.pralhad.dailyexpneses.project_db;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ScrollView;

public class SharedPreferenceAccessor {

    private String USER_CONTACT = "user_contact";
    private String USER_PASSWORD = "user_password";
    private String USER_ID = "user_id";
    private String USER_NAME = "user_name";
    private String REMAINING_AMOUNT = "remaining_amount";

    private Context context;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    public SharedPreferenceAccessor(Context context) {
        this.context = context;
        this.sharedPref = ((Activity)context).getPreferences(Context.MODE_PRIVATE);
        this.editor = sharedPref.edit();
    }

    public void setUserContact(String contact) {
        editor.putString(USER_CONTACT, contact);
        editor.commit();
    }

    public void setUserPassword(String password) {
        editor.putString(USER_PASSWORD, password);
        editor.commit();
    }

    public void setUserName(String name) {
        editor.putString(USER_NAME, name);
        editor.commit();
    }

    public String getUserName() {
        return sharedPref.getString(USER_NAME, null);
    }


    public String getUserContact() {
        return sharedPref.getString(USER_CONTACT, null);
    }

    public String getUserPassword() {
        return sharedPref.getString(USER_PASSWORD, null);
    }

    public void setRemainingAmount(int remainingAmount) {
        editor.putInt(REMAINING_AMOUNT, remainingAmount);
        editor.commit();
    }

    public int getRemainingAmount(){
        return sharedPref.getInt(REMAINING_AMOUNT, 0);
    }

    public void setUserId(int userId){
        editor.putInt(USER_ID, userId);
        editor.commit();
    }

    public int getUserId() {
        return sharedPref.getInt(USER_ID, 0);
    }

}
