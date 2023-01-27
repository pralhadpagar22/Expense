package com.example.pralhad.dailyexpneses.data_source;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.util.Log;

import com.example.pralhad.dailyexpneses.activity.MainActivity;
import com.example.pralhad.dailyexpneses.general.Constants;
import com.example.pralhad.dailyexpneses.general.SharedVariable;
import com.example.pralhad.dailyexpneses.model_class.Expense;
import com.example.pralhad.dailyexpneses.model_class.Transaction;
import com.example.pralhad.dailyexpneses.project_db.DBExpenses;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class TransactionsDataSource {
    private MainDataSource dataSource;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_TIME_FORMAT);

    public TransactionsDataSource(MainDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean transactionEntry(Transaction transaction) {
        ContentValues values = setTransactionValue(transaction, 1);
        long trId = dataSource.insert(DBExpenses.TBL_TRANSACTION, null, values);
        setAvailableBalance(transaction.getTrType(), transaction.getTrAmount());
        return trId > 0 && updateExpBalance(transaction.getTrDate().toString());
    }

    private int setAvailableBalance(int trType, int trAmount) {
        int availableBalance;
        if (trType == 1) {
            availableBalance = SharedVariable.addBalance(trAmount);
        } else {
            availableBalance = SharedVariable.subtractionBalance(trAmount);
        }
        return availableBalance;
    }

    private boolean updateExpBalance(String trDate) {
        Expense expense = new Expense();
        try {
            Date date = simpleDateFormat.parse(trDate);
            expense.setExDate(java.sql.Timestamp.valueOf(simpleDateFormat.format(date)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        expense.setExBalance(dataSource.sPref.getRemainingAmount());
        expense.setUserId(MainActivity.dataSource.sPref.getUserId());
        expense.setIsActive((byte) 0);
        return new ExpensesDataSource(dataSource).expensesEntry(expense);
    }

    private ContentValues setTransactionValue(Transaction transactionData, int isActive) {
        ContentValues values = new ContentValues();
        values.put(DBExpenses.TR_TYPE, transactionData.getTrType());
        //values.put(DBExpenses.TR_PERSON, transactionData.getTrPerson());
        values.put(DBExpenses.TR_DATE, simpleDateFormat.format(transactionData.getTrDate()));
        values.put(DBExpenses.TR_AMOUNT, transactionData.getTrAmount());
        //values.put(DBExpenses.TR_DUE_DATE, transactionData.getTrDueDate() != null ? simpleDateFormat.format(transactionData.getTrDueDate()) : "");
        values.put(DBExpenses.IS_ACTIVE, isActive);
        values.put(DBExpenses.USER_ID, String.valueOf(MainActivity.dataSource.sPref.getUserId()));
        return values;
    }

    public void showData() {
        Cursor cursor = dataSource.rawQuery("select * from " + DBExpenses.TBL_TRANSACTION, null);
        Log.i("***result", DatabaseUtils.dumpCursorToString(cursor));
    }

    public List getAllTransaction() {
        Cursor cursor = dataSource.rawQuery("select * from " + DBExpenses.TBL_TRANSACTION + " where " + DBExpenses.IS_ACTIVE + "= 1 " + " ORDER BY " + DBExpenses.TR_ID + " DESC", null);
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
            transaction.setTrId(cursor.getInt(cursor.getColumnIndex(DBExpenses.TR_ID)));
            transaction.setTrType(cursor.getInt(cursor.getColumnIndex(DBExpenses.TR_TYPE)));
            //transaction.setTrPerson(cursor.getString(cursor.getColumnIndex(DBExpenses.TR)));
            try {
                date = simpleDateFormat.parse(cursor.getString(cursor.getColumnIndex(DBExpenses.TR_DATE)));
                transaction.setTrDate(java.sql.Timestamp.valueOf(simpleDateFormat.format(date)));
//                if (cursor.getString(cursor.getColumnIndex(DBExpenses.TR)) != null) {
//                    date = simpleDateFormat.parse(cursor.getString(cursor.getColumnIndex("trDueDate")));
//                    transaction.setTrDueDate(java.sql.Timestamp.valueOf(simpleDateFormat.format(date)));
//                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            transaction.setTrAmount(cursor.getInt(cursor.getColumnIndex("trAmount")));
            transaction.setIsActive((byte) cursor.getInt(cursor.getColumnIndex(DBExpenses.IS_ACTIVE)));
            transactions.add(transaction);
            cursor.moveToNext();
        }
        return transactions;
    }

    // TODO: 17/11/18 for multi delete feture we take ArrayList<Transaction> transactions, multi delete feture is remains.
    public boolean deleteTransactions(ArrayList<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            String whereClause = DBExpenses.TR_ID + " = " + transaction.getTrId();
            int result = dataSource.update(DBExpenses.TBL_TRANSACTION, setTransactionValue(transaction, 0), whereClause, null);
            if (result > 0) {
                if (transaction.getTrType() == 1)
                    SharedVariable.subtractionBalance(transaction.getTrAmount());
                else if (transaction.getTrType() == 2 || transaction.getTrType() == 3)
                    SharedVariable.addBalance(transaction.getTrAmount());
                return updateExpBalance(transaction.getTrDate().toString());
            }
        }
        return false;
    }

    public boolean updateTransactions(Transaction transaction, int oldAmount) {
        String whereClause = DBExpenses.TR_ID + " = " + transaction.getTrId();
        int result = dataSource.update(DBExpenses.TBL_TRANSACTION, setTransactionValue(transaction, 1), whereClause, null);
        if (result > 0) {
            setBalance(transaction, oldAmount);
            updateExpBalance(transaction.getTrDate().toString());
            return true;
        }
        return false;
    }

    private void setBalance(Transaction transaction, int oldAmount) {
        int remainingAmount = 0;
        if (transaction.getTrType() == 1) {
            remainingAmount = MainActivity.dataSource.sPref.getRemainingAmount() - oldAmount;
            remainingAmount = remainingAmount + transaction.getTrAmount();
            dataSource.sPref.setRemainingAmount(remainingAmount);
        } else if (transaction.getTrType() == 2 || transaction.getTrType() == 3) {
            remainingAmount = MainActivity.dataSource.sPref.getRemainingAmount() + oldAmount;
            remainingAmount = remainingAmount - transaction.getTrAmount();

            dataSource.sPref.setRemainingAmount(remainingAmount);
        } else if (transaction.getTrType() == 0) {
            remainingAmount = MainActivity.dataSource.sPref.getRemainingAmount() + oldAmount;
            dataSource.sPref.setRemainingAmount(remainingAmount);
        }
        showData();
    }
}
