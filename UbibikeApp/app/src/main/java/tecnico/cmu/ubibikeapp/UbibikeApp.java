package tecnico.cmu.ubibikeapp;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * Created by david on 09-05-2016.
 */
public class UbibikeApp extends Application {

    private static final String TAG = "UbibikeApp";
    private static Context context;

    public static Context getAppContext(){
        return context;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "Application creating");
        context = getApplicationContext();
        super.onCreate();
    }
}
