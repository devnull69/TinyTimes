package org.theiner.tinytimes.data;

/**
 * Created by TTheiner on 09.01.2017.
 */

public class PreferenceData {
    private double standardStundensatz = 10.0f;
    private double standardStundenzahl = 1.83f;
    private double steuerAbzug = 2.0f;

    public double getSteuerAbzug() {
        return steuerAbzug;
    }

    public void setSteuerAbzug(double steuerAbzug) {
        this.steuerAbzug = steuerAbzug;
    }

    public double getStandardStundensatz() {
        return standardStundensatz;
    }

    public void setStandardStundensatz(double standardStundensatz) {
        this.standardStundensatz = standardStundensatz;
    }

    public double getStandardStundenzahl() {
        return standardStundenzahl;
    }

    public void setStandardStundenzahl(double standardStundenzahl) {
        this.standardStundenzahl = standardStundenzahl;
    }
}
