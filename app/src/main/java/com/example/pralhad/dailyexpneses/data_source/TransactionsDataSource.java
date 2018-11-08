package com.example.pralhad.dailyexpneses.data_source;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.util.Log;

import com.example.pralhad.dailyexpneses.activity.MainActivity;
import com.example.pralhad.dailyexpneses.general.Constant;
import com.example.pralhad.dailyexpneses.general.SharedVariable;
import com.example.pralhad.dailyexpneses.model_class.Transaction;
import com.example.pralhad.dailyexpneses.project_db.UserExpenseDB;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TransactionsDataSource {
    private MainDataSource dataSource;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constant.DATEFORMAT);

    public TransactionsDataSource(MainDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean transactionEntry(Transaction transaction) {
        ContentValues values  = setTransactionValue(transaction, 1);
        long trId = dataSource.insert(UserExpenseDB.TRANSACTION_TABLE, null, values);
        return trId > 0 && updateExpBalance(transaction.getTrAmount(), transaction.getTrDate() + "", String.valueOf(transaction.getTrType()));

    }

    private boolean updateExpBalance(int balanceAmount, String dateTime, String inOutTransaction) {
        Map<String, String> expensesData = new HashMap<>();
        int availableBalance;
        if (Integer.parseInt(inOutTransaction) == 1) {
            availableBalance = SharedVariable.addBalance(balanceAmount);
        } else {
            availableBalance = SharedVariable.subtractionBalance(balanceAmount);
        }
        expensesData.put("exDate", dateTime);
        expensesData.put("exParticular", "null");
        expensesData.put("exAmount", "null");
        expensesData.put("exBalance", String.valueOf(availableBalance));
        expensesData.put("exBill", "null");
        expensesData.put("userId", String.valueOf(MainActivity.dataSource.sPref.getUserId()));
        return new ExpensesDataSource(dataSource).expensesEntry(expensesData);
    }

    private ContentValues setTransactionValue( Transaction transactionData, int isActive) {
        ContentValues values = new ContentValues();
        values.put(UserExpenseDB.TR_TYPE, transactionData.getTrType());
        values.put(UserExpenseDB.TR_PERSON, transactionData.getTrPerson());
        values.put(UserExpenseDB.TR_DATE, simpleDateFormat.format(transactionData.getTrDate()));
        values.put(UserExpenseDB.TR_AMOUNT, transactionData.getTrAmount());
        values.put(UserExpenseDB.TR_DUE_DATE, transactionData.getTrDueDate() != null ? simpleDateFormat.format(transactionData.getTrDueDate()) : "");
        values.put(UserExpenseDB.IS_ACTIVE, isActive);
        values.put(UserExpenseDB.USER_ID, String.valueOf(MainActivity.dataSource.sPref.getUserId()));
        return  values;
    }

    public void showData() {
        Cursor cursor = dataSource.rawQuery("select * from " + UserExpenseDB.TRANSACTION_TABLE, null);
        Log.i("***result", DatabaseUtils.dumpCursorToString(cursor));
    }

    public List getAllTransaction() {
        Cursor cursor = dataSource.rawQuery("select * from " + UserExpenseDB.TRANSACTION_TABLE + " where " + UserExpenseDB.IS_ACTIVE + "= 1 " + " ORDER BY " + UserExpenseDB.TR_ID + " DESC", null);
        if (cursor.getCount() > 0)
            return cursorToList(cursor);
        else return null;
    }

    private List<Transaction> cursorToList(Cursor cursor) {
        cursor.moveToFirst();
        List<Transaction> transactions = new LinkedList<>();
        while (!cursor.isAfterLast()) {
            Date date;
            Transaction transaction = new Transaction();
            transaction.setTrId(cursor.getInt(cursor.getColumnIndex("trId")));
            transaction.setTrType(cursor.getInt(cursor.getColumnIndex("trType")));
            transaction.setTrPerson(cursor.getString(cursor.getColumnIndex("trPerson")));
            try {
                date = simpleDateFormat.parse(cursor.getString(cursor.getColumnIndex("trDate")));
                transaction.setTrDate(java.sql.Timestamp.valueOf(simpleDateFormat.format(date)));
                if (cursor.getString(cursor.getColumnIndex("trDueDate")) != null) {
                    date = simpleDateFormat.parse(cursor.getString(cursor.getColumnIndex("trDueDate")));
                    transaction.setTrDueDate(java.sql.Timestamp.valueOf(simpleDateFormat.format(date)));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            transaction.setTrAmount(cursor.getInt(cursor.getColumnIndex("trAmount")));
            transactions.add(transaction);
            cursor.moveToNext();
        }
        return transactions;
    }

    public boolean deleteTransactions(ArrayList<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            String whereClause = UserExpenseDB.TR_ID + " = " + transaction.getTrId();
            int result = dataSource.update(UserExpenseDB.TRANSACTION_TABLE, setTransactionValue(transaction, 0), whereClause, null);
            if (result > 0) {
                if (transaction.getTrType() == 1)
                    SharedVariable.subtractionBalance(transaction.getTrAmount());
                else if (transaction.getTrType() == 2 || transaction.getTrType() == 3)
                    SharedVariable.addBalance(transaction.getTrAmount());
            }
        }
        return true;
    }
    public boolean updateTransactions(Transaction transaction) {
        String whereClause = UserExpenseDB.TR_ID + " = " + transaction.getTrId();
        int result = dataSource.update(UserExpenseDB.TRANSACTION_TABLE, setTransactionValue(transaction, 1), whereClause, null);
        return result > 0;
    }
}
