package com.getpoint.farminfomanager.fragment.Mission;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getpoint.farminfomanager.R;
import com.getpoint.farminfomanager.weights.spinnerWheel.CardWheelHorizontalView;
import com.getpoint.farminfomanager.weights.spinnerWheel.adapters.NumericWheelAdapter;

/**
 * Created by Station on 2016/8/2.
 */
public class FramePointFragment extends PointDetailFragment implements
        CardWheelHorizontalView.OnCardWheelChangedListener{

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_editor_detail_frame;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = super.onCreateView(inflater, container, savedInstanceState);

        final Context context = getActivity().getApplicationContext();

        final NumericWheelAdapter altitudeAdapter = new NumericWheelAdapter(context,
                R.layout.wheel_text_centered, MIN_ALTITUDE,	MAX_ALTITUDE, "%d m");
        CardWheelHorizontalView altitudePicker = (CardWheelHorizontalView) view.findViewById(R.id
                .altitudePicker);
        altitudePicker.setViewAdapter(altitudeAdapter);
        altitudePicker.addChangingListener(this);

        return view;
    }

    @Override
    public void onChanged(CardWheelHorizontalView cardWheel, int oldValue, int newValue) {

    }
}
