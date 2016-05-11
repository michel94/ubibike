package tecnico.cmu.ubibikeapp.model;

/**
 * Created by michel on 5/6/16.
 */
public class Bike {
    private String bikeId, stationId, name;
    private boolean reserved;

    public Bike(String bikeId, String name, String stationId, boolean reserved){
        this.bikeId = bikeId;
        this.name = name;
        this.stationId = stationId;
        this.reserved = reserved;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getBikeId() {
        return bikeId;
    }

    public void setBikeId(String bikeId) {
        this.bikeId = bikeId;
    }

    public boolean isReserved() {
        return reserved;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    public String toString(){
        return this.name;
    }
}
