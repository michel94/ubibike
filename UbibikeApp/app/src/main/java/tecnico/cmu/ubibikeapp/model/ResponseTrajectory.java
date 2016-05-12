package tecnico.cmu.ubibikeapp.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import tecnico.cmu.ubibikeapp.Utils;

/**
 * Created by david on 30-04-2016.
 */
public class ResponseTrajectory {

    private boolean success;
    private String message;
    private ArrayList<Trajectory> trajectories;

    public ResponseTrajectory(boolean success, String message, ArrayList<Trajectory> trajectories) {
        this.success = success;
        this.message = message;
        this.trajectories = trajectories;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Trajectory> getTrajectories() {
        return trajectories;
    }

    public void setTrajectory(ArrayList<Trajectory> trajectories) {
        this.trajectories = trajectories;
    }

}
