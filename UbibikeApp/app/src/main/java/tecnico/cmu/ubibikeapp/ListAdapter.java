package tecnico.cmu.ubibikeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Fredy Felisberto on 4/21/2016.
 */
public class ListAdapter extends ArrayAdapter<ActivityAttr> {



    public ListAdapter(Context context, int resource) {super(context,0);}

    public ListAdapter(Context context, int resource, List<ActivityAttr> myList) {
        super(context,R.layout.tabela, myList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tabela, parent, false);
        }

        View v = convertView;


        ActivityAttr attr = getItem(position);

        if (attr != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.pontos);
            TextView tt2 = (TextView) v.findViewById(R.id.hora);


            if (tt1 != null) {
                tt1.setText(attr.getPontos());
            }

            if (tt2 != null) {
                tt2.setText(attr.getTempo());
            }
        }

        return v;

    }
}
