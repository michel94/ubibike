package tecnico.cmu.ubibikeapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public static int getNewMessageId(){
        SharedPreferences preferences = UbibikeApp.getAppContext().getSharedPreferences(UBI_PREFS, Context.MODE_PRIVATE);
        int id = (preferences != null) ? preferences.getInt("message_counter", 0) : 0;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("message_counter", id+1);
        editor.apply();

        return id;
    }

    public static void incrementMessageCounter(String password){
        SharedPreferences preferences = UbibikeApp.getAppContext().getSharedPreferences(UBI_PREFS, Context.MODE_PRIVATE);

    }


    /*public static void saveLastLocation(Location mLastLocation) {
        SharedPreferences preferences = UbibikeApp.getAppContext().getSharedPreferences(UBI_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.put("user_statistics", userStats);
        editor.apply();
    }*/

    public static JSONArray getPendingData(){
        SharedPreferences preferences = UbibikeApp.getAppContext().getSharedPreferences(UBI_PREFS, Context.MODE_PRIVATE);
        if(preferences != null){
            Gson gson = new Gson();
            JSONArray array;
            array = gson.fromJson(preferences.getString("pending_data", null), JSONArray.class);
            if(array == null){
                array = new JSONArray();
            }
            return array;
        } else {
            return new JSONArray();
        }
    }

    public static void savePendingData(JSONArray pendingData){
        JSONArray currentData = getPendingData();
        for(int i = 0; i!= pendingData.length(); i++) {
            try {
                currentData.put(pendingData.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        SharedPreferences preferences = UbibikeApp.getAppContext().getSharedPreferences(UBI_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String pendingString = gson.toJson(currentData);
        editor.putString("pending_data", pendingString);
        editor.apply();
    }

    public static void clearPendingData(){
        SharedPreferences preferences = UbibikeApp.getAppContext().getSharedPreferences(UBI_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("pending_data", "");
        editor.apply();
    }

    public static void setMessageLog(JSONObject messageLog){
        SharedPreferences preferences = UbibikeApp.getAppContext().getSharedPreferences(UBI_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("messageLog", messageLog.toString());
        editor.apply();
    }

    public static JSONObject getMessageLog(){
        SharedPreferences preferences = UbibikeApp.getAppContext().getSharedPreferences(UBI_PREFS, Context.MODE_PRIVATE);
        try {
        return (preferences != null) ? new JSONObject(preferences.getString("messageLog", "")) : new JSONObject();
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONObject();

        }
    }
}

