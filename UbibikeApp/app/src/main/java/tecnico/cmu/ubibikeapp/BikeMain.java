package tecnico.cmu.ubibikeapp;

/**
 * Created by Fredy Felisberto on 5/1/2016.
 */
public class BikeMain {

    int Number;
    String Name;

    public BikeMain(String name) {
        Name = name;
    }

    public BikeMain() {

    }

    public BikeMain(int number, String name) {
        Number = number;
        Name = name;
    }

    public int getNumber() {
        return Number;
    }

    public void setNumber(int number) {
        Number = number;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
