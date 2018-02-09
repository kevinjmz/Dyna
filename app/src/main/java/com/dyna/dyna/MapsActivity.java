package com.dyna.dyna;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dyna.dyna.List.ListActivity;
import com.firebase.client.Firebase;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.security.auth.Subject;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationListAdapter.OnItemClickListener, Serializable, Observer {

    private GoogleMap mMap;
    private static final int MY_PERMISSION_REQUEST_FINE_LOCATION = 101;
    private static final int MY_PERMISSION_REQUEST_COARSE_LOCATION = 102;
    private boolean permissionIsGranted = false;
    private ArrayList<Store> storeList = new ArrayList<>();
    private double currentLocation_latitude;
    private double currentLocation_longitude;

    //private Button mlogin_btn;//

    //For toolbar
    private ArrayList<Drawable> icons;
    private ArrayList<String> labels;
    private DrawerLayout navDrawer;
    private RecyclerView navList;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;

    Firebase rootRef;

    private Observable databaseObserver;

    Store cashItH = new Store("Cash it Here", 31.776246, -106.472977, "20.00", "20.00");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Firebase.setAndroidContext(this);

        setUpToolBar();

        databaseObserver = DatabaseManager.getInstance();
        databaseObserver.addObserver(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(31.75387, -106.485619)));//focus starting camera to melekPaisano
        mMap.moveCamera(CameraUpdateFactory.zoomTo((float) 13));//adjust zoom on camera
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));

        storeList.add(new Store ("TEST 1", 31.776246, -106.472977 ,"20.00","20.00"));

        if (!permissionIsGranted) {
            requestLocationUpdates();
        }
    }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 1 && resultCode == RESULT_OK) {
                String sellbuy = data.getData().toString();
                String[] parts = sellbuy.split(" ");
                Firebase childRefBuy = rootRef.child("/" + String.valueOf(cashItH.getName())).child("Buy");
                Firebase childRefSell = rootRef.child("/" + String.valueOf(cashItH.getName())).child("Sell");
                childRefSell.setValue(parts[0]);
                childRefBuy.setValue(parts[1]);
            }

        }

/*
        private void sendToLogin() {
            Intent intent = new Intent(MapsActivity.this, Login.class);
            //startActivity(intent);
            MapsActivity.this.startActivity(intent);
        }*/

        //Adds the markers to the map according to the Stores stored at the List given
        private void addMarkers(List<Store>List){
            for(Store S: List){
                Log.d("Marker Created for: ", S.getName());
                Marker m = mMap.addMarker(new MarkerOptions().position(new LatLng(S.getLatitude(), S.getLongitude())).title(S.getName())
                        .snippet("Sell $" + S.getSell() + "    Buy $" + S.getBuy()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
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
                Location myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                currentLocation_latitude = myLocation.getLatitude();
                currentLocation_longitude = myLocation.getLongitude();
            }catch(NullPointerException e){
                Toast.makeText(this, "Make sure you have your location enabled on your device! ", Toast.LENGTH_LONG).show();
            }

            Log.d("Developer","User's Latitude: "+currentLocation_latitude+" Longitude:  "+currentLocation_longitude);

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
                                "please fix  it on Settings/Applications", Toast.LENGTH_SHORT).show();
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
                                "please fix  it on Settings/Applications", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }

        public void changeSnippet(Store S, String sell, String buy) {
            S.getMarker().setSnippet("Sell $" + sell + "  Buy $" + buy);
        }

        private void setUpNavDrawer(ArrayList<Drawable> icons, ArrayList<String> labels) {
            navDrawer = (DrawerLayout) findViewById(R.id.nvd_act_main);
            NavigationListAdapter adapter = new NavigationListAdapter(this, icons, labels);
            adapter.setOnClickListener(this);

            navList = (RecyclerView) findViewById(R.id.lst_nav_drawer);
            navList.setAdapter(adapter);

            toggle = new ActionBarDrawerToggle(this, navDrawer, toolbar, R.string.open, R.string.close) {
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
        }


        //For navigation drawer options
        @Override
        public void onNavigationItemClick(int position) {
            switch (position) {
                case 0:
                    startActivity(new Intent(MapsActivity.this, Login.class));
                    break;
                case 1:
                    startActivity(new Intent(this,ListActivity.class));
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

            icons = new ArrayList<>();
            icons.add(ContextCompat.getDrawable(this, R.drawable.common_google_signin_btn_icon_dark));
            icons.add(ContextCompat.getDrawable(this, R.drawable.common_google_signin_btn_icon_dark_normal));
            icons.add(ContextCompat.getDrawable(this, R.drawable.common_google_signin_btn_icon_dark_normal));

            labels = new ArrayList<>();
            labels.add("Login");
            labels.add("List");
            labels.add("Exchange House Options");
            labels.add("Profile");

            setUpNavDrawer(icons, labels);
        }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof DatabaseManager){
            DatabaseManager databaseManager = (DatabaseManager)o;
            storeList = databaseManager.getStoreList();
            addMarkers(storeList);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseObserver.deleteObserver(this);
    }
}