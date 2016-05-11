package tecnico.cmu.ubibikeapp.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;

import tecnico.cmu.ubibikeapp.LocationTrackerActivity;
import tecnico.cmu.ubibikeapp.R;
import tecnico.cmu.ubibikeapp.Utils;
import tecnico.cmu.ubibikeapp.model.ResponseUser;
import tecnico.cmu.ubibikeapp.model.User;
import tecnico.cmu.ubibikeapp.network.API;
import tecnico.cmu.ubibikeapp.network.ResponseCallback;

/**
 * Created by david on 31-03-2016.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(
                R.layout.fragment_home, container, false);

        getUserStatistics(rootView);

        ((TextView) rootView.findViewById(R.id.current_bike)).setText("Bicicleta exemplo");

        final FloatingActionButton button = (FloatingActionButton) rootView.findViewById(R.id.fab);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LocationTrackerActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    private void getUserStatistics(final View rootView){
        API api = new API();

        api.getUserStats(Utils.getUsername(), new ResponseCallback() {
            @Override
            public void onDataReceived(JSONObject response) {
                Log.d(TAG, "RECEIVED: " + response);
                Gson gson = new Gson();
                ResponseUser responseUser = gson.fromJson(response.toString(), ResponseUser.class);
                User user;
                if(responseUser.isSuccess()){
                    user = responseUser.getUser();
                } else {
                    user = Utils.getUserStats();
                }
                Log.d(TAG, "USER " + user.toString());
                ((TextView) rootView.findViewById(R.id.points)).setText(user.getPoints() + "");
                TextView distance = ((TextView) rootView.findViewById(R.id.distance));
                distance.setText(user.getDistance() + " " +distance.getText());
                Utils.saveUserStats(user);
            }

            @Override
            public void onError(Exception e) {
                User user = Utils.getUserStats();
                Log.d(TAG, "USER " + user.toString());
                ((TextView) rootView.findViewById(R.id.points)).setText(user.getPoints() + "");
                TextView distance = ((TextView) rootView.findViewById(R.id.distance));
                distance.setText(user.getDistance() + " " +distance.getText());
                Utils.saveUserStats(user);
            }
        });
    }



}
