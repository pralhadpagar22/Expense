package com.example.pralhad.dailyexpneses.data_source;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.example.pralhad.dailyexpneses.Interface_class.Interface;
import com.example.pralhad.dailyexpneses.R;
import com.example.pralhad.dailyexpneses.activity.MainActivity;

import java.util.List;

public class AsyncTaskDataExpTran extends AsyncTask<Object, Integer, List> {

    private byte TAG_DASHBOARD = 1;
    private byte TAG_TRANSACTION = 2;
    private byte TAG_EXPENSES = 3;
    private byte type = 0;
    public Interface anInterface;
    private Context context;
    public static ProgressDialog progressDialog;

    public AsyncTaskDataExpTran (byte type, Context context, Interface anInterface) {
        this.type = type;
        this.anInterface = anInterface;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getResources().getString(R.string.msg_loading_dialog));
        this.context = context;
    }

    @Override
    protected List doInBackground(Object... objects) {
        List list = null;
        if (type == TAG_TRANSACTION) {

        } else if (type == TAG_EXPENSES) {
            TransactionsDataSource transactionsDataSource = new TransactionsDataSource(MainActivity.dataSource);
            list = transactionsDataSource.getAllTransaction();

        } else if (type == TAG_DASHBOARD) {

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
        anInterface.showListData(list);

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
