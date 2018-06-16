package com.example.samuel.gestures;

public interface ItemTouchHelperInterface {
    void onItemMoved(int from, int to);
    void onSwiped(int position,int viewHolderPosition);
}
