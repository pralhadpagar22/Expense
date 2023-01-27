package com.example.pralhad.dailyexpneses.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pralhad.dailyexpneses.R;
import com.example.pralhad.dailyexpneses.fragment.FilterDialog;
import com.example.pralhad.dailyexpneses.model_class.Transaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionView> implements Filterable {
    public List transactionsList;
    public List filterList;
    private Context context;
    private TransactionFilter transactionFilter;

    @Override
    public Filter getFilter() {
        return transactionFilter;
    }

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
        filterList = new ArrayList<>(transactionsList);
        this.context = context;
        this.transactionFilter = new TransactionFilter();
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
//        holder.prName.setText(transaction.getTrPerson());
        holder.trAmount.setText(String.valueOf(context.getResources().getString(R.string.text_tr_amount_type) + transaction.getTrAmount()));
        holder.trDate.setText(simpleDateFormat.format(transaction.getTrDate()));
        if (transaction.getTrType() == 1) {
            holder.trType.setText(context.getResources().getText(R.string.text_tr_type_income));
            holder.TrTypeBgColor.setImageResource(R.color.color_income);
            holder.trDueDate.setVisibility(View.GONE);
        } else if (transaction.getTrType() == 2) {
            holder.trType.setText(context.getResources().getText(R.string.text_tr_type_given));
            holder.TrTypeBgColor.setImageResource(R.color.color_outgoing);
            holder.trDueDate.setVisibility(View.GONE);
        } else if (transaction.getTrType() == 3) {
            holder.trDueDate.setVisibility(View.VISIBLE);
            holder.trType.setText(context.getResources().getText(R.string.text_tr_type_given_due));
            holder.TrTypeBgColor.setImageResource(R.color.color_due);
//            holder.trDueDate.setText(String.valueOf(context.getResources().getString(R.string.text_tr_due_date) + " " + simpleDateFormat.format(transaction.getTrDueDate())));
        } else if (transaction.getTrType() == 0) {
            holder.trType.setText(context.getResources().getText(R.string.text_tr_type_paid));
            holder.TrTypeBgColor.setImageResource(R.color.color_paid);
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

    private class TransactionFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List filterableList = filterListOfTransaction();

            FilterResults results = new FilterResults();
            if (charSequence != null && !charSequence.toString().isEmpty()) {
                String searchString = charSequence.toString().toLowerCase().trim();

                final List resultList = new ArrayList<>(filterableList.size());

                Transaction filterableTransaction;
                for (Object obj : filterableList) {
                    filterableTransaction = (Transaction) obj;
//                    if (filterableTransaction.getTrPerson().toLowerCase().contains(searchString) || String.valueOf(filterableTransaction.getTrAmount()).contains(searchString))
//                        resultList.add(filterableTransaction);
                }
                results.values = resultList;

            } else results.values = filterableList;

            //Collections.sort((List) results.values, TransactionComparator);//now sort the result as required

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            transactionsList.clear();
            transactionsList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    }

    private List filterListOfTransaction() {
        List filterableList = new ArrayList();
        if (Transaction.FILTER_BY_INCOME == FilterDialog.FILTER_PREFERENCE_TRANSACTION) {
            for (Object obj : filterList) {
                Transaction transaction = (Transaction) obj;
                if (((byte) transaction.getTrType()) == Transaction.TR_TYPE_INCOME)
                    filterableList.add(obj);
            }
        } else if (Transaction.FILTER_BY_GIVE == FilterDialog.FILTER_PREFERENCE_TRANSACTION) {
            for (Object obj : filterList) {
                Transaction transaction = (Transaction) obj;
                if (((byte) transaction.getTrType()) == Transaction.TR_TYPE_GIVE)
                    filterableList.add(obj);
            }
//        } else if (Transaction.FILTER_BY_DUE == FilterDialog.FILTER_PREFERENCE_TRANSACTION) {
//            for (Object obj : filterList) {
//                Transaction transaction = (Transaction) obj;
//                if (((byte) transaction.getTrType()) == Transaction.TR_TYPE_DUE)
//                    filterableList.add(obj);
//            }
//        } else if (Transaction.FILTER_BY_PAID == FilterDialog.FILTER_PREFERENCE_TRANSACTION) {
//            for (Object obj : filterList) {
//                Transaction transaction = (Transaction) obj;
//                if (((byte) transaction.getTrType()) == Transaction.TR_TYPE_PAID)
//                    filterableList.add(obj);
//            }
        } else filterableList = filterList;

        List tempFilterableList = new ArrayList();
        if (FilterDialog.FILTER_BY_FROM_DATE != null && FilterDialog.FILTER_BY_TO_DATE != null) {
            for (Object obj : filterableList) {
                Transaction transaction = (Transaction) obj;
                if (transaction.getTrDate().after(FilterDialog.FILTER_BY_FROM_DATE) && FilterDialog.FILTER_BY_TO_DATE.after(transaction.getTrDate())) {
                    tempFilterableList.add(transaction);
                }
            }
            return tempFilterableList;

        } else if (FilterDialog.FILTER_BY_FROM_DATE != null && FilterDialog.FILTER_BY_TO_DATE == null) {
            for (Object obj : filterableList) {
                Transaction transaction = (Transaction) obj;
                if (transaction.getTrDate().after(FilterDialog.FILTER_BY_FROM_DATE)) {
                    tempFilterableList.add(transaction);
                }
            }
            return tempFilterableList;

        } else if (FilterDialog.FILTER_BY_FROM_DATE == null && FilterDialog.FILTER_BY_TO_DATE != null) {
            for (Object obj : filterableList) {
                Transaction transaction = (Transaction) obj;
                if (FilterDialog.FILTER_BY_TO_DATE.after(transaction.getTrDate())) {
                    tempFilterableList.add(transaction);
                }
            }
            return tempFilterableList;
        }

        return filterableList;
    }

    //if we need then we will reuse this code. this code used for sort list data.

//    public static Comparator<Transaction> TransactionComparator = new Comparator<Transaction>() {
//
//        @Override
//        public int compare(Transaction transaction1, Transaction transaction2) {
////                return SortFilterDialog.SORT_PREFERENCE_ORDER == SortFilterDialog.SORT_ORDER_ASC ? compareWithSortOrder(client1, client2) : compareWithSortOrder(client2, client1);
//            return compareWithSortOrder(transaction1, transaction2);
//        }
//
//        private int compareWithSortOrder(Transaction transaction1, Transaction transaction2) {
//            if (Transaction.FILTER_BY_GIVE == FilterDialog.FILTER_PREFERENCE_TRANSACTION) {
//                return String.valueOf(transaction1.getTrType()).compareTo(String.valueOf(transaction2.getTrType())); //compareToIgnoreCase
////            } else if (SortFilterDialog.SORT_PREFERENCE_CLIENTS == SortFilterDialog.SORT_BY_C_UPDATED_ON) {
////                return client1.getUpdated_on().compareTo(client2.getUpdated_on());
//            }
//            return 0;
//        }
//    };
}

