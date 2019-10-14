package com.example.nikita.gpslogger;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements LocationListener {

    TextView mLatitude, mLongitude, mSpeed;
    Button startButton, stopButton, viewMapButton;
    LocationManager locationManager;
    Location mLocation = null;
    String bestProvider, fileName, _latitude, _longitude, _speed;
    ArrayList<String[] > locations = new ArrayList <>();
    Timer timer = new Timer();
    FileWriter writer = null;

    public class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            new GPSAsyncTask().execute();
        }
    }

    public class GPSAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            locations.add(new String[]{_latitude, _longitude, _speed});
            System.out.println("background task: " + _latitude + " " + _longitude + " " + _speed);
            return null;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLatitude = (TextView) findViewById(R.id.txtLat);
        mLongitude = (TextView) findViewById(R.id.txtLong);
        mSpeed = (TextView) findViewById(R.id.txtSpeed);
        startButton = (Button) findViewById(R.id.startButton);
        stopButton = (Button) findViewById(R.id.stopButton);
        viewMapButton = (Button) findViewById(R.id.viewMap);
//        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//        Criteria criteria = new Criteria();
//        bestProvider = locationManager.getBestProvider(criteria, true);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                requestPermissions(new String[]{
//                        Manifest.permission.INTERNET,
//                        Manifest.permission.ACCESS_COARSE_LOCATION,
//                        Manifest.permission.ACCESS_FINE_LOCATION,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        Manifest.permission.READ_EXTERNAL_STORAGE
//                }, 1);
//            } //else {}
//            return;
//        }
//        mLocation = locationManager.getLastKnownLocation(bestProvider);
//        if (mLocation != null) {
//            onLocationChanged(mLocation);
//        }
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                }, 1);
            } //else {}
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 0, this);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, this);


        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Started logging", Toast.LENGTH_LONG).show();
                TimerTask timerTask = new MyTimerTask();
                timer.schedule(timerTask, 0, 1000);
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                WriteToCsv writeToCsv = new WriteToCsv(locations);
                try {
                writeToCsv.write();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
        });

        viewMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CSVList.class);
                startActivity(intent);

            }
        });

    }

    @Override
    public void onLocationChanged(Location location) {
        _latitude = Double.toString(location.getLatitude());
        _longitude = Double.toString(location.getLongitude());
        _speed = Float.toString(location.getSpeed());
        mLatitude.setText(_latitude);
        mLongitude.setText(_longitude);
        mSpeed.setText(_speed);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) { }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 0, this);
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, this);
                }
        }
    }

}
