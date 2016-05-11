package tecnico.cmu.ubibikeapp.network;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by michel on 5/6/16.
 */
public class MoveManager {
    private String currentBikeId = null;
    private ArrayList<LatLng> trajectory = new ArrayList<>();
    private boolean bikeInRange = false;
    private String stationInRange = null;
    private LocalStorage localStorage;
    private final String TAG = "MoveManager";

    public MoveManager(LocalStorage localStorage){
        this.localStorage = localStorage;
    }

    public void setCurrentBike(String bikeId){
        Log.d(TAG, "Current bike: " + bikeId);
        currentBikeId = bikeId;
    }

    public String getCurrentBike() {
        return currentBikeId;
    }

    public void updatePeerList(ArrayList<String> peers){
        Log.d(TAG, "Received peer list");
        if(currentBikeId ==  null) { // no bike is currently requested
            return;
        }

        boolean hadBike = bikeInRange;
        bikeInRange = false;

        for(String peer: peers){
            Log.d(TAG, "Peer: " + peer);
            if(peer.startsWith("bike_")){
                Log.d(TAG, "Bike in range: " + peer);
                String bikeId = peer.substring(5);
                if(currentBikeId.equals(bikeId)){
                    bikeInRange = true; // bike picked up
                    Log.d(TAG, "My bike " + bikeId + " in range");
                }
            }else if(peer.startsWith("station_")){
                Log.d(TAG, "Station " + peer + " in range");
                stationInRange = peer.substring(8); // station nearby
            }
        }
        if(hadBike && !bikeInRange && stationInRange != null){ // drop off condition: bike was in range, but is no longer, and there is a station nearby.
            Log.d(TAG, "Finished trip in station " + stationInRange);
            currentBikeId = null;
            finishTrip();
        }

    }

    private void finishTrip() {
        double distance = 0;
        for (int i=1; i<trajectory.size(); i++){
            Location loc1 = new Location(""), loc2 = new Location("");
            loc1.setLatitude(trajectory.get(i-1).latitude);
            loc1.setLongitude(trajectory.get(i-1).longitude);
            loc2.setLatitude(trajectory.get(i).latitude);
            loc2.setLongitude(trajectory.get(i).longitude);
            distance += loc1.distanceTo(loc2);
        }
        int points = (int)(distance / 100);
        localStorage.putTrip(trajectory, points);
    }

    public void onLocationChanged(LatLng newLoc){
        Log.d(TAG, "Received new location: " + newLoc.latitude + ", " + newLoc.longitude);
        Log.d(TAG, "currentBikeId != null " + (currentBikeId != null)  + "bikeInRange" + bikeInRange);
        if(currentBikeId != null && bikeInRange){ // has request a bike that is in range, so update trajectory with new location.
            Log.d(TAG, "Adding point to path");
            trajectory.add(newLoc);
        }
    }
}
