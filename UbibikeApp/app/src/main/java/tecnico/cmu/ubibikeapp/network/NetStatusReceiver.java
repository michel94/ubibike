package tecnico.cmu.ubibikeapp.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by michel on 5/7/16.
 */
public class NetStatusReceiver extends BroadcastReceiver {
    private final NetworkListener callback;

    public NetStatusReceiver(NetworkListener callback){
        this.callback = callback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Net", "Ayy lmao");

        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isConnected = activeNetInfo != null && activeNetInfo.isConnectedOrConnecting();
        if (isConnected)
            Log.i("NET", "connected " + isConnected);
        else Log.i("NET", "Not connected " + isConnected);

        if(callback != null)
            callback.onConnection(isConnected);
    }

    public interface NetworkListener {
        void onConnection(boolean connected);
    }
}

