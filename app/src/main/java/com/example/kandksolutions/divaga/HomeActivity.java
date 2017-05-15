package com.example.kandksolutions.divaga;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.kandksolutions.divaga.AcercaDe.AcercaDeActivity;
import com.example.kandksolutions.divaga.Favoritos.FavoritosActivity;
import com.example.kandksolutions.divaga.HomeFragments.EventosFragement;
import com.example.kandksolutions.divaga.HomeFragments.LugaresFragment;
import com.example.kandksolutions.divaga.HomeFragments.NoticiasFragment;
import com.example.kandksolutions.divaga.Modelos.Noticia;
import com.example.kandksolutions.divaga.SubirContenido.SubirActivity;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    Intent intent;
    private static final String TAG = "HomeActivity";
    private DrawerLayout mDrawerLayout;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        FacebookSdk.sdkInitialize(getApplicationContext());

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //mTextMessage = (TextView) findViewById(R.id.message);

        // Set the color of status bar
        TypedValue value = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimaryDark, value, true);
        mDrawerLayout.setStatusBarBackgroundColor(value.data);

        // Retrieve the Toolbar from our content view, and set it as the action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Toggle icon
        assert toolbar != null;
        toolbar.setNavigationIcon(R.drawable.ic_menu_black_18dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        // Menu
        NavigationView navigation = (NavigationView) findViewById(R.id.navigation);
        assert navigation != null;
        navigation.setNavigationItemSelectedListener(mNavigationItemSelectedListener);
        navigation.inflateHeaderView(R.layout.home_design_navigation_header);


        // Tab Layout Implementation with view pager swipe support
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        assert tabLayout != null;
        tabLayout.setupWithViewPager(viewPager);


        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        checkSesion();

    }



    private NavigationView.OnNavigationItemSelectedListener mNavigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            if (handleNavigationItemSelected(item)) {
                mDrawerLayout.closeDrawers();
                return true;
            }
            return false;
        }
    };

    private boolean handleNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_item_1:
                //intent = new Intent(HomeActivity.this, IniciarSesionActivity.class);
                //startActivity(intent);
                DatabaseReference mDatabaseReference;
                mDatabaseReference = FirebaseDatabase.getInstance().getReference();

                Noticia movie = new Noticia("Noticia x3", "Descripcion", "noticia_12");
                //referring to movies node and setting the values from movie object to that location
                mDatabaseReference.child("MisNoticias").push().setValue(movie);
                //fragmentNoticias.onCreateView());



                return true;
            case R.id.navigation_item_2:
                    intent = new Intent(HomeActivity.this,  MainActivity.class);
                startActivity(intent);

                /*
                StorageReference storageRef;
                storageRef = FirebaseStorage.getInstance().getReference();
                storageRef.child("/noticias/noticia_12.jpeg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // TODO: handle uri
                        Log.i("DEBUG IMAGES", String.valueOf(uri));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        Log.i("DEBUG IMAGES", String.valueOf(exception.getStackTrace()));
                    }
                });
*/
                return true;
            case R.id.navigation_item_3:
                intent = new Intent(HomeActivity.this, FavoritosActivity.class);
                startActivity(intent);

                return true;
            case R.id.navigation_item_4:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Descarga la aplicacion Divaga";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Divaga");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Compartir por:"));
                return true;
            case R.id.navigation_item_5:
                intent = new Intent(HomeActivity.this, AcercaDeActivity.class);
                startActivity(intent);
                return true;
            case R.id.log_out:
                removeSesion();
                signOut();
                return true;
            default:
                return false;
        }
    }



    private void showToast(String res) {

        Toast.makeText(this, res ,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }



    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new LugaresFragment(), "Lugares");
        adapter.addFragment(new NoticiasFragment(), "Noticias");
        adapter.addFragment(new EventosFragement(), "Eventos");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public void signOut() {
        mAuth.signOut();
        LoginManager.getInstance().logOut();
    }

    void removeSesion() {
        String filename = "sesion.txt";
        String string = "false";
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
            Log.i("FILE","EXITO");
        } catch (Exception e) {
            e.printStackTrace();
        }

        checkSesion();
    }


    void checkSesion() {

        if (readFromFile(this).equals("false")){
            intent = new Intent(HomeActivity.this, IniciarSesionActivity.class);
            startActivity(intent);
        }
    }

    private String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("sesion.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        return ret;
    }


}
