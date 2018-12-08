package com.dyna.dyna.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.transition.Fade;
import android.transition.TransitionInflater;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dyna.dyna.Changer2Fragment;
import com.dyna.dyna.ChangerFragment;
import com.dyna.dyna.DetailsFragment;
import com.dyna.dyna.Utility.DatabaseManager;
import com.dyna.dyna.NavigationDrawer.NavigationListAdapter;
import com.dyna.dyna.R;
import com.dyna.dyna.Slider.SliderFragment;
import com.dyna.dyna.Utility.Store;
import com.dyna.dyna.showDetailsInterface;
import com.firebase.client.Firebase;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationListAdapter.OnItemClickListener, Serializable, Observer, GoogleMap.OnMarkerClickListener, showDetailsInterface, GoogleMap.OnInfoWindowClickListener {

    public GoogleMap mMap;
    private static final int MY_PERMISSION_REQUEST_FINE_LOCATION = 101;
    private static final int MY_PERMISSION_REQUEST_COARSE_LOCATION = 102;
    private boolean permissionIsGranted = false;
    private double currentLocation_latitude;
    private double currentLocation_longitude;
    private String TAG = "Developer";

    public ArrayList<Store> storeList;

    private Toolbar toolbar;

    Firebase rootRef;

    private Observable databaseObserver;

    private static Context mContext;

    private boolean isAttached = false;

    public static Store storeToChange;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();

        setUpMap();
        setUpToolBar();

        databaseObserver = DatabaseManager.getInstance();
        databaseObserver.addObserver(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(31.75387, -106.485619)));//focus starting camera to melekPaisano
        mMap.moveCamera(CameraUpdateFactory.zoomTo((float) 12));//adjust zoom on camera
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));
        mMap.setPadding(0, 100, 0, 0);  //lower the location button
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setCompassEnabled(false);//remove compass
        uiSettings.setMapToolbarEnabled(false);//remove buttons triggered by markers

        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                removeSlider();
                isAttached = false;
            }
        });

        mMap.setOnInfoWindowClickListener(this);


        if (!permissionIsGranted) {
            requestLocationUpdates();
        }

    }

    //Adds the markers to the map according to the Stores stored at the List given
    private void addMarkers(List<Store> List) {
        for (Store S : List) {
            //Log.d("Marker Created for: ", S.getName());
            Marker m = mMap.addMarker(new MarkerOptions().position(new LatLng(S.getLatitude(), S.getLongitude())).title(S.getName())
                    .snippet("Sell $" + S.getSell() + "    Buy $" + S.getBuy()).icon(BitmapDescriptorFactory.fromResource(R.drawable.pointer_02)));
            S.setMarker(m);
        }
    }

    public void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Permission has not been granted

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_FINE_LOCATION);
            }
            return;
        }
        mMap.setMyLocationEnabled(true);
        try {
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location myLocation = null;
            if (lm != null) {
                myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
            if (myLocation != null) {
                currentLocation_latitude = myLocation.getLatitude();
            }
            if (myLocation != null) {
                currentLocation_longitude = myLocation.getLongitude();
            }
        } catch (NullPointerException e) {
            Toast.makeText(this, "Make sure you have your location enabled on your device! ", Toast.LENGTH_LONG).show();
        }

        //    Log.d("Developer","User's Latitude: "+currentLocation_latitude+" Longitude:  "+currentLocation_longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(currentLocation_latitude, currentLocation_longitude)));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission granted
                    permissionIsGranted = true;
                } else {
                    //permission denied
                    permissionIsGranted = false;
                    Toast.makeText(getApplicationContext(), "This app requires location permission to be granted, " +
                            "please fix  it at Settings/Applications", Toast.LENGTH_SHORT).show();
                }
                break;

            case MY_PERMISSION_REQUEST_COARSE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission granted
                    permissionIsGranted = true;
                } else {
                    //permission denied
                    permissionIsGranted = false;
                    Toast.makeText(getApplicationContext(), "This app requires location permission to be granted, " +
                            "please fix  it at Settings/Applications", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void changeSnippet(Store S, String sell, String buy) {
        Log.d("Developer", "changed to new snippet sell:"+ sell + "  buy:"+buy);
        S.getMarker().setSnippet("Sell $" + sell + "  Buy $" + buy);
    }

    private void setUpNavDrawer(ArrayList<Drawable> icons, ArrayList<String> labels) {
        DrawerLayout navDrawer = findViewById(R.id.nvd_act_main);
        NavigationListAdapter adapter = new NavigationListAdapter(this, icons, labels);
        adapter.setOnClickListener(this);

        RecyclerView navList = findViewById(R.id.lst_nav_drawer);
        navList.setAdapter(adapter);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, navDrawer, toolbar, R.string.open, R.string.close) {
            public void onDrawerClosed(View drawer) {
                super.onDrawerClosed(drawer);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawer) {
                super.onDrawerOpened(drawer);
                invalidateOptionsMenu();
            }
        };
        navDrawer.addDrawerListener(toggle);
        toggle.syncState();
    }


    //For navigation drawer options
    @Override
    public void onNavigationItemClick(int position) {
        switch (position) {
            case 0:
                startActivity(new Intent(MapsActivity.this, Login.class));
                break;
            case 1:
                startActivity(new Intent(this, ListActivity.class));
                break;
            case 2:
                addChanger();
                break;
            case 3:
                break;
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void setUpToolBar() {
        //For toolbar
        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        rootRef = new Firebase("https://dyna-ba42b.firebaseio.com/ExchangeHouses");

        ArrayList<Drawable> icons = new ArrayList<>();
        icons.add(ContextCompat.getDrawable(this, R.drawable.login));
        icons.add(ContextCompat.getDrawable(this, R.drawable.list));
        icons.add(ContextCompat.getDrawable(this, R.drawable.house));

        ArrayList<String> labels = new ArrayList<>();

        labels.add("Login");
        labels.add("List");
        labels.add("Exchange House Options");
        labels.add("Profile");
        labels.add("Log Out");

        setUpNavDrawer(icons, labels);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof DatabaseManager) {
            DatabaseManager databaseManager = (DatabaseManager) o;
            storeList = databaseManager.getStoreList();
            saveList(storeList);
            addMarkers(storeList);
            computeDistancesToCities();
           // setUpList();
         //   Log.d("Developer", "markers added! @Maps/update");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseObserver.deleteObserver(this);
        saveList(storeList);
    }

    public void setUpMap(){
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Firebase.setAndroidContext(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(isAttached==false) {
            addSlider();
            isAttached = true;
        }
        else {
            scrollToMarkerPosition(marker);
        }
        return false;
    }

    public void changeItemAdapter(Store newStore, String newSell, String newBuy) {
        int index=0;
        if (storeList == null){
            storeList = retrieveList();
        }

        for(Store s: storeList)
            Log.d(TAG, s.getName() + "buy>> "+s.getBuy()+"sell>> "+s.getSell());

        for(Store s : storeList) {
            if (s.getName().equals(newStore.getName())){//replace s object in the list with the one that has updated value
                index = storeList.indexOf(s);
                s.setSell(newSell);
                s.setBuy(newBuy);
            }
        }

        for(Store s: storeList)
            Log.d(TAG, "AFTER " + "buy>> "+s.getBuy()+"sell>> "+s.getSell());

    }

    public void saveList(ArrayList<Store> list){

        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String listAsJson = gson.toJson(list);
        prefsEditor.putString("StoreList", listAsJson);
        prefsEditor.apply();

    }

    public ArrayList<Store> retrieveList (){

        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("StoreList", null);
        Type type = new TypeToken<List<Store>>(){}.getType();
        return gson.fromJson(json, type);

    }

    private void addSlider (){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SliderFragment sliderFragment = new SliderFragment();
        sliderFragment.listener = this;
        fragmentTransaction.replace(R.id.map,sliderFragment,"slider");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void removeSlider(){
        Fragment fm = getSupportFragmentManager().findFragmentByTag("slider");
        if(fm != null)
            getSupportFragmentManager().beginTransaction().remove(fm).commit();
    }

    private void scrollToMarkerPosition(Marker marker){
        FragmentManager fm = getSupportFragmentManager();
        SliderFragment fragment = (SliderFragment) fm.findFragmentByTag("slider");
        if (fragment!=null){
            fragment.scrollToMarkerPosition(marker);
        }
    }

    private void addChanger(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ChangerFragment changerFragment = new ChangerFragment();
        fragmentTransaction.replace(R.id.map, changerFragment ,"changer");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void swapToChanger2(){
        // Create new fragment and transaction
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack();
        Fragment newFragment = new Changer2Fragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();


        transaction.replace(R.id.changerContainer, newFragment, "changer2");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void showDetails(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().setCustomAnimations(R.xml.bottom_to_top_anim, 0,R.xml.top_to_bottom_anim,0);
        DetailsFragment detailsFragment = new DetailsFragment();
        fragmentTransaction.replace(R.id.map, detailsFragment ,"details");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void performTransaction() {
        if (isDestroyed())
        {
            return;
        }
        FragmentManager mFragmentManager = getSupportFragmentManager();
        SliderFragment sliderFragment = new SliderFragment();
        DetailsFragment nextFragment = new DetailsFragment();

        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        // 1. Exit for Previous Fragment
        Fade exitFade = new Fade();
        exitFade.setDuration(300);
        sliderFragment.setExitTransition(exitFade);

        // 2. Shared Elements Transition
        TransitionSet enterTransitionSet = new TransitionSet();
        enterTransitionSet.addTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.move));
        enterTransitionSet.setDuration(1000);
        enterTransitionSet.setStartDelay(300);
        nextFragment.setSharedElementEnterTransition(enterTransitionSet);

        // 3. Enter Transition for New Fragment
        Fade enterFade = new Fade();
        enterFade.setStartDelay(1000 + 300);
        enterFade.setDuration(300);
        nextFragment.setEnterTransition(enterFade);

        TextView store_name = findViewById(R.id.item_store_name);
        fragmentTransaction.addSharedElement(store_name, store_name.getTransitionName());
        fragmentTransaction.replace(R.id.map, nextFragment);
        fragmentTransaction.commitAllowingStateLoss();
        fragmentTransaction.addToBackStack(null);
    }

    public void changeStoreValues(Store oldStore, String newSell, String newBuy){

            Firebase childRefBuy = rootRef.child("/" + String.valueOf(oldStore.getName())).child("Buy");
            Firebase childRefSell = rootRef.child("/" + String.valueOf(oldStore.getName())).child("Sell");

            if(newSell != null){
                childRefSell.setValue(newSell);
            }
            if (newBuy != null) {
                childRefBuy.setValue(newBuy);
            }
    }

    public void computeDistancesToCities(){
        Location usr_loc = new Location("");
        usr_loc.setLatitude(currentLocation_latitude);
        usr_loc.setLongitude(currentLocation_longitude);

        for(Store s : storeList){
            float pk = (float) (180.f/Math.PI);

            float a1 = (float)currentLocation_latitude / pk;
            float a2 = (float)currentLocation_longitude / pk;
            float b1 = (float)s.getLatitude() / pk;
            float b2 = (float)s.getLongitude() / pk;

            double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
            double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
            double t3 = Math.sin(a1) * Math.sin(b1);
            double tt = Math.acos(t1 + t2 + t3);

            s.setDistanceToUserInMiles((float)((6366000 * tt)*0.000621371192));
            s.setGetDistanceToUserInMeters((float)(6366000 * tt));
            Log.d(TAG, "distance to store "+s.getName()+" is "+s.getDistanceToUser());
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        showDetails();
    }

}