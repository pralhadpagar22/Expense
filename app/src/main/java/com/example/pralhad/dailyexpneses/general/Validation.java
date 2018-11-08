package com.example.pralhad.dailyexpneses.general;

import android.app.Activity;
//import android.support.design.widget.TextInputLayout;

import com.example.pralhad.dailyexpneses.R;
import com.example.pralhad.dailyexpneses.activity.MainActivity;
import com.example.pralhad.dailyexpneses.data_source.UsersDataSource;
import com.example.pralhad.dailyexpneses.fragment.SimpleDialog;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.fragment.app.FragmentManager;

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
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constant.DATEFORMAT);
            Date datetime;
            try {
                datetime = simpleDateFormat.parse(date);
                java.sql.Timestamp ts = java.sql.Timestamp.valueOf(simpleDateFormat.format(datetime));
                return ts.after(java.sql.Timestamp.valueOf(new SimpleDateFormat(Constant.DATEFORMAT).format(Calendar.getInstance().getTime())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } return true;
    }

    public static boolean checkTrEditAmount(int transactionType, int trAmount, int oldAmount, Activity activity, FragmentManager fm) {
        if (transactionType == 1) {
            int balance = MainActivity.dataSource.sPref.getRemainingAmount() - oldAmount;
            balance = balance + trAmount;
            if (balance < 0) {
                SimpleDialog alertDialog = SimpleDialog.newInstance(activity.getResources().getString(R.string.alert_message), activity.getResources().getString(R.string.msg_valid_edit_remaining_bal), true);
                alertDialog.show(fm, Constant.SIMPLE_DIALOG);
                return true;
            }
        } else if (transactionType == 2 || transactionType == 3) {
            int balance = MainActivity.dataSource.sPref.getRemainingAmount() + oldAmount;
            balance = balance - trAmount;
            if (balance < 0) {
                SimpleDialog alertDialog = SimpleDialog.newInstance(activity.getResources().getString(R.string.alert_message), activity.getResources().getString(R.string.msg_valid_not_sufficient_amount), true);
                alertDialog.show(fm, Constant.SIMPLE_DIALOG);
                return true;
            }
        }
        return false;
    }
}
