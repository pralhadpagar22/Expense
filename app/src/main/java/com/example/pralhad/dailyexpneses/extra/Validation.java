package com.example.pralhad.dailyexpneses.extra;

import android.app.Activity;
//import android.support.design.widget.TextInputLayout;

import com.example.pralhad.dailyexpneses.R;
import com.example.pralhad.dailyexpneses.activity.MainActivity;
import com.example.pralhad.dailyexpneses.dataExchange.UsersDataSource;
import com.google.android.material.textfield.TextInputLayout;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Validation {

    public static boolean nameValidation(String str) {
        if (!str.equals("") && str.length() > 2)
            return true;
        else
            return false;
    }

    public static boolean passwordValidation(String password) {
        if (!password.equals("") && password.length() > 6)
            return true;
        else
            return false;
    }

    public static boolean contactValidation(String contact) {
        if (!contact.equals("") && (contact.length() > 9 && contact.length() < 11))
            return true;
        else
            return false;
    }

    public static boolean contactValidation(String userContact, TextInputLayout inputContact, Activity activity) {
        if (!(userContact.length() > 9 && userContact.length() < 11)) {
            inputContact.setError(activity.getResources().getString(R.string.error_message_contact));
            return false;
        } else if (new UsersDataSource(MainActivity.dataSource).userContactRepetition(userContact)) {
            inputContact.setError(activity.getResources().getString(R.string.error_contact_exists));
            return false;
        }
        return true;
    }

    public static boolean amountValidation(String str) {
        if (!str.equals(""))
            return true;
        else
            return false;
    }

    public static boolean checkBalanceValidation(int tranAmount) {
        return (MainActivity.dataSource.sPref.getRemainingAmount() - tranAmount) >= 0;
    }

    public static boolean dateIsAfterSetDate(String date) {
        if (date != null) {
            java.sql.Timestamp ts = java.sql.Timestamp.valueOf(date);
            return ts.after(java.sql.Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime())));
        } else return true;
    }
}
