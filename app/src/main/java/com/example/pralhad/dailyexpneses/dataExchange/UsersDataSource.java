package com.example.pralhad.dailyexpneses.dataExchange;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.util.Log;

import com.example.pralhad.dailyexpneses.project_db.MainDataSource;
import com.example.pralhad.dailyexpneses.project_db.UserExpenseDB;

import java.util.Map;


public class UsersDataSource {
    private MainDataSource dataSource;

    public UsersDataSource(MainDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public long createUser(Map<String, String> usersData) {
        ContentValues values = new ContentValues();
        setUserValue(values, usersData);
//        showData();
        return dataSource.insert(UserExpenseDB.USER_TABLE, null, values);
    }

    private void setUserValue(ContentValues values, Map<String, String> usersData) {
        values.put(UserExpenseDB.USER_PASSWORD, usersData.get("usrPassword"));
        values.put(UserExpenseDB.IS_ACTIVE, 1);
        values.put(UserExpenseDB.USER_CONTACT, usersData.get("usrContact"));
        values.put(UserExpenseDB.USER_LNAME, usersData.get("usrLastName"));
        values.put(UserExpenseDB.USER_FNAME, usersData.get("usrFirstName"));


    }

    public void showData() {
        Cursor cursor = dataSource.rawQuery("select * from " + UserExpenseDB.TRANSACTION_TABLE, null);
        Log.i("***result", DatabaseUtils.dumpCursorToString(cursor));

    }

    public boolean userAuthentication(String contact, String password) {
        Cursor cursor = dataSource.rawQuery("select " + UserExpenseDB.USER_CONTACT + ", " + UserExpenseDB.USER_PASSWORD + ", " + UserExpenseDB.USER_ID + " from " + UserExpenseDB.USER_TABLE + " where " + UserExpenseDB.USER_CONTACT + " = '" + contact + "' AND " + UserExpenseDB.USER_PASSWORD + " = '" + password + "' AND " + UserExpenseDB.IS_ACTIVE + " =  1 ;", null);

        Log.i("***user Authentication", DatabaseUtils.dumpCursorToString(cursor));
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            dataSource.sPref.setUserId(cursor.getInt(cursor.getColumnIndex(UserExpenseDB.USER_ID)));
            return true;
        }

        return false;
    }

    public boolean userContactRepetition(String contact) {
        Cursor cursor = dataSource.rawQuery("select  count("+ contact +") as asContact from " + UserExpenseDB.USER_TABLE + " where " + UserExpenseDB.USER_CONTACT + " = '" + contact + "' AND " + UserExpenseDB.IS_ACTIVE + " =  1 ;", null);
        cursor.moveToFirst();
        return ((cursor.getInt(cursor.getColumnIndex("asContact")) >= 1));
    }

}
