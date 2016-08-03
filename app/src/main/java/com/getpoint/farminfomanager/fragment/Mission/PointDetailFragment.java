package com.getpoint.farminfomanager.fragment.Mission;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.getpoint.farminfomanager.R;
import com.getpoint.farminfomanager.entity.PointItemType;
import com.getpoint.farminfomanager.utils.adapters.AdapterMissionItems;
import com.getpoint.farminfomanager.weights.spinners.SpinnerSelfSelect;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Gui Zhou on 2016-07-05.
 */
public class PointDetailFragment extends Fragment implements SpinnerSelfSelect.
        OnSpinnerItemSelectedListener {

    private static final String TAG = "PointDetailFragment";

    protected static final int MIN_ALTITUDE = -200;
    protected static final int MAX_ALTITUDE = +200;

    protected SpinnerSelfSelect typeSpinner;
    protected AdapterMissionItems commandAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(getLayoutResource(), container, false);

        PointItemType.FRAMEPOINT.setLabel(getActivity().getApplicationContext().getResources().getString(
                R.string.frame_point
        ));

        PointItemType.DANGERPOINT.setLabel(getActivity().getApplicationContext().getResources().getString(
                R.string.danger_point
        ));

        List<PointItemType> list = new LinkedList<>(Arrays.asList(PointItemType.values()));

        commandAdapter = new AdapterMissionItems(getActivity(),
                android.R.layout.simple_list_item_1, list.toArray(new PointItemType[list.size()]));

        typeSpinner = (SpinnerSelfSelect)view.findViewById(R.id.spinnerWaypointType);
        typeSpinner.setAdapter(commandAdapter);
        typeSpinner.setOnSpinnerItemSelectedListener(this);

        return view;
    }

    protected  int getLayoutResource() {
        return R.layout.fragment_editor_detail_generic;
    }

    @Override
    public void onSpinnerItemSelected(Spinner spinner, int position) {

    }
}
