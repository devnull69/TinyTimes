package org.theiner.tinytimes.data;

/**
 * Created by TTheiner on 09.01.2017.
 */

public class Monat {
    private Tag[] tage = new Tag[32];

    public Tag[] getTage() {
        return tage;
    }

    public void setTage(Tag[] tage) {
        this.tage = tage;
    }

    public void setTag(int tagNummer, Tag tag) {
        tage[tagNummer] = tag;
    }
}
