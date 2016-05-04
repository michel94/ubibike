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

    public class Trajectory {

        private String _id;
        private ArrayList<Coordinate> coordinates;
        private int points;
        private int distance;
        private String beginDate;
        private String endDate;

        public Trajectory(String id, ArrayList<Coordinate> coordinates, int points, int distance, String beginDate, String endDate) {
            this._id = id;
            this.coordinates = coordinates;
            this.points = points;
            this.distance = distance;
            this.beginDate = beginDate;
            this.endDate = endDate;
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

        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
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

        //TODO This method needs to be improved
        public String getDuration() throws ParseException {
            return getBeginDate().getTime() - getEndDate().getTime() + "";
        }

        @Override
        public String toString() {
            return "Trajectory: " + getId() + ", " + getPoints() + ", coordinates: " + getCoordinates();
        }

        public class Coordinate {

            private double lat;
            private double lng;

            public Coordinate(int lat, int lng) {
                this.lat = lat;
                this.lng = lng;
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

    }
}
