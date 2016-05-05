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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import tecnico.cmu.ubibikeapp.Booking;
import tecnico.cmu.ubibikeapp.MainActivity;
import tecnico.cmu.ubibikeapp.R;


/**
 * Created by david on 31-03-2016.
 */
public class StationsFragment extends Fragment {
    private static final String TAG = "StationsFragment";
    private GoogleMap googleMap;
    TextView textView;
    String name;
    ArrayList<Marker> markers = new ArrayList<>();
    /*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stations, container, false);

        textView = (TextView)rootView.findViewById(R.id.stationame);

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

                Marker m = googleMap.addMarker(options);
                builder.include(position);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 12));
                markers.add(m);
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
    */

    public List<LatLng> genTestLocations() {
        List<LatLng> latLng = new ArrayList<>();

        LatLng lng = new LatLng(38.7738718, -9.0958291);
        LatLng lng1 = new LatLng(38.7354228, -9.1466685);
        LatLng lng2 = new LatLng(38.7334772, -9.1366397);
        LatLng lng3 = new LatLng(38.7552882, -9.1163247);

        latLng.add(lng);
        latLng.add(lng1);
        latLng.add(lng2);
        latLng.add(lng3);

        return latLng;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        View rootView = inflater.inflate(R.layout.fragment_stations, container, false);

        textView = (TextView) rootView.findViewById(R.id.stationame);
        setUpMapIfNeeded(); // For setting up the MapFragment

        return rootView;
    }

    /***** Sets up the map if it is possible to do so *****/
    public void setUpMapIfNeeded() {
        if (googleMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            SupportMapFragment mapFrag = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
            Log.d(TAG, "SupportMapFrag: " + (mapFrag != null));
            googleMap = mapFrag.getMap();
            // Check if we were successful in obtaining the map.
            if (googleMap != null)
                setUpMap();
        }
    }

    private void setUpMap() {
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);


        // crashing, pls remove:
        //final double longitude = location.getLongitude();
        //final double latitude = location.getLatitude();

        if (googleMap == null) {
            googleMap = ((MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map)).getMap();
        }

        List<LatLng> testLocations = genTestLocations();
        final LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (int i = 0; i < testLocations.size(); i++) {
            LatLng position = new LatLng(testLocations.get(i).latitude, testLocations.get(i).longitude);
            MarkerOptions options = new MarkerOptions().position(position).title("Estacao " + (i + 1) + "/" + 4 + " I RESERVAR ---");

            Marker m = googleMap.addMarker(options);
            builder.include(position);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 12));
            markers.add(m);
        }

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Intent intent = new Intent(getActivity(), Booking.class);

                startActivity(intent);
                return false;
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (googleMap != null)
            setUpMap();

        if (googleMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            SupportMapFragment mapFrag = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
            googleMap = mapFrag.getMap(); // getMap is deprecated
            // Check if we were successful in obtaining the map.
            if (googleMap != null)
                setUpMap();
        }
    }

    /**** The mapfragment's id must be removed from the FragmentManager
     **** or else if the same it is passed on the next time then
     **** app will crash ****/

    @Override
    public void onPause() {
        super.onPause();

        SupportMapFragment mapFrag = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
        if(mapFrag != null)
        getChildFragmentManager().beginTransaction()
                .remove(mapFrag)
                .commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (googleMap != null) {
            //SupportMapFragment mapFrag = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
            //getChildFragmentManager().beginTransaction().remove(mapFrag).commit();
            googleMap = null;
        }
    }

}
