package tecnico.cmu.ubibikeapp.network;

/**
 * Created by michel on 5/3/16.
 */
public abstract class RequestCallback {
    public RequestCallback(){

    }
    public abstract void onFinish(boolean success);
}
