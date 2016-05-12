package tecnico.cmu.ubibikeapp;

import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        trajectoryID = getIntent().getStringExtra("_id");

        mDistance = (TextView) findViewById(R.id.trajectory_distance);
        mPoints = (TextView) findViewById(R.id.trajectory_points);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap map) {
        Log.d(TAG, "Map ready");
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
                    for(Coordinate coordinate: trajectory.getCoordinates()){
                        Log.d(TAG, "Adding coordinate: " + coordinate.getPosition());
                        polylineOptions.add(coordinate.getPosition());
                    }
                    map.addPolyline(polylineOptions);
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

}
