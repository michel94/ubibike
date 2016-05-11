package tecnico.cmu.ubibikeapp.network;

import org.json.JSONException;
import org.json.JSONObject;

import tecnico.cmu.ubibikeapp.MyData;

/**
 * Created by michel on 4/12/16.
 */
public class API {
    private final String serverIp = MyData.SERVER_IP;

    private final int port = 3000;
    private final String serverUrl = "http://" + serverIp +  ":" + port + "/";
    private static final String GET = "GET";
    private static final String POST = "POST";

    public void getTest(ResponseCallback callback){
        new RestTask(GET, serverUrl + "test", callback).execute();
    }

    public void getUsers(ResponseCallback callback){
        new RestTask(GET, serverUrl + "users", callback).execute();
    }

    public void login(String username, String password, ResponseCallback callback){
        JSONObject data = new JSONObject();
        try {
            data.put("username", username);
            data.put("password", password);
        } catch (JSONException e) {}
        new RestTask("POST", serverUrl + "login", callback, data).execute();
    }

    public void register(String username, String password, ResponseCallback callback){
        JSONObject data = new JSONObject();
        try {
            data.put("username", username);
            data.put("password", password);
        } catch (JSONException e) {}
        new RestTask(POST, serverUrl + "register", callback, data).execute();
    }

    public void getUserStats(String username, ResponseCallback callback){
        JSONObject data = new JSONObject();
        try {
            data.put("username", username);
        } catch (JSONException e){}
        new RestTask(POST, serverUrl + "userStats", callback, data).execute();
    }

    public void getTrajectory(String id, ResponseCallback callback){
        JSONObject data = new JSONObject();
        try {
            data.put("_id", id);
        } catch (JSONException e){}
        new RestTask(POST, serverUrl + "trajectories/info", callback, data).execute();
    }

    public void getAllTrajectories(ResponseCallback callback){
        new RestTask(GET, serverUrl + "trajectories", callback).execute();
    }

    public void getStations(ResponseCallback callback) {
        new RestTask(GET, serverUrl + "stations", callback).execute();
    }

    public void getStationInfo(String stationId, ResponseCallback callback){
        JSONObject data = new JSONObject();
        try {
            data.put("stationId", stationId);
        } catch (JSONException e){}
        new RestTask(POST, serverUrl + "stationInfo", callback, data).execute();
    }

    public void requestBike(String userId, String bikeId, ResponseCallback callback){
        JSONObject data = new JSONObject();
        try {
            data.put("user", userId);
            data.put("bike", bikeId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new RestTask(POST, serverUrl + "requestBike", callback, data).execute();
    }

    public void returnBike(String userId, ResponseCallback callback){
        JSONObject data = new JSONObject();
        try {
            data.put("user", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new RestTask(POST, serverUrl + "requestBike", callback, data).execute();
    }

}
