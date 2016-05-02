package tecnico.cmu.ubibikeapp;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity {

    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                 return;
        }

        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        //pls remove this, crashing
        //double longitude = location.getLongitude();
        //double latitude = location.getLatitude();

        try {
            if (googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager().
                        findFragmentById(R.id.map)).getMap();
            }

            Liatitutelongetude();
            final LatLngBounds.Builder builder = new LatLngBounds.Builder();

            for (int i = 0; i < Liatitutelongetude().size(); i++) {
                final LatLng position = new LatLng(Liatitutelongetude().get(i).latitude, Liatitutelongetude().get(i).longitude);
                final MarkerOptions options = new MarkerOptions().position(position).title("Estacao"+(i+1)+"/"+4);

                googleMap.addMarker(options);
                builder.include(position);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position,12));


            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }


        }



    public List<LatLng> Liatitutelongetude(){
        List<LatLng> latLng = new ArrayList<>();

        LatLng lng = new LatLng(38.7738718, -9.0958291);
        LatLng lng1 = new LatLng(38.7354228,-9.1466685);
        LatLng lng2 = new LatLng(38.7334772,-9.1366397);
        LatLng lng3 = new LatLng(38.7552882,-9.1163247);

        latLng.add(lng);
        latLng.add(lng1);
        latLng.add(lng2);
        latLng.add(lng3);

        return latLng;
    }
}