package tecnico.cmu.ubibikeapp.model;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import tecnico.cmu.ubibikeapp.Utils;

/**
 * Created by david on 11-05-2016.
 */
public class Trajectory {
    private String _id;
    private String user_id;
    private ArrayList<Coordinate> coordinates;
    private int points;
    private double distance;
    private String beginDate;
    private String endDate;
    private String bike_id;

    public Trajectory(String id, String username, String user_id, ArrayList<Coordinate> coordinates, int points, double distance, String beginDate, String endDate) {
        this._id = id;
        this.coordinates = coordinates;
        this.points = points;
        this.distance = distance;
        this.beginDate = beginDate;
        this.endDate = endDate;
    }

    public Trajectory(ArrayList<Coordinate> coordinates, String beginDate) {
        this.user_id = Utils.getUserID();
        this.coordinates = coordinates;
        this.beginDate = beginDate;
        this.distance = 0;
        this.points = 0;
        this.bike_id = Utils.getCurrentBike();
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public ArrayList<Coordinate> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(ArrayList<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }



    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Date getBeginDate() throws ParseException {
        return Utils.convertStringToDate(beginDate);
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() throws ParseException{
        return Utils.convertStringToDate(endDate);
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getBikeId() {
        return bike_id;
    }

    public void setBikeId(String bike_id) {
        this.bike_id = bike_id;
    }

    public void addCoordinates(Coordinate coordinate){
        calculateDistanceTo(coordinate);
        coordinates.add(coordinate);
    }

    private void calculateDistanceTo(Coordinate coordinate){
        if(coordinates.size() != 0){
            float[] results = new float[1];
            Coordinate lastLocation = coordinates.get(coordinates.size()-1);
            Location.distanceBetween(coordinate.getLat(),coordinate.getLng(),
                    lastLocation.getLat(), lastLocation.getLng(), results);
            distance += results[0];
        }

    }

    //TODO This method needs to be improved
    public String getDuration() throws ParseException {
        return getBeginDate().getTime() - getEndDate().getTime() + "";
    }

    @Override
    public String toString() {
        return "Trajectory: " + getId() + ", " + getPoints() + ", coordinates: " + getCoordinates();
    }

    public String getBeginDateSimplified() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM");
        String dateString = "";
        Date date = getBeginDate();
        dateString = formatter.format(date);
        return dateString;


    }
}
