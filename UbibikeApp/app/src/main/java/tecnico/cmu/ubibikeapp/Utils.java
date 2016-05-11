package tecnico.cmu.ubibikeapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import tecnico.cmu.ubibikeapp.model.ResponseUser;
import tecnico.cmu.ubibikeapp.model.User;
import tecnico.cmu.ubibikeapp.network.ResponseCallback;

/**
 * Created by david on 30-04-2016.
 */
public class Utils {

    private static final String UBI_PREFS = "UbibikePreferences";

    public static String getUserID(){
        SharedPreferences preferences = UbibikeApp.getAppContext().getSharedPreferences(UBI_PREFS, Context.MODE_PRIVATE);
        return (preferences != null) ? preferences.getString("userID", null) : null;
    }

    public static void setUserID(String userID) {
        SharedPreferences preferences = UbibikeApp.getAppContext().getSharedPreferences(UBI_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userID", userID);
        editor.apply();
    }

    public static String getCurrentBike(){
        SharedPreferences preferences = UbibikeApp.getAppContext().getSharedPreferences(UBI_PREFS, Context.MODE_PRIVATE);
        return (preferences != null) ? preferences.getString("bikeID", null) : null;
    }

    public static void setCurrentBike(String bikeID) {
        SharedPreferences preferences = UbibikeApp.getAppContext().getSharedPreferences(UBI_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("bikeID", bikeID);
        editor.apply();
    }

    public static String getUsername(){
        SharedPreferences preferences = UbibikeApp.getAppContext().getSharedPreferences(UBI_PREFS, Context.MODE_PRIVATE);
        return (preferences != null) ? preferences.getString("username", null) : null;
    }

    public static void setUsername(String username){
        SharedPreferences preferences = UbibikeApp.getAppContext().getSharedPreferences(UBI_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", username);
        editor.apply();
    }

    public static String getPassword(){
        SharedPreferences preferences = UbibikeApp.getAppContext().getSharedPreferences(UBI_PREFS, Context.MODE_PRIVATE);
        return (preferences != null) ? preferences.getString("password", null) : null;
    }

    public static void setPassword(String password){
        SharedPreferences preferences = UbibikeApp.getAppContext().getSharedPreferences(UBI_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("password", password);
        editor.apply();
    }

    public static void saveUserStats(User user){
        Gson gson = new Gson();
        String userStats = gson.toJson(user);
        SharedPreferences preferences = UbibikeApp.getAppContext().getSharedPreferences(UBI_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_statistics", userStats);
        editor.apply();
    }

    public static User getUserStats(){
        SharedPreferences preferences = UbibikeApp.getAppContext().getSharedPreferences(UBI_PREFS, Context.MODE_PRIVATE);
        if(preferences != null){
            Gson gson = new Gson();
            User user = gson.fromJson(preferences.getString("user_statistics", null), User.class);
            return (user == null) ? new ResponseUser().getUser() : user;
        } else {
            return new ResponseUser().getUser();
        }
    }

    public static Date convertStringToDate(String dateString) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return format.parse(dateString);
    }

    public static String convertDateToString(Date date) {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return dateformat.format(date);
    }

    public static void setIsRequestingLocationUpdates(boolean isRequestingLocationUpdates) {
        SharedPreferences preferences = UbibikeApp.getAppContext().getSharedPreferences(UBI_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("requesting_location", isRequestingLocationUpdates);
        editor.apply();
    }

    public static boolean isRequestingLocationUpdates(){
        SharedPreferences preferences = UbibikeApp.getAppContext().getSharedPreferences(UBI_PREFS, Context.MODE_PRIVATE);
        return (preferences != null) && preferences.getBoolean("requesting_location", false);
    }

    /*public static void saveLastLocation(Location mLastLocation) {
        SharedPreferences preferences = UbibikeApp.getAppContext().getSharedPreferences(UBI_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.put("user_statistics", userStats);
        editor.apply();
    }*/
}

