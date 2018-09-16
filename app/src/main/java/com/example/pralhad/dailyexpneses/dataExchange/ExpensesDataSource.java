package com.example.pralhad.dailyexpneses.dataExchange;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.util.Log;

import com.example.pralhad.dailyexpneses.activity.MainActivity;
import com.example.pralhad.dailyexpneses.project_db.MainDataSource;
import com.example.pralhad.dailyexpneses.project_db.UserExpenseDB;

import java.util.List;
import java.util.Map;

public class ExpensesDataSource {
    private MainDataSource dataSource;

    public ExpensesDataSource(MainDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean expensesEntry(Map<String, String> expensesData) {
        ContentValues values = new ContentValues();
        setExpensesValue(values, expensesData);
        long expId = dataSource.insert(UserExpenseDB.EXPENSES_TABLE, null, values);
        if (expId > 0)
            return true;
        else return false;


    }

    private void setExpensesValue(ContentValues values, Map<String, String> expensesData) {
        values.put(UserExpenseDB.EX_DATE, expensesData.get("exDate"));
        values.put(UserExpenseDB.EX_PARTICULAR, expensesData.get("exParticular"));
        values.put(UserExpenseDB.EX_AMOUNT, expensesData.get("exAmount"));
        values.put(UserExpenseDB.EX_BALANCE, expensesData.get("exBalance"));
        values.put(UserExpenseDB.EX_BILL, expensesData.get("exBill"));
        values.put(UserExpenseDB.USER_ID, String.valueOf(MainActivity.dataSource.sPref.getUserId()));
        values.put(UserExpenseDB.IS_ACTIVE, 0);
    }

    public void showData() {
        Cursor cursor = dataSource.rawQuery("select * from " + UserExpenseDB.EXPENSES_TABLE, null);
        Log.i("***result", DatabaseUtils.dumpCursorToString(cursor));
    }


}
