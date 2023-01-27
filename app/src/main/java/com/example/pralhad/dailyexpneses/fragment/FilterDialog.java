package com.example.pralhad.dailyexpneses.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.pralhad.dailyexpneses.R;
import com.example.pralhad.dailyexpneses.backend_sync_service.AdapterDataAsyncTask;
import com.example.pralhad.dailyexpneses.general.Constants;
import com.example.pralhad.dailyexpneses.general.SharedVariable;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;

public class FilterDialog extends DialogFragment implements View.OnClickListener {
    private Tab tab;
    private byte tabType;
    private boolean applyFilter = false;
    public static String TAG_FILTER_DIALOG = "tag_filter_dialog";
    public static final byte FILTER_BY_ALL = 1;
    public static final byte FILTER_BY_INCOME = 2;
    public static final byte FILTER_BY_GIVE = 3;
    public static final byte FILTER_BY_GIVE_ON_RETURN_POL = 4;
    public static final byte FILTER_BY_PAID = 5;
    public static java.sql.Date FILTER_BY_FROM_DATE = null;
    public static java.sql.Date FILTER_BY_TO_DATE = null;
    private MaterialButton fromDateBtn, toDateBtn;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);

    //using temp variables to save temp user selection.
    public static byte FILTER_PREFERENCE_TRANSACTION = FILTER_BY_ALL; //default sort is by TRANSACTION. Sort preference for transaction by field
    public static byte FILTER_PREFERENCE_TRANSACTION_TEMP = FILTER_BY_ALL; //default sort is by TRANSACTION. Sort preference for transaction by field


    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    public static FilterDialog newInstance(boolean cancelable, Tab tab, byte tabType) {
        FilterDialog filterDialog = new FilterDialog();
        filterDialog.tab = tab;
        filterDialog.tabType = tabType;
        filterDialog.setCancelable(cancelable);
        return filterDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.filter_dialog_fragment, container, false);

        MaterialButton cancelBtn = root.findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(this);

        MaterialButton applyFilterBtn = root.findViewById(R.id.apply_filter_btn);
        applyFilterBtn.setOnClickListener(this);

        fromDateBtn = root.findViewById(R.id.from_date_btn);
        fromDateBtn.setOnClickListener(this);

        toDateBtn = root.findViewById(R.id.to_date_btn);
        toDateBtn.setOnClickListener(this);


        if (tabType == AdapterDataAsyncTask.DATA_TYPE_TRANSACTION) {
            RadioGroup transactionRG = root.findViewById(R.id.transactionRG);
            switch (FILTER_PREFERENCE_TRANSACTION) {
                case FILTER_BY_ALL:
                    ((RadioButton) transactionRG.findViewById(R.id.rbTransactionAll)).setChecked(true);
                    break;
                case FILTER_BY_INCOME:
                    ((RadioButton) transactionRG.findViewById(R.id.rbIncome)).setChecked(true);
                    break;
                case FILTER_BY_GIVE:
                    ((RadioButton) transactionRG.findViewById(R.id.rbGiveTransaction)).setChecked(true);
                    break;
                case FILTER_BY_GIVE_ON_RETURN_POL:
                    ((RadioButton) transactionRG.findViewById(R.id.rbTransactionOnReturnPolicy)).setChecked(true);
                    break;
                case FILTER_BY_PAID:
                    ((RadioButton) transactionRG.findViewById(R.id.rbPaidTransaction)).setChecked(true);
                    break;
            }
            transactionRG.setOnCheckedChangeListener(rgTransactionListener);
        }

        return root;
    }

    private RadioGroup.OnCheckedChangeListener rgTransactionListener = (group, checkedId) -> {
        applyFilter = true;
        switch (checkedId) {
            case R.id.rbTransactionAll:
                FILTER_PREFERENCE_TRANSACTION_TEMP = FILTER_BY_ALL;
                break;
            case R.id.rbIncome:
                FILTER_PREFERENCE_TRANSACTION_TEMP = FILTER_BY_INCOME;
                break;
            case R.id.rbGiveTransaction:
                FILTER_PREFERENCE_TRANSACTION_TEMP = FILTER_BY_GIVE;
                break;
//            case R.id.rbTransactionOnReturnPolicy:
//                FILTER_PREFERENCE_TRANSACTION_TEMP = FILTER_BY_DUE;
//                break;
            case R.id.rbPaidTransaction:
                FILTER_PREFERENCE_TRANSACTION_TEMP = FILTER_BY_PAID;
                break;
        }
    };

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.cancel_btn:
                dismiss();
                break;

            case R.id.apply_filter_btn:
                Tab tab = null;
                if (tabType == AdapterDataAsyncTask.DATA_TYPE_TRANSACTION && getFragmentManager() != null) {
                    FILTER_PREFERENCE_TRANSACTION = FILTER_PREFERENCE_TRANSACTION_TEMP;
                    tab = (Tab) getFragmentManager().findFragmentByTag(Constants.FRAGMENT_ACCOUNT);
                }
                FILTER_BY_FROM_DATE = setDate(fromDateBtn.getText().toString());
                FILTER_BY_TO_DATE = setDate(toDateBtn.getText().toString());
                Log.i("date From To", "From: " + FILTER_BY_FROM_DATE + " " + "From: " + FILTER_BY_TO_DATE);
                if (tab != null) {
                    tab.onSortFilterValuesPicked(applyFilter);
                    applyFilter = false;
                    dismiss();
                }
                break;

            case R.id.from_date_btn:
                applyFilter = true;
                SharedVariable.datePicker(getContext(), fromDateBtn);
                break;

            case R.id.to_date_btn:
                applyFilter = true;
                SharedVariable.datePicker(getContext(), toDateBtn);
                break;
        }
    }

    public java.sql.Date setDate(String inputDateString) {
        java.sql.Date dateStamp = null;
        try {
            java.util.Date date = simpleDateFormat.parse(inputDateString);
            dateStamp = java.sql.Date.valueOf(simpleDateFormat.format(date));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return dateStamp;
    }
}
