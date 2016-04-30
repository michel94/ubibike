package tecnico.cmu.ubibikeapp.model;

/**
 * Created by Fredy Felisberto on 4/29/2016.
 */
public class Message {
    String text;
    String name;
    Long time;

    public Message(String text, String name, Long time) {
        this.text = text;
        this.name = name;
        this.time = time;
    }

    public Message() {

            }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
