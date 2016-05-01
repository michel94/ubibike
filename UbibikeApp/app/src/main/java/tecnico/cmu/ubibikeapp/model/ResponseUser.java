package tecnico.cmu.ubibikeapp.model;

import java.util.ArrayList;

/**
 * Created by david on 30-04-2016.
 */
public class ResponseUser {

    private boolean success;
    private String message;
    private User user;

    public ResponseUser(boolean success, String message, User user) {
        this.success = success;
        this.message = message;
        this.user = user;
    }

    public ResponseUser(){
        user = new User();
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public class User {

        private String username;
        private String password;
        private int distance;
        private int points;
        private int id;
        private ArrayList<Trajectory> trajectories;

        public User(String username, String password, int distance, int points, ArrayList<Trajectory> trajectories) {
            this.username = username;
            this.password = password;
            this.distance = distance;
            this.points = points;
            this.trajectories = trajectories;
        }

        public User(){
            username = "";
            password = "";
            distance = 0;
            points = 0;
            trajectories = new ArrayList<>();
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public int getPoints() {
            return points;
        }

        public void setPoints(int points) {
            this.points = points;
        }

        public ArrayList<Trajectory> getTrajectories() {
            return trajectories;
        }

        public void setTrajectories(ArrayList<Trajectory> trajectories) {
            this.trajectories = trajectories;
        }

        @Override
        public String toString() {
            return "Username " + username + "/ points: " + points + "/ distance: " + distance;
        }

        private class Trajectory{

        }


    }

}

