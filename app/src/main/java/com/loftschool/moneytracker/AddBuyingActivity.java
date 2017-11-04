package com.loftschool.moneytracker;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class AddBuyingActivity extends AppCompatActivity {

    EditText titleOfBuying;
    EditText priceOfBuying;
    ImageButton addBtn;
    String rouble = getResources().getString(R.string.currency_symbol);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_buying);

        titleOfBuying = findViewById(R.id.titleOfBuying_et);
        priceOfBuying = findViewById(R.id.priceOfBuying_et);
        addBtn = findViewById(R.id.add_btn);

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

    public void onClick(View view){
        Toast.makeText(this, "The button is work", Toast.LENGTH_SHORT).show();
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


}
