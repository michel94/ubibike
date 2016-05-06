package tecnico.cmu.ubibikeapp.network;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by michel on 4/12/16.
 */
public class API {

    private final String serverIp = MyData.SERVER_IP;

    private final int port = 3000;
    private final String serverUrl = "http://" + serverIp +  ":" + port + "/";

    public void getTest(ResponseCallback callback){
        new RestTask(serverUrl + "test", callback).execute();
    }

    public void getUsers(ResponseCallback callback){
        new RestTask(serverUrl + "users", callback).execute();
    }

    public void login(String username, String password, ResponseCallback callback){
        JSONObject data = new JSONObject();
        try {
            data.put("username", username);
            data.put("password", password);
        } catch (JSONException e) {}
        new RestTask(serverUrl + "login", callback, data).execute();
    }

    public void register(String username, String password, ResponseCallback callback){
        JSONObject data = new JSONObject();
        try {
            data.put("username", username);
            data.put("password", password);
        } catch (JSONException e) {}
        new RestTask(serverUrl + "register", callback, data).execute();
    }

    public void getUserStats(String username, ResponseCallback callback){
        JSONObject data = new JSONObject();
        try {
            data.put("username", username);
        } catch (JSONException e){}
        new RestTask(serverUrl + "userStats", callback, data).execute();
    }

    public void getStations(ResponseCallback callback) {
        new RestTask(serverUrl + "stations", callback).execute();
    }

    public void getStationInfo(String stationId, ResponseCallback callback){
        JSONObject data = new JSONObject();
        try {
            data.put("stationId", stationId);
        } catch (JSONException e){}
        new RestTask(serverUrl + "stationInfo", callback, data).execute();
    }
}
