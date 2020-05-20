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
import android.os.Bundle;
import android.provider.Settings;
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

import com.gnorizon.solutions.pandemic_aid.R;
import com.gnorizon.solutions.pandemic_aid.fragment.ContactChannels;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ikhiloyaimokhai.nigeriastatesandlgas.Nigeria;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TakeTest extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener, OnMapReadyCallback  {

    private AppCompatActivity activity = TakeTest.this;
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
    private Spinner mStateSpinner, mLgaSpinner;
    private String mState, mLga;
    private List<String> states;
    private static final int SPINNER_HEIGHT = 500;

    private RadioGroup val1, val2,val3, val4,val5,val6,val7,val8,val9,val10,val11,val12;
    private EditText etname, etphone;
    private String dry_cough="No", diarrhea="No", cold="No", sore_throat="No", headache="No", fatigue="No",high_temp="No",difficult_breathing="No", body_aches="No", have_traveled="No";
    private String travel_history="No", direct_contact="No";
    private String name,phone;
    private Button submit_test;
    private KProgressHUD hud;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_test);

        initControls();
        // First we need to check availability of play services
        if (checkPlayServices()) {
            // Building the GoogleApi client
            buildGoogleApiClient();
            createLocationRequest();

        }

        etphone = findViewById(R.id.test_number);
        etname = findViewById(R.id.test_name);
        val1 = findViewById(R.id.cough_group);
        val2 = findViewById(R.id.diarrhea_group);
        val3 = findViewById(R.id.cold_group);
        val4 = findViewById(R.id.sore_group);
        val5 = findViewById(R.id.aches_group);
        val6 = findViewById(R.id.fatigue_group);
        val7 = findViewById(R.id.fever_group);
        val8 = findViewById(R.id.difficult_group);
        val9 = findViewById(R.id.body_aches_group);
        val10 = findViewById(R.id.recent_group);
        val11 = findViewById(R.id.history_group);
        val12 = findViewById(R.id.direct_group);
        submit_test = findViewById(R.id.submit_test);

        val1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i==R.id.dry_cough_yes){
                    dry_cough="Yes";
                } else if (i==R.id.dry_cough_no){
                    dry_cough="No";
                }

            }
        });
        val2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i==R.id.diarrhea_yes){
                    diarrhea="Yes";
                } else if (i==R.id.diarrhea_no){
                    diarrhea="No";
                }

            }
        });
        val3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i==R.id.cold_yes){
                    cold="Yes";
                } else if (i==R.id.cold_no){
                    cold="No";
                }

            }
        });
        val4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i==R.id.sore_yes){
                    sore_throat="Yes";
                } else if (i==R.id.sore_no){
                    sore_throat="No";
                }

            }
        });
        val5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i==R.id.aches_yes){
                    headache="Yes";
                } else if (i==R.id.aches_no){
                    headache="No";
                }

            }
        });
        val6.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i==R.id.fatigue_yes){
                    fatigue="Yes";
                } else if (i==R.id.fatigue_no){
                    fatigue="No";
                }

            }
        });
        val7.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i==R.id.fever_yes){
                    high_temp ="Yes";
                } else if (i==R.id.fever_no){
                    high_temp="No";
                }

            }
        });
        val8.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i==R.id.difficult_yes){
                    difficult_breathing="Yes";
                } else if (i==R.id.difficult_no){
                    difficult_breathing="No";
                }

            }
        });
        val9.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i==R.id.body_aches_yes){
                    body_aches="Yes";
                } else if (i==R.id.body_aches_no){
                    body_aches="No";
                }

            }
        });
        val10.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i==R.id.recent_yes){
                    have_traveled="Yes";
                } else if (i==R.id.recent_no){
                    have_traveled="No";
                }

            }
        });
        val11.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i==R.id.history_yes){
                    travel_history="Yes";
                } else if (i==R.id.history_no){
                    travel_history="No";
                }

            }
        });
        val12.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i==R.id.direct_yes){
                    direct_contact="Yes";
                } else if (i==R.id.direct_no){
                    direct_contact="No";
                }

            }
        });


        mStateSpinner = findViewById(R.id.stateSpinner);
        mLgaSpinner = findViewById(R.id.lgaSpinner);
        resizeSpinner(mStateSpinner, SPINNER_HEIGHT);
        resizeSpinner(mLgaSpinner, SPINNER_HEIGHT);

        states = Nigeria.getStates();

        //call to method that'll set up state and lga spinner
        setupSpinners();
        hud = KProgressHUD.create(TakeTest.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please Wait")
                .setDetailsLabel("Sending Your Test")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .setBackgroundColor(Color.BLACK)
                .setAutoDismiss(true);



        submit_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = etname.getText().toString().trim();
                phone = etphone.getText().toString().trim();

                if (name.isEmpty()){
                    etname.setError("Pls give your name");
                } else  if (phone.isEmpty()|| !phone.startsWith("0")) {
                    etphone.setError("Pls give a valid number");
                }
                else
                    {
                    hud.show();

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    Map<String, Object> test = new HashMap<>();
                    test.put("Has Dry Cough", dry_cough);
                    test.put("Has Diarrhea", diarrhea);
                    test.put("Has Cold", cold);
                    test.put("Has Sore Throat", sore_throat);
                    test.put("Has Headache", headache);
                    test.put("Has Fatigue", fatigue);
                    test.put("Has High Temperature", high_temp);
                    test.put("Has Difficulty Breathing", difficult_breathing);
                    test.put("Has Body Ache", body_aches);
                    test.put("Had Travelled Recently", have_traveled);
                    test.put("Had Travelled to a Covid-19 Area", travel_history);
                    test.put("Had Direct Contact with a COVID-19 Patient", direct_contact);
                    test.put("name", name);
                    test.put("phone", phone);
                    test.put("state", mState);
                    test.put("lga", mLga);
                    test.put("longitude", longitude);
                    test.put("latitude", latitude);

                    db.collection("Test")
                            .add(test)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    hud.dismiss();
                                    if (have_traveled.equals("Yes") || travel_history.equals("Yes") || direct_contact.equals("Yes") && sore_throat.equals("Yes") && body_aches.equals("Yes")){
                                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(TakeTest.this);
                                        alertDialog.setTitle("Test Submitted Successfully");

                                        alertDialog.setMessage("From Your test Result you are Suspected to have symptoms of COVID-19. Pls Contact NCDC for proper Check Up");

                                        alertDialog.setPositiveButton("Ok Contact NCDC", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,int which) {
                                                Intent contactChannel = new Intent(TakeTest.this, ContactChannels.class);
                                                startActivity(contactChannel);
                                                dialog.cancel();
                                                finish();
                                            }
                                        });
                                        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent contactChannel = new Intent(TakeTest.this, MainActivity.class);
                                                contactChannel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                contactChannel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(contactChannel);
                                                finish();
                                                dialog.cancel();

                                            }
                                        });

                                        // Showing Alert Message
                                        alertDialog.show();
                                    } else {
                                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(TakeTest.this);
                                        alertDialog.setTitle("Test Submitted Successfully");

                                        alertDialog.setMessage("From your test result, you are free from COVID-19! Pls Stay Safe");

                                        alertDialog.setPositiveButton("Back to Home", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,int which) {
                                                Intent contactChannel = new Intent(TakeTest.this, MainActivity.class);
                                                contactChannel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                contactChannel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(contactChannel);
                                                dialog.cancel();
                                                finish();
                                            }
                                        });
                                        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent contactChannel = new Intent(TakeTest.this, MainActivity.class);
                                                contactChannel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                contactChannel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(contactChannel);
                                                dialog.cancel();
                                                finish();                                            }
                                        });

                                        // Showing Alert Message
                                        alertDialog.show();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    hud.dismiss();
                                    MDToast.makeText(TakeTest.this,"Connection not successful, pls try again",MDToast.LENGTH_LONG,MDToast.TYPE_ERROR).show();

                                }
                            });
                }
            }
        });


        Log.d("longitude",""+longitude);

    }

    private void initControls() {
// Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map1);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
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
