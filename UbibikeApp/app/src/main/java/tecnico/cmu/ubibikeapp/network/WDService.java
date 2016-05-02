package tecnico.cmu.ubibikeapp.network;

import pt.inesc.termite.wifidirect.SimWifiP2pBroadcast;
import pt.inesc.termite.wifidirect.SimWifiP2pDevice;
import pt.inesc.termite.wifidirect.SimWifiP2pInfo;
import pt.inesc.termite.wifidirect.SimWifiP2pManager;
import pt.inesc.termite.wifidirect.SimWifiP2pManager.Channel;
import pt.inesc.termite.wifidirect.service.SimWifiP2pService;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocketManager;
import pt.inesc.termite.wifidirect.SimWifiP2pDeviceList;
import pt.inesc.termite.wifidirect.SimWifiP2pManager.PeerListListener;
import pt.inesc.termite.wifidirect.SimWifiP2pManager.GroupInfoListener;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;

import android.location.Location;
import android.location.LocationListener;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WDService extends Service implements
        PeerListListener, GroupInfoListener, LocationListener {

    private static final String TAG = "WDService";
    public static int PORT = 10001;

    private SimWifiP2pManager mManager = null;
    private Channel mChannel = null;
    private Messenger mService = null;
    private WDServer server;
    private boolean mBound = false;
    private WDEventReceiver mReceiver;
    private Location currentLocation;

    private ArrayList<Peer> peerList = new ArrayList<Peer>();

    @Override
    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // initialize the WDSim API
        SimWifiP2pSocketManager.Init(getApplicationContext());

        // register broadcast receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_STATE_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_PEERS_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_NETWORK_MEMBERSHIP_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_GROUP_OWNERSHIP_CHANGED_ACTION);
        mReceiver = new WDEventReceiver(this);
        registerReceiver(mReceiver, filter);

        wifiOn();

        return START_STICKY;
    }


    private final IBinder binder = new LocalBinder();

    public class LocalBinder extends Binder {
        public WDService getService() {
            // Return this instance of LocalService so clients can call public methods
            return WDService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    private void wifiOn(){

        Intent intent = new Intent(this, SimWifiP2pService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        mBound = true;

        server = new WDServer(this);
        server.start();

    };

    private void wifiOff(){
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    public void requestPeers(){
        if (mBound) {
            mManager.requestPeers(mChannel, WDService.this);
        } else {
            //Toast.makeText(v.getContext(), "Service not bound", Toast.LENGTH_SHORT).show();
        }
    }

    public void requestGroupInfo(){
        if (mBound) {
            mManager.requestGroupInfo(mChannel, WDService.this);
        } else {
            //Toast.makeText(v.getContext(), "Service not bound", Toast.LENGTH_SHORT).show();
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        // callbacks for service binding, passed to bindService()

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = new Messenger(service);
            mManager = new SimWifiP2pManager(mService);
            mChannel = mManager.initialize(getApplication(), getMainLooper(), null);
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mService = null;
            mManager = null;
            mChannel = null;
            mBound = false;
        }
    };

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
    }

    public Location getLocation(){
        return currentLocation;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

	/*
	 * Listeners associated to Termite
	 */

    @Override
    public void onPeersAvailable(SimWifiP2pDeviceList peers) {

        StringBuilder peersStr = new StringBuilder();

        // compile list of devices in range
        for (SimWifiP2pDevice device : peers.getDeviceList()) {
            String devstr = "" + device.deviceName + " (" + device.getVirtIp() + ")\n";
            peersStr.append(devstr);
        }
    }

    @Override
    public void onGroupInfoAvailable(SimWifiP2pDeviceList devices,
                                     SimWifiP2pInfo groupInfo) {

        peerList.clear();

        // compile list of network members
        StringBuilder peersStr = new StringBuilder();
        for (String deviceName : groupInfo.getDevicesInNetwork()) {
            SimWifiP2pDevice device = devices.getByName(deviceName);
            String devstr = "" + deviceName + " (" +
                    ((device == null)?"??":device.getVirtIp()) + ")\n";
            peersStr.append(devstr);
            //testPeer(device);

            final Peer peer = new Peer(this, device);
            peer.identify();
            peerList.add(peer);
        }

        Log.d(TAG, "Current group peers: " + peersStr.toString());
    }

    private void testPeer(SimWifiP2pDevice device){
        Peer peer = new Peer(this, device);
        peer.sendMessage("Ayy lmao", new ResponseCallback() {
            @Override
            public void onDataReceived(JSONObject response) {
                Log.d(TAG, "Response received: " + response.toString());
            }
            @Override
            public void onError(Exception e) {
                // Parse json to find out if there is an error
            }
        });
    }

    public void testMethod(String l){
        Log.d(TAG, "Called service method: " + l);
    }

    public boolean isUserAvailable(String userID){
        //Log.d(TAG, "isUser " + userID + " Available?");
        for(Peer peer : peerList) {
            //Log.d(TAG, peer.getUserID() + " " + peer.getUsername());
            if (peer.getUserID().equals(userID))
                return true;
        }

        return false;
    }

    @Override
    public void onDestroy(){
        server.interrupt();
        Log.d(TAG, "Service destroyed");
    }

}
