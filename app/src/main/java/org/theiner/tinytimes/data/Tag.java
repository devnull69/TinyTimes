package org.theiner.tinytimes.data;

import java.io.Serializable;

/**
 * Created by TTheiner on 09.01.2017.
 */

public class Tag implements Serializable{
    private double stundenzahl;
    private double stundensatz;
    private boolean isUrlaub;

    private static final long serialVersionUID = 0L;

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
        return isUrlaub;
    }

    public void setUrlaub(boolean urlaub) {
        isUrlaub = urlaub;
    }
}
