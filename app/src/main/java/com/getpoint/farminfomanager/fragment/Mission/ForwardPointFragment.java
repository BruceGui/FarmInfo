package com.getpoint.farminfomanager.fragment.Mission;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mapapi.map.Marker;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Station on 2016/8/4.
 */

public class ForwardPointFragment extends DangerPointFragment implements
        CardWheelHorizontalView.OnCardWheelChangedListener {

    private ForwardPoint forwardPoint;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = super.onCreateView(inflater, container, savedInstanceState);

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
                markerToAdd.add(mapFragment.updateMarker(pointMarker));

                innerIndex.setText(String.valueOf(forwardPoint.getInnerPoint().size()));
            }
        });

        return view;
    }

    public ForwardPoint getForwardPoint() {
        clearInnerVar();
        final ForwardPoint byp = forwardPoint;
        return byp;
    }

    public void clearInnerPoint() {
        this.forwardPoint = new ForwardPoint();
    }

    @Override
    public void onChanged(CardWheelHorizontalView cardWheel, int oldValue, int newValue) {

    }
}
