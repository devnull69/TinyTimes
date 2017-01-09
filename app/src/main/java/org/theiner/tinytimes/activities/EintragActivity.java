package org.theiner.tinytimes.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import org.theiner.tinytimes.R;
import org.theiner.tinytimes.context.TinyTimesApplication;
import org.theiner.tinytimes.data.PreferenceData;
import org.theiner.tinytimes.data.Tag;

import java.text.DecimalFormat;
import java.text.ParseException;

public class EintragActivity extends AppCompatActivity {

    TinyTimesApplication app = null;

    EditText editStundenzahl = null;
    EditText editStundensatz = null;
    Switch swUrlaubstag = null;

    String datum = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eintrag);

        app = (TinyTimesApplication) getApplicationContext();

        Bundle extras = getIntent().getExtras();
        Tag aktuellerTag = (Tag) extras.getSerializable(MainActivity.TAG_EXTRA_MESSAGE);
        datum = extras.getString(MainActivity.DATUM_EXTRA_MESSAGE);

        TextView txtDatum = (TextView) findViewById(R.id.txtDatum);
        txtDatum.setText(datum);

        editStundenzahl = (EditText) findViewById(R.id.editStundenzahl);
        editStundensatz = (EditText) findViewById(R.id.editStundensatz);
        swUrlaubstag = (Switch) findViewById(R.id.swUrlaubstag);

        if(aktuellerTag != null) {
            // Werte befüllen aus gespeicherten Daten
            double stundenzahl = Math.floor(aktuellerTag.getStundenzahl() * 100.0f + 0.5f) / 100.0f;
            editStundenzahl.setText(String.valueOf(stundenzahl));

            double stundensatz = Math.floor(aktuellerTag.getStundensatz() * 100.0f + 0.5f) / 100.0f;
            editStundensatz.setText(String.valueOf(stundensatz));

            swUrlaubstag.setChecked(aktuellerTag.isUrlaub());
        } else {
            // Werte befüllen aus Default Preferences
            PreferenceData prefData = app.getPrefData();
            double stundenzahl = Math.floor(prefData.getStandardStundenzahl() * 100.0f + 0.5f) / 100.0f;
            editStundenzahl.setText(String.valueOf(stundenzahl));

            double stundensatz = Math.floor(prefData.getStandardStundensatz() * 100.0f + 0.5f) / 100.0f;
            editStundensatz.setText(String.valueOf(stundensatz));
        }
    }

    public void onCancel(View view) {
        this.finish();
    }

    public void onSave(View view) {
        // Wenn alle Werte passen
        DecimalFormat df = new DecimalFormat("0.00");

        double stundenzahl = Double.parseDouble(editStundenzahl.getText().toString());
        double stundensatz = Double.parseDouble(editStundensatz.getText().toString());
        boolean urlaub = swUrlaubstag.isChecked();

        Tag neuerTag = new Tag();
        neuerTag.setStundenzahl(stundenzahl);
        neuerTag.setStundensatz(stundensatz);
        neuerTag.setUrlaub(urlaub);

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(MainActivity.TAG_EXTRA_MESSAGE, neuerTag);
        bundle.putString(MainActivity.DATUM_EXTRA_MESSAGE, datum);
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, intent);
        this.finish();
    }
}
