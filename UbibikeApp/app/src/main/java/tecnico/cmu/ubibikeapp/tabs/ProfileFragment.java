package tecnico.cmu.ubibikeapp.tabs;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import tecnico.cmu.ubibikeapp.Booking;
import tecnico.cmu.ubibikeapp.R;


/**
 * Created by david on 31-03-2016.
 */
public class ProfileFragment extends Fragment {
    private GoogleMap googleMap;
    TextView textView;
    String name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_collection_object, container, false);
        View root = inflater.inflate(R.layout.activity_booking, container, false);

        textView =(TextView)root.findViewById(R.id.stationame);

        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(rootView.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(rootView.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        // crashing, pls remove:
        //final double longitude = location.getLongitude();
        //final double latitude = location.getLatitude();

        try {
            if (googleMap == null) {
                googleMap = ((MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map)).getMap();
            }

            List<LatLng> testLocations = genTestLocations();
            final LatLngBounds.Builder builder = new LatLngBounds.Builder();

            for (int i = 0; i < testLocations.size(); i++) {
                 LatLng position = new LatLng(testLocations.get(i).latitude, testLocations.get(i).longitude);
                 MarkerOptions options = new MarkerOptions().position(position).title("Estacao " + (i + 1) + "/" + 4 + " I RESERVAR ---");

                googleMap.addMarker(options);
                builder.include(position);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 12));
            }

            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Intent intent = new Intent(getActivity(), Booking.class);

                    startActivity(intent);
                    return false;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return  rootView;
    }

    public List<LatLng> genTestLocations(){
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
