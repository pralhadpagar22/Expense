package com.example.pralhad.dailyexpneses.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.pralhad.dailyexpneses.Adapter.TransactionAdapter;
import com.example.pralhad.dailyexpneses.InterfaceClass.Interface;
import com.example.pralhad.dailyexpneses.R;
import com.example.pralhad.dailyexpneses.activity.MainActivity;
import com.example.pralhad.dailyexpneses.dataExchange.AsyncTaskDataExpTran;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentTransaction;


public class Account extends Fragment implements Interface{
    private byte type;
    private RecyclerView trList;
    private TransactionAdapter transactionAdapter;
    public static String TAG_ACCOUNT = "tag_account";

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
        // Inflate the layout for this fragment
        new AsyncTaskDataExpTran(type, getContext(), this).execute();

        trList = (RecyclerView) root.findViewById(R.id.account_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        trList.setLayoutManager(mLayoutManager);
        trList.setItemAnimator(new DefaultItemAnimator());

        return root;
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
            TransactionsDialog newFragment = TransactionsDialog.newInstance(false, this);
            newFragment.show(ft, TransactionsDialog.TAG_TRANSACTIONS_DIALOG);
            return true;
        }
        if (id == R.id.action_sort){
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
                    } else if (dy < 0 ) {
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
}
