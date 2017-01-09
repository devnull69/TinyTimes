package org.theiner.tinytimes.context;

import android.app.Application;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.theiner.tinytimes.data.Kalender;
import org.theiner.tinytimes.data.PreferenceData;

/**
 * Created by TTheiner on 09.01.2017.
 */

public class TinyTimesApplication extends Application {

    private PreferenceData prefData = null;
    private Kalender kalender = null;

    private String MY_PREFERENCES = "TinyTimesFile";

    public PreferenceData getPrefData() {
        if(prefData == null) {
            // Aus den SharedPreferences holen (falls möglich)
            SharedPreferences settings = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);

            String jsonPrefs = settings.getString("preferences", "");
            if(!jsonPrefs.equals("")) {
                Gson gson = new Gson();
                prefData = gson.fromJson(jsonPrefs, PreferenceData.class);
            } else {
                prefData = new PreferenceData();
            }
        }
        return prefData;
    }

    public void savePrefData() {
        // Speichern
        SharedPreferences settings = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        Gson gson = new Gson();
        editor.putString("preferences", gson.toJson(prefData));
        editor.commit();
    }

    public Kalender getKalender() {
        if(kalender == null) {
            // Aus den SharedPreferences holen (falls möglich)
            SharedPreferences settings = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);

            String jsonPrefs = settings.getString("kalender", "");
            if(!jsonPrefs.equals("") || jsonPrefs.equals("null")) {
                Gson gson = new Gson();
                kalender = gson.fromJson(jsonPrefs, Kalender.class);
            } else {
                kalender = new Kalender();
            }
        }
        return kalender;
    }

    public void saveKalender() {
        // Speichern
        SharedPreferences settings = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        Gson gson = new Gson();
        editor.putString("kalender", gson.toJson(kalender));
        editor.commit();
    }
}
