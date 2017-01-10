package org.theiner.tinytimes.fragments;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

import org.theiner.tinytimes.adapter.CaldroidTinyTimesAdapter;

/**
 * Created by TTheiner on 10.01.2017.
 */

public class CaldroidTinyTimesFragment extends CaldroidFragment {

    @Override
    public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
        // TODO Auto-generated method stub
        return new CaldroidTinyTimesAdapter(getActivity(), month, year, getCaldroidData(), extraData);
    }

}