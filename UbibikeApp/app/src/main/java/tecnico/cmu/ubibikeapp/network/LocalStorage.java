package tecnico.cmu.ubibikeapp.network;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.ServiceConfigurationError;

/**
 * Created by michel on 5/6/16.
 */
public class LocalStorage {
    private static final String TAG = "LocalStorage";
    Context context;
    public JSONArray pendingData;
    public LocalStorage(Context context){
        pendingData = new JSONArray(); // TODO: load from shared preferences
        this.context = context;
    }

    public void putMessage(){

    }

    public void putTrip(ArrayList<LatLng> trajectory, int points){
        try {
            JSONObject jTrip = new JSONObject();
            jTrip.put("type", "trip");
            jTrip.put("points", points);

            JSONArray jTrajectory = new JSONArray();
            for(LatLng location : trajectory){
                JSONObject jLoc = new JSONObject();
                jLoc.put("latitude", location.latitude);
                jLoc.put("longitude", location.longitude);
                jTrajectory.put(jLoc);
            }
            jTrip.put("trajectory", jTrajectory);
            Log.d(TAG, "Storing trip with " + points + " earned points");

            pendingData.put(jTrip);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void extendTrips(JSONArray trips){
        String first = pendingData.toString();
        first = first.substring(0, first.length()-1);
        String second = trips.toString();
        second = second.substring(0, second.length()-1);
        try {
            pendingData = new JSONArray(first + ", " + second);
            Log.d("extendTrips", pendingData.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*public void putTransactions(ArrayList<Transaction> transactions){

    }*/
}