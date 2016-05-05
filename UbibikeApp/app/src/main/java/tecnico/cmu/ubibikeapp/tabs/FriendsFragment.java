package tecnico.cmu.ubibikeapp.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;

import tecnico.cmu.ubibikeapp.MainActivity;
import tecnico.cmu.ubibikeapp.R;
import tecnico.cmu.ubibikeapp.UserActivity;
import tecnico.cmu.ubibikeapp.network.API;
import tecnico.cmu.ubibikeapp.network.DataHandler;
import tecnico.cmu.ubibikeapp.network.Peer;
import tecnico.cmu.ubibikeapp.network.ResponseCallback;

/**
 * Created by david on 31-03-2016.
 */
public class FriendsFragment extends ListFragment {

    private static final String TAG = "FriendsFragment";

    private ArrayAdapter<String> adapter;
    private ArrayList<String> contacts = new ArrayList<String>();
    ListView listView;
    private Hashtable<String, String> usernameToUserID = new Hashtable<>();
    private DataHandler dataHandler;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_users, container, false);

                adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,
                contacts);

         return rootView;
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        getListView().setAdapter(adapter);

        listView = (ListView)getListView().findViewById(android.R.id.list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), UserActivity.class);
                String username = contacts.get((int) id);
                String userID = usernameToUserID.get(username);
                intent.putExtra("username", username);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });

        API api = new API();
        api.getUsers(new ResponseCallback() {
            @Override
            public void onDataReceived(JSONObject response) {
                try {
                    JSONArray users = response.getJSONArray("users");
                    contacts.clear();
                    for(int u=0; u<users.length(); u++){
                        JSONObject user = (JSONObject) users.get(u);
                        Log.d(TAG, "Users: " + user.toString());
                        String username = user.getString("username");
                        String userID = user.getString("_id");
                        contacts.add(username);
                        usernameToUserID.put(username, userID);
                    }
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onError(Exception e){
                Log.d("UsersFrag", "Error: " + e.getMessage());
            }
        });


        /*WDService service = ((MainActivity) getActivity()).getWDService();

        ArrayList<Peer> peerList = service.getPeerList();
        for(Peer peer : peerList) {
            String username = peer.getUsername();
            if(!contacts.contains(username)) {
                contacts.add(username);
                usernameToUserID.put(username, peer.getUserID());
            }
        }
        adapter.notifyDataSetChanged();*/

    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG, "Focus");
        MainActivity act = (MainActivity)getActivity();
        dataHandler = new DataHandler(act.getWDService()) {
            @Override
            public boolean onStatusChanged(boolean online, Peer peer) {
                if(online){
                    contacts.add(peer.getUsername());
                    usernameToUserID.put(peer.getUsername(), peer.getUserID());
                }else{
                    contacts.remove(peer.getUsername());
                }
                adapter.notifyDataSetChanged();
                return true;
            }
        };
        dataHandler.bind();

    }

    @Override
    public void onPause(){
        super.onPause();
        dataHandler.unbind();
        dataHandler = null;
        Log.d(TAG, "Lost focus");
    }

}
