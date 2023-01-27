package com.example.pralhad.dailyexpneses.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Html;

import com.example.pralhad.dailyexpneses.Interface_class.Interface;
import com.example.pralhad.dailyexpneses.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
//import android.support.v4.app.DialogFragment;

public class SimpleAlertDialog extends DialogFragment {
    private Integer title;
    private Object msg;
    private static int alertType;
    private boolean isDismiss;
    private static Interface.DialogActionListener dialogActionListener;

    public static final int ALERT_TYPE_SINGLE_BUTTON = 1;
    public static final int ALERT_TYPE_TWO_BUTTONS = 2;

    public SimpleAlertDialog() {
        // Empty constructor required for DialogFragment
    }

    public static SimpleAlertDialog newInstance(Integer title, Object msg) {
        SimpleAlertDialog simpleAlertDialog = new SimpleAlertDialog();
        simpleAlertDialog.title = title;
        simpleAlertDialog.msg = msg;
        simpleAlertDialog.isDismiss = false;
        simpleAlertDialog.dialogActionListener = null;
        simpleAlertDialog.alertType = ALERT_TYPE_SINGLE_BUTTON;

        return simpleAlertDialog;
    }

    /**
     * Alert type initializer
     */
    public static SimpleAlertDialog newInstance(Integer title, Object msg, Interface.DialogActionListener dialogActionListener, int alertType, boolean isDismiss) {
        SimpleAlertDialog simpleAlertDialog = new SimpleAlertDialog();
        simpleAlertDialog.title = title;
        simpleAlertDialog.msg = msg;
        simpleAlertDialog.isDismiss = isDismiss;
        simpleAlertDialog.dialogActionListener = dialogActionListener;
        simpleAlertDialog.alertType = alertType;

        return simpleAlertDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            isDismiss = savedInstanceState.getBoolean("isDismiss");
            if (isDismiss) dismiss();
            title = savedInstanceState.getInt("title");
            alertType = savedInstanceState.getInt("alertType");

            if (savedInstanceState.getString("msg") != null)
                msg = savedInstanceState.getString("msg");
            else msg = savedInstanceState.getInt("msg");
        }
        setCancelable(false);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("title", title);
        outState.putInt("alertType", alertType);
        outState.putBoolean("isDismiss", isDismiss);
        if (msg instanceof Integer)
            outState.putInt("msg", (Integer) msg);
        else outState.putString("msg", (String) msg);
        super.onSaveInstanceState(outState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (title != null)
            builder.setTitle(title);
        if (alertType == ALERT_TYPE_TWO_BUTTONS) {
            builder.setPositiveButton(getContext().getResources().getString(R.string.text_yes), (dialog, id) -> {
                dismiss();
                if (dialogActionListener != null)
                    dialogActionListener.onDismissDialog(true);
            });
            builder.setNegativeButton(getContext().getResources().getString(R.string.text_no), (dialog, i) -> {
                dialog.dismiss();
                dialogActionListener.onDismissDialog(false);
            });
        } else {
            builder.setPositiveButton(getContext().getResources().getString(R.string.text_ok), (dialog, id) -> {
//                if (finishActivity)
//                    getActivity().finish();
                if (dialogActionListener != null)
                    dialogActionListener.onDismissDialog(false);
                else
                    dialog.dismiss();

            });
        }
        //.setCancelable(isDismiss);//For some reason this isn't working here so doing it in newInstance

        if (msg != null) {
            if (msg instanceof Integer)
                builder.setMessage((Integer) msg);
            else
                builder.setMessage(Html.fromHtml((String) msg));
        }
        // Create the AlertDialog object and return it
        return builder.create();
    }
}


//old code used in old project.
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        setStringTitleMessage();
//        if (getArguments().getBoolean("buttonFlag")) {
//            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    // on success
//                }
//            });
//        } else {
//            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    if (dialog != null) {
//                        dialog.dismiss();
//                    }
//                }
//
//            });
//        }
//
//        return alertDialogBuilder.create();
//    }