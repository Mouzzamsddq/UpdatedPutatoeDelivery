package com.example.updatedputatoedelivery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.example.updatedputatoedelivery.HelperClasses.DirectionsJSONParser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final int PERMISSION_REQUEST_CODE = 9001;
    public static final int PLAY_SERVICES_ERROR_CODE = 9002;
    public static final int GPS_REQUEST_CODE = 9003;
    public boolean mLocationPermissionGranted;


    //create a google map reference variable
    private GoogleMap mGoogleMap;
    private LatLng mOrigin;
    private LatLng mDestination;
    private Polyline mPolyline;
    private LatLng origin, destination;


    //create reference of FusedLocationProviderClient
    private FusedLocationProviderClient mFusedLocationProviderClient;

    //create LocationCallBack Reference
    private LocationCallback mLocationCallback;


    //create a origin marker obejct
    private Marker originMarker;

    //boolean  variable to move camera
    private boolean isMove= true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //method to init all the requirements to run google map
        initGoogleMap();


        //initialize the fused location provider client object
        mFusedLocationProviderClient = new FusedLocationProviderClient(this);

        //initialize the mLocationCallBack reference
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }

                Location location = locationResult.getLastLocation();
                //update mOrigin when location update
                origin = new LatLng(location.getLatitude() , location.getLongitude());
                mOrigin = origin;
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mOrigin,18);
                mGoogleMap.animateCamera(cameraUpdate);
                if(originMarker != null)
                {
                    originMarker.remove();
                }

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title("origin");
                markerOptions.position(origin);
                originMarker = mGoogleMap.addMarker(markerOptions);
                drawRoute();
                Toast.makeText(MainActivity.this, "Device Current Location:" + location.getLatitude() + "\n" + location.getLongitude(), Toast.LENGTH_SHORT).show();



            }
        };


        //set khala bazar for destiation
        destination = new LatLng(26.8189, 80.9079);


        // Get a handle to the fragment and register the callback.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.googleMapFragment);
        mapFragment.getMapAsync(this);


        //this method gives device location updates
        getLocationUpdates();


    }


    //this method get device location updates
    private void getLocationUpdates() {
        // create LocationRequest object
        LocationRequest locationRequest = LocationRequest.create();

        //set all the properties
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(1000);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            return;
        }
        mFusedLocationProviderClient.requestLocationUpdates(locationRequest, mLocationCallback, null);
    }


    //get device current location
    private void getDeviceCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {


                //if task is successful
                if (task.isSuccessful()) {
                    Location location = task.getResult();
                    goToLocation(location.getLatitude(), location.getLongitude());
                }
                else
                {
                    //if task is not succesful
                    Log.d("Error","get Current Location Error:"+task.getException().getMessage());

                }

            }
        });
    }

    //this method move map user current location with give lat and lng
    private void goToLocation(double latitude  , double longitude)
    {

        CameraUpdate  cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude),18);
        mGoogleMap.animateCamera(cameraUpdate);

    }


    //this method is used to show marker on the map
    private void showMarker(double latitude, double longitude)
    {
        //show marker on device current location
        MarkerOptions markerOptions = new MarkerOptions();

        //set marker title and position
        markerOptions.title("Device Location");
        markerOptions.position(new LatLng(latitude,longitude));

        mGoogleMap.addMarker(markerOptions);

    }

    //this method ise used to check all the permission and required services from device to run google map
    private void initGoogleMap() {
        if (isServiceOk()) {
            if (isGpsEnabled()) {
                if (checkLocationPermission()) {
                    Toast.makeText(this, "Ready to Map", Toast.LENGTH_SHORT).show();


                } else
                    requestPermission();
            }
        }

    }


    // this method is used to check gps is enabled or not
    private boolean isGpsEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean providerEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (providerEnabled) {
            return true;
        } else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("GPS Permissions")
                    .setMessage("GPS is required for getting your current location")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent, GPS_REQUEST_CODE);
                        }
                    }).setCancelable(false)
                    .create().show();
        }

        return false;
    }


    //this method  called when user enabled the gps
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GPS_REQUEST_CODE) {


            if (isGpsEnabled()) {
                Toast.makeText(this, "GPS is enabled", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "GPS is not enabled, unable to show user location", Toast.LENGTH_SHORT).show();
            }
        }
    }


    //this method check google play services available on the device or not
    private boolean isServiceOk() {

        //check google play services availbale on device
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();

        int result = googleApiAvailability.isGooglePlayServicesAvailable(this);

        if (result == ConnectionResult.SUCCESS) {
            return true;
        } else if (googleApiAvailability.isUserResolvableError(result)) {
            Dialog dialog = googleApiAvailability.getErrorDialog(this, result, PLAY_SERVICES_ERROR_CODE, task ->
                    Toast.makeText(this, "Dialog is cancelled  by the user", Toast.LENGTH_SHORT).show());
            dialog.show();
        } else {
            Toast.makeText(this, "google play services required to use this feature", Toast.LENGTH_SHORT).show();
        }

        return false;

    }

    //this method check location permission
    private boolean checkLocationPermission() {

        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }


    //when permission is not granted, request for runtime permission
    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
            }
        }
    }


    //when user granted the permission, this method called
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Permission not granted !", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        //initialize the reference variable
        mGoogleMap = googleMap;

        //create destination marker
        MarkerOptions destinationMarker = new MarkerOptions();
        //set title and marker position
        destinationMarker.title("destination").position(destination);


        mGoogleMap.addMarker(destinationMarker);
        mDestination = destination;

//        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(mOrigin , 14);
//        mGoogleMap.animateCamera(location);

//        drawRoute();

//        getDeviceCurrentLocation();




    }


    private void drawRoute() {

        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(mOrigin, mDestination);

        DownloadTask downloadTask = new DownloadTask();
        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
    }


    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Key
        String key = "key=" + getString(R.string.google_maps_direction_api_key);

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }


    /**
     * A class to download data from Google Directions URL
     */
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("DownloadTask", "DownloadTask : " + data);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception on download", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    /** A class to parse the Google Directions in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }
        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.BLUE);
            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null) {
                if(mPolyline != null){
                    mPolyline.remove();
                }
                mPolyline = mGoogleMap.addPolyline(lineOptions);

            }else
                Toast.makeText(getApplicationContext(),"No route is found", Toast.LENGTH_LONG).show();
        }


    }


    //when app goes on background
    @Override
    protected void onPause() {
        super.onPause();

        //when device is on background stop taking location updates
        if(mLocationCallback != null)
        {
            mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
        }

    }


    //when device come on running state , then start taking location updates
    @Override
    protected void onResume() {
        super.onResume();
        getLocationUpdates();
    }
}