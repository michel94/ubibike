package tecnico.cmu.ubibikeapp.network;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by michel on 4/12/16.
 */
public class API {
    //private final String serverIp = "194.210.231.171";
    private final String serverIp = "192.168.1.6";
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
}
