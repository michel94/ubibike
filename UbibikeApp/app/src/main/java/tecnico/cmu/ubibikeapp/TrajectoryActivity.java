package tecnico.cmu.ubibikeapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.w3c.dom.Text;

import tecnico.cmu.ubibikeapp.model.Coordinate;
import tecnico.cmu.ubibikeapp.model.ResponseTrajectory;
import tecnico.cmu.ubibikeapp.model.Trajectory;
import tecnico.cmu.ubibikeapp.network.API;
import tecnico.cmu.ubibikeapp.network.ResponseCallback;

/**
 * Created by david on 30-04-2016.
 */
public class TrajectoryActivity extends AppCompatActivity
    implements OnMapReadyCallback{

    private String trajectoryID;
    private static final String TAG = "TrajectoryActivity";

    private TextView mDistance;
    private TextView mPoints;

    private GoogleMap mGoogleMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        trajectoryID = getIntent().getStringExtra("_id");

        mDistance = (TextView) findViewById(R.id.trajectory_distance);
        mPoints = (TextView) findViewById(R.id.trajectory_points);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap map) {
        Log.d(TAG, "Map ready");
        mGoogleMap = map;
        getTrajectoryFromServer(map);
    }

    private void getTrajectoryFromServer(final GoogleMap map){
        API api = new API();

        api.getTrajectory(trajectoryID, new ResponseCallback() {
            @Override
            public void onDataReceived(JSONObject response) {
                Gson gson = new Gson();
                Log.d(TAG, response.toString());
                ResponseTrajectory responseTrajectory = gson.fromJson(response.toString(), ResponseTrajectory.class);

                if(responseTrajectory.isSuccess()){
                    PolylineOptions polylineOptions = new PolylineOptions();
                    Trajectory trajectory = responseTrajectory.getTrajectories().get(0);
                    Log.d(TAG, trajectory.toString());
                    mDistance.setText(Double.toString(trajectory.getDistance()));
                    mPoints.setText(Integer.toString(trajectory.getPoints()));
                    LatLngBounds.Builder bounds = new LatLngBounds.Builder();
                    for(Coordinate coordinate: trajectory.getCoordinates()){
                        Log.d(TAG, "Adding coordinate: " + coordinate.getPosition());
                        polylineOptions.add(coordinate.getPosition());
                        bounds.include(coordinate.getPosition());
                    }
                    map.addPolyline(polylineOptions);
                    zoomMap(bounds.build());
                } else {
                    Log.d(TAG, "Error getting trajectory");
                }
            }

            @Override
            public void onError(Exception e) {
                Log.d(TAG, "Error connectivity");
            }
        });
    }

    private void zoomMap(LatLngBounds bounds){
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bounds.getCenter(), 10));
    }


    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    Log.d(TAG, "Recreating Main Activity");
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(this)
                            // Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(upIntent)
                            // Navigate up to the closest parent
                            .startActivities();
                } else {
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.
                    Log.d(TAG, "Simply going up");
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
}
