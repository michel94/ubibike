package tecnico.cmu.ubibikeapp.network;

import pt.inesc.termite.wifidirect.SimWifiP2pBroadcast;
import pt.inesc.termite.wifidirect.SimWifiP2pDevice;
import pt.inesc.termite.wifidirect.SimWifiP2pManager;
import pt.inesc.termite.wifidirect.SimWifiP2pManager.Channel;
import pt.inesc.termite.wifidirect.service.SimWifiP2pService;
import pt.inesc.termite.wifidirect.SimWifiP2pDeviceList;
import pt.inesc.termite.wifidirect.SimWifiP2pManager.PeerListListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.util.Log;

public class WDService extends Service implements
        PeerListListener, LocationListener {

    public static final String TAG = "WDService";

    private SimWifiP2pManager mManager = null;
    private Channel mChannel = null;
    private boolean mBound = false;
    private WDEventReceiver mReceiver;

    public class LocalBinder extends Binder {
    }
    private final IBinder mBinder = new LocalBinder();

    @Override
    public void onCreate() {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private void wifiOn(){
        Log.d(TAG, "Wifi ON");
        Intent intent = new Intent(this, SimWifiP2pService.class);
        boolean r = bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
            startService(intent);
        Log.d(TAG, "Binding service to mConnection: " + r);
        mBound = true;
    };

    private void wifiOff(){
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    private void requestPeers(){
        if (mBound) {
            mManager.requestPeers(mChannel, WDService.this);
        } else {
            //Toast.makeText(v.getContext(), "Service not bound", Toast.LENGTH_SHORT).show();
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        // callbacks for service binding, passed to bindService()

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.d(TAG, "Connected");
            mManager = new SimWifiP2pManager(new Messenger(service));
            mChannel = mManager.initialize(getApplication(), getMainLooper(), null);
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.d(TAG, "Disconnected");
            mManager = null;
            mChannel = null;
            mBound = false;
        }
    };

    @Override
    public void onPeersAvailable(SimWifiP2pDeviceList peers) {
        Log.d(TAG, "Peers Available");
        StringBuilder peersStr = new StringBuilder();

        // compile list of devices in range
        for (SimWifiP2pDevice device : peers.getDeviceList()) {
            String devstr = "" + device.deviceName + " (" + device.getVirtIp() + ")\n";
            peersStr.append(devstr);
        }
        Log.d(TAG, peersStr.toString());

        // display list of devices in range
        new AlertDialog.Builder(this)
                .setTitle("Devices in WiFi Range")
                .setMessage(peersStr.toString())
                .setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("GPS", "Location Changed " + location.toString());
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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // register broadcast receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_STATE_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_PEERS_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_NETWORK_MEMBERSHIP_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_GROUP_OWNERSHIP_CHANGED_ACTION);
        mReceiver = new WDEventReceiver();
        registerReceiver(mReceiver, filter);

        // Setup Location manager and receiver
        LocationManager lManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            lManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, this);
        }catch (SecurityException e){;}

        wifiOn();
        return START_STICKY; // read more on: http://developer.android.com/reference/android/app/Service.html
    }
}
