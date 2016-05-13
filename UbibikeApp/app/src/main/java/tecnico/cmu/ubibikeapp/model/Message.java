package tecnico.cmu.ubibikeapp.model;

import org.json.JSONException;
import org.json.JSONObject;

import tecnico.cmu.ubibikeapp.Utils;

/**
 * Created by Fredy Felisberto on 4/29/2016.
 */
public class Message {

    public boolean left;
    public String message;
    private String from, to;

    public Message(boolean left, String message, String from, String to) {
        super();
        this.left = left;
        this.message = message;
        this.from = from;
        this.to = to;
    }

    public Message(JSONObject data) {
        super();
        try {
            to = data.getString("to");
            from = data.getString("from");
            message = data.getString("message");
            left = to.equals(Utils.getUserID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public JSONObject toJson(){
        JSONObject data = new JSONObject();

        try {
            data.put("message", message);
            data.put("to", to);
            data.put("from", from);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }

    public boolean sentByMe(){
        return from.equals(Utils.getUserID());
    }
}
