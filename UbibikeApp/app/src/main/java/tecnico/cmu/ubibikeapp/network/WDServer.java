package tecnico.cmu.ubibikeapp.network;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocket;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocketServer;

public class WDServer extends Thread {
    static String TAG = "WDServer";
    private WDService service;
    PeerAPIBackend peerBackend;

    public WDServer(WDService service){
        this.service = service;
        peerBackend = new PeerAPIBackend(service);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void run(){
        Log.d(TAG, "Server thread running");
        SimWifiP2pSocketServer serverSocket = null;

        try {
            serverSocket = new SimWifiP2pSocketServer(WDService.PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (!Thread.currentThread().isInterrupted()) {
            try {
                SimWifiP2pSocket socket = serverSocket.accept();
                try {
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String data = buffer.readLine();
                    Log.d(TAG, "Received data: " + data);

                    JSONObject response = null;
                    try {
                        JSONObject parsedJson = new JSONObject(data);
                        String methodName = parsedJson.getString("_methodName");


                        if(methodName.equals("sendPoints"))
                            response = peerBackend.sendPoints(parsedJson);
                        else if(methodName.equals("sendMessage"))
                            response = peerBackend.sendMessage(parsedJson);
                        else if(methodName.equals("getIdentity"))
                            response = peerBackend.getIdentity();
                        else if(methodName.equals("getLocation"))
                            response = peerBackend.getLocation();



                    } catch (JSONException e) {
                        response = new JSONObject();
                        try {
                            response.put("error", e.toString());
                        } catch (JSONException e1) {;}
                        e.printStackTrace();
                    }

                    OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8);

                    if(response != null)
                        out.write(response.toString() + "\n");
                    out.close();

                } catch (IOException e) {
                    Log.d("Error reading socket:", e.getMessage());
                } finally {
                    socket.close();
                }
            } catch (IOException e) {
                Log.d("Error socket:", e.getMessage());
                break;
            }
        }
    }
}
