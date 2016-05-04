package tecnico.cmu.ubibikeapp.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;

import tecnico.cmu.ubibikeapp.R;
import tecnico.cmu.ubibikeapp.Utils;
import tecnico.cmu.ubibikeapp.model.ResponseUser;
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
                R.layout.home_fragment, container, false);
        Log.d(TAG, "ON CREATE VIEW");
        getUserStatistics(rootView);

        ((TextView) rootView.findViewById(R.id.current_bike)).setText("Bicicleta exemplo");

        return rootView;
    }

    private void getUserStatistics(final View rootView){
        API api = new API();

        api.getUserStats(Utils.getUsername(getActivity()), new ResponseCallback() {
            @Override
            public void onDataReceived(JSONObject response) {
                Log.d(TAG, "RECEIVED: " + response);
                Gson gson = new Gson();
                ResponseUser responseUser = gson.fromJson(response.toString(),ResponseUser.class);
                ResponseUser.User user;
                if(responseUser.isSuccess()){
                    user = responseUser.getUser();
                } else {
                    user = Utils.getUserStats(getActivity());
                }
                Log.d(TAG, "USER " + user.toString());
                ((TextView) rootView.findViewById(R.id.points)).setText(user.getPoints() + "");
                TextView distance = ((TextView) rootView.findViewById(R.id.distance));
                distance.setText(user.getDistance() + " " +distance.getText());
                Utils.saveUserStats(getActivity(), user);
            }

            @Override
            public void onError(Exception e) {
                ResponseUser.User user = Utils.getUserStats(getActivity());
                Log.d(TAG, "USER " + user.toString());
                ((TextView) rootView.findViewById(R.id.points)).setText(user.getPoints() + "");
                TextView distance = ((TextView) rootView.findViewById(R.id.distance));
                distance.setText(user.getDistance() + " " +distance.getText());
                Utils.saveUserStats(getActivity(), user);
            }
        });
    }



}
