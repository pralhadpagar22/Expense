package com.example.pralhad.dailyexpneses.data_source;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.util.Log;

import com.example.pralhad.dailyexpneses.activity.MainActivity;
import com.example.pralhad.dailyexpneses.model_class.Expense;
import com.example.pralhad.dailyexpneses.project_db.DBExpenses;

public class ExpensesDataSource {
    private MainDataSource dataSource;

    public ExpensesDataSource(MainDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean expensesEntry(Expense expense) {
        ContentValues values = new ContentValues();
        setExpensesValue(values, expense);
        long expId = dataSource.insert(DBExpenses.TBL_EXPENSES, null, values);
        if (expId > 0)
            return true;
        else return false;


    }

    private void setExpensesValue(ContentValues values, Expense expensesData) {
        values.put(DBExpenses.EX_DATE, expensesData.getExDate().toString());
        //values.put(DBExpenses.EX_PARTICULAR, expensesData.getExParticular());
        values.put(DBExpenses.EX_AMOUNT, expensesData.getExAmount());
       // values.put(DBExpenses.EX_BALANCE, expensesData.getExBalance());
        //values.put(DBExpenses.EX_BILL, expensesData.getExBill());
        values.put(DBExpenses.USER_ID, String.valueOf(MainActivity.dataSource.sPref.getUserId()));
        values.put(DBExpenses.IS_ACTIVE, expensesData.getIsActive());
    }

    public void showData() {
        Cursor cursor = dataSource.rawQuery("select * from " + DBExpenses.TBL_EXPENSES, null);
        Log.i("***result", DatabaseUtils.dumpCursorToString(cursor));
    }


}
