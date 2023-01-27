package com.example.pralhad.dailyexpneses.backend_sync_service;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.example.pralhad.dailyexpneses.Interface_class.Interface;
import com.example.pralhad.dailyexpneses.R;
import com.example.pralhad.dailyexpneses.activity.MainActivity;
import com.example.pralhad.dailyexpneses.data_source.TransactionsDataSource;
import com.example.pralhad.dailyexpneses.fragment.Tab;

import java.util.List;

public class AdapterDataAsyncTask extends AsyncTask<Object, Integer, List> {

    public static byte DATA_TYPE_DASHBOARD = 1;
    public static byte DATA_TYPE_EXPENSES = 2;
    public static byte DATA_TYPE_TRANSACTION = 3;
    public static byte DATA_TYPE_PROFILE = 4;

    private byte type = 0;
    public Interface.listData listData;
    private Context context;
    public static ProgressDialog progressDialog;

    public AdapterDataAsyncTask(byte type, Context context, Tab anInterface) {
        this.type = type;
        this.listData = anInterface;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getResources().getString(R.string.msg_loading_dialog));
        this.context = context;
    }

    @Override
    protected List doInBackground(Object... objects) {
        List list = null;
        if (type == DATA_TYPE_DASHBOARD) {

        } else if (type == DATA_TYPE_EXPENSES) {

        } else if (type == DATA_TYPE_TRANSACTION) {
            TransactionsDataSource transactionsDataSource = new TransactionsDataSource(MainActivity.dataSource);
            list = transactionsDataSource.getAllTransaction();
        }
        return list;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(List list) {
        super.onPostExecute(list);
        listData.showListData(list);

        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(List list) {
        super.onCancelled(list);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
}
