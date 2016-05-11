package tecnico.cmu.ubibikeapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tecnico.cmu.ubibikeapp.model.Bike;
import tecnico.cmu.ubibikeapp.model.Message;
import tecnico.cmu.ubibikeapp.network.API;
import tecnico.cmu.ubibikeapp.network.DataHandler;
import tecnico.cmu.ubibikeapp.network.Peer;
import tecnico.cmu.ubibikeapp.network.ResponseCallback;
import tecnico.cmu.ubibikeapp.network.WDService;

public class BookingActivity extends AppCompatActivity {

    ListView listView;
    ArrayAdapter<Bike> bikeAdapter;
    private String stationName, stationId;
    private final String TAG = "BookingActivity";
    private List<Bike> bikes;
    private Bike selected = null;
    private API api = new API();
    private WDService wdservice=null;

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
        bikeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bikes);
        listView.setAdapter(bikeAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
                selected = bikes.get(position);
                Log.d(TAG, "Please");
            }
        });

        Button b = (Button) findViewById(R.id.bookBtn);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selected != null && wdservice != null)
                    api.requestBike(Utils.getUserID(v.getContext()), selected.getBikeId(), new ResponseCallback() {
                        @Override
                        public void onDataReceived(JSONObject response) {
                            try {
                                Log.d(TAG, "Response: " + response.toString());
                                if(response.getBoolean("success") == true) {
                                    Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                                    Utils.setCurrentBike(getApplicationContext(), selected.getBikeId());
                                    wdservice.getMoveManager().setCurrentBike(selected.getBikeId());
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.d(TAG, "error");
                        }
                    });
            }
        });

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

    public void onStart(){
        super.onStart();
        Intent intent = new Intent(getApplicationContext(), WDService.class);
        bindService(intent, serviceConn, Context.BIND_AUTO_CREATE);
    }

    protected void onStop(){
        super.onStop();
        unbindService(serviceConn);
    }

    private ServiceConnection serviceConn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder serviceBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            WDService.LocalBinder binder = (WDService.LocalBinder) serviceBinder;
            wdservice = binder.getService();

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            wdservice = null;
        }
    };

}
