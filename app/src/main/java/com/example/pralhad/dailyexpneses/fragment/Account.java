package com.example.pralhad.dailyexpneses.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.pralhad.dailyexpneses.adapter.TransactionAdapter;
import com.example.pralhad.dailyexpneses.Interface_class.Interface;
import com.example.pralhad.dailyexpneses.R;
import com.example.pralhad.dailyexpneses.activity.MainActivity;
import com.example.pralhad.dailyexpneses.data_source.AsyncTaskDataExpTran;
import com.example.pralhad.dailyexpneses.data_source.TransactionsDataSource;
import com.example.pralhad.dailyexpneses.general.Constant;
import com.example.pralhad.dailyexpneses.general.RecyclerItemTouchHelper;
import com.example.pralhad.dailyexpneses.model_class.Transaction;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentTransaction;


public class Account extends Fragment implements Interface, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    private byte type;
    private RecyclerView trList;
    private TransactionAdapter transactionAdapter;
    private LinearLayout linearLayout;
    public static String TAG_ACCOUNT = "tag_account";
    private boolean isDelete = true; //check transaction item is delete or not.
    private TransactionsDataSource transactionsDataSource = new TransactionsDataSource(MainActivity.dataSource);

    //transaction type 1 = income.
    //transaction type 2 = give money.
    //transaction type 3 = give money on return date policy.
    //transaction type 0 = give money on return date policy is nell.

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            type = getArguments().getByte("tag_type");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_account, container, false);
        linearLayout = root.findViewById(R.id.transaction_view);
        trList = root.findViewById(R.id.account_list);
        setUp();
        return root;
    }

    public void setUp() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        trList.setLayoutManager(mLayoutManager);
        trList.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                ((LinearLayoutManager) mLayoutManager).getOrientation());
        trList.addItemDecoration(dividerItemDecoration);
        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(trList);

        //load Transaction data
        new AsyncTaskDataExpTran(type, getContext(), this).execute();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_add).setVisible(true);
        menu.findItem(R.id.action_sort).setVisible(true);
        menu.findItem(R.id.action_settings).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            // DialogFragment.show() will take care of adding the fragment
            // in a transaction.  We also want to remove any currently showing
            // dialog, so make our own transaction and take care of that here.
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Fragment prev = getFragmentManager().findFragmentByTag("dialog");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);

            // Create and show the dialog.
            TransactionsDialog newFragment = TransactionsDialog.newInstance(false, this, null, (byte) 0);
            newFragment.show(ft, TransactionsDialog.TAG_TRANSACTIONS_DIALOG);
            return true;
        }
        if (id == R.id.action_sort) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Fragment prev = getFragmentManager().findFragmentByTag("dialog");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);

            // Create and show the dialog.
            FilterDialog newFragment = FilterDialog.newInstance(true, this);
            newFragment.show(ft, FilterDialog.TAG_FILTER_DIALOG);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void showListData(List result) {
        if (result != null) {
            transactionAdapter = new TransactionAdapter(result, getContext());
            trList.setAdapter(transactionAdapter);
            trList.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if (dy > 0 && MainActivity.navigation.isShown()) {
                        MainActivity.navigation.setVisibility(View.GONE);
                    } else if (dy < 0) {
                        MainActivity.navigation.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                    super.onScrollStateChanged(recyclerView, newState);
                }
            });
        }
    }

    @Override
    public void updateUI() {
        new AsyncTaskDataExpTran(type, getContext(), this).execute();
    }


    /**
     * callback when recycler view is swiped
     * item will be removed on swiped
     * undo option will be provided in snackbar to restore the item
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (direction == 4)
            deleteItem(viewHolder);
        else if (direction == 8)
            editItem(viewHolder);
    }

    private void editItem(RecyclerView.ViewHolder viewHolder) {
        final Transaction transaction = (Transaction) transactionAdapter.transactionsList.get(viewHolder.getAdapterPosition());
        final int position = viewHolder.getAdapterPosition();
       transactionAdapter.notifyDataSetChanged();

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        TransactionsDialog newFragment = TransactionsDialog.newInstance(false, this, transaction, (byte) 1);
        newFragment.show(ft, TransactionsDialog.TAG_TRANSACTIONS_DIALOG);

    }

    private void deleteItem(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof TransactionAdapter.TransactionView) {
            // backup of removed item for undo purpose
            final Transaction transaction = (Transaction) transactionAdapter.transactionsList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();
            if (checkValidation(transaction))
                return;
            custSnackbar(viewHolder);
        }
    }
    private boolean checkValidation(Transaction transaction) {
        if (transaction.getTrType() == 1) {
            if (MainActivity.dataSource.sPref.getRemainingAmount() - transaction.getTrAmount() < 0) {
                SimpleDialog alertDialog = SimpleDialog.newInstance(getResources().getString(R.string.alert_message), getResources().getString(R.string.msg_valid_restrict_amount_delete), true);
                alertDialog.show(getActivity().getSupportFragmentManager(), Constant.SIMPLE_DIALOG);
                transactionAdapter.notifyDataSetChanged();
                return true;
            }
        }
        return false;
    }

    public void custSnackbar(final RecyclerView.ViewHolder viewHolder) {
        // get the removed item name to display it in snack bar
        final Transaction snacktransaction = ((Transaction) transactionAdapter.transactionsList.get(viewHolder.getAdapterPosition()));
        final int position = viewHolder.getAdapterPosition();
        // remove the item from recycler view
        transactionAdapter.removeItem(position);
        // showing snack bar with Undo option
        Snackbar snackbar = Snackbar.make(linearLayout, snacktransaction.getTrPerson() + getContext().getString(R.string.delete_btn), Snackbar.LENGTH_LONG);
        snackbar.setAction(getContext().getString(R.string.msg_undo), new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // undo is selected, restore the deleted item
                isDelete = false;
                transactionAdapter.restoreItem(snacktransaction, position);
            }
        });
        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.show();
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                if (isDelete) {
                    ArrayList<Transaction> transactions = new ArrayList<>();
                    transactions.add(snacktransaction);
                    transactionsDataSource.deleteTransactions(transactions);
                }
                isDelete = true;
                super.onDismissed(transientBottomBar, event);
            }
        });
    }
}
