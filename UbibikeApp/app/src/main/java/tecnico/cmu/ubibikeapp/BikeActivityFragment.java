package tecnico.cmu.ubibikeapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
        List<Attribute> yourItem = CriarListaActivity();
        if(yourItem!=null) System.out.println(CriarListaActivity());
        listAdapter = new ListAdapter(view.getContext().getApplicationContext(),R.layout.tabela, yourItem);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), MapActivity.class);
                startActivity(intent);
            }
        });



        return rootView;
    }



    public List<Attribute> CriarListaActivity(){
        List<Attribute> attrs = new ArrayList<Attribute>();
        Attribute attr = new Attribute("1132","00:30:20");
        Attribute attr1 = new Attribute("1132","01:30:20");
        Attribute attr2 = new Attribute("1332","02:30:20");
        Attribute attr3 = new Attribute("1432","01:00:20");
        Attribute attr4 = new Attribute("1532","02:00:20");
        Attribute attr5 = new Attribute("5132","03:30:20");
        Attribute attr6 = new Attribute("3132","04:30:20");
        Attribute attr7 = new Attribute("1332","02:30:20");
        Attribute attr8 = new Attribute("1432","01:00:20");
        Attribute attr9 = new Attribute("1532","02:00:20");
     /*   Attribute attr10 = new Attribute("5132","03:30:20");
        Attribute attr11 = new Attribute("3132","04:30:20"); */
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
