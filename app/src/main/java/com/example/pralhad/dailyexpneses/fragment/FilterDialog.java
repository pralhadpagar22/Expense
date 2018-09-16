package com.example.pralhad.dailyexpneses.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pralhad.dailyexpneses.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class FilterDialog extends DialogFragment implements View.OnClickListener {
    private Account account;
    public static String TAG_FILTER_DIALOG = "tag_filter_dialog";

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    public static FilterDialog newInstance(boolean cancelable, Account account) {
        FilterDialog filterDialog = new FilterDialog();
        filterDialog.account = account;
        filterDialog.setCancelable(cancelable);
        return filterDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
            dismiss();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.filter_dialog_fragment, container, false);

        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private boolean checkValidation() {
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel_btn:
                if (getDialog().isShowing())
                    dismiss();
                break;
            case R.id.save_transactions:
                Toast.makeText(getContext(), "Transaction add Successfully", Toast.LENGTH_LONG).show();
                if (account != null)
                    account.updateUI();
        }
        dismiss();
    }
}
