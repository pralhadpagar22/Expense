package com.example.pralhad.dailyexpneses.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pralhad.dailyexpneses.R;
import com.example.pralhad.dailyexpneses.activity.MainActivity;
import com.example.pralhad.dailyexpneses.data_source.TransactionsDataSource;
import com.example.pralhad.dailyexpneses.general.Constant;
import com.example.pralhad.dailyexpneses.general.LabeledSwitch;
import com.example.pralhad.dailyexpneses.general.SharedVariable;
import com.example.pralhad.dailyexpneses.general.Validation;
import com.example.pralhad.dailyexpneses.model_class.Transaction;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

//import android.support.annotation.NonNull;
//import android.support.v4.app.DialogFragment;

public class TransactionsDialog extends DialogFragment implements View.OnClickListener {
    private TextInputEditText transactionPersonName, transactionAmount;
    private TextInputLayout inputTransactionPersonName, inputTransactionAmount;
    private String tranPersonName, tranAmount, tranDateTime, dataTime = null, dueDateTime = null;
    private int transactionType = 1; // 1-income, 2-give(Deele), 3-due, 0-null amount nil.
    private MaterialButton trDateTime, trDueDate;
    private Account account;
    static String TAG_TRANSACTIONS_DIALOG = "tag_transactions_dialog";
    private LabeledSwitch switchReturnMoney, switchInOutTransaction;
    private Transaction transaction = null;
    private byte trUpdateORNew; // 0 = new transaction entry, 1 = update transaction.
    private int trId, oldAmount = 0;

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    public static TransactionsDialog newInstance(boolean cancelable, Account account, Transaction transaction, byte trUpdateORNew) {
        TransactionsDialog transactionsDialog = new TransactionsDialog();
        transactionsDialog.account = account;
        transactionsDialog.setCancelable(cancelable);
        transactionsDialog.transaction = transaction;
        transactionsDialog.trUpdateORNew = trUpdateORNew;
        if (trUpdateORNew == 1)
            transactionsDialog.trId = transaction.getTrId();
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

        if (savedInstanceState == null) // set transaction object value.
            if (trUpdateORNew == 1 && transaction != null) {
                switchInOutTransaction.setDisableToggle(false);
                switchReturnMoney.setDisableToggle(false);
                oldAmount = transaction.getTrAmount();
                transactionType = transaction.getTrType();
                if (transactionType == 3) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constant.DATEFORMAT);
                    try {
                        Date date = simpleDateFormat.parse(transaction.getTrDueDate().toString());
                        trDueDate.setText(simpleDateFormat.format(date));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

        return root;
    }

    private void setTransactionData(Transaction transaction, LinearLayout[] linearLSetup) {
        //trUpdateORNew is  0 = new transaction entry or 1 = update transaction.
//        if (trUpdateORNew == 1 && transaction == null)
//            dismiss();

        if (transaction != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constant.DATEFORMAT);
            transactionPersonName.setText(transaction.getTrPerson());
            transactionAmount.setText(String.valueOf(transaction.getTrAmount()));
            trDateTime.setText(simpleDateFormat.format(transaction.getTrDate()));
            if (transaction.getTrType() == 1)
                switchInOutTransaction.setOn(true);
            else if (transaction.getTrType() == 2) {
                linearLSetup[0].setVisibility(View.VISIBLE);
                switchInOutTransaction.setOn(false);
            } else if (transaction.getTrType() == 3) {
                linearLSetup[0].setVisibility(View.VISIBLE);
                linearLSetup[1].setVisibility(View.VISIBLE);
                switchReturnMoney.setOn(true);
                trDueDate.setText(simpleDateFormat.format(transaction.getTrDueDate()));
            }
        }
    }

    private void seUpSwitchAndText(Bundle savedInstanceState, final LinearLayout[] linearLSetup) {
        switchInOutTransaction.setOnToggledListener((toggleableView, isOn) -> {
            if (isOn)
                transactionType = 1;
            else transactionType = 2;
            if (transactionType == 2)
                linearLSetup[0].setVisibility(View.VISIBLE);
            else
                linearLSetup[0].setVisibility(View.GONE);
        });
//        switchInOutTransaction.setOnToggledListener(new OnToggledListener() {
//            @Override
//            public void onSwitched(LabeledSwitch labeledSwitch, boolean isOn) {
//                if (trUpdateORNew == 1) {
//                    switchInOutTransaction.setOn(false);
//                } else {
//                    if (isOn)
//                        transactionType = 1;
//                    else transactionType = 2;
//                    if (transactionType == 2)
//                        linearLSetup[0].setVisibility(View.VISIBLE);
//                    else
//                        linearLSetup[0].setVisibility(View.GONE);
//                }
//            }
//        });

//        switchReturnMoney.setOnToggledListener(new OnToggledListener() {
//            @Override
//            public void onSwitched(LabeledSwitch labeledSwitch, boolean isOn) {
//                if (trUpdateORNew == 1) {
//                    switchReturnMoney.setOn(true);
//                } else {
//                    if (isOn) {
//                        transactionType = 3;
//                    } else transactionType = 2;
//                    if (isOn)
//                        linearLSetup[1].setVisibility(View.VISIBLE);
//                    else linearLSetup[1].setVisibility(View.GONE);
//                }
//            }
//        });
        switchReturnMoney.setOnToggledListener((toggleableView, isOn) -> {
            if (isOn) {
                transactionType = 3;
            } else transactionType = 2;
            if (isOn)
                linearLSetup[1].setVisibility(View.VISIBLE);
            else linearLSetup[1].setVisibility(View.GONE);

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
            trUpdateORNew = savedInstanceState.getByte("trUpdateORNew");
            trId = savedInstanceState.getInt("trId");
            oldAmount = savedInstanceState.getInt("oldAmount");
        } else {
            dataTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
            setTransactionData(this.transaction, linearLSetup);
        }
        trDateTime.setText(dataTime);
        trDueDate.setText(dueDateTime == null ? getContext().getResources().getString(R.string.text_due_date) : dueDateTime);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("transactionType", transactionType);
        outState.putString("dataTime", (String) trDateTime.getText());
        outState.putString("dueDateTime", transactionType == 1 || transactionType == 2 ? null : (String) trDueDate.getText());//trDueDate set if transactionType = 3 other wise set null
        outState.putByte("trUpdateORNew", trUpdateORNew);
        outState.putInt("trId", trId);
        outState.putInt("oldAmount", oldAmount);
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

        if (transactionType == 2 || transactionType == 3) {
            if (!Validation.checkBalanceValidation(Integer.parseInt(tranAmount))) {
                SimpleDialog alertDialog = SimpleDialog.newInstance(getResources().getString(R.string.alert_message), getResources().getString(R.string.msg_valid_not_sufficient_amount), true);
                alertDialog.show(getActivity().getSupportFragmentManager(), Constant.SIMPLE_DIALOG);
                return false;
            }
        }

        if (!Validation.dateIsAfterSetDate(dueDateTime)) {
            SimpleDialog alertDialog = SimpleDialog.newInstance(getResources().getString(R.string.alert_message), getResources().getString(R.string.msg_valid_due_date), true);
            alertDialog.show(getActivity().getSupportFragmentManager(), Constant.SIMPLE_DIALOG);
            return false;
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
            case R.id.save_transactions:
                setTransactionData();
                if (checkValidation()) {
                    if (addUpdateTransaction()) {
                        if (trUpdateORNew == 0)
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

    private boolean addUpdateTransaction() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constant.DATEFORMAT);
        Transaction transaction = new Transaction();
        transaction.setTrId(trId);
        transaction.setTrPerson(tranPersonName);
        transaction.setTrType(transactionType);
        transaction.setTrAmount(Integer.parseInt(tranAmount));
        try {
            Date date = simpleDateFormat.parse(tranDateTime);
            transaction.setTrDate(java.sql.Timestamp.valueOf(simpleDateFormat.format(date)));
            if (dueDateTime != null) {
                date = simpleDateFormat.parse(dueDateTime);
                transaction.setTrDueDate(java.sql.Timestamp.valueOf(simpleDateFormat.format(date)));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        TransactionsDataSource transactionsDataSource = new TransactionsDataSource(MainActivity.dataSource);
        if (trUpdateORNew == 0)
            return transactionsDataSource.transactionEntry(transaction);
        else if (trUpdateORNew == 1) {
            return transactionsDataSource.updateTransactions(transaction);
        }
        return false;
    }
}
