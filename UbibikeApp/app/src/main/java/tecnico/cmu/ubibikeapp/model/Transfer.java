package tecnico.cmu.ubibikeapp.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by michel on 5/11/16.
 */
public class Transfer {
    private String srcUser, destUser;
    private int quantity;

    public Transfer(String srcUser, String destUser, int quantity){
        this.srcUser = srcUser;
        this.destUser = destUser;
        this.quantity = quantity;
    }

    public Transfer(JSONObject data){
        try {
            this.srcUser = data.getString("srcUser");
            this.destUser = data.getString("destUser");
            this.quantity = data.getInt("quantity");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSrcUser() {
        return srcUser;
    }

    public void setSrcUser(String srcUser) {
        this.srcUser = srcUser;
    }

    public String getDestUser() {
        return destUser;
    }

    public void setDestUser(String destUser) {
        this.destUser = destUser;
    }

    public JSONObject toJson() {
        JSONObject data = new JSONObject();
        try {
            data.put("type", "transfer");
            data.put("srcUser", srcUser);
            data.put("destUser", destUser);
            data.put("quantity", quantity);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }
}
