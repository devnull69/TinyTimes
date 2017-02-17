package org.theiner.tinytimes.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import org.theiner.tinytimes.R;
import org.theiner.tinytimes.context.TinyTimesApplication;
import org.theiner.tinytimes.data.Kalender;
import org.theiner.tinytimes.data.Monat;
import org.theiner.tinytimes.data.Tag;
import org.theiner.tinytimes.fragments.CaldroidTinyTimesFragment;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private CaldroidTinyTimesFragment cfKalender = null;
    private TextView txtNetto = null;
    private TextView txtGesamtstunden = null;
    private HashMap<String, Monat> kalenderMonate = new HashMap<String, Monat>();
    private final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

    private String monatKey = null;

    private TinyTimesApplication app = null;

    public static String TAG_EXTRA_MESSAGE = "org.theiner.tinytimes.UPDATETAG";
    public static String DATUM_EXTRA_MESSAGE = "org.theiner.tinytimes.DATUMVOLL";
    private static int REQUEST_UPDATE_TAG = 12345;
    private static int REQUEST_SET_PREFS = 23456;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        app = (TinyTimesApplication) getApplicationContext();

        // Zum Zurücksetzen des Kalenders
//        app.resetKalenderInPrefs();

        txtNetto = (TextView) findViewById(R.id.txtNetto);
        txtGesamtstunden = (TextView) findViewById(R.id.txtGesamtstunden);

        cfKalender = new CaldroidTinyTimesFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.MONDAY);
        args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, true);
        cfKalender.setArguments(args);

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar, cfKalender);
        t.commit();

        // Aktuellen Tag mit roter Schrift hervorheben
        ColorDrawable red = new ColorDrawable(Color.RED);
        cfKalender.setTextColorForDate(R.color.colorRed, new Date());

        final CaldroidListener listener = new CaldroidListener() {

            // Klick auf Kalendereintrag erfassen
            @Override
            public void onSelectDate(Date date, View view) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
                SimpleDateFormat sdfTag = new SimpleDateFormat("dd");
                monatKey = sdf.format(date);
                final int day = Integer.valueOf(sdfTag.format(date));

                SimpleDateFormat sdfVoll = new SimpleDateFormat("dd.MM.yyyy");

                Tag currentTag = null;

                if(kalenderMonate.containsKey(monatKey) && kalenderMonate.get(monatKey).getTage()[day] != null) {
                    currentTag = kalenderMonate.get(monatKey).getTage()[day];
                }

                // Activity Kalendereintrag bearbeiten auswählen
                Intent intent = new Intent(MainActivity.this, EintragActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(TAG_EXTRA_MESSAGE, currentTag);
                bundle.putString(DATUM_EXTRA_MESSAGE, sdfVoll.format(date));
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_UPDATE_TAG);
            }

            // Ändern des Monats
            @Override
            public void onChangeMonth(int month, int year) {
                DecimalFormat df = new DecimalFormat("00");
                monatKey = String.valueOf(year) + df.format(month);
                updateView();
            }

            // Löschen eines Tages
            @Override
            public void onLongClickDate(Date date, View view) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
                SimpleDateFormat sdfTag = new SimpleDateFormat("dd");
                monatKey = sdf.format(date);
                final int day = Integer.valueOf(sdfTag.format(date));

                SimpleDateFormat sdfVoll = new SimpleDateFormat("dd.MM.yyyy");

                if(kalenderMonate.containsKey(monatKey) && kalenderMonate.get(monatKey).getTage()[day] != null) {
                    // Soll dieser Eintrag gelöscht werden?
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("Eintrag löschen");
                    alertDialog.setMessage("Sollen die erfassten Stunden für den " + sdfVoll.format(date) + " wirklich gelöscht werden?");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ja",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    kalenderMonate.get(monatKey).getTage()[day] = null;

                                    cfKalender.refreshView();

                                    updateView();

                                    app.saveKalender();

                                    dialog.dismiss();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Nein",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            }
        };

        cfKalender.setCaldroidListener(listener);

        // Kalender holen
        Kalender mykalender = app.getKalender();
        kalenderMonate = mykalender.getKalenderMonate();

        // Daten aus Kalender markieren
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        monatKey = sdf.format(new Date());

        updateView();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_exit) {
            System.exit(0);
            return true;
        }

        if (id == R.id.action_options) {
            Intent intent = new Intent(this, OptionActivity.class);
            startActivityForResult(intent, REQUEST_SET_PREFS);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_UPDATE_TAG && resultCode == Activity.RESULT_OK) {

            Bundle extras = data.getExtras();

            SimpleDateFormat sdfVoll = new SimpleDateFormat("dd.MM.yyyy");

            String datum = extras.getString(DATUM_EXTRA_MESSAGE);
            Tag aktuellerTag = (Tag) extras.getSerializable(TAG_EXTRA_MESSAGE);
            try {
                Date date = sdfVoll.parse(datum);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
                SimpleDateFormat sdfTag = new SimpleDateFormat("dd");

                monatKey = sdf.format(date);
                int day = Integer.valueOf(sdfTag.format(date));

                Tag updateTag = null;
                if(kalenderMonate.containsKey(monatKey) && kalenderMonate.get(monatKey).getTage()[day] != null) {
                    // Update
                    updateTag = kalenderMonate.get(monatKey).getTage()[day];
                } else {
                    // Create
                    updateTag = new Tag();
                    Monat monat = null;
                    if(!kalenderMonate.containsKey(monatKey)) {
                        monat = new Monat();
                        kalenderMonate.put(monatKey, monat);
                    } else {
                        monat = kalenderMonate.get(monatKey);
                    }
                    monat.getTage()[day] = updateTag;
                }
                updateTag.setStundenzahl(aktuellerTag.getStundenzahl());
                updateTag.setStundensatz(aktuellerTag.getStundensatz());
                updateTag.setTagesart(aktuellerTag.getTagesart());
                updateTag.setMarkiert(aktuellerTag.isMarkiert());

                // Speichern
                app.saveKalender();

                // Anzeigen
                updateView();

                cfKalender.refreshView();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // Gibt es den Tag schon, dann update, sonst neu erstellen

        }

        if(requestCode == REQUEST_SET_PREFS && resultCode == Activity.RESULT_OK) {
                // Anzeigen
                updateView();
        }
    }

    private void updateView() {
        double summe = 0.0f;
        double stundensumme = 0.0f;
        double monatsstundensatz = app.getPrefData().getStandardStundensatz();
        if (kalenderMonate.containsKey(monatKey)) {
            Monat monatAktuell = kalenderMonate.get(monatKey);
            for (int i = 1; i <= 31; i++) {
                Tag aktuellerTag = monatAktuell.getTage()[i];
                if (aktuellerTag != null) {
                    //summe += aktuellerTag.getStundensatz() * aktuellerTag.getStundenzahl();
                    if(aktuellerTag.getStundensatz() != monatsstundensatz)
                        monatsstundensatz = aktuellerTag.getStundensatz();
                    stundensumme += aktuellerTag.getStundenzahl();
                }
            }
        }

        // Summe aktualisieren
        stundensumme = Math.floor(stundensumme * 100.0f + 0.5f) / 100.0f;
        summe = monatsstundensatz * stundensumme;
        summe = summe * (1 - app.getPrefData().getSteuerAbzug()/100.0f);  // Abzug
        DecimalFormat nettoFormat = new DecimalFormat("0.00");
        txtNetto.setText(nettoFormat.format(summe));
        txtGesamtstunden.setText(nettoFormat.format(stundensumme) + " Stunden");

        // kalenderMonate als extraData an den Adapter übertragen und REFRESH
        // zusätzlich die ca. Zellengröße
        HashMap<String, Object> extraData = (HashMap<String,Object>) cfKalender.getExtraData();

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Integer cellwidth = metrics.widthPixels / 7 - 16;

        extraData.put("kalender", kalenderMonate);
        extraData.put("cellwidth", cellwidth);
        cfKalender.refreshView();
    }
}
