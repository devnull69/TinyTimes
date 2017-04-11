package org.theiner.tinytimes.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import org.theiner.tinytimes.R;
import org.theiner.tinytimes.context.TinyTimesApplication;
import org.theiner.tinytimes.data.PreferenceData;
import org.theiner.tinytimes.data.Tag;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

import hirondelle.date4j.DateTime;

public class EintragActivity extends AppCompatActivity {

    TinyTimesApplication app = null;

    EditText editStundenzahl = null;
    EditText editStundensatz = null;
    Spinner spnSelectTagesart = null;
    Switch swMarkieren = null;

    String datum = "";

    private String[] tagesartArray = new String[] {"Normal", "Urlaub", "Krank", "Feiertag"};
    private String[] wochentagArray = new String[] {"So", "Mo", "Di", "Mi", "Do", "Fr", "Sa"};

    private double aktuelleStunden = 0.0f;

    private int currentIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eintrag);

        app = (TinyTimesApplication) getApplicationContext();

        Bundle extras = getIntent().getExtras();
        Tag aktuellerTag = (Tag) extras.getSerializable(MainActivity.TAG_EXTRA_MESSAGE);
        datum = extras.getString(MainActivity.DATUM_EXTRA_MESSAGE);

        // Wochentag ermitteln
        DateTime dateTime = new DateTime(datum.substring(6) + "-" + datum.substring(3, 5) + "-" + datum.substring(0, 2));
        int weekday = dateTime.getWeekDay() - 1;

        TextView txtDatum = (TextView) findViewById(R.id.txtDatum);
        txtDatum.setText(wochentagArray[weekday] + " " + datum);

        editStundenzahl = (EditText) findViewById(R.id.editStundenzahl);
        editStundensatz = (EditText) findViewById(R.id.editStundensatz);
        spnSelectTagesart = (Spinner) findViewById(R.id.spnSelectTagesart);
        swMarkieren = (Switch) findViewById(R.id.swMarkieren);

        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tagesartArray);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSelectTagesart.setAdapter(spinnerAdapter);

        NumberFormat nf2 = NumberFormat.getNumberInstance(Locale.US);
        DecimalFormat df2 = (DecimalFormat)nf2;
        df2.setMaximumFractionDigits(2);
        df2.setMinimumFractionDigits(2);

        NumberFormat nf3 = NumberFormat.getNumberInstance(Locale.US);
        DecimalFormat df3 = (DecimalFormat)nf3;
        df3.setMaximumFractionDigits(3);
        df3.setMinimumFractionDigits(3);

        if(aktuellerTag != null) {
            // Werte befüllen aus gespeicherten Daten
            double stundenzahl = Math.floor(aktuellerTag.getStundenzahl() * 1000.0f + 0.5f) / 1000.0f;
            editStundenzahl.setText(df3.format(stundenzahl));
            aktuelleStunden = stundenzahl;

            double stundensatz = Math.floor(aktuellerTag.getStundensatz() * 100.0f + 0.5f) / 100.0f;
            editStundensatz.setText(df2.format(stundensatz));

            swMarkieren.setChecked(aktuellerTag.isMarkiert());

            currentIndex = aktuellerTag.getTagesart().getValue();
        } else {
            // Werte befüllen aus Default Preferences
            PreferenceData prefData = app.getPrefData();
            double stundenzahl = Math.floor(prefData.getStandardStundenzahl() * 1000.0f + 0.5f) / 1000.0f;
            editStundenzahl.setText(df3.format(stundenzahl));
            aktuelleStunden = stundenzahl;

            double stundensatz = Math.floor(prefData.getStandardStundensatz() * 100.0f + 0.5f) / 100.0f;
            editStundensatz.setText(df2.format(stundensatz));

            swMarkieren.setChecked(false);

            currentIndex = 0;
        }
        spnSelectTagesart.setSelection(currentIndex);

        // Änderung der Tagesart
        spnSelectTagesart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Änderung der Stundenzahl
        editStundenzahl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    aktuelleStunden = Double.parseDouble(editStundenzahl.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void onCancel(View view) {
        this.finish();
    }

    public void onSave(View view) {
        // Wenn alle Werte passen

        double stundenzahl = Double.parseDouble(editStundenzahl.getText().toString());
        double stundensatz = Double.parseDouble(editStundensatz.getText().toString());
        Tag.Tagesart tagesart = Tag.Tagesart.values()[currentIndex];
        boolean markiert = swMarkieren.isChecked();

        Tag neuerTag = new Tag();
        neuerTag.setStundenzahl(stundenzahl);
        neuerTag.setStundensatz(stundensatz);
        neuerTag.setTagesart(tagesart);
        neuerTag.setMarkiert(markiert);

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(MainActivity.TAG_EXTRA_MESSAGE, neuerTag);
        bundle.putString(MainActivity.DATUM_EXTRA_MESSAGE, datum);
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, intent);
        this.finish();
    }

    public void onAddStunden(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(EintragActivity.this);
        alertDialog.setTitle("Stunden hinzufügen");

        View editView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.alert_value_layout, null);
        alertDialog.setView(editView);
        final EditText valueInput = (EditText) editView.findViewById(R.id.edtValue);

        alertDialog.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        double stunden = Double.parseDouble(valueInput.getText().toString());

                        aktuelleStunden += stunden;
                        editStundenzahl.setText(String.valueOf(aktuelleStunden));

                        dialog.dismiss();
                    }
                });

        alertDialog.setNegativeButton("Abbrechen",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }
}
