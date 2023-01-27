package com.example.pralhad.dailyexpneses.project_db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBExpenses extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_APP_VERSION_1_0_0     = 1;
    private static final int DATABASE_APP_VERSION_1_0_5     = 2;
    private static final int DATABASE_APP_VERSION_1_0_6     = 3;
    private static final int DATABASE_APP_VERSION_1_1_4     = 4;
    private static final int DATABASE_APP_VERSION_1_1_5     = 5;
    private static final int DATABASE_APP_VERSION_1_1_6     = 6;
    private static final int DATABASE_APP_VERSION_1_1_7     = 7;
    private static final int DATABASE_VERSION               = DATABASE_APP_VERSION_1_0_0;
    private static final String DATABASE_NAME               = "db_expenses.db";

    //Common filed to all table.
    public static final String UPDATED_ON                   = "updated_on";
    public static final String CREATED_ON                   = "created_on";
    public static final byte   ACTIVE                       = 1;
    public static final byte   INACTIVE                     = 0;
    public static final String IS_ACTIVE                    = "is_active";
    public static final String SYNCID                       = "syncId";

    //User table
    public static final String TBL_USER                     = "tbl_users";
    public static final String OFF_TBL_USER                 = "off_tbl_user";
    public static final String USER_ID                      = "user_id";
    public static final String USER_NAME                    = "user_name";
    public static final String USER_EMAIL                   = "user_email";
    public static final String USER_PHONE                   = "user_phone";
    public static final String USER_PASSWORD                = "user_password";

    //Transaction table
    public static final String TBL_TRANSACTION              = "tbl_transactions";
    public static final String TR_ID                        = "tr_id";
    public static final String TR_TYPE                      = "tr_type";
    public static final String TR_DATE                      = "trDate";
    public static final String TR_AMOUNT                    = "trAmount";

    //Income table
    public static final String TBL_INCOME                   = "tbl_income";
    public static final String INC_ID                       = "inc_id";
    public static final String INC_SOURCE                   = "inc_source";

    //Lender table relation with transaction table.
    public static final String TBL_LENDER                   = "tbl_lender";
    public static final String LEN_ID                       = "len_id";
    public static final String LEN_NAME                     = "len_name";
    public static final String LEN_EMAIL                    = "len_email";
    public static final String LEN_PHONE                    = "len_phone";
    public static final String LEN_DOB                      = "len_dob";
    public static final String LEN_GENDER                   = "len_gender";
    public static final String LEN_PAID_STATUS              = "len_paid_status";
    public static final String LEN_DUE_AMOUNT               = "len_due_amount";
    public static final String LEN_DUE_DATE                 = "trDueDate";

    //Category table relation with expenses table.
    public static final String TBL_EX_CATEGORY              = "tbl_ex_category";
    public static final String EX_CATEGORY_ID               = "ex_category_id";
    public static final String EX_CATEGORY_NAME             = "ex_category_name";

    //Expenses table
    public static final String TBL_EXPENSES                 = "tbl_expenses";
    public static final String OFF_TBL_EXPENSES             = "off_tbl_expenses";
    public static final String EX_ID                        = "ex_id";
    public static final String EX_DESCRIPTION               = "ex_description";
    public static final String EX_DATE                      = "ex_date";
    public static final String EX_AMOUNT                    = "ex_amount";
    public static final String EX_BIL_IMAGE                 = "ex_bil_image";

    //Account table
    public static final String TBL_ACCOUNT                  = "tbl_account";
    public static final String ACC_ID                       = "acc_id";
    public static final String ACC_NAME                     = "acc_name";
    public static final String ACC_AMOUNT                   = "acc_amount";
    public static final String ACC_IS_DEFAULT               = "acc_is_default";

    private final String CREATE_TBL_USER = "CREATE TABLE " + TBL_USER + " ( " +
            USER_ID          + " integer primary key AUTOINCREMENT,                                   " +
            USER_NAME        + " varchar(255) NOT NULL,                                               " +
            USER_PHONE       + " bigint(20) NOT NULL,                                                 " +
            USER_EMAIL       + " varchar(255) NOT NULL,                                               " +
            USER_PASSWORD    + " varchar(255) NOT NULL,                                               " +
            UPDATED_ON       + " timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,                        " +
            CREATED_ON       + " timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,                        " +
            IS_ACTIVE        + " tinyint(1) NOT NULL DEFAULT '1' )";

    private final String CREATE_TBL_EXPENSE = "CREATE TABLE " + TBL_EXPENSES + "( " +
            EX_ID            + " bigint(20) primary key,                                              " +
            USER_ID          + " bigint(20) NOT NULL,                                                 " +
            EX_CATEGORY_ID   + " int(11) NOT NULL,                                                    " +
            EX_DESCRIPTION   + " text NOT NULL,                                                       " +
            EX_DATE          + " datetime DEFAULT NULL,                                               " +
            EX_AMOUNT        + " bigint(11) NOT NULL,                                                 " +
            EX_BIL_IMAGE     + " varchar(255),                                                        " +
            UPDATED_ON       + " timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,                        " +
            CREATED_ON       + " timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,                        " +
            IS_ACTIVE        + " tinyint(1) NOT NULL DEFAULT '1' )";

//    private final String CREATE_OFF_TBL_EXPENSE = "CREATE TABLE " + OFF_TBL_EXPENSES + "( " +
//            EX_ID            + " bigint(20) primary key,                                              " +
//            USER_ID          + " bigint(20) NOT NULL,                                                 " +
//            EX_CATEGORY_ID   + " int(11) NOT NULL,                                                    " +
//            EX_DESCRIPTION   + " text NOT NULL,                                                       " +
//            EX_DATE          + " datetime DEFAULT NULL,                                               " +
//            EX_AMOUNT        + " bigint(11) NOT NULL,                                                 " +
//            EX_BIL_IMAGE     + " varchar(255),                                                        " +
//            UPDATED_ON       + " timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,                        " +
//            CREATED_ON       + " timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,                        " +
//            IS_ACTIVE        + " tinyint(1) NOT NULL DEFAULT '1',                                     " +
//            SYNCID           + " text default null);";

    private final String CREATE_TBL_EX_CATEGORY = "CREATE TABLE " + TBL_EX_CATEGORY + " ( " +
            EX_CATEGORY_ID   + " int(11) primary key,                                                 " +
            USER_ID          + " bigint(20) DEFAULT NULL,                                             " +
            EX_CATEGORY_NAME + " varchar(255) NOT NULL,                                               " +
            UPDATED_ON       + " timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,                        " +
            CREATED_ON       + " timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,                        " +
            IS_ACTIVE        + " tinyint(1) NOT NULL DEFAULT '1' )";

    private final String CREATE_TBL_TRANSACTION = "CREATE TABLE " + TBL_TRANSACTION + " (" +
            TR_ID            + " int(11) primary key,                                                 " +
            TR_TYPE          + " int(2) NOT NULL,                                                     " +
            TR_AMOUNT        + " int(11) NOT NULL,                                                    " +
            TR_DATE          + " datetime DEFAULT NULL,                                               " +
            INC_ID           + " int(11) DEFAULT NULL,                                                " +
            LEN_ID           + " int(20) DEFAULT NULL,                                                " +
            USER_ID          + " int(11) NOT NULL,                                                    " +
            EX_ID            + " bigint(20) DEFAULT NULL,                                             " +
            UPDATED_ON       + " timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,                        " +
            CREATED_ON       + " timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,                        " +
            IS_ACTIVE        + " tinyint(1) NOT NULL DEFAULT '1' )";

    private final String CREATE_TBL_INCOME = "CREATE TABLE " + TBL_INCOME + " (" +
            INC_ID           + " int(11) primary key,                                                 " +
            INC_SOURCE       + " varchar(25) NOT NULL )";

    private final String CREATE_TBL_ACCOUNT = "CREATE TABLE " + TBL_ACCOUNT + " ( " +
            ACC_ID           + " int(11) primary key,                                                 " +
            USER_ID          + " bigint(11) NOT NULL,                                                 " +
            ACC_NAME         + " varchar(255) NOT NULL,                                               " +
            ACC_AMOUNT       + " int(11) NOT NULL,                                                    " +
            ACC_IS_DEFAULT   + " tinyint(1) NOT NULL DEFAULT '0',                                     " +
            UPDATED_ON       + " timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,                        " +
            CREATED_ON       + " timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,                        " +
            IS_ACTIVE        + " tinyint(1) NOT NULL DEFAULT '1' );";

    private final String CREATE_TBL_LENDER = "CREATE TABLE " + TBL_LENDER + " ( " +
            LEN_ID           + " bigint(20) primary key,                                              " +
            USER_ID          + " bigint(20) NOT NULL,                                                 " +
            LEN_NAME         + " varchar(255) NOT NULL,                                               " +
            LEN_EMAIL        + " varchar(255) NOT NULL,                                               " +
            LEN_PHONE        + " bigint(20) NOT NULL,                                                 " +
            LEN_DOB          + " datetime DEFAULT NULL,                                               " +
            LEN_GENDER       + " varchar(10) NOT NULL DEFAULT 'M',                                    " +
            LEN_PAID_STATUS  + " varchar(10) NOT NULL DEFAULT 'U',                                    " +
            LEN_DUE_AMOUNT   + " int(11) NOT NULL,                                                    " +
            LEN_DUE_DATE     + " date DEFAULT NULL,                                                   " +
            CREATED_ON       + " timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,                        " +
            UPDATED_ON       + " timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,                        " +
            IS_ACTIVE        + " tinyint(1) NOT NULL DEFAULT '1' )";

    private static final String INSERT_INCOME_SOURCE = "INSERT OR IGNORE INTO `" + TBL_INCOME + "` (`inc_id`, `inc_source`) VALUES " ;

    private void defaultDataOfIncomeTbl(SQLiteDatabase database) {
        String[] defaultDataOfIncome = new String[]{
                INSERT_INCOME_SOURCE + "(1, 'other');" ,
                INSERT_INCOME_SOURCE + "(2, 'salary');" ,
                INSERT_INCOME_SOURCE + "(3, 'business');"};

        for (String defaultDataOfIncomeStr : defaultDataOfIncome) {
            database.execSQL(defaultDataOfIncomeStr);
        }
    }

    public DBExpenses(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TBL_USER);
        db.execSQL(CREATE_TBL_ACCOUNT);
        db.execSQL(CREATE_TBL_TRANSACTION);
        db.execSQL(CREATE_TBL_INCOME);
        defaultDataOfIncomeTbl(db);
        db.execSQL(CREATE_TBL_LENDER);
        db.execSQL(CREATE_TBL_EXPENSE);
       // db.execSQL(CREATE_OFF_TBL_EXPENSE);
        db.execSQL(CREATE_TBL_EX_CATEGORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
