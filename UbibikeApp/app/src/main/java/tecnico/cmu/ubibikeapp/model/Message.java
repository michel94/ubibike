package tecnico.cmu.ubibikeapp.model;

/**
 * Created by Fredy Felisberto on 4/29/2016.
 */
public class Message {

    public boolean left;
    public String message;
    private String from, to;

    public Message(boolean left, String message) {
        super();
        this.left = left;
        this.message = message;
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
}
