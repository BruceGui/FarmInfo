package com.getpoint.farminfomanager.fragment.Mission;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import static com.getpoint.farminfomanager.utils.PixDPConvert.*;

import com.baidu.mapapi.map.Marker;
import com.getpoint.farminfomanager.R;
import com.getpoint.farminfomanager.entity.markers.DangerPointMarker;
import com.getpoint.farminfomanager.entity.points.DangerPoint;
import com.getpoint.farminfomanager.entity.points.PointInfo;
import com.getpoint.farminfomanager.entity.points.enumc.DangerPointType;
import com.getpoint.farminfomanager.utils.adapters.IndexAdapter;
import com.getpoint.farminfomanager.utils.proxy.MissionItemProxy;
import com.getpoint.farminfomanager.utils.proxy.MissionProxy;
import com.getpoint.farminfomanager.weights.spinnerWheel.CardWheelHorizontalView;
import com.getpoint.farminfomanager.weights.spinnerWheel.adapters.NumericWheelAdapter;
import com.getpoint.farminfomanager.weights.spinners.SpinnerSelfSelect;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Station on 2016/8/2.
 */

public class DangerPointFragment extends PointDetailFragment implements
        CardWheelHorizontalView.OnCardWheelChangedListener {

    private static final String TAG = "DangerPoint";

    private TextView pointIndex;
    protected TextView innerIndex;
    protected List<Marker> markerToAdd;
    private RadioGroup dPointTypeSel;
    private CardWheelHorizontalView altitudePickerMeter;
    private CardWheelHorizontalView altitudePickerCentimeter;

    private List<String> pointIND = new ArrayList<>();
    private List<String> innerInList = new ArrayList<>();

    private DangerPoint currDP;
    private PointInfo   currInP;

    private boolean addInP = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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

        innerIndex = (TextView) view.findViewById(R.id.dangerInnerIndex);
        innerIndex.setText(String.valueOf(0));

        pointIndex = (TextView) view.findViewById(R.id.dangerPointIndex);

        /**
         *  障碍点类型的选择
         */
        dPointTypeSel = (RadioGroup) view.findViewById(R.id.danger_point_type_rgroup);


        /**
         *   选择障碍点 及 内部点 的东西
         */
        final SpinnerSelfSelect pointINDSel = (SpinnerSelfSelect) view.findViewById(R.id.spinnerDPIndex);
        pointINDSel.setOnSpinnerItemSelectedListener(new SpinnerSelfSelect.OnSpinnerItemSelectedListener() {
            @Override
            public void onSpinnerItemSelected(Spinner spinner, int position) {

                setPointIndex(position);

                addNew = false;

                updateInnerIndex(missionProxy.getDangerItemProxies().get(position));

                currDP = (DangerPoint) missionProxy.getDangerItemProxies()
                        .get(position).getPointInfo();

                switch (currDP.getdPType()) {
                    case BYPASS:
                        dPointTypeSel.check(R.id.bypass_point_radio);
                        break;
                    case CLIMB:
                        dPointTypeSel.check(R.id.climb_point_radio);
                        break;
                    case FORWARD:
                        dPointTypeSel.check(R.id.forward_point_radio);
                        break;
                    default:
                        break;
                }

            }
        });


        final IndexAdapter pINDAdapter = new IndexAdapter(getActivity(), pointIND);
        pointINDSel.setAdapter(pINDAdapter);

        final SpinnerSelfSelect pointInnerSel = (SpinnerSelfSelect) view.findViewById(R.id.spinnerDPInnerIndex);
        pointInnerSel.setOnSpinnerItemSelectedListener(new SpinnerSelfSelect.OnSpinnerItemSelectedListener() {
            @Override
            public void onSpinnerItemSelected(Spinner spinner, int position) {
                //TODO
                innerIndex.setText(String.valueOf(position+1));
                addInP = false;
            }
        });
        final IndexAdapter innerSelAdapter = new IndexAdapter(getActivity(), innerInList);
        pointInnerSel.setAdapter(innerSelAdapter);

        markerToAdd = new ArrayList<>();
        return view;
    }

    /**
     * 当取消添加点的时候，然后就可以删除 marker
     */
    public void removeMarker() {

        for (Marker marker : markerToAdd) {
            marker.remove();
        }

        clearInnerVar();
    }

    /**
     *   获取当前障碍点的 类型
     * @return 障碍点类型
     */

    public DangerPointType getCurrentDPType() {

        switch (dPointTypeSel.getCheckedRadioButtonId()) {
            case R.id.bypass_point_radio:
                return DangerPointType.BYPASS;
            case R.id.forward_point_radio:
                return DangerPointType.FORWARD;
            case R.id.climb_point_radio:
                return DangerPointType.CLIMB;
            default:
                return null;
        }

    }

    /**
     *  更新障碍点的 索引
     * @param m
     */
    public void updatePointIND(MissionProxy m) {
        pointIND.clear();

        int length = m.getDangerItemProxies().size();

        for (int i = 0; i < length; i++) {
            pointIND.add(String.valueOf(DangerPointMarker.IND[i]));
        }
    }

    public void updateInnerIndex(MissionItemProxy m) {

        innerInList.clear();

        List<PointInfo> p = ((DangerPoint)m.getPointInfo()).getInnerPoints();

        int length = p.size();

        for (int i = 0; i < length; i++) {
            innerInList.add(String.valueOf(i+1));
        }

    }

    /**
     *  更新当前障碍点 的信息
     */
    public void updateCurrentDP() {

        if(addInP) {
            //TODO 添加障碍点的内部点
        } else {
            updateCurrentInP();
        }

        addNew = true;

    }

    public void updateCurrentInP() {

        addInP = true;
    }

    public void clearInnerVar() {
        markerToAdd.clear();
        innerInList.clear();
        innerIndex.setText(String.valueOf(0));
    }

    public void setPointIndex(int index) {
        pointIndex.setText(String.valueOf(DangerPointMarker.IND[index]));
    }

    public float getFlyHeight() {

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

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_editor_detail_danger;
    }

    @Override
    public void onChanged(CardWheelHorizontalView cardWheel, int oldValue, int newValue) {

    }

}
