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

public class PointEditorFragment extends DialogFragment {

    protected static final int MIN_ALTITUDE = -200;
    protected static final int MAX_ALTITUDE = +200;
    protected static final int MIN_CENTIMETER = 0;
    protected static final int MAX_CENTIMETER = 99;

    PointEditorListener listener;

    public static PointEditorFragment newInstance(PointEditorListener listener) {

        PointEditorFragment p = new PointEditorFragment();

        p.listener = listener;

        return p;

    }

    public interface PointEditorListener {

        void onPConfirm();

        void onPCancel();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getDialog().setTitle(R.string.change_f_height);

        final View rootview = inflater.inflate(R.layout.point_editor_fragment, container, false);

        final Context context = getActivity().getApplicationContext();

        final NumericWheelAdapter altitudeAdapterMeter = new NumericWheelAdapter(context,
                R.layout.wheel_text_centered, MIN_ALTITUDE, MAX_ALTITUDE, "%d m");
        final NumericWheelAdapter altitudeAdapterCentimeter = new NumericWheelAdapter(context,
                R.layout.wheel_text_centered, MIN_CENTIMETER, MAX_CENTIMETER, "%d cm");

        CardWheelHorizontalView altitudePickerMeter = (CardWheelHorizontalView) rootview.findViewById(R.id
                .altitudePickerMeter);
        altitudePickerMeter.setViewAdapter(altitudeAdapterMeter);
        altitudePickerMeter.setCurrentValue(0);

        CardWheelHorizontalView altitudePickerCentimeter = (CardWheelHorizontalView) rootview.findViewById(R.id
                .altitudePickerCentimeter);
        altitudePickerCentimeter.setViewAdapter(altitudeAdapterCentimeter);
        altitudePickerCentimeter.setCurrentValue(0);

        /**
         *   按钮监听函数
         */
        Button mConfirm = (Button) rootview.findViewById(R.id.btnConfirm);
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPConfirm();
            }
        });

        Button mCancel = (Button) rootview.findViewById(R.id.btnCancel);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPCancel();
            }
        });

        return rootview;
    }


}
