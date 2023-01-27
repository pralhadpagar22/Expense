package com.example.pralhad.dailyexpneses.model_class;

import android.database.Cursor;
import android.util.Log;

import com.example.pralhad.dailyexpneses.project_db.DBExpenses;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by theblitz on 10/29/15.
 */
public class Table {
    public byte                     isActive    ;
    public java.sql.Timestamp       updated_on  ;
    public java.sql.Timestamp       created_on  ;

    public Table(){
        this.isActive = DBExpenses.ACTIVE;
    }

    public Table(Table table) {
        this.isActive = table.isActive;
        this.updated_on = table.updated_on;
        this.created_on = table.created_on;
    }

    public byte getIsActive() {
        return isActive;
    }

    public void setIsActive(byte isActive) {
        this.isActive = isActive;
    }

    public java.sql.Timestamp getUpdated_on() {
        return updated_on;
    }

    public void setUpdated_on(java.sql.Timestamp updated_on) {
        this.updated_on = updated_on;
    }

    public java.sql.Timestamp getCreated_on() {
        return created_on;
    }

    public void setCreated_on(java.sql.Timestamp created_on) {
        this.created_on = created_on;
    }

//    /**
//     * This function is used to show alert message
//     * @param context
//     * @param validationErrors it contains the list of error messages in string format
//     * @param alertType 1. if alertType == ALERT_TYPE_SINGLE_BUTTON -> Show dialog with messages and "OK" button
//     *                  2. if alertType == ALERT_TYPE_TWO_BUTTONS -> Show dialog with messages and "Yes" and "No" buttons
//     * @param alertTitle 1. if alertType == ALERT_TYPE_SINGLE_BUTTON -> null
//     *                   2. if alertType == ALERT_TYPE_TWO_BUTTONS -> Alert Title in String
//     * @param dialogActionListener is an interface object used to get the click listener of "Yes" button
//     *                         in following cases if alertType is ALERT_TYPE_SINGLE_BUTTON pass this parameter as null
//     */
//    public static void showValidationErrors(Context context, List<String> validationErrors, int alertType, Integer alertTitle, final Interfaces.DialogActionListener dialogActionListener) {
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//        String message = "";
//        for (String error:validationErrors) {
//            message += (error + "<br/>");
//        }
//
//        if (context instanceof MainActivity)
//            new SimpleAlertDialog().newInstance(alertTitle, message, dialogActionListener, alertType, false, true).show(((MainActivity) context).getSupportFragmentManager(), Constants.FRAGMENT_SIMPLE_ALERT_DIALOG);
//        else
//            new SimpleAlertDialog().newInstance(alertTitle, message, dialogActionListener, alertType, false, true).show(((invoiceActivity) context).getSupportFragmentManager(), Constants.FRAGMENT_SIMPLE_ALERT_DIALOG);
//    }

    /**
     * These methods can be overridden in respective tables unlike static methods. thus not static
     */
    protected JSONObject cursor_row_to_json(Cursor cursor){
        int totalColumn = cursor.getColumnCount();
        JSONObject rowObject = new JSONObject();
        for (int i = 0; i < totalColumn; i++) {
            if (cursor.getColumnName(i) != null) {
                try {
                    String value = cursor.getString(i);
                    if(value == null)
                        rowObject.put(cursor.getColumnName(i), JSONObject.NULL);
                    else
                        rowObject.put(cursor.getColumnName(i), value);
                } catch (Exception e) {
                    Log.d("ERROR cur2Json", e.getMessage());
                }
            }
        }
        return rowObject;
    }

    /**
     * These methods can be overridden in respective tables unlike static methods. thus not static
     * e.g. CompanyDetails Class
     */
    public JSONObject cursorRowToJson(Cursor cursor) throws JSONException {
        return cursor_row_to_json(cursor);
    }

    /**
     * These methods can be overridden in respective tables unlike static methods. thus not static
     */
    public JSONArray cursorToJson(Cursor cursor) throws JSONException {
        JSONArray resultSet = new JSONArray();
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            JSONObject rowObject = cursorRowToJson(cursor);
            resultSet.put(rowObject);
            cursor.moveToNext();
        }

        return resultSet;
    }
}