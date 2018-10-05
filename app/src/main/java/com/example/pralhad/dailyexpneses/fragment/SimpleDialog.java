package com.example.pralhad.dailyexpneses.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
//import android.support.v4.app.DialogFragment;

public class SimpleDialog extends DialogFragment {
    AlertDialog.Builder alertDialogBuilder;
    public SimpleDialog() {
        // Empty constructor required for DialogFragment
    }

    public static SimpleDialog newInstance(String title, String message, boolean buttonFlag) {
        SimpleDialog frag = new SimpleDialog();
        Bundle args = new Bundle();
        args.putString("message", message);
        args.putString("prName", title);
        args.putBoolean("buttonFlag", buttonFlag);
        frag.setArguments(args);
        return frag;
    }

    private void setStringTitleMessage (){
        String title = getArguments().getString("prName");
        String message = getArguments().getString("message");
        alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setStringTitleMessage();
        if (getArguments().getBoolean("buttonFlag")) {
            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // on success
                }
            });
        } else {
            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }

            });
        }

        return alertDialogBuilder.create();
    }
}
