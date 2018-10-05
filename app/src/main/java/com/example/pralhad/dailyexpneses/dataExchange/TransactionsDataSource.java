package com.example.pralhad.dailyexpneses.dataExchange;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.util.Log;

import com.example.pralhad.dailyexpneses.activity.MainActivity;
import com.example.pralhad.dailyexpneses.extra.SharedVariable;
import com.example.pralhad.dailyexpneses.pojoClass.Transaction;
import com.example.pralhad.dailyexpneses.project_db.MainDataSource;
import com.example.pralhad.dailyexpneses.project_db.UserExpenseDB;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TransactionsDataSource {
    private MainDataSource dataSource;

    public TransactionsDataSource(MainDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean transactionEntry(Map<String, String> transactionData) {
        ContentValues values = new ContentValues();
        setTransactionValue(values, transactionData);
        long trId = dataSource.insert(UserExpenseDB.TRANSACTION_TABLE, null, values);
        return trId > 0 && updateExpBalance(Integer.parseInt(transactionData.get("tranAmount")), transactionData.get("tranDateTime"), transactionData.get("transactionType"));

    }

    private boolean updateExpBalance(int balanceAmount, String dateTime, String inOutTransaction) {
        Map<String, String> expensesData = new HashMap<>();
        int availableBalance;
        if (Integer.parseInt(inOutTransaction) == 1) {
            availableBalance = SharedVariable.addBalance(balanceAmount);
        }else {
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

    private void setTransactionValue(ContentValues values, Map<String, String> transactionData) {
        values.put(UserExpenseDB.TR_TYPE, transactionData.get("transactionType"));
        values.put(UserExpenseDB.TR_PERSON, transactionData.get("tranPersonName"));
        values.put(UserExpenseDB.TR_DATE, transactionData.get("tranDateTime"));
        values.put(UserExpenseDB.TR_AMOUNT, transactionData.get("tranAmount"));
        values.put(UserExpenseDB.TR_DUE_DATE, transactionData.get("trDueDate"));
        values.put(UserExpenseDB.IS_ACTIVE, 1);
        values.put(UserExpenseDB.USER_ID, transactionData.get("userId"));
    }

    public void showData() {
        Cursor cursor = dataSource.rawQuery("select * from " + UserExpenseDB.TRANSACTION_TABLE, null);
        Log.i("***result", DatabaseUtils.dumpCursorToString(cursor));
    }

    public List getAllTransaction() {
        Cursor cursor = dataSource.rawQuery("select * from " + UserExpenseDB.TRANSACTION_TABLE + " where " + UserExpenseDB.IS_ACTIVE + "= 1", null);
        if (cursor.getCount() > 0)
           return cursorToList(cursor);
        else return null;
    }

    private List<Transaction> cursorToList(Cursor cursor) {
        cursor.moveToFirst();
        List<Transaction> transactions = new LinkedList<>();
        while (!cursor.isAfterLast()) {
            Transaction transaction = new Transaction();
            transaction.setTrId(cursor.getInt(cursor.getColumnIndex("trId")));
            transaction.setTrType(cursor.getInt(cursor.getColumnIndex("trType")));
            transaction.setTrPerson(cursor.getString(cursor.getColumnIndex("trPerson")));
            transaction.setTrDate(java.sql.Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("trDate"))));
            transaction.setTrAmount(cursor.getInt(cursor.getColumnIndex("trAmount")));
            transactions.add(transaction);
            cursor.moveToNext();
        }
        return transactions;
    }

}
