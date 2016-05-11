package tecnico.cmu.ubibikeapp;

import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.Locale;

/**
 * Created by david on 06-05-2016.
 */
public class LocationTrackerActivity extends ActionBarActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final String TAG = "LocationTrackerActivity";

    private static final int REQUEST_CHECK_SETTINGS = 1;
    private static final int MINIMUM_METERS = 1;
    private static final int LOCATION_REQUEST_INTERVAL = 10000;
    private static final int LOCATION_REQUEST_FASTEST_INTERVAL = 5000;

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private LocationRequest mLocationRequest;
    private boolean mRequestingLocationUpdates;

    private TextView mLatitudeText;
    private TextView mLongitudeText;
    private Button mStartStopButton;
    private Chronometer mChronometer;
    private State mTrackingState;
    private TextView mDistanceTracked;
    private float mTotalDistance;

    private enum State { RUNNING, PAUSED, STOPED }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_tracker);
        mLatitudeText = (TextView) findViewById(R.id.latitude_text);
        mLongitudeText = (TextView) findViewById(R.id.longitude_text);
        mStartStopButton = (Button) findViewById(R.id.start_button);
        mChronometer = (Chronometer) findViewById(R.id.bikeChronometer);
        mDistanceTracked = (TextView) findViewById(R.id.distance_tracked);
        mTotalDistance = 0;
        mTrackingState = State.STOPED;

        setButtonListeners();

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        createLocationRequest();
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            updateUI();
        }

        if (mRequestingLocationUpdates)
            startLocationUpdates();

    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Connection Suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "Connection Failed");
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(LOCATION_REQUEST_INTERVAL);
        mLocationRequest.setFastestInterval(LOCATION_REQUEST_FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                //final LocationSettingsStates = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can
                        // initialize location requests here.

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    LocationTrackerActivity.this,
                                    REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.

                        break;
                }
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        float[] results = new float[1];
        Location.distanceBetween(mLastLocation.getLatitude(),mLastLocation.getLongitude(),
                location.getLatitude(), location.getLongitude(), results);
        float distanceBetween = results[0];
        if(distanceBetween > MINIMUM_METERS) {
            mTotalDistance += distanceBetween;
            Log.d(TAG, "True, bigger than margin" + distanceBetween);
        }
        mLastLocation = location;
        updateUI();
        Log.d(TAG, "Location updated to " + location.toString());
        Toast.makeText(this, "Distance traveled: " + results[0], Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mGoogleApiClient.isConnected() && !mRequestingLocationUpdates)
            startLocationUpdates();
        mRequestingLocationUpdates = Utils.isRequestingLocationUpdates();
        mRequestingLocationUpdates = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveSettingsToPreferences();
        stopLocationUpdates();
    }

    private void saveSettingsToPreferences(){
        //Utils.saveLastLocation(mLastLocation);
        Utils.setIsRequestingLocationUpdates(mRequestingLocationUpdates);
        //Utils.setLastLocationUpdateTime(mLastUpdateTime);
    }

    private void updateUI() {
        mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
        mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
        mDistanceTracked.setText(String.format(Locale.getDefault(),"%.3f", mTotalDistance/1000));
    }

    private void setButtonListeners(){
        mStartStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTrackingState == State.RUNNING){
                    pauseTracking();
                } else if (mTrackingState == State.PAUSED){
                    startTracking();
                }
            }
        });

        mStartStopButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(mTrackingState == State.PAUSED){
                    stopTracking();
                }
                return true;
            }
        });
    }

    private void startTracking(){
        mTrackingState = State.RUNNING;
        mStartStopButton.setText(R.string.pause_button_text);
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.start();
    }

    private void pauseTracking(){
        mTrackingState = State.PAUSED;
        mStartStopButton.setText(R.string.stop_button_text);
        mChronometer.stop();
    }

    private void stopTracking(){
        mTrackingState = State.STOPED;
        mStartStopButton.setText(R.string.start_button_text);
        mChronometer.stop();
    }

}
