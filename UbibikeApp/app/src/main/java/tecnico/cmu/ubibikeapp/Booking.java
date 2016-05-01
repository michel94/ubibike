package tecnico.cmu.ubibikeapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Booking extends AppCompatActivity {

    ListView listView;
    BikeAdapter bikeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        CriarListaBike();

        listView = (ListView)findViewById(R.id.bikelist);
        List<BikeMain> yourItem = CriarListaBike();
        if(yourItem!=null) System.out.println(CriarListaBike());

        bikeAdapter = new BikeAdapter(this,R.layout.biketable,yourItem);
        listView.setAdapter(bikeAdapter);
    }



    public List<BikeMain> CriarListaBike(){
        List<BikeMain> attrs = new ArrayList<BikeMain>();
        BikeMain attr = new BikeMain("BMX X1");
        BikeMain attr1 = new BikeMain("RANGER 3X");
        BikeMain attr2 = new BikeMain("BMW BX1");
        BikeMain attr3 = new BikeMain("BMW BXX4");
        BikeMain attr4 = new BikeMain("BMW B3X");
        BikeMain attr5 = new BikeMain("BMW B4X4");

        attrs.add(attr);
        attrs.add(attr1);
        attrs.add(attr2);
        attrs.add(attr3);
        attrs.add(attr4);
        attrs.add(attr5);
        return attrs;
    }
}
