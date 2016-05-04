package tecnico.cmu.ubibikeapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import tecnico.cmu.ubibikeapp.model.ResponseUser;
import tecnico.cmu.ubibikeapp.network.ResponseCallback;

/**
 * Created by david on 30-04-2016.
 */
public class Utils {

    private static final String UBI_PREFS = "UbibikePreferences";

    public static String getUserID(Context context){
        SharedPreferences preferences = context.getSharedPreferences(UBI_PREFS, Context.MODE_PRIVATE);
        return (preferences != null) ? preferences.getString("userID", null) : null;
    }

    public static void setUserID(Context context, String userID) {
        SharedPreferences preferences = context.getSharedPreferences(UBI_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userID", userID);
        editor.apply();
    }

    public static String getUsername(Context context){
        SharedPreferences preferences = context.getSharedPreferences(UBI_PREFS, Context.MODE_PRIVATE);
        return (preferences != null) ? preferences.getString("username", null) : null;
    }

    public static void setUsername(Context context, String username){
        SharedPreferences preferences = context.getSharedPreferences(UBI_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", username);
        editor.apply();
    }

    public static String getPassword(Context context){
        SharedPreferences preferences = context.getSharedPreferences(UBI_PREFS, Context.MODE_PRIVATE);
        return (preferences != null) ? preferences.getString("password", null) : null;
    }

    public static void setPassword(Context context, String password){
        SharedPreferences preferences = context.getSharedPreferences(UBI_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("password", password);
        editor.apply();
    }

    public static void saveUserStats(Context context, ResponseUser.User user){
        Gson gson = new Gson();
        String userStats = gson.toJson(user);
        SharedPreferences preferences = context.getSharedPreferences(UBI_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_statistics", userStats);
        editor.apply();
    }

    public static ResponseUser.User getUserStats(Context context){
        SharedPreferences preferences = context.getSharedPreferences(UBI_PREFS, Context.MODE_PRIVATE);
        if(preferences != null){
            Gson gson = new Gson();
            ResponseUser.User user = gson.fromJson(preferences.getString("user_statistics", null), ResponseUser.User.class);
            return (user == null) ? new ResponseUser().getUser() : user;
        } else {
            return new ResponseUser().getUser();
        }
    }

    public static Date convertStringToDate(String dateString) throws ParseException{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return format.parse(dateString);
    }

    public static String convertDateToString(Date date) {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return dateformat.format(date);
    }

}

