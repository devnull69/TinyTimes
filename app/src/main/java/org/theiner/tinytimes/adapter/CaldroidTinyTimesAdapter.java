package org.theiner.tinytimes.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

import org.theiner.tinytimes.R;
import org.theiner.tinytimes.data.Monat;
import org.theiner.tinytimes.data.Tag;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import hirondelle.date4j.DateTime;

/**
 * Created by TTheiner on 10.01.2017.
 */

public class CaldroidTinyTimesAdapter extends CaldroidGridAdapter {

    public CaldroidTinyTimesAdapter(Context context, int month, int year,
                                    Map<String, Object> caldroidData,
                                    Map<String, Object> extraData) {
        super(context, month, year, caldroidData, extraData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View cellView = convertView;

        // For reuse
        if (convertView == null) {
            cellView = inflater.inflate(R.layout.custom_cell, null);
        }

        // extraData holen
        HashMap<String, Monat> kalenderMonate = (HashMap<String, Monat>) extraData.get("kalender");
        Integer cellwidth = (Integer) extraData.get("cellwidth");

        int topPadding = cellView.getPaddingTop();
        int leftPadding = cellView.getPaddingLeft();
        int bottomPadding = cellView.getPaddingBottom();
        int rightPadding = cellView.getPaddingRight();

        TextView tv1 = (TextView) cellView.findViewById(R.id.tv1);
        ImageView iv1 = (ImageView) cellView.findViewById(R.id.iv1);
        ImageView iv2 = (ImageView) cellView.findViewById(R.id.iv2);

        ///
        /// Standard-Werte festlegen
        ///
        tv1.setTextColor(Color.BLACK);
        iv1.setImageDrawable(null);
        iv2.setImageDrawable(null);

        // Get dateTime of this cell
        DateTime dateTime = this.datetimeList.get(position);
        Resources resources = context.getResources();

        // Set color of the dates in previous / next month
        if (dateTime.getMonth() != month) {
            tv1.setTextColor(resources
                    .getColor(com.caldroid.R.color.caldroid_darker_gray));
        }

        boolean shouldResetDiabledView = false;
        boolean shouldResetSelectedView = false;

        // Customize for disabled dates and date outside min/max dates
        if ((minDateTime != null && dateTime.lt(minDateTime))
                || (maxDateTime != null && dateTime.gt(maxDateTime))
                || (disableDates != null && disableDates.indexOf(dateTime) != -1)) {

            tv1.setTextColor(CaldroidFragment.disabledTextColor);
            if (CaldroidFragment.disabledBackgroundDrawable == -1) {
                cellView.setBackgroundResource(com.caldroid.R.drawable.disable_cell);
            } else {
                cellView.setBackgroundResource(CaldroidFragment.disabledBackgroundDrawable);
            }

            if (dateTime.equals(getToday())) {
                cellView.setBackgroundResource(com.caldroid.R.drawable.red_border_gray_bg);
            }

        } else {
            shouldResetDiabledView = true;
        }

        // Customize for selected dates
        if (selectedDates != null && selectedDates.indexOf(dateTime) != -1) {
            cellView.setBackgroundColor(resources
                    .getColor(com.caldroid.R.color.caldroid_sky_blue));

            tv1.setTextColor(Color.BLACK);

        } else {
            shouldResetSelectedView = true;
        }

        if (shouldResetDiabledView && shouldResetSelectedView) {
            // Customize for today
            if (dateTime.equals(getToday())) {
                cellView.setBackgroundResource(com.caldroid.R.drawable.red_border);
            } else {
                cellView.setBackgroundResource(com.caldroid.R.drawable.cell_bg);
            }
        }

        tv1.setText("" + dateTime.getDay());

        // Somehow after setBackgroundResource, the padding collapse.
        // This is to recover the padding
        cellView.setPadding(leftPadding, topPadding, rightPadding,
                bottomPadding);

        // Set custom color if required
        setCustomResources(dateTime, cellView, tv1);

        ///
        /// Custom-Werte im View anpassen
        ///
        DecimalFormat twoDigits = new DecimalFormat("00");
        String monatKey = String.valueOf(year) + twoDigits.format(month);

        if(kalenderMonate.containsKey(monatKey)) {
            Tag aktuellerTag = kalenderMonate.get(monatKey).getTage()[dateTime.getDay()];
            if(aktuellerTag != null && dateTime.getMonth() == month) {
                switch(aktuellerTag.getTagesart()) {
                    case NORMAL:
                        cellView.setBackgroundColor(resources.getColor(R.color.colorGreen));
                        break;
                    case URLAUB:
                        cellView.setBackgroundColor(resources.getColor(R.color.colorYellow));
                        break;
                    case KRANK:
                        cellView.setBackgroundResource(R.color.caldroid_lighter_gray);
                        iv1.setImageDrawable(resources.getDrawable(R.drawable.roteskreuz));
                        break;
                    case FEIERTAG:
                        cellView.setBackgroundColor(resources.getColor(R.color.colorDarkGreen));
                        break;
                }

                if(aktuellerTag.isMarkiert())
                    iv2.setImageDrawable(resources.getDrawable(R.drawable.mark));
            }
        }

        cellView.setMinimumHeight(cellwidth);

        return cellView;
    }

}