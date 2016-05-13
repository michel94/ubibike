package tecnico.cmu.ubibikeapp.tabs;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tecnico.cmu.ubibikeapp.BookingActivity;
import tecnico.cmu.ubibikeapp.R;
import tecnico.cmu.ubibikeapp.model.Station;
import tecnico.cmu.ubibikeapp.network.API;
import tecnico.cmu.ubibikeapp.network.ResponseCallback;


/**
 * Created by david on 31-03-2016.
 */
public class StationsFragment extends Fragment {
    private static final String TAG = "StationsFragment";
    private GoogleMap googleMap;
    TextView textView;
    String name;
    ArrayList<Marker> markers = new ArrayList<Marker>();
    ArrayList<Station> stations = new ArrayList<Station>();
    HashMap<String, Station> markerIdToStation = new HashMap<>();
    private String selectedMarker = "";

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

    public void setUpMapIfNeeded() {
        if (googleMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            SupportMapFragment mapFrag = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
            Log.d(TAG, "SupportMapFrag: " + (mapFrag != null));
            googleMap = mapFrag.getMap();
            // Check if we were successful in obtaining the map.
            if (googleMap != null){
                setUpMap();
            }
        }
    }

    void drawMarkers(){
        final LatLngBounds.Builder builder = new LatLngBounds.Builder();
        Log.d(TAG, "N Stations: " + stations.size());
        for (int i = 0; i < stations.size(); i++) {
            LatLng location = stations.get(i).getLocation();
            String name = stations.get(i).getName();
            MarkerOptions options = new MarkerOptions().position(location).title(name);

            Marker marker = googleMap.addMarker(options);
            builder.include(location);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 12));
            markers.add(marker);
            markerIdToStation.put(marker.getId(), stations.get(i));
        }

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                if(selectedMarker.equals(marker.getId())){
                    Intent intent = new Intent(getActivity(), BookingActivity.class);
                    Station station = markerIdToStation.get(marker.getId());
                    intent.putExtra("stationName", station.getName());
                    intent.putExtra("stationId", station.getId());

                    startActivity(intent);
                }
                selectedMarker = marker.getId();

                return false;

            }
        });
    }

    private void setUpMap() {
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        googleMap.setMyLocationEnabled(true);


        // crashing, pls remove:
        //final double longitude = location.getLongitude();
        //final double latitude = location.getLatitude();

        if (googleMap == null) {
            googleMap = ((MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map)).getMap();
        }

        API api = new API();
        api.getStations(new ResponseCallback() {
            @Override
            public void onDataReceived(JSONObject response) {
                try {
                    JSONArray st = response.getJSONArray("stations");
                    Log.d(TAG, st.toString());
                    stations.clear();
                    for(int i=0; i<st.length(); i++){
                        JSONObject s = (JSONObject) st.get(i);
                        JSONObject loc = s.getJSONObject("location");
                        LatLng location = new LatLng(loc.getDouble("latitude"), loc.getDouble("longitude"));
                        Station station = new Station((String) s.get("_id"), (String) s.get("name"), location);
                        Log.d(TAG, "Station ID: "+ station.getId());

                        stations.add(station);
                    }
                    drawMarkers();
                } catch (JSONException e) {
                    // TODO: Show error
                    e.printStackTrace();
                }

            }
            @Override
            public void onError(Exception e) {
                // TODO: Show network error
                Log.d(TAG, "Error " + e);
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
