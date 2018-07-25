package com.dyna.dyna;

import android.drm.DrmStore;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.google.android.gms.common.data.DataBufferObserver;

import java.io.Serializable;
import java.util.Observable;


public class Action_Listener extends MapsActivity implements Serializable {
    Store store;

    public Action_Listener( Store store){
        this.store=store;
    }

    public ChildEventListener createCEV(){
         ChildEventListener CEV = new ChildEventListener() {
             @Override
             public void onChildAdded(DataSnapshot dataSnapshot, String s) {

             }

             @Override
             public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                 //checks the previous child to see if buy or sell is changed
                 Log.d("Developer", "a value changed to"+dataSnapshot.getValue().toString());
                 Log.d("Developer", "s is: "+s);
                 if (s == "Address") {//checks for previous item in database
                     store.setBuy(dataSnapshot.getValue().toString());
                 } else {
                     store.setSell( dataSnapshot.getValue().toString());
                 }
                 Action_Listener.super.changeSnippet(store,store.getSell(),store.getBuy());
                 Action_Listener.super.changeItemAdapter(store, store.getSell(),store.getBuy());
                 Log.d("Developer", "something changed in Database ");
             }

             @Override
             public void onChildRemoved(DataSnapshot dataSnapshot) {

             }

             @Override
             public void onChildMoved(DataSnapshot dataSnapshot, String s) {

             }

             @Override
             public void onCancelled(FirebaseError firebaseError) {

             }

         };
        return CEV;

    }


}
