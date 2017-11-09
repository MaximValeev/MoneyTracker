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

    ItemsAdapter(Context context) {
        this.context = context;

        items.add(new Item("Молоко", 35));
        items.add(new Item("Зубная щетка", 1500));
        items.add(new Item("Сковородка с антипригарным покрытием", 55));
        items.add(new Item("Проезд на месяц", 3500));
        items.add(new Item("Курс на LoftSchool", 12000));
        items.add(new Item("Nintendo Switch", 22000));
        items.add(new Item("На телефон", 500));
        items.add(new Item("Кино", 1100));
        items.add(new Item("SSD", 3200));
        items.add(new Item("На НГ", 2000));
        items.add(new Item("Ремонт телефона", 4000));
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
            Spannable colorCurrency = new SpannableString(String.valueOf(item.getPrice()) + context.getString(R.string.currency_symbol));
            colorCurrency.setSpan(new ForegroundColorSpan(Color.rgb(109, 111, 114)),colorCurrency.length()-1, colorCurrency.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            name.setText(item.getName());
            price.setText(colorCurrency);
        }
    }

}
