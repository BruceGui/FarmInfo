package com.getpoint.farminfomanager.fragment.Mission;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.getpoint.farminfomanager.R;
import com.getpoint.farminfomanager.weights.spinners.SpinnerSelfSelect;

/**
 * Created by Gui Zhou on 2016-07-05.
 */
public class PointDetailFragment extends Fragment implements SpinnerSelfSelect.
        OnSpinnerItemSelectedListener {

    private static final String TAG = "PointDetailFragment";

    protected static final int MIN_ALTITUDE = -200;
    protected static final int MAX_ALTITUDE = +200;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutResource(), container, false);
    }

    protected  int getLayoutResource() {
        return R.layout.fragment_editor_detail_generic;
    }

    @Override
    public void onSpinnerItemSelected(Spinner spinner, int position) {

    }
}
