package tecnico.cmu.ubibikeapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by david on 31-03-2016.
 */
public class BikeActivityFragment extends Fragment {

    ListView listView;
    ListAdapter listAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.bakiactivity, container, false);
        View view=inflater.inflate(R.layout.tabela, container, false);


        CriarListaActivity();

        listView = (ListView)rootView.findViewById(R.id.idlist);
        List<ActivityAttr> yourItem = CriarListaActivity();
        if(yourItem!=null) System.out.println(CriarListaActivity());
        listAdapter = new ListAdapter(view.getContext().getApplicationContext(),R.layout.tabela, yourItem);
        listView.setAdapter(listAdapter);

        return rootView;
    }


    public List<ActivityAttr> CriarListaActivity(){
        List<ActivityAttr> attrs = new ArrayList<ActivityAttr>();
        ActivityAttr attr = new ActivityAttr("1132","00:30:20");
        ActivityAttr attr1 = new ActivityAttr("1132","01:30:20");
        ActivityAttr attr2 = new ActivityAttr("1332","02:30:20");
        ActivityAttr attr3 = new ActivityAttr("1432","01:00:20");
        ActivityAttr attr4 = new ActivityAttr("1532","02:00:20");
        ActivityAttr attr5 = new ActivityAttr("5132","03:30:20");
        ActivityAttr attr6 = new ActivityAttr("3132","04:30:20");
        ActivityAttr attr7 = new ActivityAttr("1332","02:30:20");
        ActivityAttr attr8 = new ActivityAttr("1432","01:00:20");
        ActivityAttr attr9 = new ActivityAttr("1532","02:00:20");
     /*   ActivityAttr attr10 = new ActivityAttr("5132","03:30:20");
        ActivityAttr attr11 = new ActivityAttr("3132","04:30:20"); */
        attrs.add(attr);
        attrs.add(attr1);
        attrs.add(attr2);
        attrs.add(attr3);
        attrs.add(attr4);
        attrs.add(attr5);
        attrs.add(attr6);
        attrs.add(attr7);
        attrs.add(attr8);
        attrs.add(attr9);
    /*    attrs.add(attr10);
        attrs.add(attr11); */
        return attrs;
    }

}
