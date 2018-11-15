package com.dyna.dyna;

import android.accessibilityservice.AccessibilityService;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dyna.dyna.Activities.MapsActivity;
import com.dyna.dyna.Utility.Store;

import java.util.ArrayList;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class Changer2Fragment extends Fragment {

    private TextView tv_storeName;
    private EditText et_newSell;
    private EditText et_newBuy;
    private Button btn_submit;

    ArrayList <Store> storeList;
    MapsActivity mapsActivity;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv_storeName = getView().findViewById(R.id.tv_storeName);
        et_newSell = getView().findViewById(R.id.et_newSell);
        et_newBuy = getView().findViewById(R.id.et_newBuy);
        btn_submit = getView().findViewById(R.id.btn_change);


        mapsActivity = (MapsActivity)getActivity();
        storeList = mapsActivity.storeList;

        final Store storeToChange = mapsActivity.storeToChange;
        tv_storeName.setText(storeToChange.getName());

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mapsActivity)
                        .setTitle("Confirmation")
                        .setMessage("Are you sure you want to change your value?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String newSell = et_newSell.getText().toString();
                                String newBuy =  et_newBuy.getText().toString();
                                if (newSell.equals("")){
                                    newSell=null;
                                }
                                if (newBuy.equals("")){
                                    newBuy=null;
                                }
                                mapsActivity.changeStoreValues(storeToChange, newSell , newBuy);
                                FragmentManager fragmentManager = mapsActivity.getSupportFragmentManager();
                                fragmentManager.popBackStack();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        }).show();

            }
        });

        et_newSell.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    hideKeyboard(mapsActivity);
                }
                return false;
            }
        });

        et_newBuy.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    hideKeyboard(mapsActivity);
                }
                return false;
            }
        });


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.changer2, container, false);
        return v;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
