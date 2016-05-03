package tecnico.cmu.ubibikeapp.network;

import tecnico.cmu.ubibikeapp.model.Message;

/**
 * Created by michel on 5/3/16.
 */
public abstract class MessageHandler {
    private boolean bound = false;
    private WDService service;

    public MessageHandler(WDService service){
        this.service = service;
    }

    public void bind(String userID){
        bound = true;
        service.bindMessageHandler(this, userID);
    }

    public void unbind(){
        bound = false;
        service.unbindMessageHandler(this);
    }

    public abstract void onMessage(String text);


}
