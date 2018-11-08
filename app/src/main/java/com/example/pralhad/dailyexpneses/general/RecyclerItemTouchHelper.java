package com.example.pralhad.dailyexpneses.general;

import android.graphics.Canvas;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pralhad.dailyexpneses.adapter.TransactionAdapter;
import com.example.pralhad.dailyexpneses.R;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private RecyclerItemTouchHelperListener listener;

    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs, RecyclerItemTouchHelperListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            final View foregroundView = ((TransactionAdapter.TransactionView) viewHolder).viewForeground;

            getDefaultUIUtil().onSelected(foregroundView);
        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {
        final View foregroundView = ((TransactionAdapter.TransactionView) viewHolder).viewForeground;
        getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final View foregroundView = ((TransactionAdapter.TransactionView) viewHolder).viewForeground;
        getDefaultUIUtil().clearView(foregroundView);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        final View backgroundView = ((TransactionAdapter.TransactionView) viewHolder).viewBackground;
        final View foregroundView = ((TransactionAdapter.TransactionView) viewHolder).viewForeground;
        RelativeLayout relativeLayout = backgroundView.findViewById(R.id.view_background);
        ImageView imageView1 = relativeLayout.findViewById(R.id.delete_icon);
        ImageView imageView2 = relativeLayout.findViewById(R.id.edit_icon);
        TextView textView1 = relativeLayout.findViewById(R.id.text_delete);
        TextView textView2 = relativeLayout.findViewById(R.id.text_edit);

        if (dX < 0) {
            backgroundView.setBackgroundResource(android.R.color.holo_red_light);
            imageView2.setVisibility(View.INVISIBLE);
            textView2.setVisibility(View.INVISIBLE);
            imageView1.setVisibility(View.VISIBLE);
            textView1.setVisibility(View.VISIBLE);
        } else if (dX > 0) {
            backgroundView.setBackgroundResource(android.R.color.holo_green_dark);
            imageView1.setVisibility(View.INVISIBLE);
            textView1.setVisibility(View.INVISIBLE);
            imageView2.setVisibility(View.VISIBLE);
            textView2.setVisibility(View.VISIBLE);
        }
        getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    public interface RecyclerItemTouchHelperListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
    }
}
