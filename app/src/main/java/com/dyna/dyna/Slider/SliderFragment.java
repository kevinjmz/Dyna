package com.dyna.dyna.Slider;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dyna.dyna.Activities.MapsActivity;
import com.dyna.dyna.R;
import com.dyna.dyna.Utility.Store;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SliderFragment extends android.support.v4.app.Fragment {
    LinearLayoutManager layoutManager;
    ArrayList<Store> storeList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.slider, container, false);
        RecyclerView slider_container = v.findViewById(R.id.maps_list);
        slider_container.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        slider_container.setLayoutManager(layoutManager);

        final MapsActivity mapsActivity = (MapsActivity)getActivity();
        storeList = mapsActivity.storeList;
        Maps_itemAdapter itemAdapter = new Maps_itemAdapter(getActivity().getApplicationContext(),storeList);
        slider_container.setAdapter(itemAdapter);
        slider_container.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);


        slider_container.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int cardIndex = layoutManager.findLastCompletelyVisibleItemPosition();
                if(cardIndex!=-1){
                    storeList.get(cardIndex).getMarker().showInfoWindow();

                    MapsActivity mapsActivity = (MapsActivity) getActivity();
                    GoogleMap mMap = mapsActivity.mMap;
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(storeList.get(cardIndex).getMarker().getPosition()), 250, null);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        return v;
    }

    public ArrayList<Store> retrieveList (){

        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("StoreList", null);
        Type type = new TypeToken<List<Store>>(){}.getType();
        return gson.fromJson(json, type);

    }

    public void scrollToMarkerPosition(Marker marker){
        for(Store S : storeList) {
            if (marker.getTitle().equals(S.getName())){
                layoutManager.scrollToPositionWithOffset(storeList.indexOf(S),100);
            }
        }

    }
}
