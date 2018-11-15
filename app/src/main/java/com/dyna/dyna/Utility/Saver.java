package com.dyna.dyna.Utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.dyna.dyna.Activities.MapsActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Saver extends MapsActivity {
    Context mContext;
    public Saver (Context context){
        mContext = context;
    }

    public void saveList(ArrayList<Store> list){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString("StoreList", json);
        editor.apply();
    }

    public ArrayList<Store> retrieveList (){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("StoreList", null);
        Type type = new TypeToken<ArrayList<Store>>() {}.getType();
        return gson.fromJson(json, type);
    }
}
