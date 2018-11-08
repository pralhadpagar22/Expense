package com.example.pralhad.dailyexpneses.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pralhad.dailyexpneses.R;
import com.example.pralhad.dailyexpneses.model_class.Transaction;

import java.text.SimpleDateFormat;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionView> {
    public List transactionsList;
    private Context context;

    public class TransactionView extends RecyclerView.ViewHolder {
        TextView prName, trDate, trAmount, trType, trDueDate;
        CircleImageView TrTypeBgColor;
        public RelativeLayout viewBackground, viewForeground;

        TransactionView(View view) {
            super(view);
            prName = view.findViewById(R.id.pr_name);
            trAmount = view.findViewById(R.id.tr_amount);
            trDate = view.findViewById(R.id.tr_date);
            trType = view.findViewById(R.id.tr_type);
            trDueDate = view.findViewById(R.id.tr_due_date);
            TrTypeBgColor = view.findViewById(R.id.profile_image);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
        }
    }


    public TransactionAdapter(List<Transaction> transactionsList, Context context) {
        this.transactionsList = transactionsList;
        this.context = context;
    }

    @Override
    public TransactionView onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_transaction, parent, false);
        return new TransactionView(itemView);
    }


    @Override
    public void onBindViewHolder(TransactionView holder, int position) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yy");
        Transaction transaction = (Transaction) transactionsList.get(position);
        holder.prName.setText(transaction.getTrPerson());
        holder.trAmount.setText(String.valueOf(context.getResources().getString(R.string.text_tr_amount_type) + transaction.getTrAmount()));
        holder.trDate.setText(simpleDateFormat.format(transaction.getTrDate()));
        if (transaction.getTrType() == 1) {
            holder.trType.setText(context.getResources().getText(R.string.text_tr_type_income));
            holder.TrTypeBgColor.setImageResource(R.color.color_income_text);
            holder.trDueDate.setVisibility(View.GONE);
        } else if (transaction.getTrType() == 2) {
            holder.trType.setText(context.getResources().getText(R.string.text_tr_type_given));
            holder.TrTypeBgColor.setImageResource(R.color.color_outgoing_text);
            holder.trDueDate.setVisibility(View.GONE);
        } else if (transaction.getTrType() == 3) {
            holder.trDueDate.setVisibility(View.VISIBLE);
            holder.trType.setText(context.getResources().getText(R.string.text_tr_type_given_due));
            holder.TrTypeBgColor.setImageResource(R.color.color_due_text);
            holder.trDueDate.setText(String.valueOf(context.getResources().getString(R.string.text_tr_due_date) + " " + simpleDateFormat.format(transaction.getTrDueDate())));
        } else if (transaction.getTrType() == 0) {
            holder.trType.setText(context.getResources().getText(R.string.text_tr_type_paid));
        }
    }

    @Override
    public int getItemCount() {
        return transactionsList.size();
    }

    public void removeItem(int position) {
        transactionsList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(Transaction transaction, int position) {
        transactionsList.add(position, transaction);
        // notify item added by position
        notifyItemInserted(position);
    }
}
