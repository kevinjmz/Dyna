package com.dyna.dyna;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dyna.dyna.Activities.MapsActivity;
import com.dyna.dyna.Utility.Store;

import java.util.ArrayList;

public class ChangerFragment extends Fragment {
    private Button btn_send;
    private TextView tv_wrongPass;
    private TextView instructions;
    private EditText et_password;
    ArrayList<Store> storeList;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btn_send = getView().findViewById(R.id.btn_send);
        tv_wrongPass = getView().findViewById(R.id.tv_error);
        instructions = getView().findViewById(R.id.tv_01);
        et_password = getView().findViewById(R.id.et_password);


        final MapsActivity mapsActivity = (MapsActivity)getActivity();
        storeList = mapsActivity.storeList;

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = String.valueOf(et_password.getText());
                if ( input ==null){
                    Toast.makeText(getContext(), "Oops you forgot to enter the password!",Toast.LENGTH_LONG).show();
                }
                else {
                    boolean rightPass = false;
                    for(Store s : storeList) {
                        if (input.equals(s.getPassword())){
                            Toast.makeText(getContext(),"Access granted for: "+s.getName(),Toast.LENGTH_SHORT).show();
                            rightPass = true;
                            mapsActivity.storeToChange = s;
                            mapsActivity.swapToChanger2();
                        }
                    }
                    if(!rightPass)
                        tv_wrongPass.setAlpha(1);
                    else
                        tv_wrongPass.setAlpha(0);
                }
            }
        });
        et_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    hideKeyboard(mapsActivity);
                }
                return false;
            }
        });

        getView().setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    return true;
                }
                return false;
            }
        } );



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.changer, container, false);
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
