package tecnico.cmu.ubibikeapp.network;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import tecnico.cmu.ubibikeapp.MainActivity;
import tecnico.cmu.ubibikeapp.R;
import tecnico.cmu.ubibikeapp.UbibikeApp;
import tecnico.cmu.ubibikeapp.Utils;
import tecnico.cmu.ubibikeapp.model.Coordinate;
import tecnico.cmu.ubibikeapp.model.Trajectory;

/**
 * Created by michel on 5/6/16.
 */
public class MoveManager {
    private String currentBikeId = null;
    //private ArrayList<LatLng> trajectory = new ArrayList<>();
    private boolean bikeInRange = false;
    private String stationInRange = null;
    private LocalStorage localStorage;
    private final String TAG = "MoveManager";

    private Trajectory mTrajectory;

    private boolean mNotifyBikeInRange;

    public MoveManager(LocalStorage localStorage){
        this.localStorage = localStorage;
        mTrajectory = new Trajectory(new ArrayList<Coordinate>(),
                Utils.convertDateToString(new Date(System.currentTimeMillis())));
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
                    if(!mNotifyBikeInRange){
                        notifyUserBike(true);
                        mNotifyBikeInRange = true;
                    }
                    bikeInRange = true; // bike picked up
                    //TODO Notify the user of the pick up

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
            //TODO Notify the drop off
            mNotifyBikeInRange = false;
            notifyUserBike(false);
            finishTrip();
        }

    }

    private void finishTrip() {
        Utils.setCurrentBike("no_bike");
        Utils.setCurrentStation(stationInRange);
        double distance = mTrajectory.getDistance();
        int points = (int)(distance / 100);
        mTrajectory.setPoints(points);
        mTrajectory.setEndDate(Utils.convertDateToString(new Date(System.currentTimeMillis())));
        localStorage.putTrip(mTrajectory);
        localStorage.returnBikeToStation();
    }

    public void onLocationChanged(LatLng newLoc){
        Log.d(TAG, "Received new location: " + newLoc.latitude + ", " + newLoc.longitude);
        Log.d(TAG, "currentBikeId != null " + (currentBikeId != null)  + "bikeInRange" + bikeInRange);
        if(currentBikeId != null && bikeInRange){ // has request a bike that is in range, so update trajectory with new location.
            Log.d(TAG, "Adding point to path");
            mTrajectory.addCoordinates(new Coordinate(newLoc));
        }
    }

    private void notifyUserBike(boolean pickup){
        //Intent intent = new Intent(getActivity(), LocationTrackerActivity.class);
        //startActivity(intent);
        int mId = 1;
        String title;
        String text;
        if(pickup){
            title = "Bike pick up";
            text = "You pick up a bike from a station";
        } else {
            title = "Bike drop off";
            text = "You drop a bike in a station";

        }

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(UbibikeApp.getAppContext())
                        .setSmallIcon(R.drawable.bike)
                        .setContentTitle(title)
                        .setContentText(text)
                        .setAutoCancel(true);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(UbibikeApp.getAppContext(), MainActivity.class);
        resultIntent.putExtra("loading_from_notifications", true);
        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(UbibikeApp.getAppContext());
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) UbibikeApp.getAppContext().getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(mId, mBuilder.build());
    }
}
