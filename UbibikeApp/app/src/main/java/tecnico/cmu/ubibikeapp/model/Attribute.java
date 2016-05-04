package tecnico.cmu.ubibikeapp.model;

import java.util.Date;

/**
 * Created by Fredy Felisberto on 4/21/2016.
 */
public class Attribute {
    String id;
    String pontos;
    float dista;
    String tempo;
    Date data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPontos() {
        return pontos;
    }

    public void setPontos(String pontos) {
        this.pontos = pontos;
    }

    public float getDista() {
        return dista;
    }

    public void setDista(float dista) {
        this.dista = dista;
    }

    public String getTempo() {
        return tempo;
    }

    public void setTempo(String tempo) {
        this.tempo = tempo;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Attribute(String id, String pontos, float dista, String tempo, Date data) {
        this.id = id;
        this.pontos = pontos;
        this.dista = dista;
        this.tempo = tempo;
        this.data = data;
    }

    public Attribute(String id, String pontos, String tempo) {
        this.id = id;
        this.pontos = pontos;
        this.tempo = tempo;
    }

    public Attribute() {

    }
}
