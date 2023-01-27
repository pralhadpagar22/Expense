package com.example.pralhad.dailyexpneses.general;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.core.graphics.drawable.DrawableCompat;

import com.example.pralhad.dailyexpneses.R;
import com.example.pralhad.dailyexpneses.activity.MainActivity;
import com.google.android.material.button.MaterialButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

//import com.google.android.material.textfield.TextInputEditText;
//import com.google.android.material.textfield.TextInputLayout;

//import android.support.design.widget.TextInputEditText;
//import android.support.design.widget.TextInputLayout;

public class SharedVariable {
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT_LIST);
    private static String dateTime;

//    public static void setErrorMsg(TextInputEditText textInputEditText, TextInputLayout textInputLayout, String errorMsg) {
//        textInputLayout.setError(errorMsg);
////        hideKeyboardFrom(textInputEditText);
//    }

    public static void hideKeyboardFrom(View view, Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null)
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
//
//    public static String datePicker(final Context context, final MaterialButton setDateTime) {
//        // Get Current Date
//        final Calendar c = Calendar.getInstance();
//        int mYear = c.get(Calendar.YEAR);
//        int mMonth = c.get(Calendar.MONTH);
//        int mDay = c.get(Calendar.DAY_OF_MONTH);
//
//        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
//                (view, year, monthOfYear, dayOfMonth) -> {
//
//                    dateTime = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
//                    //*************Call Time Picker Here ********************
//                    dateTime = timePicker(context, setDateTime);
//                }, mYear, mMonth, mDay);
//        datePickerDialog.show();
//        return dateTime;
//
//    }
//
//    private static String timePicker(Context context, final MaterialButton setDateTime) {
//        // Get Current Time
//        final Calendar c = Calendar.getInstance();
//        final int mHour = c.get(Calendar.HOUR_OF_DAY);
//        final int mMinute = c.get(Calendar.MINUTE);
//
//        // Launch Time Picker Dialog
//        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
//                (view, hourOfDay, minute) -> {
//                    dateTime = dateTime + " " + hourOfDay + ":" + minute + ":01";
//                    setDateTime.setText(dateTime);
//                }, mHour, mMinute, false);
//        timePickerDialog.show();
//        return dateTime;
//    }

    public static void datePicker(final Context context, final MaterialButton setDateStringToBtn) {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                (view, year, monthOfYear, dayOfMonth) -> {

                    dateTime = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    //*************Call Time Picker Here ********************
                    DateFormat dateFormat = new SimpleDateFormat("yy-mmm-dd");
                    String strDate = dateFormat.format(dateTime);
                    setDateStringToBtn.setText(strDate);
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
//        return dateTime;

    }

    public static void setOnFocusChangeToET (final EditText textView, final Drawable drawable, Activity activity) {
        textView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean hasfocus) {
                if (hasfocus) {
                    setDrawableColor(drawable, activity.getResources().getColor(R.color.colorAccent), textView);
                } else {
                    setDrawableColor(drawable, Color.BLACK, textView);
                }
            }
        });
    }
    public static void setDrawableColor(Drawable drawable, int color, EditText editText) {
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, color);
        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_ATOP);
        editText.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
    }

    /**
     * hide keyboard
     * @param mActivity
     */
    public static void hideSoftKeyboard(Activity mActivity) {
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * hide keyboard form view
     * @param mActivity
     */
    public static void hideKeyBoardFromView(Activity mActivity) {
        InputMethodManager inputMethodManager = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = mActivity.getCurrentFocus();
        if (view == null) {
            view = new View(mActivity);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
