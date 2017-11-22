package com.loftschool.moneytracker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.loftschool.moneytracker.api.Api;
import com.loftschool.moneytracker.api.BalanceResult;

import java.io.IOException;

public class BalanceFragment extends Fragment {

    private static final int LOADER_UPDATE = 4;
    private Api api;

    private TextView balance;
    private TextView expense;
    private TextView income;
    private DiagramView diagram;

    public static BalanceFragment createFragment(){
        return new BalanceFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_balance, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        api = ((App) getActivity().getApplication()).getApi();
        balance = view.findViewById(R.id.balance);
        expense = view.findViewById(R.id.expense);
        income = view.findViewById(R.id.income);
        diagram = view.findViewById(R.id.diagram);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && isResumed()){
            updateData();
        }
    }

    private void updateData() {
        getLoaderManager().restartLoader(LOADER_UPDATE, null, new LoaderManager.LoaderCallbacks<BalanceResult>() {
            @Override
            public Loader<BalanceResult> onCreateLoader(int id, Bundle args) {
                return new AsyncTaskLoader<BalanceResult>(getContext()) {
                    @Override
                    public BalanceResult loadInBackground() {
                        try {
                            BalanceResult balanceResult =api.balance().execute().body();
                            return balanceResult;
                        } catch (IOException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                };
            }

            @Override
            public void onLoadFinished(Loader<BalanceResult> loader, BalanceResult balanceResult) {
                if(balanceResult != null && balanceResult.isSuccess()) {
                    balance.setText(getString(R.string.price, balanceResult.totalIncome - balanceResult.totalExpenses));
                    expense.setText(getString(R.string.price, balanceResult.totalExpenses));
                    income.setText(getString(R.string.price, balanceResult.totalIncome));
                    diagram.update(balanceResult.totalExpenses, balanceResult.totalIncome);
                } else Toast.makeText(getContext(), R.string.error_balance_loading, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoaderReset(Loader<BalanceResult> loader) {

            }
        }).forceLoad();

    }
}
