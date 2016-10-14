package com.getpoint.farminfomanager.fragment.Mission;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.getpoint.farminfomanager.R;
import com.getpoint.farminfomanager.entity.markers.DangerPointMarker;
import com.getpoint.farminfomanager.entity.points.BypassPoint;
import com.getpoint.farminfomanager.entity.points.ClimbPoint;
import com.getpoint.farminfomanager.entity.points.DangerPoint;
import com.getpoint.farminfomanager.entity.points.ForwardPoint;
import com.getpoint.farminfomanager.entity.points.PointItemType;
import com.getpoint.farminfomanager.utils.proxy.MissionItemProxy;
import com.getpoint.farminfomanager.weights.spinnerWheel.CardWheelHorizontalView;
import com.getpoint.farminfomanager.weights.spinnerWheel.adapters.NumericWheelAdapter;

/**
 * Created by Station on 2016/8/4.
 */

public class ForwardPointFragment extends PointDetailFragment implements
        CardWheelHorizontalView.OnCardWheelChangedListener {

    private TextView pointIndex;
    private TextView innerIndex;
    private ForwardPoint forwardPoint;
    private CardWheelHorizontalView altitudePickerMeter;
    private CardWheelHorizontalView altitudePickerCentimeter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = super.onCreateView(inflater, container, savedInstanceState);

        final TextView pointType = (TextView) view.findViewById(R.id.WaypointType);
        pointType.setText(R.string.forward_danger_poi);

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
        pointIndex.setText(String.valueOf(missionProxy.getCurrentForwardNumber()));

        forwardPoint = new ForwardPoint();

        /**
         *  添加内点的监听函数
         */
        Button addInerPoint = (Button) view.findViewById(R.id.id_add_danger_point);
        addInerPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DangerPoint fp = new DangerPoint(mapFragment.getCurrentCoord(),
                        0);

                fp.setPointType(PointItemType.FORWAEDPOINT);
                fp.setPointNum(ForwardPoint.INDICATE_NUM);
                fp.setFlyheight(getFlyHeight());

                MissionItemProxy newItem = new MissionItemProxy(missionProxy, fp);
                forwardPoint.addInnerPoint(fp);

                DangerPointMarker pointMarker = new DangerPointMarker(newItem);
                pointMarker.setMarkerNum(forwardPoint.getInnerPoint().indexOf(fp));
                mapFragment.updateMarker(pointMarker);

                innerIndex.setText(String.valueOf(forwardPoint.getInnerPoint().size()));
            }
        });

        return view;
    }

    public void setPointIndex(int index) {
        pointIndex.setText(String.valueOf(index));
    }

    public ForwardPoint getForwardPoint() {
        innerIndex.setText(String.valueOf(0));
        final ForwardPoint byp = forwardPoint;
        return byp;
    }

    public void clearInnerPoint() {
        this.forwardPoint = new ForwardPoint();
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
