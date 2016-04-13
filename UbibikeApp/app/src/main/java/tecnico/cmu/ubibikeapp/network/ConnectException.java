package tecnico.cmu.ubibikeapp.network;

/**
 * Created by michel on 4/12/16.
 */
public class ConnectException extends Exception{
    public ConnectException(){

    }
    public ConnectException(Exception e){
        super(e.getMessage());
    }

}
