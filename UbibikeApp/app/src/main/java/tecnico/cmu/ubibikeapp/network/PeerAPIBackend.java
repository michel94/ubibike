package tecnico.cmu.ubibikeapp.network;

import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tecnico.cmu.ubibikeapp.Utils;
import tecnico.cmu.ubibikeapp.model.Transfer;

/**
 * Created by michel on 5/1/16.
 */
public class PeerAPIBackend {
    WDService service;
    private final String TAG = "PeerAPIBackend";

    public PeerAPIBackend(WDService service){
        this.service = service;
    }

    public JSONObject sendPoints(JSONObject data) throws JSONException {

        Transfer transfer = new Transfer(data.getJSONObject("transfer"));
        Log.d(TAG, "New transfer: from " + transfer.getSrcUser() + " to " + transfer.getDestUser() + " with " + transfer.getQuantity() + " points");
        // TODO: Do something with points: update local storage, redraw view...

        JSONArray jPending = data.getJSONArray("pending");
        LocalStorage storage = service.getLocalStorage();
        storage.extendData(jPending);

        JSONObject response = new JSONObject();
        response.put("success", true);
        return response;
    }

    public JSONObject sendMessage(JSONObject data) throws JSONException {

        String message = data.getString("message");
        String srcID = data.getString("userIDSrc");
        String srcUsername = data.getString("usernameSrc");

        service.handleMessage(message, srcID, srcUsername);

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
        response.put("userID", Utils.getUserID());
        response.put("username", Utils.getUsername());

        return response;

    }
}
