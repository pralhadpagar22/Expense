package com.example.pralhad.dailyexpneses.general;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.FragmentManager;

import com.example.pralhad.dailyexpneses.R;
import com.example.pralhad.dailyexpneses.activity.MainActivity;
import com.example.pralhad.dailyexpneses.data_source.UsersDataSource;
import com.example.pralhad.dailyexpneses.fragment.SimpleAlertDialog;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import android.support.design.widget.TextInputLayout;

public class Validation {

    public static boolean emailValidation(String email) {
         Pattern emailPattern = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
            Matcher matcher = emailPattern.matcher(email);
        return matcher.matches();
    }

    public static boolean contactValidation(String userContact, TextInputLayout inputContact, Activity activity) {
        if (!(userContact.length() > 5 && userContact.length() < 15)) {
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

    public static boolean dateIsAfterSetDate(String date, Context context, FragmentManager fm) {
        SimpleAlertDialog alertDialog;
        if (date != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_TIME_FORMAT);
            Date datetime;
            try {
                datetime = simpleDateFormat.parse(date);
                java.sql.Timestamp ts = java.sql.Timestamp.valueOf(simpleDateFormat.format(datetime));
                if (!ts.after(java.sql.Timestamp.valueOf(new SimpleDateFormat(Constants.DATE_TIME_FORMAT).format(Calendar.getInstance().getTime())))){
                    alertDialog = SimpleAlertDialog.newInstance(R.string.alert_message, context.getResources().getString(R.string.msg_valid_due_date));
                    alertDialog.show(fm, Constants.SIMPLE_DIALOG);
                    return false;
                }else return true;
            } catch (ParseException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            alertDialog = SimpleAlertDialog.newInstance(R.string.alert_message, context.getResources().getString(R.string.msg_valid_due_date_is_empty));
            alertDialog.show(fm, Constants.SIMPLE_DIALOG);
            return false;
        }
    }


    public static boolean checkTrEditAmount(int transactionType, int trAmount, int oldAmount, Activity activity, FragmentManager fm) {
        if (transactionType == 1) {
            int balance = MainActivity.dataSource.sPref.getRemainingAmount() - oldAmount;
            balance = balance + trAmount;
            if (balance < 0) {
                SimpleAlertDialog alertDialog = SimpleAlertDialog.newInstance(R.string.alert_message, activity.getResources().getString(R.string.msg_valid_edit_remaining_bal));
                alertDialog.show(fm, Constants.SIMPLE_DIALOG);
                return true;
            }
        } else if (transactionType == 2 || transactionType == 3) {
            int balance = MainActivity.dataSource.sPref.getRemainingAmount() + oldAmount;
            balance = balance - trAmount;
            if (balance < 0) {
                SimpleAlertDialog alertDialog = SimpleAlertDialog.newInstance(R.string.alert_message, activity.getResources().getString(R.string.msg_valid_not_sufficient_amount));
                alertDialog.show(fm, Constants.SIMPLE_DIALOG);
                return true;
            }
        }
        return false;
    }
}
