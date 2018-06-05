package com.mehedicr.tourmate;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.mehedicr.tourmate.fragment.EventListFragment;
import com.mehedicr.tourmate.fragment.NearByFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FusedLocationProviderClient client;

    public static double latitude;
    public static double longitude;

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        firebaseAuth = FirebaseAuth.getInstance();

        client = LocationServices.getFusedLocationProviderClient(this);

        //getLocations();

//        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        displaySelectedScreen(R.id.action_home);
        checkLocationPermission();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        displaySelectedScreen(id);
        return true;
    }

    public void getLocations() {
        checkLocationPermission();
        client.getLastLocation()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Finding location failed", Toast.LENGTH_SHORT).show();
                        Log.e("Location error: ", e.getMessage());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                    Log.d("lat: ", latitude + "");
                    Log.d("lon: ", longitude + "");
                }
            });
    }

    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    501);
            return;
        }
    }

    public void displaySelectedScreen(int selectedMenuId){
        Fragment fragment = null;
        switch (selectedMenuId){
            case R.id.action_home:
                fragment = new EventListFragment();
                break;
            case R.id.action_weather_report:
                startActivity(new Intent(MainActivity.this,WeatherActivity.class));
                break;
            case R.id.action_map:
                break;
            case R.id.action_near_by:
                fragment = new NearByFragment();
                break;
            case R.id.action_Update_user:
                break;
            case R.id.action_logout:
                logoutUser();
                break;
        }

        if (fragment!=null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainLayout,fragment);
            ft.commit();
        }
        drawer.closeDrawers();
    }

    private void logoutUser() {
        firebaseAuth.signOut();
        startActivity(new Intent(MainActivity.this,LoginActivity.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 501 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLocations();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == 10 && resultCode == RESULT_OK && data!=null){
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("eventDetails");
            fragment.onActivityResult(requestCode,resultCode,data);
        }
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == 10 && resultCode == RESULT_OK && data!=null){
            for (Fragment fragment: getSupportFragmentManager().getFragments()){
                fragment.onActivityResult(requestCode,resultCode,data);
            }
        }
    }*/
}
