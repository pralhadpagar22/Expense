package com.example.pralhad.dailyexpneses.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pralhad.dailyexpneses.R;
import com.example.pralhad.dailyexpneses.pojoClass.Transaction;

import java.text.SimpleDateFormat;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.transactionView> {
    private List transactionsList;
    private Context context;

    public class transactionView extends RecyclerView.ViewHolder {
        public TextView prName, trDate, trAmount, trType;

        public transactionView(View view) {
            super(view);
            prName = (TextView) view.findViewById(R.id.pr_name);
            trAmount = (TextView) view.findViewById(R.id.tr_amount);
            trDate = (TextView) view.findViewById(R.id.tr_date);
            trType = (TextView) view.findViewById(R.id.tr_type);
        }
    }


    public TransactionAdapter(List<Transaction> transactionsList, Context context) {
        this.transactionsList = transactionsList;
        this.context = context;
    }

    @Override
    public transactionView onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_transaction, parent, false);
        return new transactionView(itemView);
    }


    @Override
    public void onBindViewHolder(transactionView holder, int position) {
        Transaction transaction = (Transaction) transactionsList.get(position);
        holder.prName.setText(transaction.getTrPerson());
        holder.trAmount.setText(String.valueOf(transaction.getTrAmount()));
        holder.trDate.setText(new SimpleDateFormat("MMM dd yyyy").format(transaction.getTrDate()));
        if (transaction.getTrType() == 1)
            holder.trType.setText(context.getResources().getText(R.string.text_tr_type_in));
        else holder.trType.setText(context.getResources().getText(R.string.text_tr_type_out));

    }

    @Override
    public int getItemCount() {
        return transactionsList.size();
    }
}
