package com.getpoint.farminfomanager.fragment.Mission;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.baidu.mapapi.map.Text;
import com.getpoint.farminfomanager.R;
import com.getpoint.farminfomanager.entity.coordinate.LatLong;
import com.getpoint.farminfomanager.entity.points.FramePoint;
import com.getpoint.farminfomanager.utils.adapters.IndexAdapter;
import com.getpoint.farminfomanager.utils.proxy.MissionProxy;
import com.getpoint.farminfomanager.weights.spinnerWheel.CardWheelHorizontalView;
import com.getpoint.farminfomanager.weights.spinnerWheel.adapters.NumericWheelAdapter;
import com.getpoint.farminfomanager.weights.spinners.SpinnerSelfSelect;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Station on 2016/8/2.
 */
public class FramePointFragment extends PointDetailFragment implements
        CardWheelHorizontalView.OnCardWheelChangedListener {

    private static final String TAG = "FramePoint";

    private TextView pointIndex;
    private CardWheelHorizontalView altitudePickerMeter;
    private CardWheelHorizontalView altitudePickerCentimeter;
    private List<String> pointNum = new ArrayList<>();

    private FramePoint currFP;

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

        final NumericWheelAdapter altitudeAdapterMeter = new NumericWheelAdapter(context,
                R.layout.wheel_text_centered, MIN_ALTITUDE, MAX_ALTITUDE, "%d m");
        final NumericWheelAdapter altitudeAdapterCentimeter = new NumericWheelAdapter(context,
                R.layout.wheel_text_centered, MIN_CENTIMETER, MAX_CENTIMETER, "%d cm");
        altitudePickerMeter = (CardWheelHorizontalView) view.findViewById(R.id
                .altitudePickerMeter);
        altitudePickerMeter.setViewAdapter(altitudeAdapterMeter);
        altitudePickerMeter.setCurrentValue(0);
        altitudePickerMeter.addChangingListener(this);

        altitudePickerCentimeter = (CardWheelHorizontalView) view.findViewById(R.id
                .altitudePickerCentimeter);
        altitudePickerCentimeter.setViewAdapter(altitudeAdapterCentimeter);
        altitudePickerCentimeter.setCurrentValue(0);

        pointIndex = (TextView) view.findViewById(R.id.WaypointIndex);
        pointIndex.setText(String.valueOf(missionProxy.getCurrentFrameNumber()));

        final SpinnerSelfSelect mFPIndexSel = (SpinnerSelfSelect) view.findViewById(R.id.spinnerFPIndex);
        mFPIndexSel.setOnSpinnerItemSelectedListener(new SpinnerSelfSelect.OnSpinnerItemSelectedListener() {
            @Override
            public void onSpinnerItemSelected(Spinner spinner, int position) {

                setPointIndex(position+1);
                addNew = false;
                currFP = (FramePoint)missionProxy.getBoundaryItemProxies()
                            .get(position).getPointInfo();
                int fh = (int)currFP.getPosition().getAltitude();

                altitudePickerMeter.setCurrentValue(fh/100);
                altitudePickerCentimeter.setCurrentValue(fh%100);
            }
        });

        /**
         *  设置 mFPIndexSel 的适配器
         */
        final IndexAdapter p = new IndexAdapter(getActivity(), pointNum);
        mFPIndexSel.setAdapter(p);

        return view;
    }

    /**
     *    更新适配器 list 的信息
     * @param m 任务
     */
    public void updatePointNum(MissionProxy m) {

        pointNum.clear();

        int length = m.getBoundaryItemProxies().size();

        for (int i = 0; i < length; i++) {
            pointNum.add(String.valueOf(i + 1));
        }
    }

    /**
     *  更新当前点的信息
     */
    public void updateCurrentFP() {
        int fh = altitudePickerMeter.getCurrentValue()*100
                + altitudePickerCentimeter.getCurrentValue();

        currFP.getPosition().setAltitude(fh);

        addNew = true;
    }

    public float getAltitude() {

        float altitude;

        if (altitudePickerMeter.getCurrentValue() < 0) {
            altitude = altitudePickerMeter.getCurrentValue() * 100
                    - altitudePickerCentimeter.getCurrentValue();
        } else {
            altitude = altitudePickerMeter.getCurrentValue() * 100
                    + altitudePickerCentimeter.getCurrentValue();
        }

        return altitude;
    }

    public void setPointIndex(int index) {
        pointIndex.setText(String.valueOf(index));
    }


    @Override
    public void onChanged(CardWheelHorizontalView cardWheel, int oldValue, int newValue) {

    }

}
