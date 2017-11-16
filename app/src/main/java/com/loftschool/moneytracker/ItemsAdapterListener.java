package com.loftschool.moneytracker;

import android.view.ActionMode;

/**
 * Created by Max on 15.11.2017.
 */

public interface ItemsAdapterListener {

    void onItemClick(Item item, int position);

    void onItemLongClick(Item item, int position);
}
