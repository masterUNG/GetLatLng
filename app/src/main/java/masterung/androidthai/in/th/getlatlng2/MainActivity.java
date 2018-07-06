package masterung.androidthai.in.th.getlatlng2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private LocationManager locationManager;
    private Criteria criteria;
    private double latADouble, lngADouble;
    private boolean aBoolean = false;
    private Button button;

    private int secAnInt = 2000;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            Log.d("6JulyV1", "Work In Thread");
            if (aBoolean) {
                Log.d("6JulyV1", "Work True");
                updateGPS();
            }

            handler.postDelayed(runnable, secAnInt);
        }   // run
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Setup Location
        setupLocation();

        updateController();

//        Start Controller
        startController();

//        Stop Controller
        stopController();

    }   // Main Method

    private void stopController() {
        Button button = findViewById(R.id.btnStopGPS);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationManager.removeUpdates(locationListener);
            }
        });
    }

    private void startController() {
        button = findViewById(R.id.btnStart);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                aBoolean = !aBoolean;
                Log.d("6JulyV1", "aBoolent ==> " + aBoolean);
                myLoop();

            }
        });
    }

    private void myLoop() {

        if (aBoolean) {
            button.setText("Stop");

            updateGPS();
            handler.postDelayed(runnable, secAnInt);


        } else {
            button.setText("Start");
            handler.removeCallbacks(runnable);
        }


    }

    private void setupLocation() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
    }

    private void updateController() {
        Button button = findViewById(R.id.btnUpdate);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateGPS();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //granted
                } else {
                    //not granted
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    //What is permission be request
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET,Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.BLUETOOTH,Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_CONTACTS}, 101);

    }

    //Check the permission is already have
    private boolean checkIfAlreadyhavePermission() {

        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onResume() {
        super.onResume();

//        updateGPS();


    }

    private void updateGPS() {
        //        for NetWork
        Location networkLocation = myFindLocation(LocationManager.NETWORK_PROVIDER);
        if (networkLocation != null) {
            latADouble = networkLocation.getLatitude();
            lngADouble = networkLocation.getLongitude();
        }

//        for GPS Card
        Location gpsLocation = myFindLocation(LocationManager.GPS_PROVIDER);
        if (gpsLocation != null) {
            latADouble = gpsLocation.getLatitude();
            lngADouble = gpsLocation.getLongitude();
        }

        showView();
    }

    private void showView() {

        TextView latTextView = findViewById(R.id.txtLat);
        TextView lngTextView = findViewById(R.id.txtLng);

        latTextView.setText("Lat ==> " + Double.toString(latADouble));
        lngTextView.setText("Lng ==> " + Double.toString(lngADouble));

    }

    @Override
    protected void onStop() {
        super.onStop();

        locationManager.removeUpdates(locationListener);

    }

    public Location myFindLocation(String strProvider) {

        Location location = null;
        if (locationManager.isProviderEnabled(strProvider)) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }
            locationManager.requestLocationUpdates(strProvider, 1000, 10, locationListener);
            location = locationManager.getLastKnownLocation(strProvider);

        }   // if

        return location;
    }

    public LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            latADouble = location.getLatitude();
            lngADouble = location.getLongitude();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };



}   // Main Class
