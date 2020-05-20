package com.gnorizon.solutions.pandemic_aid.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.gnorizon.solutions.pandemic_aid.BuildConfig;
import com.gnorizon.solutions.pandemic_aid.R;
import com.gnorizon.solutions.pandemic_aid.fragment.AboutCovid19;
import com.gnorizon.solutions.pandemic_aid.fragment.ContactChannels;
import com.gnorizon.solutions.pandemic_aid.fragment.HighRiskAreas;
import com.gnorizon.solutions.pandemic_aid.fragment.Home;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


       // FirebaseMessaging.getInstance().subscribeToTopic("General");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        //navigationView.setItemBackground(R.drawable.box);
        if (savedInstanceState==null){
            getHomeFragment();
        }
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


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        displaySelectedScreen(id);


        return true;
    }

    private void displaySelectedScreen(int itemId) {

        Fragment fragment = null;
        Class fragmentClass = null;


        //initializing the fragment object which is selected
        switch (itemId) {

            case R.id.nav_home:
                fragmentClass = Home.class;
                getSupportActionBar().setTitle("Home");
                break;

            case R.id.nav_contact_channel:
                Intent contactChannel = new Intent(MainActivity.this, ContactChannels.class);
                startActivity(contactChannel);

                break;

            case R.id.nav_about_us:
                Intent my_profile = new Intent(MainActivity.this, AboutUs.class);
                startActivity(my_profile);
                break;

            case R.id.nav_high_risk:
                fragmentClass = HighRiskAreas.class;
                getSupportActionBar().setTitle("High Risk Areas");
                break;
            case R.id.nav_report:
                Intent report = new Intent(MainActivity.this, ReportACase.class);
                startActivity(report);
                break;

            case R.id.nav_take_test:
                Intent contact = new Intent(MainActivity.this, TakeTest.class);
                startActivity(contact);
                break;

            case R.id.nav_about:
                fragmentClass = AboutCovid19.class;
                getSupportActionBar().setTitle("About Covid-19");
                break;

            case R.id.nav_contact_us:
                Intent toContact = new Intent(MainActivity.this, ContactUs.class);
                //toContact.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(toContact);
                break;

            case R.id.nav_share_app:

                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "PandemicAid");
                    String shareMessage= "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    e.printStackTrace();
                }
                break;

            default:
                fragmentClass = Home.class;
                getSupportActionBar().setTitle("Home");
                break;

        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }


        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_layout, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void getHomeFragment() {

        Fragment home = new Home();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out);
        getSupportActionBar().setTitle("Home");
        fragmentTransaction.replace(R.id.frame_layout, home);
        fragmentTransaction.commit();
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    public void toGallery(){
        String url = "https://photos.app.goo.gl/XuXxQrqDA43Lpk2c9";

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

        boolean canOpen = browserIntent.resolveActivity(getPackageManager()) != null;
        if (canOpen) {
            startActivity(browserIntent);
        }

    }
}
