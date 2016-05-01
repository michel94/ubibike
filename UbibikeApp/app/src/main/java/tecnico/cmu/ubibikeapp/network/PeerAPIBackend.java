package tecnico.cmu.ubibikeapp.network;

import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import tecnico.cmu.ubibikeapp.Utils;
import tecnico.cmu.ubibikeapp.model.ResponseUser;
import tecnico.cmu.ubibikeapp.model.ResponseUser.User;

/**
 * Created by michel on 5/1/16.
 */
public class PeerAPIBackend {
    WDService service;
    public PeerAPIBackend(WDService service){
        this.service = service;
    }

    public JSONObject sendPoints(JSONObject data) throws JSONException {
        JSONObject response = new JSONObject();
        response.put("success", false);
        return response;
    }

    public JSONObject sendMessage(JSONObject data) throws JSONException {
        Location location = service.getLocation();

        Handler handler = new Handler(Looper.getMainLooper());
        final String message = data.getString("username") + ": " + data.getString("message");
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(service, "Received message from " + message, Toast.LENGTH_SHORT).show();
            }
        });

        JSONObject response = new JSONObject();
        response.put("success", true);
        return response;
    }
    public JSONObject getLocation() throws JSONException {
        Location location = service.getLocation();
        JSONObject response = new JSONObject();
        response.put("latitude", location.getLatitude());
        response.put("longitude", location.getLongitude());

        return response;

    }
    public JSONObject getIdentity() throws JSONException {
        JSONObject response = new JSONObject();
        response.put("type", "user");
        response.put("userID", Utils.getUserID(service));
        response.put("username", Utils.getUsername(service));

        return response;

    }
}