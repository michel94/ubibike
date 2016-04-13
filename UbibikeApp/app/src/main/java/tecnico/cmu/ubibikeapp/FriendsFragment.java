package tecnico.cmu.ubibikeapp;

import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

import tecnico.cmu.ubibikeapp.network.API;
import tecnico.cmu.ubibikeapp.network.ResponseCallback;

/**
 * Created by david on 31-03-2016.
 */
public class FriendsFragment extends ListFragment {
    private ArrayAdapter<String> adapter;
    private ArrayList<String> contacts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.users_layout, container, false);
        //ListView listView = (ListView) rootView.findViewById(R.id.list);

        String[] contacts = {"asdasd", "bfgfg", "asdasd", "bfgfg", "asdasd", "bfgfg", "asdasd", "bfgfg", "asdasd", "bfgfg", "asdasd", "bfgfg", "asdasd", "bfgfg", "asdasd", "bfgfg", "asdasd", "bfgfg"};
        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,
                contacts);
        //listView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        getListView().setAdapter(adapter);

        API api = new API();
        api.getTest(new ResponseCallback() {
            @Override
            public void onDataReceived(JSONObject response) {
                Log.d("F", "AYY LMAO " + response);
            }
            @Override
            public void onError(Exception e){
                Log.d("F", "Error: " + e.getMessage()); e.printStackTrace();
            }
        });

    }

}
