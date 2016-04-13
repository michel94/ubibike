package tecnico.cmu.ubibikeapp.network;

/**
 * Created by michel on 4/12/16.
 */
public class API {
    private final String serverIp = "10.0.0.101";
    private final int port = 3000;
    private final String serverUrl = "http://" + serverIp +  ":" + port + "/";

    public void getTest(ResponseCallback callback){
        new RestTask(serverUrl + "test", callback).execute();
    }

    public void getUsers(ResponseCallback callback){
        new RestTask(serverUrl + "test", callback).execute();
    }
}
