package com.getpoint.farminfomanager.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.getpoint.farminfomanager.R;
import com.getpoint.farminfomanager.weights.spinnerWheel.CardWheelHorizontalView;
import com.getpoint.farminfomanager.weights.spinnerWheel.adapters.NumericWheelAdapter;

/**
 * Created by Gui Zhou on 2016/10/19.
 */

//TODO 飞行高度修改

public class PointEditorFragment extends DialogFragment {

    protected static final int MIN_ALTITUDE = -200;
    protected static final int MAX_ALTITUDE = +200;
    protected static final int MIN_CENTIMETER = 0;
    protected static final int MAX_CENTIMETER = 99;

    private Button mConfirmBtn;
    private Button mCancelBtn;

    private CardWheelHorizontalView altitudePickerMeter;
    private CardWheelHorizontalView altitudePickerCentimeter;

    public static PointEditorFragment newInstance(   ) {

        PointEditorFragment p = new PointEditorFragment();

        return p;

    }

    public interface PointEditorListener {

        void onConfirm();

        void onCancel();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootview = inflater.inflate(R.layout.point_editor_fragment, container, false);

        final Context context = getActivity().getApplicationContext();

        final NumericWheelAdapter altitudeAdapterMeter = new NumericWheelAdapter(context,
                R.layout.wheel_text_centered, MIN_ALTITUDE, MAX_ALTITUDE, "%d m");
        final NumericWheelAdapter altitudeAdapterCentimeter = new NumericWheelAdapter(context,
                R.layout.wheel_text_centered, MIN_CENTIMETER, MAX_CENTIMETER, "%d cm");

        altitudePickerMeter = (CardWheelHorizontalView) rootview.findViewById(R.id
                .altitudePickerMeter);
        altitudePickerMeter.setViewAdapter(altitudeAdapterMeter);
        altitudePickerMeter.setCurrentValue(0);

        altitudePickerCentimeter = (CardWheelHorizontalView) rootview.findViewById(R.id
                .altitudePickerCentimeter);
        altitudePickerCentimeter.setViewAdapter(altitudeAdapterCentimeter);
        altitudePickerCentimeter.setCurrentValue(0);

        return rootview;
    }


}
