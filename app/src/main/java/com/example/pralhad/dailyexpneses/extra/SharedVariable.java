package com.example.pralhad.dailyexpneses.extra;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
//import android.support.design.widget.TextInputEditText;
//import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.pralhad.dailyexpneses.activity.MainActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class SharedVariable {
    private static String dateTime;

    public static void setErrorMsg(TextInputEditText textInputEditText, TextInputLayout textInputLayout, String errorMsg) {
        textInputLayout.setError(errorMsg);
//        hideKeyboardFrom(textInputEditText);
    }

    public static void hideKeyboardFrom(View view, Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public static int addBalance(int balance) {
        MainActivity.dataSource.sPref.setRemainingAmount(MainActivity.dataSource.sPref.getRemainingAmount() + balance);
        return MainActivity.dataSource.sPref.getRemainingAmount();
    }
    public static int subtractionBalance(int balance) {
        MainActivity.dataSource.sPref.setRemainingAmount(MainActivity.dataSource.sPref.getRemainingAmount() - balance);
        return MainActivity.dataSource.sPref.getRemainingAmount();
    }

    public static String datePicker(final Context context, final MaterialButton setDateTime){
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        dateTime = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        //*************Call Time Picker Here ********************
                        dateTime = timePicker(context, setDateTime);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
        return dateTime;

    }

    private static String timePicker(Context context, final MaterialButton setDateTime){
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        final int mHour = c.get(Calendar.HOUR_OF_DAY);
        final int mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        dateTime = dateTime+" "+hourOfDay + ":" + minute + ":01";
                        setDateTime.setText(dateTime);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
        return dateTime;
    }

}
