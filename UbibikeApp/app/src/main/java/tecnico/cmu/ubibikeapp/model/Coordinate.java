package tecnico.cmu.ubibikeapp.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by david on 11-05-2016.
 */
public class Coordinate {
    private double lat;
    private double lng;

    public Coordinate(int lat, int lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public Coordinate(LatLng coordinate){
        this.lat = coordinate.latitude;
        this.lng = coordinate.longitude;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(int lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(int lng) {
        this.lng = lng;
    }

    public LatLng getPosition() {
        return new LatLng(lat, lng);
    }

    @Override
    public String toString() {
        return "(" + getLat() + ", " + getLng()+ ")";
    }

}
