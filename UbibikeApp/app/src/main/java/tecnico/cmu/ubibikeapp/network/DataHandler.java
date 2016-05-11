package tecnico.cmu.ubibikeapp.network;

import tecnico.cmu.ubibikeapp.model.Message;

/**
 * Created by michel on 5/3/16.
 */

public abstract class DataHandler {
    private boolean bound = false;
    private WDService service;

    public DataHandler(WDService service){
        this.service = service;
    }

    public void bind(String userID){
        bound = true;
        service.bindDataHandler(this, userID);
    }

    public void bind(){
        bound = true;
        service.bindDataHandler(this, null);
    }

    public void unbind(){
        bound = false;
        service.unbindDataHandler(this);
    }

    public boolean onMessage(String text){
        return false;
    }
    public boolean onStatusChanged(boolean online, Peer peer){
        return false;
    }
    public boolean onScore(){
        return false;
    }


}
