package tecnico.cmu.ubibikeapp.network;

import android.app.Service;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import pt.inesc.termite.wifidirect.SimWifiP2pDevice;
import tecnico.cmu.ubibikeapp.Utils;
import tecnico.cmu.ubibikeapp.model.Transfer;

/**
 * Created by michel on 5/1/16.
 */
public class Peer {
    private SimWifiP2pDevice device;
    private WDService service;
    private String userID, username;
    private final String TAG = "Peer";

    public Peer(WDService service, SimWifiP2pDevice device){
        this.service = service;
        this.device = device;
        userID = "";
        username = "";
    }

    public SimWifiP2pDevice getDevice(){
        return device;
    }

    public void sendPoints(int quantity, ResponseCallback callback){
        try {
            Transfer transfer = new Transfer(Utils.getUserID(), userID, quantity);
            LocalStorage storage = service.getLocalStorage();
            storage.putTransfer(transfer);

            JSONObject jData = new JSONObject();
            JSONObject jTransfer = transfer.toJson();
            jData.put("transfer", jTransfer);
            jData.put("pending", storage.getPendingData());

            new WDTask(device, "sendPoints", jData, callback).execute();
        } catch (JSONException e) {;}

    }

    public void sendMessage(String text, ResponseCallback callback){
        JSONObject data = new JSONObject();
        try {
            data.put("message", text);
            data.put("usernameSrc", Utils.getUsername());
            data.put("userIDSrc", Utils.getUserID());
            data.put("usernameDest", username);
            data.put("userIDDest", userID);

            new WDTask(device, "sendMessage", data, callback).execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void identify(){
        getIdentity(new ResponseCallback() {
            @Override
            public void onDataReceived(JSONObject response) {
                try {
                    if(response.get("type").equals("user")){
                        userID = (String) response.get("userID");
                        username = (String) response.get("username");
                        Log.d(TAG, "Peer " + username + " (" + userID + ") is online.");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(Exception e) {

            }
        });

    }

    public void getIdentity(ResponseCallback callback){
        new WDTask(device, "getIdentity", null, callback).execute();
    }

    public void getLocation(ResponseCallback callback){

    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }
}
