package org.theiner.tinytimes.data;

import java.util.HashMap;

/**
 * Created by TTheiner on 09.01.2017.
 */

public class Kalender {
    private HashMap<String, Monat> kalenderMonate = new HashMap<>();

    public HashMap<String, Monat> getKalenderMonate() {
        return kalenderMonate;
    }

    public void setKalenderMonate(HashMap<String, Monat> kalenderMonate) {
        this.kalenderMonate = kalenderMonate;
    }
}
