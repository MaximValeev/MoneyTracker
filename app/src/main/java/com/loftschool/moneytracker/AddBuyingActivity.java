package com.loftschool.moneytracker;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.loftschool.moneytracker.api.AddResult;
import com.loftschool.moneytracker.api.Api;

import java.io.IOException;

public class AddBuyingActivity extends AppCompatActivity {

    public static final String EXTRA_TYPE = "type";
    public static final String RESULT_ITEM = "item";
    public static final int RC_ADD_ITEM = 99;
    private static final int LOADER_ADD = 1;

    ItemsAdapter adapter;

    private Api api;

    Context context;

    EditText titleOfBuying;
    EditText priceOfBuying;
    ImageButton addBtn;
    String rouble;
    String type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_buying_activity);

        context = this;

        adapter = new ItemsAdapter(context);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        rouble = getResources().getString(R.string.currency_symbol);

        titleOfBuying = findViewById(R.id.titleOfBuying_et);
        priceOfBuying = findViewById(R.id.priceOfBuying_et);
        addBtn = findViewById(R.id.add_btn);

        type = getIntent().getStringExtra(EXTRA_TYPE);

        titleOfBuying.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               changeButtonState();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        priceOfBuying.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               changeButtonState();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        priceOfBuying.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    if (priceOfBuying.getText().toString().endsWith(rouble)) {
                        String textWithoutRouble = priceOfBuying.getText().toString().replace(rouble, "");
                        priceOfBuying.setText(textWithoutRouble);
                    }

                }else {
                    if (!priceOfBuying.getText().toString().isEmpty()) {
                        String textWithRouble = priceOfBuying.getText().toString()+rouble;
                        priceOfBuying.setText(textWithRouble);
                    }
                }

            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(RESULT_ITEM, new Item(titleOfBuying.getText().toString(),
                      Integer.parseInt(priceOfBuying.getText().toString().replace(rouble, "")),
                        type));
                resultIntent.putExtra(EXTRA_TYPE, type);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void changeButtonState (){
        if (!titleOfBuying.getText().toString().isEmpty() && !priceOfBuying.getText().toString().isEmpty()){
            addBtn.setEnabled(true);
            addBtn.setColorFilter(Color.argb(255,0,0,0));
        } else{
            addBtn.setColorFilter(Color.argb(255,149,152,154));
            addBtn.setEnabled(false);
        }
    }

            private void showError(String error){
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
    }

}
