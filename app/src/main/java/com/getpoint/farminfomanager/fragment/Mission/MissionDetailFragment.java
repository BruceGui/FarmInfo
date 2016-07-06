package com.getpoint.farminfomanager.fragment.Mission;

import android.support.v4.app.DialogFragment;
import android.widget.Spinner;

import com.getpoint.farminfomanager.R;
import com.getpoint.farminfomanager.weights.SpinnerSelfSelect;

/**
 * Created by Gui Zhou on 2016-07-05.
 */
public class MissionDetailFragment extends DialogFragment implements SpinnerSelfSelect.
        OnSpinnerItemSelectedListener {

    protected int getResource() {
        return R.layout.fragment_editro_detail_generic;
    }

    @Override
    public void onSpinnerItemSelected(Spinner spinner, int position) {

    }
}
