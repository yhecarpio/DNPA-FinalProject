package com.example.finalproject.Controllers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.example.finalproject.Models.TouristPlace;
import com.example.finalproject.R;
import com.example.finalproject.Views.MapsActivity;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MapsController {

    private static final String TAG = MapsActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    TouristPlacesLandmarksController touristPlaces;
    private GeofencingClient geofencingClient;
    private ArrayList<Geofence> geofenceList;
    private PendingIntent geofencePendingIntent;
    private Activity activity;

    public MapsController(Activity activity){
        this.activity = activity;
        touristPlaces = new TouristPlacesLandmarksController();
        geofenceList = new ArrayList<>();
        geofencePendingIntent = null;
        geofencingClient = LocationServices.getGeofencingClient(activity);
    }

    //Adding geofences
    @SuppressWarnings("MissingPermission")
    public void addGeofences() {
        if (!checkPermissions()) {
            showSnackbar(activity.getString(R.string.insufficient_permissions));
            return;
        }

        geofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent());
    }

    //Populating the map where Geofences are created with a specified radio, duration and transition type
    //This method extracts data from Constants file
    public void populateGeofenceList() {
        for (TouristPlace touristPlace: touristPlaces.getTouristPlaces()) {
            geofenceList.add(new Geofence.Builder()
                    // Set the request ID of the geofence. This is a string to identify this geofence.
                    .setRequestId(touristPlace.getName())

                    // Set the circular region of this geofence.
                    .setCircularRegion(
                            touristPlace.getLocation().latitude,
                            touristPlace.getLocation().longitude,
                            Constants.GEOFENCE_RADIUS_IN_METERS
                    )

                    // Set the expiration duration of the geofence. This geofence gets automatically
                    // removed after this period of time.
                    .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)

                    // Set the transition types of interest. Alerts are only generated for these
                    // transition. We track entry and exit transitions in this sample.
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT)
                    // Create the geofence.
                    .build());
        }
    }

    //Specifying Geofences to monitor and setting how to related geofence events are triggered
    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geofenceList);
        return builder.build();
    }

    //Defining a PendingIntent that starts a BroadcastReceiver
    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }
        Intent intent = new Intent(activity.getApplicationContext(), GeofenceBroadcastReceiver.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        geofencePendingIntent = PendingIntent.getBroadcast(activity.getApplicationContext(), 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }

    //Getting the last known location just to set the first view on the map
    public LatLng getLocation() {
        LocationManager locationManager = (LocationManager)
                activity.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        @SuppressLint("MissingPermission")
        Location location = locationManager.getLastKnownLocation(locationManager
                .getBestProvider(criteria, false));

        if(location!=null){
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            return new LatLng(latitude, longitude);
        }
        return null;

    }
    //Filling up the map with markers
    public void fillMarkers(GoogleMap mMap){
        for (TouristPlace touristPlace: touristPlaces.getTouristPlaces()) {
            mMap.addMarker(new MarkerOptions().position(touristPlace.getLocation()).title(touristPlace.getName()).snippet(touristPlace.getDescription()));
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(activity, marker.getTitle() + "seleccionado con éxito", Toast.LENGTH_SHORT).show();
                marker.showInfoWindow();
                return false;
            }
        });
    }



    //Checking permissions to use location
    public boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(activity.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    //Requesting permissions to use location
    public void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(activity,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(activity,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    //Using a SnackBar to show a simple message
    private void showSnackbar(final String text) {
        View container = activity.findViewById(android.R.id.content);
        if (container != null) {
            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
        }
    }

    //Using a SnackBar to show an elaborated message
    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                activity.findViewById(android.R.id.content),
                activity.getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(activity.getString(actionStringId), listener).show();
    }

}
