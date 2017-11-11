package com.loftschool.moneytracker;

/**
 * Created by Max on 02.11.2017.
 */

public class Item {

    static final String TYPE_UNKNOWN = "unknown";
    static final String TYPE_EXPENSES = "expense";
    static final String TYPE_INCOME = "income";

    public int id;
    public int price;
    public String name;
    public String type;

    Item(String name, int price, String type) {
        this.name = name;
        this.price = price;
        this.type = type;
    }
}
