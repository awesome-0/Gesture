package com.example.samuel.gestures;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.example.samuel.gestures.Adapters.CartRecyclerAdapter;
import com.example.samuel.gestures.interfaces.ItemTouchHelperInterface;

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {
    ItemTouchHelperInterface mAdapter;

    public ItemTouchHelperCallback(ItemTouchHelperInterface mAdapter) {
        this.mAdapter = mAdapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        // this is used to give the direction in which the view holder can be moved

        if(viewHolder instanceof CartRecyclerAdapter.HeaderViewHolder){
            return 0;
        }
        final int vertFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        final int horiFlag = ItemTouchHelper.START | ItemTouchHelper.END;

        return makeMovementFlags(vertFlag, horiFlag);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        mAdapter.onItemMoved(viewHolder.getAdapterPosition(),target.getAdapterPosition());
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        //pass swipe position to the adapter
        mAdapter.onSwiped(direction,viewHolder.getAdapterPosition());

    }

    @Override
    public boolean isLongPressDragEnabled() {
      //we will use the long press feature of the GestureDetector
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        //called when there is a release ...
        viewHolder.itemView.setBackgroundColor(Color.WHITE);

    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if(actionState == ItemTouchHelper.ACTION_STATE_DRAG){
            //user is currently dragging
            viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
        }
    }
}
