package com.loftschool.moneytracker;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.loftschool.moneytracker.api.Api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ItemsFragment extends Fragment {

    private static final int LOADER_ITEMS = 0;
    private static final int LOADER_ADD = 1;

    private static final String KEY_TYPE = "TYPE";

    private String type = Item.TYPE_UNKNOWN;

    private ItemsAdapter adapter;
    private Api api;

    public static ItemsFragment CreateItemsFragment(String type){
        ItemsFragment fragment = new ItemsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ItemsFragment.KEY_TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        type = getArguments().getString(KEY_TYPE, Item.TYPE_UNKNOWN);

        if (type.equals(Item.TYPE_UNKNOWN)){
            throw new IllegalStateException("Unknown Fragment type");
        }

        adapter = new ItemsAdapter(getContext());
        api = ((App) getActivity().getApplication()).getApi();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_items, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        RecyclerView recycler = view.findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(adapter);

        FloatingActionButton fab = view.findViewById(R.id.fab_add);

        loadItems();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddBuyingActivity.class);
                intent.putExtra(AddBuyingActivity.EXTRA_TYPE, type);
                startActivityForResult(intent, AddBuyingActivity.RC_ADD_ITEM);
            }
        });


    }

    private void loadItems(){
        getLoaderManager().initLoader(LOADER_ITEMS, null, new LoaderManager.LoaderCallbacks<List<Item>>() {
            @Override
            public Loader<List<Item>> onCreateLoader(int id, Bundle args) {
                return new AsyncTaskLoader<List<Item>>(getContext()) {
                    @Override
                    public List<Item> loadInBackground() {
                        try {
                            List<Item> items = api.items(type).execute().body();
                            return items;
                        } catch (IOException e) {
                            showError(e.getMessage());
                            return null;
                        }
                    }
                };
            }

            @Override
            public void onLoadFinished(Loader<List<Item>> loader, List<Item> items) {
                if (items == null){
                    showError("произошла ошибка");
                } else {
                    adapter.setItems(items);
                }
            }

            @Override
            public void onLoaderReset(Loader<List<Item>> loader) {
            }

        }).forceLoad();
    }

    private void addItems(final Item item){
        getLoaderManager().restartLoader(LOADER_ADD, null, new LoaderManager.LoaderCallbacks<Item> () {
            @Override
            public Loader<Item> onCreateLoader(int id, Bundle args) {
                return new AsyncTaskLoader<Item>(getContext()) {
                    @Override
                    public Item loadInBackground() {
                        try {
                            return api.add(item.name, item.price, item.type).execute().body();
                        } catch (IOException e) {
                            showError(e.getMessage());
                            return null;
                        }
                    }
                };
            }

            @Override
            public void onLoadFinished(Loader<Item> loader, Item item1) {
                if(item == null){
                    showError("произошла ошибка");
                }
            }

            @Override
            public void onLoaderReset(Loader<Item> loader) {
            }
        }).forceLoad();
    }

    private void showError(String error){
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==AddBuyingActivity.RC_ADD_ITEM && resultCode == AddBuyingActivity.RESULT_OK){
            Item item = (Item) data.getSerializableExtra(AddBuyingActivity.RESULT_ITEM);
            Toast.makeText(getContext(), item.name + " " + item.price, Toast.LENGTH_SHORT).show();
        }
    }

    //    private void loadItems(){
//
//        new AsyncTask<Void, Void, List<Item>>() {
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//            }
//
//            @Override
//            protected List<Item> doInBackground(Void... voids) {
//                try {
//                    List<Item> items = api.items(type).execute().body();
//                    return items;
//                } catch (IOException e) {
//                    showError(e.getMessage());
//                    return null;
//                }
//            }
//
//            @Override
//            protected void onPostExecute(List<Item> items) {
//                super.onPostExecute(items);
//                adapter.setItems(items);
//            }
//        }.execute();
//    }

//    private void loadItems(){
//        new LoadItemsTask(new Handler(Looper.getMainLooper()) {
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                switch (msg.what){
//                    case ITEMS_LOADED:
//                        //noinspection unchecked
//                        adapter.setItems((List<Item>) msg.obj);
//                        break;
//
//                    case ITEMS_ERROR:
//                        showError((String) msg.obj);
//                }
//            }
//        }).start();
//    }
//
//    private final static int ITEMS_LOADED = 0;
//    private final static int ITEMS_ERROR = 1;
//
//    private class LoadItemsTask implements Runnable{
//
//        private Thread thread;
//        private Handler handler;
//
//        LoadItemsTask(Handler handler){
//            thread = new Thread(this);
//            this.handler = handler;
//        }
//
//        void start(){
//            thread.start();
//        }
//
//        @Override
//        public void run() {
//            try {
//                List<Item> items = api.items(type).execute().body();
//                handler.obtainMessage(ITEMS_LOADED, items).sendToTarget();
//            } catch (IOException e) {
//                showError(e.getMessage());
//                handler.obtainMessage(ITEMS_ERROR, e.getMessage()).sendToTarget();
//            }
//        }
//    }


}
