package tecnico.cmu.ubibikeapp.model;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by michel on 5/5/16.
 */
public class Station {
    private String name, stationId;
    private LatLng location;
    public Station(String id, String name, LatLng location){
        this.stationId = id;
        this.name = name;
        this.location = location;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return stationId;
    }

    public void setId(String stationId) {
        this.stationId = stationId;
    }
}
