package com.loftschool.moneytracker;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    private List<Item> items = new ArrayList<>();

    private Context context;

    public void setItems(List<Item> items){
        this.items = items;
        notifyDataSetChanged();
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
        holder.bind(item, context);
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView price;

        ItemViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.item_name);
            price = itemView.findViewById(R.id.item_price);

        }

        void bind(Item item, Context context){
            Spannable colorCurrency = new SpannableString(String.valueOf(item.price) + context.getString(R.string.currency_symbol));
            colorCurrency.setSpan(new ForegroundColorSpan(Color.rgb(109, 111, 114)),colorCurrency.length()-1, colorCurrency.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            name.setText(item.name);
            price.setText(colorCurrency);
        }
    }

}
