package com.example.pralhad.dailyexpneses.project_db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserExpenseDB extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION_1_0_0  = 1;
    private static final String DATABASE_NAME        = "DailyReport.db";

    //user table
    public static final String USER_TABLE                   = "users";
    public static final String USER_ID                      = "userId";
    public static final String USER_FNAME                   = "userFName";
    public static final String USER_LNAME                   = "userLName";
    public static final String USER_CONTACT                 = "userContact";
    public static final String USER_PASSWORD                = "userPassword";
    public static final String IS_ACTIVE                    = "isActive";

    //user transaction table
    public static final String TRANSACTION_TABLE            = "transactions";
    public static final String TR_ID                        = "trId";
    public static final String TR_TYPE                      = "trType";
    public static final String TR_PERSON                    = "trPerson";
    public static final String TR_DATE                      = "trDate";
    public static final String TR_AMOUNT                    = "trAmount";
    public static final String TR_DUE_DATE                  = "trDueDate";

    //user expenses table
    public static final String EXPENSES_TABLE               = "expenses";
    public static final String EX_ID                        = "exId";
    public static final String EX_DATE                      = "exDate";
    public static final String EX_PARTICULAR                = "exParticular";
    public static final String EX_AMOUNT                    = "exAmount";
    public static final String EX_BALANCE                   = "exBalance";
    public static final String EX_BILL                      = "exBill";

    //user master table
    private static final String CREATE_TABLE_USER = "CREATE TABLE '"
            + USER_TABLE + "'('"                            + USER_ID
            + "' integer primary key AUTOINCREMENT, '"      + USER_FNAME
            + "' varchar(50) NOT NULL, '"                   + USER_LNAME
            + "' varchar(50) NOT NULL, '"                   + USER_CONTACT
            + "' varchar(50) NOT NULL, '"                   + USER_PASSWORD
            + "' text(100) NOT NULL,   '"                   + IS_ACTIVE
            + "' tinyint DEFAULT 1 );";

    //transaction table
    /**Todo transactionType 1 is take money and 2 To give(dene) money and 3 is return condition money **/
    private static final String CREATE_TRANSACTION_TABLE = "CREATE TABLE '"
            + TRANSACTION_TABLE + "'('"                     + TR_ID
            + "' integer primary key AUTOINCREMENT, '"      + TR_TYPE
            + "' tinyint DEFAULT 1, '"                      + TR_PERSON
            + "' varchar(50), '"                            + TR_DATE
            + "' datetime DEFAULT current_timestamp, '"     + TR_AMOUNT
            + "' integer, '"                                + TR_DUE_DATE
            + "' datetime DEFAULT NULL, '"                  + USER_ID
            + "' integer NOT NULL,   '"                     + IS_ACTIVE
            + "' tinyint DEFAULT 1 );";

    // expenses table
    private static final String CREATE_EXPENSES_TABLE = "CREATE TABLE '"
            + EXPENSES_TABLE + "'('"                        + EX_ID
            + "' integer primary key AUTOINCREMENT, '"      + EX_DATE
            + "' datetime DEFAULT current_timestamp, '"     + EX_PARTICULAR
            + "' text NOT NULL, '"                          + EX_AMOUNT
            + "' integer NOT NULL,'"                        + EX_BALANCE
            + "' integer NOT NULL, '"                       + EX_BILL
            + "' text(100) DEFAULT NULL, '"                 + USER_ID
            + "' integer NOT NULL,   '"                     + IS_ACTIVE
            + "' tinyint DEFAULT 1 );";

    public UserExpenseDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION_1_0_0);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TRANSACTION_TABLE);
        db.execSQL(CREATE_EXPENSES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
