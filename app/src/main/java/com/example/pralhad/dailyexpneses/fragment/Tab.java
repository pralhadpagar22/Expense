package com.example.pralhad.dailyexpneses.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pralhad.dailyexpneses.Interface_class.Interface.DialogActionListener;
import com.example.pralhad.dailyexpneses.Interface_class.Interface.SortDialogListener;
import com.example.pralhad.dailyexpneses.Interface_class.Interface.listData;
import com.example.pralhad.dailyexpneses.Interface_class.Interface.uiUpdate;
import com.example.pralhad.dailyexpneses.R;
import com.example.pralhad.dailyexpneses.activity.MainActivity;
import com.example.pralhad.dailyexpneses.adapter.TransactionAdapter;
import com.example.pralhad.dailyexpneses.backend_sync_service.AdapterDataAsyncTask;
import com.example.pralhad.dailyexpneses.data_source.TransactionsDataSource;
import com.example.pralhad.dailyexpneses.general.Constants;
import com.example.pralhad.dailyexpneses.general.RecyclerItemTouchHelper;
import com.example.pralhad.dailyexpneses.model_class.Transaction;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;


//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentTransaction;


public class Tab extends Fragment implements listData, uiUpdate, DialogActionListener, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener, View.OnClickListener, SortDialogListener {
    private RecyclerView trList;
    private TransactionAdapter transactionAdapter;// TODO: 15/3/19 set object hare and cast according to transaction or account or else.
    private RelativeLayout nothingToDisplay;
    public static final String TAB_TYPE_KEY = "tabType";
    private byte tabType;
    private TransactionsDataSource transactionsDataSource = new TransactionsDataSource(MainActivity.dataSource);
    private Transaction transaction = null;
    private int position;
    private androidx.appcompat.widget.SearchView searchView;
    private MenuItem addItem, sortItem, searchItem;

    //transaction type 1 = income.
    //transaction type 2 = give money.
    //transaction type 3 = give money on return date policy.
    //transaction type 0 = give money on return date policy is nell.

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null)
            tabType = getArguments().getByte(TAB_TYPE_KEY);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_account, container, false);
        nothingToDisplay = root.findViewById(R.id.nothing_to_display);
        trList = root.findViewById(R.id.account_list);
        MaterialButton add = root.findViewById(R.id.add);
        add.setOnClickListener(this);
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
        reloadData();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        searchItem = menu.findItem(R.id.action_search);
        searchItem.setVisible(true);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(textListener);
        searchView.setOnSearchClickListener(onSearchClick);
        searchView.onActionViewCollapsed();
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        addItem = menu.findItem(R.id.action_add).setVisible(true);
        sortItem = menu.findItem(R.id.action_sort).setVisible(true);
        menu.findItem(R.id.action_settings).setVisible(false);
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                addItem.setVisible(true);
                sortItem.setVisible(true);
                return true;
            }
        });
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onPause() {
        searchItem.collapseActionView();
        super.onPause();
    }

    private SearchView.OnQueryTextListener textListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            if (transactionAdapter != null)
                transactionAdapter.getFilter().filter(newText);
            return true;
        }
    };

    private View.OnClickListener onSearchClick = v -> {
        addItem.setVisible(false);
        sortItem.setVisible(false);
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();// TODO: 17/3/19 split add and sort logic into function when transaction or account will come
        if (id == R.id.action_add) {
            TransactionsDialog newFragment = TransactionsDialog.newInstance(false, null, (byte) 0);
            newFragment.show(getFragmentManager().beginTransaction(), Constants.FRAGMENT_TRANSACTIONS_DIALOG);
            return true;
        }
        if (id == R.id.action_sort) {
            FilterDialog newFragment = FilterDialog.newInstance(false, this, tabType);
            newFragment.show(getFragmentManager().beginTransaction(), FilterDialog.TAG_FILTER_DIALOG);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showListData(List result) {
        if (result != null) {
            nothingToDisplay.setVisibility(View.GONE);
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
        } else nothingToDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public void updateUI() {
        reloadData();
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
        transactionAdapter.notifyDataSetChanged();
        TransactionsDialog newFragment = TransactionsDialog.newInstance(false, transaction, (byte) 1);
        if (getFragmentManager() != null)
            newFragment.show(getFragmentManager().beginTransaction(), Constants.FRAGMENT_TRANSACTIONS_DIALOG);

    }

    private void deleteItem(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof TransactionAdapter.TransactionView) {
            // backup of removed item for undo purpose
            position = viewHolder.getAdapterPosition();
            transaction = (Transaction) transactionAdapter.transactionsList.get(position);
            if (checkValidation(transaction))
                return;
            SimpleAlertDialog alertDialog = SimpleAlertDialog.newInstance(R.string.alert_message, getResources().getString(R.string.msg_delete_entry), this, SimpleAlertDialog.ALERT_TYPE_TWO_BUTTONS, true);
            alertDialog.show(getActivity().getSupportFragmentManager(), Constants.SIMPLE_DIALOG);
            transactionAdapter.removeItem(position);
//            custSnackbar(viewHolder);
        }
    }

    private boolean checkValidation(Transaction transaction) {
        if (transaction.getTrType() == 1) {
            if (MainActivity.dataSource.sPref.getRemainingAmount() - transaction.getTrAmount() < 0) {
                SimpleAlertDialog alertDialog = SimpleAlertDialog.newInstance(R.string.alert_message, getResources().getString(R.string.msg_valid_restrict_amount_delete));
                alertDialog.show(getActivity().getSupportFragmentManager(), Constants.SIMPLE_DIALOG);
                transactionAdapter.notifyDataSetChanged();
                return true;
            }
        }
        return false;
    }

    private void reloadData() {
        new AdapterDataAsyncTask(tabType, getContext(), this).execute();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add:
                if (tabType == AdapterDataAsyncTask.DATA_TYPE_TRANSACTION && getActivity() != null) {
                    TransactionDialog newFragment = TransactionDialog.newInstance(false, null, (byte) 0);
                    newFragment.show(getActivity().getSupportFragmentManager().beginTransaction(), Constants.FRAGMENT_TRANSACTIONS_DIALOG);
                    break;
                } else if (tabType == AdapterDataAsyncTask.DATA_TYPE_EXPENSES) {

                }
        }
    }

    @Override
    public void onSortFilterValuesPicked(boolean applyFilter) {
        if (applyFilter) {
            if (tabType == AdapterDataAsyncTask.DATA_TYPE_TRANSACTION)
                transactionAdapter.getFilter().filter(searchView.getQuery());
            else if (tabType == AdapterDataAsyncTask.DATA_TYPE_EXPENSES) {
                //call expenses filter.
            }
        }
    }

    @Override
    public void onDismissDialog(boolean isDelete) {
        if (tabType == AdapterDataAsyncTask.DATA_TYPE_TRANSACTION) {
            if (isDelete) {
                ArrayList<Transaction> transactions = new ArrayList<>();
                transactions.add(transaction);
                transactionsDataSource.deleteTransactions(transactions);
            } else transactionAdapter.restoreItem(transaction, position);
        }
    }

}

//old delete logic using Snackbar.
//    private void custSnackbar(final RecyclerView.ViewHolder viewHolder) {
//        // get the removed item name to display it in snack bar
//        final Transaction snacktransaction = ((Transaction) transactionAdapter.transactionsList.get(viewHolder.getAdapterPosition()));
//        final int position = viewHolder.getAdapterPosition();
//        // remove the item from recycler view
//        transactionAdapter.removeItem(position);
//        // showing snack bar with Undo option
//        Snackbar snackbar = Snackbar.make(relativeLayout, snacktransaction.getTrPerson() + getContext().getString(R.string.btn_delete), Snackbar.LENGTH_LONG);
//        snackbar.setAction(getContext().getString(R.string.msg_undo), view -> {
//            // undo is selected, restore the deleted item
//            isDelete = false;
//            transactionAdapter.restoreItem(snacktransaction, position);
//        });
//        snackbar.setActionTextColor(Color.YELLOW);
//        snackbar.show();
//        snackbar.addCallback(new Snackbar.Callback() {
//            @Override
//            public void onDismissed(Snackbar transientBottomBar, int event) {
//                if (isDelete) {
//                    ArrayList<Transaction> transactions = new ArrayList<>();
//                    transactions.add(snacktransaction);
//
//                    if (transactionAdapter.transactionsList.isEmpty())
//                        nothingToDisplay.setVisibility(View.VISIBLE);
//                }
//                isDelete = true;
//                super.onDismissed(transientBottomBar, event);
//            }
//        });
//    }
