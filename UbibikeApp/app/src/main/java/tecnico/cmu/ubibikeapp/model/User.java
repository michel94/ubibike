package tecnico.cmu.ubibikeapp.model;

import java.util.ArrayList;

public class User {

    private String username;
    private String password;
    private int distance;
    private int score;
    private int id;
    private ArrayList<Trajectory> trajectories;
    private String currentBike;

    public User(String username, String password, int distance, int score, ArrayList<Trajectory> trajectories, String currentBike) {
        this.username = username;
        this.password = password;
        this.distance = distance;
        this.score = score;
        this.trajectories = trajectories;
    }

    public User() {
        username = "";
        password = "";
        distance = 0;
        score = 0;
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public ArrayList<Trajectory> getTrajectories() {
        return trajectories;
    }

    public void setTrajectories(ArrayList<Trajectory> trajectories) {
        this.trajectories = trajectories;
    }

    public String getCurrentBike() {
        return currentBike;
    }

    public void setCurrentBike(String currentBike) {
        this.currentBike = currentBike;
    }

    @Override
    public String toString() {
        return "Username " + username + "/ score: " + score + "/ distance: " + distance;
    }

}