package tecnico.cmu.ubibikeapp;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by david on 11-05-2016.
 */
public class SearchStationsActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_stations);

        //SupportMapFragment mapFragment =
        //        (SupportMapFragment) getFragmentManager().findFragmentById(R.id.stations_map);
        //mapFragment.getMapAsync(this);


    }


}
