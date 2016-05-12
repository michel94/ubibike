package tecnico.cmu.ubibikeapp.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import tecnico.cmu.ubibikeapp.ListAdapter;
import tecnico.cmu.ubibikeapp.MapActivity;
import tecnico.cmu.ubibikeapp.R;
import tecnico.cmu.ubibikeapp.TrajectoryActivity;
import tecnico.cmu.ubibikeapp.model.Attribute;
import tecnico.cmu.ubibikeapp.model.ResponseTrajectory;
import tecnico.cmu.ubibikeapp.model.Trajectory;
import tecnico.cmu.ubibikeapp.network.API;
import tecnico.cmu.ubibikeapp.network.ResponseCallback;

/**
 * Created by david on 31-03-2016.
 */
public class BikeActivityFragment extends Fragment {

    private static final String TAG = "BikeActivityFragment";
    ListView listView;
    ListAdapter listAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.bakiactivity, container, false);
        View view =inflater.inflate(R.layout.tabela, container, false);
        Log.d(TAG, "ON CREATE VIEW");

        //CriarListaActivity();

        listView = (ListView)rootView.findViewById(R.id.idlist);
        criarListaActivity(listView, view);
        //if(yourItem!=null) System.out.println(CriarListaActivity());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String trajectoryId = listAdapter.getItem(position).getId();
                Intent intent = new Intent(getActivity(), TrajectoryActivity.class);
                intent.putExtra("_id", trajectoryId);
                startActivity(intent);
            }
        });

        return rootView;
    }



    public void criarListaActivity(final ListView listView, final View view){

        API api = new API();
        final List<Attribute> trajectoriesList = new ArrayList<>();

        api.getAllTrajectories(new ResponseCallback() {
            @Override
            public void onDataReceived(JSONObject response) {
                Gson gson = new Gson();
                Log.d(TAG, "Full response " + response.toString());
                ResponseTrajectory responseTrajectory = gson.fromJson(response.toString(), ResponseTrajectory.class);
                if(responseTrajectory.isSuccess()){
                    for(Trajectory trajectory: responseTrajectory.getTrajectories()){
                        String duration;
                        try {
                            duration = trajectory.getDuration();
                        } catch (ParseException e) {
                            e.printStackTrace();
                            duration = "00:00:00";
                        }
                        trajectoriesList.add(new Attribute(trajectory.getId() + "", trajectory.getPoints() + "" , duration));
                    }
                } else {
                    Log.d(TAG, "Trajectories Response is ERROR");
                }

                listAdapter = new ListAdapter(view.getContext().getApplicationContext(),R.layout.tabela, trajectoriesList);
                listView.setAdapter(listAdapter);

            }

            @Override
            public void onError(Exception e) {
                Log.d(TAG, "Trajectories Response is API ERROR " + e.getMessage());
            }
        });
    }



}
