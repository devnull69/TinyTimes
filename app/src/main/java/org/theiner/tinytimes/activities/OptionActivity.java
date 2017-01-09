package org.theiner.tinytimes.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import org.theiner.tinytimes.R;
import org.theiner.tinytimes.context.TinyTimesApplication;
import org.theiner.tinytimes.data.PreferenceData;
import org.theiner.tinytimes.data.Tag;

import java.text.DecimalFormat;

public class OptionActivity extends AppCompatActivity {

    TinyTimesApplication app = null;

    EditText editStundenzahl = null;
    EditText editStundensatz = null;
    EditText editSteuerabzug = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        app = (TinyTimesApplication) getApplicationContext();

        editStundenzahl = (EditText) findViewById(R.id.editStundenzahl);
        editStundensatz = (EditText) findViewById(R.id.editStundensatz);
        editSteuerabzug = (EditText) findViewById(R.id.editSteuerabzug);

        PreferenceData prefData = app.getPrefData();
        double stundenzahl = Math.floor(prefData.getStandardStundenzahl() * 100.0f + 0.5f) / 100.0f;
        editStundenzahl.setText(String.valueOf(stundenzahl));

        double stundensatz = Math.floor(prefData.getStandardStundensatz() * 100.0f + 0.5f) / 100.0f;
        editStundensatz.setText(String.valueOf(stundensatz));

        double steuerabzug = Math.floor(prefData.getSteuerAbzug() * 100.0f + 0.5f) / 100.0f;
        editSteuerabzug.setText(String.valueOf(steuerabzug));

    }

    public void onCancel(View view) {
        this.finish();
    }

    public void onSave(View view) {
        DecimalFormat df = new DecimalFormat("0.00");

        double stundenzahl = Double.parseDouble(editStundenzahl.getText().toString());
        double stundensatz = Double.parseDouble(editStundensatz.getText().toString());
        double steuerabzug = Double.parseDouble(editSteuerabzug.getText().toString());

        PreferenceData prefData = app.getPrefData();
        prefData.setStandardStundenzahl(stundenzahl);
        prefData.setStandardStundensatz(stundensatz);
        prefData.setSteuerAbzug(steuerabzug);

        app.savePrefData();

        setResult(Activity.RESULT_OK);

        this.finish();
    }
}
