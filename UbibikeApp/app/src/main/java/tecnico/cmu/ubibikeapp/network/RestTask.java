package tecnico.cmu.ubibikeapp.network;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.CookieHandler;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by michel on 4/12/16.
 */

public class RestTask extends AsyncTask<String, String, JSONObject>{
    private String restUrl;
    private ResponseCallback callback;
    private Exception exception = null;
    private JSONObject data = null;

    public RestTask(String restUrl, ResponseCallback callback, JSONObject data){
        this.restUrl = restUrl;
        this.callback = callback;
        this.data = data;
    }

    public RestTask(String restUrl, ResponseCallback callback){
        this.restUrl = restUrl;
        this.callback = callback;
    }

    private String stream2String(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    @Override
    protected JSONObject doInBackground(String ... params){
        try {
            URL url = new URL(restUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestMethod("POST");

            OutputStream wr = conn.getOutputStream();
            if(data != null)
                wr.write(data.toString().getBytes("UTF-8"));

            wr.close();

            InputStream in = new BufferedInputStream(conn.getInputStream());
            String s = stream2String(in);
            JSONObject response = new JSONObject(s);

            in.close();
            conn.disconnect();

            return response;
        } catch (IOException e) {
            //e.printStackTrace();
            //exception = new ConnectException(e);
            exception = e;
        } catch (JSONException e) {
            //e.printStackTrace();
            exception = e;
        }
        return new JSONObject();

    }

    @Override
    protected void onPostExecute(JSONObject result) {
        if(exception != null)
            callback.onError(exception);
        else
            callback.onDataReceived(result);

        super.onPostExecute(result);
    }
}
