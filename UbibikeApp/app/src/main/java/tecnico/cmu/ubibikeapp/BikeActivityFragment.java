package tecnico.cmu.ubibikeapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by david on 31-03-2016.
 */
public class BikeActivityFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_collection_object, container, false);
        ((TextView) rootView.findViewById(android.R.id.text1)).setText("LAST ACTIVITY");
        return rootView;
    }
}
