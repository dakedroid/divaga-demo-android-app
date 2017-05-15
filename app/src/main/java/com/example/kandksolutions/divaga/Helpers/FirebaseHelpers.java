package com.example.kandksolutions.divaga.Helpers;

import com.example.kandksolutions.divaga.Modelos.Noticia;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by dakedroid on 26/01/2017.
 */

public class FirebaseHelpers {

    ArrayList<String> noticias = new ArrayList<>();
    Boolean saved = null;
    DatabaseReference db;

    public FirebaseHelpers(DatabaseReference db) {
        this.db = db;
    }
    //SAVE

    public Boolean save(Noticia noticia)
    {
        if(noticia == null)
        {
            saved=false;
        }else {
            try
            {
                db.child("Noticias").push().setValue(noticia);
                saved=true;
            }catch (DatabaseException e)
            {
                e.printStackTrace();
                saved=false;
            }
        }
        return saved;
    }
    //READ
    public ArrayList<String> retrieve()
    {

        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return noticias;
    }


    private void fetchData(DataSnapshot dataSnapshot)
    {
        noticias.clear();
        for (DataSnapshot ds : dataSnapshot.getChildren())
        {
            String name = ds.getValue(Noticia.class).getTitulo();
            noticias.add(name);
        }
    }

    public void get(){
        db.child("Noticias").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get user value
                fetchData(dataSnapshot);
                // ...
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...

            }
        });
    }


}
