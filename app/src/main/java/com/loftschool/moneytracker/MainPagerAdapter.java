package com.loftschool.moneytracker;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;


public class MainPagerAdapter extends FragmentPagerAdapter {

    private final static int PAGE_EXPENSES = 0;
    private final static int PAGE_INCOMES = 1;
    private final static int PAGE_BALANCE = 2;

    private String [] tabsTitles;

    MainPagerAdapter(FragmentManager fm, Resources res) {
        super(fm);

        tabsTitles = res.getStringArray(R.array.tabs_titles);
    }


    @Override
    public Fragment getItem(int position) {
        switch (position){
            case PAGE_EXPENSES:
                return ItemsFragment.CreateItemsFragment(Item.TYPE_EXPENSES);

            case PAGE_INCOMES:
                return ItemsFragment.CreateItemsFragment(Item.TYPE_INCOME);

            case PAGE_BALANCE:
                BalanceFragment fragment = new BalanceFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("TYPE", PAGE_BALANCE);
                fragment.setArguments(bundle);
                return fragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabsTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabsTitles[position];

    }
}
