package tecnico.cmu.ubibikeapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tecnico.cmu.ubibikeapp.model.Bike;
import tecnico.cmu.ubibikeapp.network.API;
import tecnico.cmu.ubibikeapp.network.ResponseCallback;

public class BookingActivity extends AppCompatActivity {

    ListView listView;
    BikeAdapter bikeAdapter;
    private String stationName, stationId;
    private final String TAG = "BookingActivity";
    private List<Bike> bikes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extra = getIntent().getExtras();
        stationName = extra.getString("stationName");
        stationId = extra.getString("stationId");
        Log.d(TAG, "station name: " + stationName + ", stationId: " + stationId);

        setContentView(R.layout.activity_booking);

        listView = (ListView)findViewById(R.id.bikelist);
        bikes = new ArrayList<>();
        bikeAdapter = new BikeAdapter(this, R.layout.biketable, bikes);
        listView.setAdapter(bikeAdapter);

        API api = new API();
        api.getStationInfo(stationId, new ResponseCallback() {
            @Override
            public void onDataReceived(JSONObject response) {
                try {
                    JSONArray data = response.getJSONArray("bikes");
                    Log.d(TAG, data.toString());
                    for(int i=0; i<data.length(); i++){
                        JSONObject b = (JSONObject) data.get(i);
                        Bike bike = new Bike(b.getString("_id"), b.getString("name"), b.getString("station"), b.getString("reservedBy") == null);
                        bikes.add(bike);
                    }
                    bikeAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {
                //TODO: Show error message
            }
        });

    }

}
