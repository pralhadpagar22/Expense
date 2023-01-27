package com.example.pralhad.dailyexpneses.data_source;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.util.Log;

import com.example.pralhad.dailyexpneses.model_class.User;
import com.example.pralhad.dailyexpneses.project_db.DBExpenses;

import java.math.BigInteger;


public class UsersDataSource {
    private MainDataSource dataSource;

    public UsersDataSource(MainDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public User createUser(User usersData) {
        ContentValues values = new ContentValues();
        setUserValue(values, usersData);
        long userId = dataSource.insert(DBExpenses.TBL_USER, null, values);
        if (userId > 0) {
            usersData.setUserId(new BigInteger(String.valueOf(userId)));
            return usersData;
        } else return null;
    }

    private void setUserValue(ContentValues values, User usersData) {
//        values.put(DBExpenses.USER_ID, usersData.getUserId());
        values.put(DBExpenses.USER_NAME, usersData.getUserName());
        values.put(DBExpenses.USER_PHONE, usersData.getUserContact());
        values.put(DBExpenses.USER_EMAIL, usersData.getUserEmail());
        values.put(DBExpenses.USER_PASSWORD, usersData.getUserPassword());
        values.put(DBExpenses.UPDATED_ON, usersData.getUpdated_on().toString());
        values.put(DBExpenses.CREATED_ON, usersData.getCreated_on().toString());
        values.put(DBExpenses.IS_ACTIVE, usersData.getIsActive());
    }

    public void showData() {
        Cursor cursor = dataSource.rawQuery("select * from " + DBExpenses.TBL_USER, null);
        Log.i("***result", DatabaseUtils.dumpCursorToString(cursor));

    }

    public User userAuthentication(User user) {
        Cursor cursor = dataSource.rawQuery("select " + DBExpenses.USER_EMAIL + ", " + DBExpenses.USER_NAME + ", " + DBExpenses.USER_PHONE + ", " +  DBExpenses.USER_PASSWORD + ", " + DBExpenses.USER_ID + " from " + DBExpenses.TBL_USER + " where " + DBExpenses.USER_PHONE + " = '" + user.getUserContact() + "' AND " + DBExpenses.USER_PASSWORD + " = '" + user.getUserPassword() + "' AND " + DBExpenses.IS_ACTIVE + " =  1 ;", null);
        Log.i("***User Authentication", DatabaseUtils.dumpCursorToString(cursor));
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            user.setUserId(new BigInteger(String.valueOf(cursor.getInt(cursor.getColumnIndex(DBExpenses.USER_ID)))));
            user.setUserPassword(cursor.getString(cursor.getColumnIndex(DBExpenses.USER_PASSWORD)));
            user.setUserContact(cursor.getString(cursor.getColumnIndex(DBExpenses.USER_PHONE)));
            user.setUserName(cursor.getString(cursor.getColumnIndex(DBExpenses.USER_NAME)));
            user.setUserEmail(cursor.getString(cursor.getColumnIndex(DBExpenses.USER_EMAIL)));
            return user;
        }
        return null;
    }

    public boolean userContactRepetition(String contact) {
        Cursor cursor = dataSource.rawQuery("select  count(" + contact + ") as asContact from " + DBExpenses.TBL_USER + " where " + DBExpenses.USER_PHONE + " = '" + contact + "' AND " + DBExpenses.IS_ACTIVE + " =  1 ;", null);
        cursor.moveToFirst();
        return ((cursor.getInt(cursor.getColumnIndex("asContact")) >= 1));
    }

    public User getUser(String usrContact, String usrPassword) {
        Cursor cursor = dataSource.rawQuery("select * from " + DBExpenses.TBL_USER + " where " + DBExpenses.USER_PHONE + " = '" + usrContact + "' AND " + DBExpenses.USER_PASSWORD + " = '" + usrPassword + "' AND " + DBExpenses.IS_ACTIVE + " =  1 ;", null);
        if (cursor.getCount() == 1)
            return cursorToUser(cursor);
        return null;
    }

    private User cursorToUser(Cursor cursor) {
        User user = new User();
        cursor.moveToFirst();
        user.setUserId(new BigInteger(cursor.getString(cursor.getColumnIndex(DBExpenses.USER_ID))));
        user.setUserName(cursor.getString(cursor.getColumnIndex(DBExpenses.USER_NAME)));
        user.setUserContact(cursor.getString(cursor.getColumnIndex(DBExpenses.USER_PHONE)));
        user.setUserPassword(cursor.getString(cursor.getColumnIndex(DBExpenses.USER_PASSWORD)));

        return user;
    }

}
