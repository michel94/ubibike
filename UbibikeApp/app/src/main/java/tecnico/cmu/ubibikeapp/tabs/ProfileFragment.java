package tecnico.cmu.ubibikeapp.tabs;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tecnico.cmu.ubibikeapp.R;

/**
 * Created by david on 31-03-2016.
 */
public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_collection_object, container, false);
        ((TextView) rootView.findViewById(android.R.id.text1)).setText("MY PROFILE");
        return rootView;
    }
}
