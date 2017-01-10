package org.theiner.tinytimes.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.theiner.tinytimes.R;
import org.theiner.tinytimes.context.TinyTimesApplication;
import org.theiner.tinytimes.data.PreferenceData;
import org.theiner.tinytimes.data.Tag;

import java.text.DecimalFormat;
import java.util.Calendar;

public class EintragActivity extends AppCompatActivity {

    TinyTimesApplication app = null;

    EditText editStundenzahl = null;
    EditText editStundensatz = null;
    Spinner spnSelectTagesart = null;

    String datum = "";

    private String[] tagesartArray = new String[] {"Normal", "Urlaub", "Krank"};

    private int currentIndex = -1;

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
        spnSelectTagesart = (Spinner) findViewById(R.id.spnSelectTagesart);

        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tagesartArray);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSelectTagesart.setAdapter(spinnerAdapter);

        if(aktuellerTag != null) {
            // Werte befüllen aus gespeicherten Daten
            double stundenzahl = Math.floor(aktuellerTag.getStundenzahl() * 100.0f + 0.5f) / 100.0f;
            editStundenzahl.setText(String.valueOf(stundenzahl));

            double stundensatz = Math.floor(aktuellerTag.getStundensatz() * 100.0f + 0.5f) / 100.0f;
            editStundensatz.setText(String.valueOf(stundensatz));

            currentIndex = aktuellerTag.getTagesart().getValue();
        } else {
            // Werte befüllen aus Default Preferences
            PreferenceData prefData = app.getPrefData();
            double stundenzahl = Math.floor(prefData.getStandardStundenzahl() * 100.0f + 0.5f) / 100.0f;
            editStundenzahl.setText(String.valueOf(stundenzahl));

            double stundensatz = Math.floor(prefData.getStandardStundensatz() * 100.0f + 0.5f) / 100.0f;
            editStundensatz.setText(String.valueOf(stundensatz));
            currentIndex = 0;
        }
        spnSelectTagesart.setSelection(currentIndex);

        spnSelectTagesart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void onCancel(View view) {
        this.finish();
    }

    public void onSave(View view) {
        // Wenn alle Werte passen
        DecimalFormat df = new DecimalFormat("0.00");

        double stundenzahl = Double.parseDouble(editStundenzahl.getText().toString());
        double stundensatz = Double.parseDouble(editStundensatz.getText().toString());
        Tag.Tagesart tagesart = Tag.Tagesart.values()[currentIndex];

        Tag neuerTag = new Tag();
        neuerTag.setStundenzahl(stundenzahl);
        neuerTag.setStundensatz(stundensatz);
        neuerTag.setTagesart(tagesart);

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(MainActivity.TAG_EXTRA_MESSAGE, neuerTag);
        bundle.putString(MainActivity.DATUM_EXTRA_MESSAGE, datum);
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, intent);
        this.finish();
    }
}
