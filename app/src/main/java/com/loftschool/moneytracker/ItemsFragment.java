package com.loftschool.moneytracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    private static final String TAG = "MY LOGG";

    private String type = Item.TYPE_UNKNOWN;

    private ItemsAdapter adapter;
    private Api api;

    private ActionMode actionMode;

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

        adapter.setListener(new ItemsAdapterListener() {
            @Override
            public void onItemClick(Item item, int position) {
                if (isInactionMode()){
                    toggleSelection(position);
                }
            }

            @Override
            public void onItemLongClick(Item item, int position) {
                if(isInactionMode()){
                    return;
                }
                actionMode = ((AppCompatActivity)getActivity()).startSupportActionMode(actionModCallback);
                toggleSelection(position);
            }

            private void toggleSelection(int position){
                adapter.toggleSelection(position);
                String title = getString(R.string.selected_items) + adapter.getSelectedItemsCount();
                actionMode.setTitle(title);
            }

            private boolean isInactionMode(){
                return actionMode != null;
            }
        });

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

    private ActionMode.Callback actionModCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.items_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()){
                case R.id.menu_remove:
                    showDialog();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
            adapter.clearSelections();
        }

    };

    private void showDialog() {
        ConfirmationDialog dialog = new ConfirmationDialog();
        dialog.setListener(new ConfirmDialogListener() {
            @Override
            public void onPositiveClick() {
                for (int i = adapter.getSelectedItems().size() - 1; i >= 0; i-- ){
                    adapter.remove(adapter.getSelectedItems().get(i));
                    actionMode.finish();
                }
            }

            @Override
            public void onDismissClick() {
                actionMode.finish();
            }
        });
        dialog.show(getFragmentManager(), "Confirmation");
    }

}
