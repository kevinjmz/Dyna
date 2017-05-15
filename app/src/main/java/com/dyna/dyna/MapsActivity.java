package com.dyna.dyna;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
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
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//import static android.R.id.toggle;
//import static com.dyna.dyna.R.id.toolbar;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,NavigationListAdapter.OnItemClickListener,Serializable {

    private GoogleMap mMap;
    private static final int MY_PERMISSION_REQUEST_FINE_LOCATION = 101;
    private static final int MY_PERMISSION_REQUEST_COARSE_LOCATION = 102;
    private boolean permissionIsGranted = false;
    private List<Store> storeList = new ArrayList<Store>();
    //private Button mlogin_btn;
    //For toolbar
    private ArrayList<Drawable> icons;
    private ArrayList<String> labels;
    private DrawerLayout navDrawer;
    private RecyclerView navList;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    Firebase rootRef;

    Store cashItH = new Store("Cash it Here", 31.776246, -106.472977, "0.00", "0.00");
    Store melekZ = new Store("Melek Zaragoza", 31.71689, -106.308117, "18.29", "18.57");//name, latitude, longitude, sell, buy
    Store melekP = new Store("Melek Paisano", 31.7553872, -106.4856193, "18.50", "19.29");
    Store valuntaC = new Store("Valunta Corporation", 31.756398, -106.486097, "18.75", "19.30");
    Store plainsCB = new Store("Plains Capital Bank", 31.807519, -106.510190, "20.10", "20.15");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //Firebase.setAndroidContext(this);
        if(!FirebaseApp.getApps(this).isEmpty()){
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }

        //For toolbar
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        rootRef= new Firebase("https://dyna-ba42b.firebaseio.com/ExchangeHouses");

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

    private void sendToLogin() {
        Intent intent = new Intent(MapsActivity.this, Login.class);
                //startActivity(intent);
        MapsActivity.this.startActivity(intent);
    }
    //Check changes on server
    private void checkDBchanges() {
        Firebase mRef = new Firebase("https://dyna-ba42b.firebaseio.com/ExchangeHouses");
        mRef.child("/Melek Zaragoza").addChildEventListener(new Action_Listener(melekZ).createCEV());
        mRef.child("/Cash it Here").addChildEventListener(new Action_Listener(cashItH).createCEV());
        mRef.child("/Melek Paisano").addChildEventListener(new Action_Listener(cashItH).createCEV());
        mRef.child("/Melek Zaragoza").addChildEventListener(new Action_Listener(melekZ).createCEV());
        mRef.child("/Plains Capital Bank").addChildEventListener(new Action_Listener(plainsCB).createCEV());
        mRef.child("/Valunta Corporation").addChildEventListener(new Action_Listener(valuntaC).createCEV());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        cashItH = addStore(cashItH.getName(), cashItH.getLatitude(), cashItH.getLongitude(), cashItH.getSell(), cashItH.getBuy());
        melekZ = addStore(melekZ.getName(), melekZ.getLatitude(), melekZ.getLongitude(), melekZ.getSell(), melekZ.getBuy());
        melekP = addStore(melekP.getName(), melekP.getLatitude(), melekP.getLongitude(), melekP.getSell(), melekP.getBuy());
        valuntaC = addStore(valuntaC.getName(), valuntaC.getLatitude(), valuntaC.getLongitude(), valuntaC.getSell(), valuntaC.getBuy());
        plainsCB = addStore(plainsCB.getName(), plainsCB.getLatitude(), plainsCB.getLongitude(), plainsCB.getSell(), plainsCB.getBuy());

        checkDBchanges();

        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(melekP.getLatitude(), melekP.getLongitude())));//focus starting camera to melekPaisano
        mMap.moveCamera(CameraUpdateFactory.zoomTo((float) 10.8));//adjust zoom on camera
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));

        if (!permissionIsGranted) {
            requestLocationUpdates();
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

    public Store addStore(String name, double latitude, double longitude, String sell, String buy) {
        saveOnFirebase(name, latitude, longitude, sell, buy);
        Store NS = new Store(name, latitude, longitude, sell, buy);
        addMarker(NS);
        saveInList(NS);

        return NS;
    }

    public void saveOnFirebase(String name, double latitude, double longitude, String sell, String buy) {
        //Save data in Firebase
       // Firebase mRef = new Firebase("https://dyna-ba42b.firebaseio.com/ExchangeHouses");
        Firebase mRefChild_house = rootRef.child(name);
        Firebase mRefChild_name = mRefChild_house.child("Name");
        mRefChild_name.setValue(name);
        Firebase mRefChild_latitude = mRefChild_house.child("Latitude");
        mRefChild_latitude.setValue(latitude);
        Firebase mRefChild_longitude = mRefChild_house.child("Longitude");
        mRefChild_longitude.setValue(longitude);
        Firebase mRefChild_sell = mRefChild_house.child("Sell");
        mRefChild_sell.setValue(sell);
        Firebase mRefChild_buy = mRefChild_house.child("Buy");
        mRefChild_buy.setValue(buy);
    }

    public void addMarker(Store S) {
        Marker m = mMap.addMarker(new MarkerOptions().position(new LatLng(S.getLatitude(), S.getLongitude())).title(S.getName())
                .snippet("Sell $" + S.getSell() + "    Buy $" + S.getBuy()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
        S.setMarker(m);  //same marker in object
    }

    public void saveInList(Store S) {
        storeList.add(S);
    }

    public void changeSnippet (Store S, String sell, String buy){
        S.getMarker().setSnippet("Sell $"+sell+"  Buy $"+buy);
    }

    private void setUpNavDrawer(ArrayList<Drawable> icons, ArrayList<String> labels) {
        navDrawer = (DrawerLayout) findViewById(R.id.nvd_act_main);
        NavigationListAdapter adapter = new NavigationListAdapter(this, icons, labels);
        adapter.setOnClickListener(this);

        navList = (RecyclerView) findViewById(R.id.lst_nav_drawer);
        navList.setAdapter(adapter);

        toggle = new ActionBarDrawerToggle(this, navDrawer, toolbar, R.string.open, R.string.close)
        {
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

    @Override
    public void onNavigationItemClick(int position) {
        switch (position) {
            case 0:
                startActivity(new Intent(MapsActivity.this,Login.class));
                break;
            case 1:
                startActivity(new Intent(MapsActivity.this,ListActivity.class));
                break;
            case 2:
                startExchangeHouseOP(cashItH);
                break;
            case 3:

                break;
        }
    }

    private void startExchangeHouseOP(Store store) {
        Intent i = new Intent (MapsActivity.this, ExchangeHouseOp.class);
        Bundle e = new Bundle();
        e.putSerializable("STORENAME",store.getName());
        e.putSerializable("OLDBUY",store.getBuy());
        e.putSerializable("OLDSELL",store.getSell());
        i.putExtras(e);
        startActivityForResult(i,1);//intent, request code
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode==1 && resultCode == RESULT_OK){
            String sellbuy = data.getData().toString();
            String [] parts = sellbuy.split(" ");
            Firebase childRefBuy = rootRef.child("/"+String.valueOf(cashItH.getName())).child("Buy");
            Firebase childRefSell = rootRef.child("/"+String.valueOf(cashItH.getName())).child("Sell");
            childRefSell.setValue(parts[0]);
            childRefBuy.setValue(parts[1]);
        }
    }

    protected void retrieveStores(){
        int i =0;
       // while()
        //traverse Firebase to find stores and make Stores with their current prices and information
    }
}
