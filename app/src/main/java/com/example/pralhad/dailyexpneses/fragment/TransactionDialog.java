package com.example.pralhad.dailyexpneses.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.pralhad.dailyexpneses.R;
import com.example.pralhad.dailyexpneses.activity.MainActivity;
import com.example.pralhad.dailyexpneses.data_source.TransactionsDataSource;
import com.example.pralhad.dailyexpneses.general.Constants;
import com.example.pralhad.dailyexpneses.general.SharedVariable;
import com.example.pralhad.dailyexpneses.general.Validation;
import com.example.pralhad.dailyexpneses.model_class.Transaction;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;

//import android.support.annotation.NonNull;
//import android.support.v4.app.DialogFragment;

public class TransactionDialog extends DialogFragment implements View.OnClickListener {
    private TextInputEditText transactionAmount;
    private TextInputLayout inputTransactionAmount;
    private String tranAmount, tranDateTime;
    private int transactionType = 1; // 1-income, 2-give(Deele), 3-due, 0-null amount nil.
    private MaterialButton trDateTime;
    private Transaction transaction = null;
    private byte trUpdateORNew; // 0 = new transaction entry, 1 = update transaction.
    private int trId, oldAmount = 0;

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    public static TransactionDialog newInstance(boolean cancelable, Transaction transaction, byte trUpdateORNew) {
        TransactionDialog transactionDialog = new TransactionDialog();
        transactionDialog.setCancelable(cancelable);
        transactionDialog.transaction = transaction;
        transactionDialog.trUpdateORNew = trUpdateORNew;
        if (trUpdateORNew == 1)
            transactionDialog.trId = transaction.getTrId();
        return transactionDialog;
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
        View root = inflater.inflate(R.layout.dialog_transaction_fragment, container, false);

        //available_balance
        TextView availableBalance = (TextView) root.findViewById(R.id.available_balance);
        availableBalance.setText(String.valueOf(MainActivity.dataSource.sPref.getRemainingAmount()));



        transactionAmount = (TextInputEditText) root.findViewById(R.id.transaction_amount);
        inputTransactionAmount = (TextInputLayout) root.findViewById(R.id.input_transaction_amount);

        trDateTime = (MaterialButton) root.findViewById(R.id.tr_date_time);
        trDateTime.setOnClickListener(this);

        Button cancelBtn = (Button) root.findViewById(R.id.cancel_btn);
        Button saveUpdateTransactions = (Button) root.findViewById(R.id.save_update_transactions);
        cancelBtn.setOnClickListener(this);
        saveUpdateTransactions.setOnClickListener(this);

        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("transactionType", transactionType);
        outState.putString("dataTime", (String) trDateTime.getText());
        outState.putByte("trUpdateORNew", trUpdateORNew);
        outState.putInt("trId", trId);
        outState.putInt("oldAmount", oldAmount);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private boolean checkValidation() {
        // check validation.
        if (Validation.amountValidation(tranAmount)) {
            inputTransactionAmount.setErrorEnabled(false);
        } else {
            inputTransactionAmount.setError(getResources().getString(R.string.error_message_last_name));
            SharedVariable.hideKeyboardFrom(transactionAmount, getContext());
            return false;
        }

        if (transactionType == 2 || transactionType == 3) {
            if (!Validation.checkBalanceValidation(Integer.parseInt(tranAmount))) {
                SimpleAlertDialog alertDialog = SimpleAlertDialog.newInstance(R.string.alert_message, getResources().getString(R.string.msg_valid_not_sufficient_amount));
                alertDialog.show(getActivity().getSupportFragmentManager(), Constants.SIMPLE_DIALOG);
                return false;
            }
        }

        if (trUpdateORNew == 1) { //int transactionType, int trAmount, int oldAmount, Activity activity
            int trAmount = Integer.parseInt(transactionAmount.getText().toString());
            if (Validation.checkTrEditAmount(transactionType, trAmount, oldAmount, getActivity(), getActivity().getSupportFragmentManager()))
                return false;
        }

        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel_btn:
                dismiss();
                break;
            case R.id.apply_transactions:
                setTransactionData();
                if (checkValidation()) {
                    if (addUpdateTransaction()) {
                        if (trUpdateORNew == 0)
                            new TransactionsDataSource(MainActivity.dataSource).showData();
                        ((Tab) getFragmentManager().findFragmentByTag(Constants.FRAGMENT_ACCOUNT)).updateUI();// refresh data
                    }
                    dismiss();
                }
                break;
            case R.id.tr_date_time:
                SharedVariable.datePicker(getContext(), trDateTime);
                break;
        }
    }

    private void setTransactionData() {
        // variable initialize
        tranAmount = transactionAmount.getText() + "";
        tranDateTime = trDateTime.getText() + "";

        //due transaction is paid
        if (transactionType == 3 && trUpdateORNew == 1) {
            if (!transactionAmount.getText().toString().isEmpty() && Integer.parseInt(tranAmount) == 0) {
                transactionType = 0;
            }
        }
        //due transaction is edit to same amount.
        if (transactionType == 0 && trUpdateORNew == 1) {
            if (Integer.parseInt(tranAmount) > 0) {
                transactionType = 3;
            }
        }
    }

    private boolean addUpdateTransaction() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_TIME_FORMAT);
        Transaction transaction = new Transaction();
        transaction.setTrId(trId);
        transaction.setTrType(transactionType);
        transaction.setTrAmount(Integer.parseInt(tranAmount));
//        try {
//            Date date = simpleDateFormat.parse(tranDateTime);
//            transaction.setTrDate(java.sql.Timestamp.valueOf(simpleDateFormat.format(date)));
//            if (dueDateTime != null) {
//                date = simpleDateFormat.parse(dueDateTime);
//                transaction.setTrDueDate(java.sql.Timestamp.valueOf(simpleDateFormat.format(date)));
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        TransactionsDataSource transactionsDataSource = new TransactionsDataSource(MainActivity.dataSource);
        if (trUpdateORNew == 0)
            return transactionsDataSource.transactionEntry(transaction);
        else if (trUpdateORNew == 1) {
            return transactionsDataSource.updateTransactions(transaction, oldAmount);
        }
        return false;
    }
}