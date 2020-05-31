package com.gnorizon.solutions.pandemic_aid.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.gnorizon.solutions.pandemic_aid.R;
import com.gnorizon.solutions.pandemic_aid.fragment.ContactChannels;
import com.gnorizon.solutions.pandemic_aid.helpers.JsonParser;
import com.gnorizon.solutions.pandemic_aid.helpers.ResponseModel;
import com.gnorizon.solutions.pandemic_aid.helpers.Util;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.ikhiloyaimokhai.nigeriastatesandlgas.Nigeria;
import com.jacksonandroidnetworking.JacksonParserFactory;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.valdesekamdem.library.mdtoast.MDToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportACase extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener, OnMapReadyCallback  {

    private AppCompatActivity activity = ReportACase.this;
    private final static int PLAY_SERVICES_REQUEST = 100;
    private static final int RC_SETTING = 101;
    private static final String TAG = MainActivity.class.getSimpleName();
    private Location mLocation;
    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;
    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = true;
    private LocationRequest mLocationRequest;
    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters
    private GoogleMap mMap;
    private int LOCATION_PERMISSION_CODE = 130;
    //Bundle data
    private Location location;
    private double latitude, longitude;
    private KProgressHUD hud;
    private Spinner mStateSpinner, mLgaSpinner;
    private String mState, mLga;
    private List<String> states;
    private static final int SPINNER_HEIGHT = 500;

    private CheckBox val1, val2,val3, val4,val5;
    private EditText etname, etphone, etComment;
    private int dry_cough=0, sore_throat=0, have_traveled=0,travel_history= 0,direct_contact=0;
    private String comment;
    private String name,phone;
    Button submit_report;
    private Util util;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidNetworking.initialize(getApplicationContext());
        AndroidNetworking.setParserFactory(new JacksonParserFactory());
        setContentView(R.layout.activity_report_a_case);
        util = new Util();

        initControls();
        // First we need to check availability of play services
        if (checkPlayServices()) {
            // Building the GoogleApi client
            buildGoogleApiClient();
            createLocationRequest();

        }

        hud = KProgressHUD.create(ReportACase.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please Wait")
                .setDetailsLabel("Sending Your Report")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .setBackgroundColor(Color.BLACK)
                .setAutoDismiss(true);

        etphone = findViewById(R.id.report_number);
        etname = findViewById(R.id.report_name);
        val1 = findViewById(R.id.report_cough);
        val2 = findViewById(R.id.report_sore);
        val3 = findViewById(R.id.report_recent_travel);
        val4 = findViewById(R.id.report_travel_history);
        val5 = findViewById(R.id.report_direct_contact_text);
        etComment = findViewById(R.id.report_comment);

        submit_report = findViewById(R.id.bt_report_case);

        mStateSpinner = findViewById(R.id.stateSpinner1);
        mLgaSpinner = findViewById(R.id.lgaSpinner1);
        resizeSpinner(mStateSpinner, SPINNER_HEIGHT);
        resizeSpinner(mLgaSpinner, SPINNER_HEIGHT);

        states = Nigeria.getStates();

        //call to method that'll set up state and lga spinner
        setupSpinners();

        submit_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = etname.getText().toString().trim();
                phone = etphone.getText().toString().trim();
                comment = etComment.getText().toString().trim();
                if (name.isEmpty()) {
                    etname.setError("Pls give your name");
                } else if (phone.isEmpty() || !phone.startsWith("0")) {
                    etphone.setError("Pls give a valid number");

                }  else {
                    if (util.isNetworkAvailable(ReportACase.this)){
                        try {
                            int score = sore_throat+direct_contact+have_traveled+dry_cough+travel_history;
                            String lng = Double.toString(longitude);
                            String lat = Double.toString(latitude);
                            String scr = Integer.toString(score);

                            hud.show();
                            AndroidNetworking.post("https://reportcovid19.org/api/sendreport")
                                    .addBodyParameter("name", name)
                                    .addBodyParameter("phone_number", phone)
                                    .addBodyParameter("state", mState)
                                    .addBodyParameter("lga", mLga)
                                    .addBodyParameter("lng",lng)
                                    .addBodyParameter("lat", lat)
                                    .addBodyParameter("score", scr)
                                    .setPriority(Priority.MEDIUM)
                                    .build()
                                    .getAsJSONObject(new JSONObjectRequestListener() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            hud.dismiss();

                                            try {
                                                JSONObject json = new JSONObject(String.valueOf(response));
                                                JSONObject res = json.getJSONObject("data");
                                                String report = res.getString("report");
                                                String message = res.getString("message");
                                                String name = res.getString("name");

                                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ReportACase.this);
                                                alertDialog.setTitle(message);

                                                alertDialog.setMessage("Hello " +name+" "+report);

                                                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog,int which) {
                                                        Intent contactChannel = new Intent(ReportACase.this, MainActivity.class);
                                                        contactChannel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        contactChannel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        startActivity(contactChannel);
                                                        dialog.cancel();
                                                        finish();
                                                    }
                                                });
                                                alertDialog.show();

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        @Override
                                        public void onError(ANError error) {
                                            hud.dismiss();
                                            MDToast.makeText(ReportACase.this,error.getMessage(),
                                                    MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();                                        }
                                    });


                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    } else {
                        MDToast.makeText(ReportACase.this,"No Network Connection",
                                MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR).show();
                    }
                }
                }
            });


        }

    private void initControls() {
// Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.report_cough:
                dry_cough = checked? 1:0;
                break;
            case R.id.report_sore:
                sore_throat = checked? 1:0;
                break;
            case R.id.report_recent_travel:
                have_traveled = checked? 3:0;
                break;
            case R.id.report_travel_history:
                travel_history = checked? 4:0;
                break;
            case R.id.report_direct_contact_text:
                direct_contact = checked? 5:0;
                break;
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latLng = new LatLng(-34, 151);
        if (location != null) {
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
        }
        mMap.addMarker(new MarkerOptions().position(latLng).title("My Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        if (mMap.getCameraPosition().zoom <= 9) {
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 15);
            mMap.moveCamera(update);
        } else {
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, mMap.getCameraPosition().zoom);
            mMap.moveCamera(update);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        try {
            if (mGoogleApiClient != null) {
                mGoogleApiClient.connect();
            }

        }catch (Exception e){

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
// Resuming the periodic location updates
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void displayLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//            tvLocation.setText("Location permission for this app is not granted");
        } else {
            mLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
            if (mLocation != null) {
                latitude = mLocation.getLatitude();
                longitude = mLocation.getLongitude();
                Log.d("longitude",""+longitude);
                LatLng latLng = new LatLng(latitude, longitude);
                if (mMap != null) {
                    mMap.clear();
// Location Changed
                    mMap.addMarker(new MarkerOptions().position(latLng).title("Current Location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    if (mMap.getCameraPosition().zoom <= 9) {
                        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                        mMap.moveCamera(update);
                    } else {
                        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, mMap.getCameraPosition().zoom);
                        mMap.moveCamera(update);
                    }
                }
            }
        }
    }


    /**
     * Creating google api client object
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    /**
     * Creating location request object
     */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    /**
     * Method to verify google play services on the device
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Starting the location updates
     */
    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            tvLocation.setText("Location permission for this app is not granted");
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }
    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {
// Once connected with google api, get the location
        displayLocation();
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
// Assign the new location
        mLocation = location;
// Displaying the new location on UI
        displayLocation();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SETTING && resultCode == RESULT_OK) {
            mRequestingLocationUpdates = true;
// Starting the location updates
            startLocationUpdates();
        }
    }

    public void setupSpinners() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        //populates the quantity spinner ArrayList

        ArrayAdapter<String> statesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, states);

        // Specify dropdown layout style - simple list view with 1 item per line
        statesAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        // Apply the adapter to the spinner
        statesAdapter.notifyDataSetChanged();
        mStateSpinner.setAdapter(statesAdapter);

        // Set the integer mSelected to the constant values
        mStateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mState = (String) parent.getItemAtPosition(position);
                setUpStatesSpinner(position);
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Unknown
            }
        });
    }


    /**
     * method to set up the state spinner
     *
     * @param position current position of the spinner
     */
    private void setUpStatesSpinner(int position) {
        List<String> list = new ArrayList<>(Nigeria.getLgasByState(states.get(position)));
        setUpLgaSpinner(list);
    }


    /**
     * Method to set up the local government areas corresponding to selected states
     *
     * @param lgas represents the local government areas of the selected state
     */
    private void setUpLgaSpinner(List<String> lgas) {

        ArrayAdapter lgaAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lgas);
        lgaAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        lgaAdapter.notifyDataSetChanged();
        mLgaSpinner.setAdapter(lgaAdapter);

        mLgaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                mLga = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void resizeSpinner(Spinner spinner, int height) {
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            //Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinner);

            //set popupWindow height to height
            popupWindow.setHeight(height);

        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }



}
