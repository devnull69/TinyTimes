package org.theiner.tinytimes.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by TTheiner on 09.01.2017.
 */

public class Tag implements Serializable{
    private double stundenzahl;
    private double stundensatz;
    private Tagesart tagesart = Tagesart.NORMAL;
    private boolean markiert = false;

    public enum Tagesart {
        @SerializedName("0")
        NORMAL(0),
        @SerializedName("1")
        URLAUB(1),
        @SerializedName("2")
        KRANK(2);

        private int value;
        private Tagesart(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public boolean isMarkiert() {
        return markiert;
    }

    public void setMarkiert(boolean markiert) {
        this.markiert = markiert;
    }

    private static final long serialVersionUID = 0L;

    public Tagesart getTagesart() {
        return tagesart;
    }

    public void setTagesart(Tagesart tagesart) {
        this.tagesart = tagesart;
    }

    public double getStundenzahl() {
        return stundenzahl;
    }

    public void setStundenzahl(double stundenzahl) {
        this.stundenzahl = stundenzahl;
    }

    public double getStundensatz() {
        return stundensatz;
    }

    public void setStundensatz(double stundensatz) {
        this.stundensatz = stundensatz;
    }

    public boolean isUrlaub() {
        return tagesart == Tagesart.URLAUB;
    }

    public void setUrlaub(boolean urlaub) {
        if(urlaub)
            tagesart = Tagesart.URLAUB;
        else
            tagesart = Tagesart.NORMAL;
    }

    public boolean isKrank() {
        return tagesart == Tagesart.KRANK;
    }

    public void setKrank(boolean krank) {
        if(krank)
            tagesart = Tagesart.KRANK;
        else
            tagesart = Tagesart.NORMAL;
    }

}
