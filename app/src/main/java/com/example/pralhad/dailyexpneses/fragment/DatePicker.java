package com.example.pralhad.dailyexpneses.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.pralhad.dailyexpneses.general.SharedVariable;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Pralhad on 12/2/15.
 */
public class DatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private static DatePickerFragmentListener datePickerListener;
    public static byte DATE_TYPE_INDATE = 1;
    public static byte DATE_TYPE_INDUEDATE = 2;
    private static String initialDate;

    public String getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(String initialDate) {
        this.initialDate = initialDate;
    }

    public interface DatePickerFragmentListener {
        public void onDateSet(Date date);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
            dismiss();
    }

    public void setDatePickerListener(DatePickerFragmentListener listener) {
        this.datePickerListener = listener;
    }

    public static DatePicker newInstance(DatePickerFragmentListener listener, String initialDate) {
        DatePicker fragment = new DatePicker();
        fragment.setDatePickerListener(listener);
        fragment.setInitialDate(initialDate);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        if(!getInitialDate().isEmpty()){
            //extract date from the string and then assign to the datepicker
            try {
                c.setTime(SharedVariable.dateFormat.parse(initialDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(android.widget.DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        Date date = c.getTime();

        // Here we call the listener and pass the date back to it.
        datePickerListener.onDateSet(date);
    }
}