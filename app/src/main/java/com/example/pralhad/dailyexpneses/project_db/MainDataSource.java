package com.example.pralhad.dailyexpneses.project_db;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Build;

import java.text.SimpleDateFormat;

public class MainDataSource {

    // Database fields
    public SQLiteDatabase database;
    private UserExpenseDB dbHelper;

    private static SimpleDateFormat dateFormat;
    public SharedPreferenceAccessor sPref;
    private Context context;
//    private ServiceRunner serviceRunner;

    public Context getContext() {
        return context;
    }

//    public class ServiceRunner implements Runnable {
//        private Context context;
//
//        public ServiceRunner(Context context) {
//            this.context = context;
//        }
//
//        @Override
//        public void run() {
//            context.startService(new Intent(context, SyncService.class));
//            Log.v("RAN", "RAN");
//        }
//    }

    public MainDataSource(Context context) {
        dbHelper = new UserExpenseDB(context);
        this.context = context;

//        dateFormat = new SimpleDateFormat(Constants.TIMESTAMP_FORMAT);
//        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        sPref = new SharedPreferenceAccessor(context);
//        serviceRunner = new ServiceRunner(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void openReadable() {
        database = dbHelper.getReadableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public SQLiteStatement compileStatement(String sql) {
        return database.compileStatement(sql);
    }

    public void beginTransaction() {
        database.beginTransaction();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void beginTransactionNonExclusive() {
        database.beginTransactionNonExclusive();
    }

    public void endTransaction() {
        database.endTransaction();
    }

    public void setTransactionSuccessful() {
        database.setTransactionSuccessful();
    }

    public String getCurrentTimeUTC() {
        return dateFormat.format(System.currentTimeMillis());
    }

//    public void setDeviceLastSync(String timestamp) {
//        if (timestamp != null)
//            sPref.setKey(SharedPreferenceAccessor.DEVICE_LAST_SYNC, timestamp); //sPref.setDeviceLastSync(timestamp);
//    }
//
//    public String getDeviceLastSync() {
//        return sPref.getDeviceLastSync();
//    }

    public long insert(String table, String nullColumnHack, ContentValues values) {
        //bookKeeping(values.getAsString(PISQLiteHelper.UPDATED_ON));
        return database.insert(table, nullColumnHack, values);
    }

    /**
     * So far only used in CleanerRunner. SO no need for calling bookKeeping(currentTime);
     * If that is required to be implemented in future, then use SharedVariable.callSync logic
     */
    public int delete(String table, String whereClause, String[] whereArgs) {
        return database.delete(table, whereClause, whereArgs);
    }

//    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
//        bookKeeping(values.getAsString(PISQLiteHelper.UPDATED_ON));
//        return database.update(table, values, whereClause, whereArgs);
//    }

    public Cursor rawQuery(String sql, String[] selectionArgs) {
        return database.rawQuery(sql, selectionArgs);
    }

    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        return database.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

//    /*
//    *   gets syncId from the offline table with given id & table type
//    *   id   : ID to be searched on
//    *   type : 1 - off_clients
//    *          2 - off_products
//    */
//    public Long getSyncIdFromDbOffTable(long id, int type) {
//        Cursor cursor = null;
//
//        if(type == 1)
//            cursor = rawQuery("select syncId from " + PISQLiteHelper.TABLE_OFF_CLIENTS + " where cId = " + id, null, null);
//        else if(type == 2)
//            cursor = rawQuery("select syncId from " + PISQLiteHelper.TABLE_OFF_PRODUCTS + " where pdId = " + id, null, null);
//        else if(type == 3)
//            cursor = rawQuery("select syncId from " + PISQLiteHelper.TABLE_OFF_INVOICES + " where inId = " + id, null, null);
//
//        if(cursor.getCount()!= 0) {
//            cursor.moveToFirst();
//            Long syncId = cursor.getLong(cursor.getColumnIndex(PISQLiteHelper.SYNCID));
//            cursor.close();
//            return syncId;
//        }
//
//        return null;
//    }
//
//    public BigInteger getSyncId(BigInteger id, String tableName) {
//        BigInteger syncId = BigInteger.ZERO;//online table
//        Cursor cursor = null;
//        if (tableName == PISQLiteHelper.TABLE_OFF_INVOICES)
//            cursor = rawQuery("select syncId from " + tableName + " where inId = " + id, null);
//        else if (tableName == PISQLiteHelper.TABLE_OFF_TAXES)
//            cursor = rawQuery("select syncId from " + tableName + " where txId = " + id, null);
//        else if (tableName == PISQLiteHelper.TABLE_OFF_ITEMS)
//            cursor = rawQuery("select syncId from " + tableName + " where itId = " + id, null);
//        else if (tableName == PISQLiteHelper.TABLE_OFF_CLIENTS)
//            cursor = rawQuery("select syncId from " + tableName + " where cId = " + id, null);
//        else if (tableName == PISQLiteHelper.TABLE_OFF_PRODUCTS)
//            cursor = rawQuery("select syncId from " + tableName + " where pdId = " + id, null);
//        else if (tableName == PISQLiteHelper.TABLE_OFF_PAYMENTS)
//            cursor = rawQuery("select syncId from " + tableName + " where pId = " + id, null);
//        else if (tableName == PISQLiteHelper.TABLE_OFF_RECEIPTS)
//            cursor = rawQuery("select syncId from " + tableName + " where rId = " + id, null);
//
//        if (cursor.getCount() != 0) {
//            cursor.moveToFirst();
//            syncId = !cursor.isNull(0) ? new BigInteger(cursor.getString(0)) : null;
//        }
//        cursor.close();
//
//        return syncId;
//    }

    /**
     * SyncService Related
     */
//    public void callSyncService() {
//        if (!SyncServiceAsyncTask.SYNCING)
//            serviceRunner.run();
//        else {
//            SyncService.handler.removeCallbacksAndMessages(null);//remove all other calls, already done nut just in case
//            SyncService.handler.postDelayed(serviceRunner, SyncService.TIMER_DELAY);
//            Log.v("POSTED", "POSTED");
//        }
//    }
//
//    private void bookKeeping(String currentTime) {
//        /**
//         * Regardless of SharedVariables.callSync, setDeviceLastSync is updated as changes are
//         * performed so the DeviceLastSync needs to change.
//         */
//        setDeviceLastSync(currentTime);
//
//        if (SharedVariables.callSync)
//            callSyncService();
//        else
//            SharedVariables.callSync = true;//SharedVariables.callSync
//    }

    /**
     * CleanUp Services
     */
//    public void cleanupEntries() {
//        for (String tableName : Constants.OFFLINE_TABLES) {
//            delete(tableName, PISQLiteHelper.SYNCID + " IS NOT NULL", null);
//        }
//    }

    /**
     * Returns the count of valid/Active entries in the both online and offline table of given tableName
     */
//    public long getCount(String tableName) {
////        BigInteger cmpId = sPref.getUserCmpId();
//        Cursor cursor = rawQuery("select ( select count(*) from " + tableName + " where isActive = 1 and cmpId = " + cmpId + ") " +
//                "+ (select count(*) from off_" + tableName + " where isActive = 1 and cmpId = " + cmpId + " and syncId is NULL) as totalCount", null);
//
//        cursor.moveToFirst();
//        Long count = cursor.getLong(0);
//        // make sure to close the cursor
//        cursor.close();
//        return count;
//    }
//
//    /**
//     * Used for Debugging purpose in this->replaceIdsForIntegrity()
//     */
//    public void printTableData(String tableName) {
//        if (tableName == PISQLiteHelper.TABLE_OFF_ITEMS) {
//            Cursor cursor = rawQuery("select * from " + tableName, null);
//            cursor.moveToFirst();
//            while (!cursor.isAfterLast()) {
//                String inId = cursor.getString(cursor.getColumnIndex(PISQLiteHelper.INID));
//                Log.v("DBROW", inId);
//                cursor.moveToNext();
//            }
//            // make sure to close the cursor
//            cursor.close();
//        }
//    }
//
//    public void printTableData(String tableName, String inTypeId) {
//        if (tableName == PISQLiteHelper.TABLE_OFF_INVOICES || tableName == PISQLiteHelper.TABLE_INVOICES) {
//            Cursor cursor = rawQuery("select * from " + tableName + " where cmpId = 498  AND inTypeId = " + inTypeId + " AND isActive = 1", null);
//            cursor.moveToFirst();
//            do {
//                Log.i("tableName", tableName + " \n\n");
//                Log.i("INID", cursor.getString(0));
//                Log.i("INDATE", cursor.getString(3) + "");
//                Log.i("INDUEDATE", cursor.getLong(4) + "");
//                Log.i("INTYPEID", cursor.getString(5));
//                Log.i("INPAID", cursor.getString(7));
//                Log.i("INDUE", cursor.getString(8));
//                Log.i("INSTAT", cursor.getString(12));
//                Log.i("CMPID", cursor.getString(18));
//                Log.i("ISACTIVE", cursor.getString(19));
//                if (tableName.equals("off_invoices"))
//                    Log.i("SYNCID", cursor.getLong(22) + "");
//                cursor.moveToNext();
//            }
//            while (cursor.moveToNext());
//            // make sure to close the cursor
//            cursor.close();
//        }
//    }
//
//    /**
//     * Consider a situation where the user creates an invoice. It gets InId : 11 in the off_invoices
//     * table. Now before the SyncCall response( delayed for any reasons e.g. slow network, sudden loss of wifi
//     * signal or goes offline) for invoice returns with online invoice ID suppose 34567 for that invoice,
//     * 1. User adds an item/payment/receipt (anything that has inId as a foreign key). it gets InId : 11
//     * as the response hasn't reached us or added in the tables.
//     * 2. How if we try to sync, this will cause an error as the on the server, it will produce foreign key
//     * constraint failure/as inId : 11 is might not be of that user, moreover it is a wrong invoice.
//     * 3. So we fix this issues by by calling at replaceIdsForIntegrity. It is called at two points:
//     * a. After sync response is received, we call replaceIdsForIntegrity and update that item InId
//     * from 11 -> 34567. Similarly for payments and receipts
//     * b. before sending the sync data i.e before getAllUserData - but this case is not required as
//     * it will not happen as either the response will come in updating the inId of invoiceActivity.invoiceData
//     * and if it fails, then we will not have new InId -> 34567 either
//     * or if edited later on i.e Folders-> click edit Invoice, it will by default have the new
//     * inId -> 34567 while entering from Folders
//     */
//    public void replaceIdsForIntegrity() {
//        Cursor cursor = rawQuery("select * from " + PISQLiteHelper.TABLE_OFF_INVOICES + " where " + PISQLiteHelper.SYNCID
//                + " IS NOT NULL", null);
//
//        ContentValues values = new ContentValues();
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            String oldInId = cursor.getString(cursor.getColumnIndex(PISQLiteHelper.INID));
//            String newInId = cursor.getString(cursor.getColumnIndex(PISQLiteHelper.SYNCID));
//
//            values.put(PISQLiteHelper.INID, newInId);
//            for (String tableName : Constants.REPLACE_ID_OFFLINE_TABLES) {
//                //printTableData(tableName);
//                SharedVariables.callSync = false;/** IMP **///DO NOT CALL sync service
//                int rows = update(tableName, values, PISQLiteHelper.INID + " = " + oldInId + " AND "
//                        + PISQLiteHelper.SYNCID + " IS NULL", null);
//                //Log.v("replaceIds", String.valueOf(rows));
//                //printTableData(tableName);
//            }
//
//            cursor.moveToNext();
//        }
//        cursor.close();
//    }
//
//    /**
//     * This function will not be used in future
//     * This is temporary function for data migration from old invoice PDF App to New invoice PDF App
//     * This function is called from ContentSupport -> (Button) missingData
//     * This function is used if the user fills data has not fully synced or some data is missing then this function is used
//     * to delete all new created tables
//     */
//    public void deleteAllTableRows() {
//        String[] allTableName = {PISQLiteHelper.TABLE_CLIENTS, PISQLiteHelper.TABLE_OFF_CLIENTS, PISQLiteHelper.TABLE_COMPANY_DETAILS, PISQLiteHelper.TABLE_USERS,
//                PISQLiteHelper.TABLE_PRODUCTS, PISQLiteHelper.TABLE_OFF_PRODUCTS, PISQLiteHelper.TABLE_INVOICES, PISQLiteHelper.TABLE_OFF_INVOICES, PISQLiteHelper.TABLE_INVOICE_TYPES,
//                PISQLiteHelper.TABLE_ITEMS, PISQLiteHelper.TABLE_OFF_ITEMS, PISQLiteHelper.TABLE_PAYMENTS, PISQLiteHelper.TABLE_OFF_PAYMENTS, PISQLiteHelper.TABLE_RECEIPTS,
//                PISQLiteHelper.TABLE_OFF_RECEIPTS, PISQLiteHelper.TABLE_OFF_TAXES, PISQLiteHelper.TABLE_SETTINGS};
//
//        for (int i = 0; i < allTableName.length; i++) {
//            delete(allTableName[i], null, null);
//        }
//        delete(PISQLiteHelper.TABLE_TAXES, "not txId = 1", null);//we need not delete the first taxes entry
//    }
}