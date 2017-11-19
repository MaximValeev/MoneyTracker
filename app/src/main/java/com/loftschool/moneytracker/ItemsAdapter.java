package com.loftschool.moneytracker;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    private List<Item> items = new ArrayList<>();
    private ItemsAdapterListener listener;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();

    private Context context;

    public void setItems(List<Item> items){
        this.items = items;
        notifyDataSetChanged();
    }

    public void setListener(ItemsAdapterListener listener){
        this.listener = listener;
    }

    ItemsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Item item = items.get(position);
        holder.bind(item, context, position, selectedItems.get(position, false), listener);
    }


    public void toggleSelection(int pos){
        if(selectedItems.get(pos, false)){
            selectedItems.delete(pos);
        } else {
            selectedItems.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    void clearSelections(){
        selectedItems.clear();
        notifyDataSetChanged();
    }

    int getSelectedItemsCount(){
        return selectedItems.size();
    }

    List<Integer> getSelectedItems(){
        List<Integer> items = new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++){
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    Item remove(int pos){
        final  Item item = items.remove(pos);
        notifyItemRemoved(pos);
        return item;
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView price;

        ItemViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.item_name);
            price = itemView.findViewById(R.id.item_price);

        }

        void bind(final Item item, Context context, final int position, boolean selected, final ItemsAdapterListener listener){
            Spannable currencyPlus = new SpannableString(String.valueOf(item.price) + context.getString(R.string.currency_symbol));
            currencyPlus.setSpan(new RelativeSizeSpan(0.75f),currencyPlus.length()-1, currencyPlus.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            name.setText(item.name);
            price.setText(currencyPlus);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item, position);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onItemLongClick(item, position);
                    return true;
                }
            });

            itemView.setActivated(selected);
        }
    }

}
