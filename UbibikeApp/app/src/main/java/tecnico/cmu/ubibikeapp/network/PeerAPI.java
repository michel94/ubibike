package tecnico.cmu.ubibikeapp.network;

import android.app.Service;

import org.json.JSONException;
import org.json.JSONObject;

import pt.inesc.termite.wifidirect.SimWifiP2pDevice;
import tecnico.cmu.ubibikeapp.Utils;

/**
 * Created by michel on 5/1/16.
 */
public class PeerAPI {
    private SimWifiP2pDevice device;
    private Service service;

    public PeerAPI(Service service, SimWifiP2pDevice device){
        this.service = service;
        this.device = device;
    }

    public void sendPoints(int quantity, ResponseCallback callback){
        try {
            JSONObject data = new JSONObject();
            JSONObject transfer = new JSONObject();
            transfer.put("quantity", quantity);
            transfer.put("source", Utils.getUserID(service));
            data.put("transfer", transfer);

            new WDTask(device, "sendPoints", data, callback).execute();
        } catch (JSONException e) {;}

    }

    public void sendMessage(String text, ResponseCallback callback){
        JSONObject data = new JSONObject();
        try {
            data.put("message", text);
            data.put("username", Utils.getUsername(service));

            new WDTask(device, "sendMessage", data, callback).execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void getIdentity(ResponseCallback callback){

    }

    public void getLocation(ResponseCallback callback){

    }
}
