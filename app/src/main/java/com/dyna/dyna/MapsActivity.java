package com.dyna.dyna;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.maps.CameraUpdate;
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


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationListAdapter.OnItemClickListener, Serializable, Observer, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private static final int MY_PERMISSION_REQUEST_FINE_LOCATION = 101;
    private static final int MY_PERMISSION_REQUEST_COARSE_LOCATION = 102;
    private boolean permissionIsGranted = false;
    private double currentLocation_latitude;
    private double currentLocation_longitude;
    private String TAG = "Developer";

    private ArrayList<Store> storeList;

    private Toolbar toolbar;

    Firebase rootRef;

    private Observable databaseObserver;

    private RecyclerView list_container;
    private LinearLayoutManager layout;

    private Maps_itemAdapter itemAdapter;

    private static Context mContext;

    Store cashItH = new Store("Cash it Here", 31.776246, -106.472977, "20.00", "20.00");


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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(31.75387, -106.485619)));//focus starting camera to melekPaisano
        mMap.moveCamera(CameraUpdateFactory.zoomTo((float) 12));//adjust zoom on camera
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));
        mMap.setPadding(0, 100, 0, 0);  //lower the location button
        UiSettings uiSettings=mMap.getUiSettings();
        uiSettings.setCompassEnabled(false);//remove compass
        uiSettings.setMapToolbarEnabled(false);//remove buttons triggered by markers

        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                list_container.setAlpha(0);
            }
        });

        if (!permissionIsGranted) {
            requestLocationUpdates();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {// in case store changes values of sell or buy
            if (data.getData() != null) {
                String newValue = data.getData().toString();
                String[] parts = newValue.split(" ");
                Firebase childRefBuy = rootRef.child("/" + String.valueOf(cashItH.getName())).child("Buy");
                Firebase childRefSell = rootRef.child("/" + String.valueOf(cashItH.getName())).child("Sell");

                if(parts[1].equals("sell")) {
                    childRefSell.setValue(parts[0]);
                }
                else {
                    childRefBuy.setValue(parts[0]);
                }
            }
        }

    }

/*
        private void sendToLogin() {
            Intent intent = new Intent(MapsActivity.this, Login.class);
            //startActivity(intent);
            MapsActivity.this.startActivity(intent);
        }*/

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
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        update(null, null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission granted
                    permissionIsGranted = true;
                    finish();
                    startActivity(getIntent());

                    Intent i = getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);

                } else {
                    //permission denied
                    permissionIsGranted = false;
                    Toast.makeText(getApplicationContext(), "This app requires location permission to be granted, " +
                            "please fix  it on Settings/Applications", Toast.LENGTH_SHORT).show();
                }
                break;

            case MY_PERMISSION_REQUEST_COARSE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission granted
                    permissionIsGranted = true;
                    finish();
                    startActivity(getIntent());
                } else {
                    //permission denied
                    permissionIsGranted = false;
                    Toast.makeText(getApplicationContext(), "This app requires location permission to be granted, " +
                            "please fix  it on Settings/Applications", Toast.LENGTH_SHORT).show();
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
                startExchangeHouseOP(cashItH);
                break;
            case 3:
                break;
        }
    }

    //prepare intent with information about the store to be changed before calling activity
    private void startExchangeHouseOP(Store store) {
        Intent i = new Intent(MapsActivity.this, ExchangeHouseOp.class);
        Bundle e = new Bundle();
        e.putSerializable("STORENAME", store.getName());
        e.putSerializable("OLDBUY", store.getBuy());
        e.putSerializable("OLDSELL", store.getSell());
        i.putExtras(e);
        startActivityForResult(i, 1);//intent, request code
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
            setUpList();
         //   Log.d("Developer", "markers added! @Maps/update");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseObserver.deleteObserver(this);
    }

    public void setUpMap(){
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Firebase.setAndroidContext(this);
    }

    public void setUpList(){
        list_container = findViewById(R.id.maps_list);
        list_container.setHasFixedSize(true);
        layout = new LinearLayoutManager(getApplicationContext());
        layout.setOrientation(LinearLayoutManager.HORIZONTAL);
        itemAdapter = new Maps_itemAdapter(this,storeList);
        list_container.setAdapter(itemAdapter);
        list_container.setLayoutManager(layout);
        list_container.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int cardIndex = layout.findLastCompletelyVisibleItemPosition();
                if(cardIndex!=-1){
                    storeList.get(cardIndex).getMarker().showInfoWindow();
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(storeList.get(cardIndex).getMarker().getPosition()), 250, null);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        list_container.setAlpha(1);
        for(Store S : storeList) {
            if (marker.getTitle().equals(S.getName())){
                layout.scrollToPositionWithOffset(storeList.indexOf(S),100);
            }
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

        if(itemAdapter==null){

            itemAdapter.notifyItemChanged(index);
        }
        else{
            itemAdapter.notifyItemChanged(index);
        }

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

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState
//        outState.putAll();
//    }
}