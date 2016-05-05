package tecnico.cmu.ubibikeapp.model;

import java.util.ArrayList;

/**
 * Created by david on 30-04-2016.
 */
public class ResponseUser {

    private boolean success;
    private String message;
    private User user;

    public ResponseUser(boolean success, String message, User user) {
        this.success = success;
        this.message = message;
        this.user = user;
    }

    public ResponseUser(){
        user = new User();
    }


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}

