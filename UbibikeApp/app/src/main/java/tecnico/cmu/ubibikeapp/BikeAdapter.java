package tecnico.cmu.ubibikeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Fredy Felisberto on 5/1/2016.
 */
public class BikeAdapter extends ArrayAdapter<BikeMain> {


    public BikeAdapter(Context context, int resource) {
        super(context, resource);
    }

    public BikeAdapter(Context context, int resource, List<BikeMain> Mylist) {
        super(context, R.layout.biketable, Mylist);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.biketable, parent, false);
        }

        View v = convertView;


        BikeMain attr = getItem(position);

        if (attr != null) {
            TextView t1 = (TextView) v.findViewById(R.id.bikename);


            if (t1 != null) {
                t1.setText(attr.getName());
            }

        }

        return v;

    }
}

