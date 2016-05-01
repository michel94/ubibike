package tecnico.cmu.ubibikeapp.network;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.UnknownHostException;

import pt.inesc.termite.wifidirect.SimWifiP2pDevice;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocket;

/**
 * Created by michel on 4/30/16.
 */
public class WDTask extends AsyncTask<String, String, JSONObject> {
    private static String TAG = "WDSendTask";
    private ResponseCallback callback;
    private SimWifiP2pDevice device;
    private int port;
    private String ip;
    private JSONObject data;
    private Exception exception;

    public WDTask(SimWifiP2pDevice device, String method, JSONObject data, ResponseCallback callback){
        this.ip = device.getVirtIp();
        this.port = WDService.PORT;
        this.data = data;
        this.callback = callback;

        try {
            this.data.put("_methodName", method);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String stream2String(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        try {
            Log.d(TAG, "Connecting to " + ip);
            SimWifiP2pSocket socket = new SimWifiP2pSocket(ip, port);
            Log.d(TAG, "Sending data: " + data);
            OutputStream stream = socket.getOutputStream();//.write(data.getBytes());
            stream.write(data.toString().getBytes());
            stream.close();

            InputStream in = new BufferedInputStream(socket.getInputStream());
            String s = stream2String(in);
            JSONObject response = new JSONObject(s);

            in.close();
            socket.close();

            return response;

        } catch (UnknownHostException e) {
            Log.d(TAG, "Unknown Host: " + e.getMessage());
            exception = e;
        } catch (IOException e) {
            Log.d(TAG, "IO error: " + e.getMessage());
            exception = e;
        }catch (Exception e){
            Log.d(TAG, "Random Exception: " + e.toString() + e.getMessage());
            exception = e;
            e.printStackTrace();
        }

        return new JSONObject();
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        if(exception != null)
            callback.onError(exception);
        else
            callback.onDataReceived(result);

        super.onPostExecute(result);
    }

}
