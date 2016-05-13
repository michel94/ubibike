package tecnico.cmu.ubibikeapp.network;

import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceConfigurationError;
import java.util.Set;

import tecnico.cmu.ubibikeapp.UbibikeApp;
import tecnico.cmu.ubibikeapp.Utils;
import tecnico.cmu.ubibikeapp.model.Message;
import tecnico.cmu.ubibikeapp.model.Trajectory;
import tecnico.cmu.ubibikeapp.model.Transfer;

/**
 * Created by michel on 5/6/16.
 */
public class LocalStorage implements NetStatusReceiver.NetworkListener{
    private static final String TAG = "LocalStorage";
    Context context;
    public JSONArray pendingData;
    private boolean connected;
    private Hashtable<String, ArrayList<Message>> messages;

    private NetStatusReceiver mNetStatusReceiver;

    public LocalStorage(Context context){
        pendingData = Utils.getPendingData();
        Log.d(TAG, pendingData.toString());
        this.context = context;

        mNetStatusReceiver = new NetStatusReceiver(this);
        IntentFilter netFilter = new IntentFilter();
        netFilter.addAction(android.net.ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(mNetStatusReceiver, netFilter);

        load();
    }

    public void load(){
        messages = new Hashtable<>();
        JSONObject data = Utils.getMessageLog();
        Log.d(TAG, "Message Log: " + data.toString());
        Iterator<?> keys = data.keys();

        while( keys.hasNext() ) {
            String key = (String)keys.next();
            try {
                JSONArray chat = data.getJSONArray(key);
                messages.put(key, new ArrayList<Message>());
                for(int i=0; i<chat.length(); i++){
                    messages.get(key).add(new Message(chat.getJSONObject(i)));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    /*public void unregisterReceiver(){
        try{
            UbibikeApp.getAppContext().unregisterReceiver(mNetStatusReceiver);
        } catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }*/

    public ArrayList<Message> getMessages(String userId){
        return messages.get(userId);
    }

    public void putMessage(Message message){
        Log.d(TAG, message.getFrom());
        String user = message.getFrom().equals(Utils.getUserID()) ? message.getTo() : message.getFrom();
        if(!messages.containsKey(user))
            messages.put(user, new ArrayList<Message>());
        messages.get(user).add(message);
    }

    public void saveMessages(){
        Set<String> keys = messages.keySet();
        JSONObject data = new JSONObject();
        for (String userId: keys){
            JSONArray chat = new JSONArray();
            for(Message message: messages.get(userId)){
                chat.put(message.toJson());
            }
            try {
                data.put(userId, chat);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "Saving data " + data.toString());
        Utils.setMessageLog(data);
    }

    public void putTransfer(Transfer transfer){ // returns a jsonobject with the transfer, stores it in the pending data
        JSONObject data = new JSONObject();
        try {
            data = transfer.toJson();
            data.put("messageId", Utils.getNewMessageId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        putOnPending(data);
    }

    public JSONArray getPendingData(){
        return pendingData;
    }

    public void putTrip(Trajectory trajectory){
        try {
            Gson gson = new Gson();
            JSONObject jTrip = new JSONObject();
            jTrip.put("type", "trip");
            jTrip.put("trajectory", gson.toJson(trajectory));

            Log.d(TAG, "Storing trip with " + trajectory.getPoints() + " earned points: " + trajectory);

            putOnPending(jTrip);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    void putOnPending(JSONObject o){
        pendingData.put(o);
        sendPendingDataToServer();
    }

    private void sendPendingDataToServer(){
        API api = new API();
        api.sendTransactions(pendingData, new ResponseCallback() {
            @Override
            public void onDataReceived(JSONObject response) {
                boolean success;
                try {
                    success = response.getBoolean("success");
                } catch (JSONException e) {
                    e.printStackTrace();
                    success = false;
                }
                if(success){
                    pendingData = new JSONArray();
                    Utils.clearPendingData();
                    Log.d(TAG, "Successfully send transactions to server");
                } else {
                    Utils.savePendingData(pendingData);
                }
            }

            @Override
            public void onError(Exception e) {
                Log.d(TAG, "Error sending data to server, saving to storage");
                Utils.savePendingData(pendingData);
            }
        });
    }

    public void extendData(JSONArray trips){
        String first = pendingData.toString();
        first = first.substring(0, first.length()-1);
        Log.d(TAG, first);
        String second = trips.toString();
        second = second.substring(1, second.length());
        Log.d(TAG, second);
        try {
            pendingData = new JSONArray(first + ", " + second);
            sendPendingDataToServer();
            Log.d("extendTrips", pendingData.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnection(boolean connected) {
        Log.d(TAG, "Updated network status. Connected: " + connected + " pending data length " +
            pendingData.length());
        this.connected = connected;
        if(connected && pendingData.length() != 0){
            Log.d(TAG, "Connected, sending pending data to server...");
            sendPendingDataToServer();
        }
        String currentBike = Utils.getCurrentBike();
        if(currentBike!=null){
            if(!currentBike.equals("no_bike")){
                returnBikeToStation();
            }
        }
    }

    public void destroy(){
        context.unregisterReceiver(mNetStatusReceiver);
    }

    public void returnBikeToStation(){
        API api = new API();
        api.returnBike(Utils.getUserID(), new ResponseCallback() {
            @Override
            public void onDataReceived(JSONObject response) {
                Log.d(TAG, "Return bike: " + response.toString());
            }

            @Override
            public void onError(Exception e) {
                Log.d(TAG, "Return bike error: " + e.toString());

            }
        });
    }

}
