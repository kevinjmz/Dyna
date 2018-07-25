package com.dyna.dyna;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.io.Serializable;

public class ExchangeHouseOp extends AppCompatActivity implements Serializable {

    private TextView StoreName;
    private TextView OldSell;
    private TextView OldBuy;
    private EditText NewSell;
    private EditText NewBuy;
    private Store store=null;
    private Button submitSell;
    private Firebase rootRef;
    private Button submitBuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_house_op);
        StoreName = findViewById(R.id.storeName_TV);
        OldSell =  findViewById(R.id.oldSell_TV);
        OldBuy =  findViewById(R.id.oldBuy_TV);
        NewSell =  findViewById(R.id.newSell_TE);
        NewBuy =  findViewById(R.id.newBuy_TE);
        submitSell =  findViewById(R.id.submit_BTN);
        submitBuy = findViewById(R.id.sumit_btn_buy);


        rootRef = new Firebase("https://dyna-ba42b.firebaseio.com/ExchangeHouses");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            StoreName.setText(extras.getString("STORENAME"));
            OldSell.setText(String.valueOf(extras.getDouble("OLDSELL")));
            OldBuy.setText(String.valueOf(extras.getDouble("OLDBUY")));
        }
        submitSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmDialog("sell");
            }
        });

        submitBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmDialog("buy");
            }
        });
    }

    public void showConfirmDialog(final String type) {
        if (type.equals("sell")) {
            new AlertDialog.Builder(this)
                    .setTitle("Confirmation")
                    .setMessage("Are you sure you want to change your sell value?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // changeValues();
                            Intent result = new Intent();
                            result.setData(Uri.parse(NewSell.getText().toString()+" "+ type));
                            setResult(RESULT_OK, result);
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    }).show();
        }
        else {
            new AlertDialog.Builder(this)
                    .setTitle("Confirmation")
                    .setMessage("Are you sure you want to change your buy value?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // changeValues();
                            Intent result = new Intent();
                            result.setData(Uri.parse(NewBuy.getText().toString()+" "+ type));
                            setResult(RESULT_OK, result);
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    }).show();
        }
    }

    private void changeValues() {
        Firebase childRef = rootRef.child("/"+String.valueOf(StoreName.getText())).child("Buy");
        childRef.setValue(NewBuy.getText().toString());
    }
}
