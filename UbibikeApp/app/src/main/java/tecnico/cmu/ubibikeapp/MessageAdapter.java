package tecnico.cmu.ubibikeapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import tecnico.cmu.ubibikeapp.model.Message;

/**
 * Created by Fredy Felisberto on 4/29/2016.
 */
public class MessageAdapter extends BaseAdapter {

    Context messageContext;
    ArrayList<Message> messageList;

    public MessageAdapter(Context context, ArrayList<Message> messages){
        messageList = messages;
        messageContext = context;
    }


    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
