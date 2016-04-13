package tecnico.cmu.ubibikeapp.network;

import org.json.JSONObject;

/**
 * Created by michel on 4/12/16.
 */
public abstract class ResponseCallback {
    public ResponseCallback(){

    }
    public abstract void onDataReceived(JSONObject response);
    public abstract void onError(Exception e);
}
