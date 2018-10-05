package com.example.pralhad.dailyexpneses.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pralhad.dailyexpneses.R;
import com.example.pralhad.dailyexpneses.activity.MainActivity;
import com.example.pralhad.dailyexpneses.dataExchange.TransactionsDataSource;
import com.example.pralhad.dailyexpneses.extra.Constant;
import com.example.pralhad.dailyexpneses.extra.SharedVariable;
import com.example.pralhad.dailyexpneses.extra.Validation;
import com.github.angads25.toggle.LabeledSwitch;
import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

//import android.support.annotation.NonNull;
//import android.support.v4.app.DialogFragment;

public class TransactionsDialog extends DialogFragment implements View.OnClickListener {
    private TextInputEditText transactionPersonName, transactionAmount;
    private TextInputLayout inputTransactionPersonName, inputTransactionAmount;
    private String tranPersonName, tranAmount, tranDateTime, dataTime = null, dueDateTime = null;
    private int transactionType = 1;
    private MaterialButton trDateTime, trDueDate;
    private Account account;
    public static String TAG_TRANSACTIONS_DIALOG = "tag_transactions_dialog";
    private LabeledSwitch switchReturnMoney, switchInOutTransaction;

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
   public static TransactionsDialog newInstance(boolean cancelable, Account account) {
        TransactionsDialog transactionsDialog = new TransactionsDialog();
        transactionsDialog.account = account;
        transactionsDialog.setCancelable(cancelable);
        return transactionsDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_transactions_fragment, container, false);

        //available_balance
        TextView availableBalance = (TextView) root.findViewById(R.id.available_balance);
        availableBalance.setText(String.valueOf(MainActivity.dataSource.sPref.getRemainingAmount()));


        transactionPersonName = (TextInputEditText) root.findViewById(R.id.transaction_person_name);
        transactionAmount = (TextInputEditText) root.findViewById(R.id.transaction_amount);

        inputTransactionPersonName = (TextInputLayout) root.findViewById(R.id.input_transaction_person_name);
        inputTransactionAmount = (TextInputLayout) root.findViewById(R.id.input_transaction_amount);

        trDateTime = (MaterialButton) root.findViewById(R.id.tr_date_time);
        trDateTime.setOnClickListener(this);

        trDueDate = (MaterialButton) root.findViewById(R.id.tr_due_date);
        trDueDate.setOnClickListener(this);

        Button cancelBtn = (Button) root.findViewById(R.id.cancel_btn);
        Button saveTransactions = (Button) root.findViewById(R.id.save_transactions);
        cancelBtn.setOnClickListener(this);
        saveTransactions.setOnClickListener(this);

        switchInOutTransaction = root.findViewById(R.id.switch_in_out_transaction);
        switchReturnMoney = root.findViewById(R.id.switch_return_money);
        final LinearLayout switchReturnMoneyLl = (LinearLayout) root.findViewById(R.id.switch_return_money_ll);
        final LinearLayout dueDateLl = (LinearLayout) root.findViewById(R.id.due_date_ll);
        LinearLayout[] linearLSetup = {switchReturnMoneyLl, dueDateLl};

        seUpSwitchAndText(savedInstanceState, linearLSetup);
        return root;
    }

    private void seUpSwitchAndText(Bundle savedInstanceState, final LinearLayout[] linearLSetup) {
        switchInOutTransaction.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(LabeledSwitch labeledSwitch, boolean isOn) {
                if (isOn)
                    transactionType = 1;
                else transactionType = 2;
                if (transactionType == 2)
                    linearLSetup[0].setVisibility(View.VISIBLE);
                else
                    linearLSetup[0].setVisibility(View.GONE);
            }
        });

        switchReturnMoney.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(LabeledSwitch labeledSwitch, boolean isOn) {
                if (isOn) {
                    transactionType = 3;
                } else transactionType = 2;

                if (isOn)
                    linearLSetup[1].setVisibility(View.VISIBLE);
                else linearLSetup[1].setVisibility(View.GONE);
            }
        });

        //get variable if application rotate
        if (savedInstanceState != null) {
            transactionType = savedInstanceState.getInt("transactionType");
            if (transactionType == 1)
                switchInOutTransaction.setOn(true);
            else {
                switchInOutTransaction.setOn(false);
                linearLSetup[0].setVisibility(View.VISIBLE);
                if (transactionType == 3) {
                    switchReturnMoney.setOn(true);
                    linearLSetup[1].setVisibility(View.VISIBLE);
                } else switchReturnMoney.setOn(false);
            }

            dataTime = savedInstanceState.getString("dataTime");
            dueDateTime = savedInstanceState.getString("dueDateTime");
        } else
            dataTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        trDateTime.setText(dataTime);
        trDueDate.setText(dueDateTime == null ? getContext().getResources().getString(R.string.text_due_date) : dueDateTime);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("transactionType", transactionType);
        outState.putString("dataTime", (String) trDateTime.getText());
        outState.putString("dueDateTime", transactionType == 1 || transactionType == 2 ? null : (String) trDueDate.getText());
        super.onSaveInstanceState(outState);
    }

    private boolean checkValidation() {

        // check validation.
        if (Validation.nameValidation(tranPersonName)) {
            inputTransactionPersonName.setErrorEnabled(false);
        } else {
            inputTransactionPersonName.setError(getResources().getString(R.string.error_message_first_name));
            SharedVariable.hideKeyboardFrom(transactionPersonName, getContext());
            return false;
        }

        if (Validation.amountValidation(tranAmount)) {
            inputTransactionAmount.setErrorEnabled(false);
        } else {
            inputTransactionAmount.setError(getResources().getString(R.string.error_message_last_name));
            SharedVariable.hideKeyboardFrom(transactionAmount, getContext());
            return false;
        }

        if (transactionType == 2) {
            if (!Validation.checkBalanceValidation(Integer.parseInt(tranAmount))) {
                SimpleDialog alertDialog = SimpleDialog.newInstance(getResources().getString(R.string.alert_message), getResources().getString(R.string.error_valid_not_sufficient_amount), true);
                alertDialog.show(getActivity().getSupportFragmentManager(), Constant.SIMPLE_DIALOG);
                return false;
            }
        }

        if (!Validation.dateIsAfterSetDate(dueDateTime)) {
            SimpleDialog alertDialog = SimpleDialog.newInstance(getResources().getString(R.string.alert_message), getResources().getString(R.string.error_valid_due_date), true);
            alertDialog.show(getActivity().getSupportFragmentManager(), Constant.SIMPLE_DIALOG);
            return false;
        }


        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel_btn:
                if (getDialog().isShowing())
                    dismiss();
                break;
            case R.id.save_transactions:
                setTransactionData();
                if (checkValidation()) {
                    if (addTransaction()) {
                        Toast.makeText(getContext(), "Add Successfully", Toast.LENGTH_LONG).show();
                        new TransactionsDataSource(MainActivity.dataSource).showData();
                        if (account != null)
                            account.updateUI();
                    }
                    dismiss();
                }
                break;
            case R.id.tr_date_time:
                SharedVariable.datePicker(getContext(), trDateTime);
                break;

            case R.id.tr_due_date:
                SharedVariable.datePicker(getContext(), trDueDate);
                break;
        }
    }

    private void setTransactionData() {
        // variable initialize
        tranPersonName = transactionPersonName.getText() + "";
        tranAmount = transactionAmount.getText() + "";
        tranDateTime = trDateTime.getText() + "";
        if (transactionType == 1 || transactionType == 2)
            dueDateTime = null;
        else
            dueDateTime = ((String) trDueDate.getText()).equals(getContext().getResources().getString(R.string.text_due_date)) ? null : (String) trDueDate.getText();
    }

    private boolean addTransaction() {
        Map<String, String> transactionData = new HashMap<>();
        //show pop msg
        transactionData.put("transactionType", String.valueOf(transactionType));
        transactionData.put("tranPersonName", tranPersonName);
        transactionData.put("tranDateTime", tranDateTime);
        transactionData.put("tranAmount", tranAmount);
        transactionData.put("trDueDate", dueDateTime);
        transactionData.put("userId", String.valueOf(MainActivity.dataSource.sPref.getUserId()));
        return new TransactionsDataSource(MainActivity.dataSource).transactionEntry(transactionData);
    }
}
