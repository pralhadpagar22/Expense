package com.example.pralhad.dailyexpneses.Interface_class;

import java.util.List;

public interface Interface {

    interface listData {
        void showListData(List result);
    }

    interface uiUpdate {
        void updateUI();
    }

    /**
     * Called upon selection of item in the ListValuePicker(clients, products)
     */
    interface DialogActionListener {
        void onDismissDialog(boolean isDelete);
    }

    /**
     * Called upon selection of sort options in sortDialog
     */
    interface SortDialogListener {
        void onSortFilterValuesPicked(boolean applyFilter);
    }
}