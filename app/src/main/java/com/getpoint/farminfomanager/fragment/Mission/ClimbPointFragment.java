package com.getpoint.farminfomanager.fragment.Mission;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.getpoint.farminfomanager.R;
import com.getpoint.farminfomanager.entity.markers.DangerPointMarker;
import com.getpoint.farminfomanager.entity.points.ClimbPoint;
import com.getpoint.farminfomanager.entity.points.DangerPoint;
import com.getpoint.farminfomanager.entity.points.PointItemType;
import com.getpoint.farminfomanager.utils.proxy.MissionItemProxy;
import com.getpoint.farminfomanager.weights.spinnerWheel.CardWheelHorizontalView;


/**
 * Created by Station on 2016/8/4.
 */
public class ClimbPointFragment extends DangerPointFragment implements
        CardWheelHorizontalView.OnCardWheelChangedListener {

    private ClimbPoint climbPoint;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = super.onCreateView(inflater, container, savedInstanceState);

        climbPoint = new ClimbPoint();
        /**
         *  添加内点的监听函数
         */
        Button addInerPoint = (Button) view.findViewById(R.id.id_add_danger_point);
        addInerPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DangerPoint cp = new DangerPoint(mapFragment.getCurrentCoord(),
                        0);

                //cp.setPointType(PointItemType.CLIMBPOINT);
                cp.setPointNum(ClimbPoint.INDICATE_NUM);
                cp.setFlyheight(getFlyHeight());

                MissionItemProxy newItem = new MissionItemProxy(missionProxy, cp);
                climbPoint.addInnerPoint(cp);

                DangerPointMarker pointMarker = new DangerPointMarker(newItem);
                pointMarker.setMarkerNum(climbPoint.getInnerPoint().indexOf(cp));
                markerToAdd.add(mapFragment.updateMarker(pointMarker));

                innerIndex.setText(String.valueOf(climbPoint.getInnerPoint().size()));
            }
        });

        return view;
    }

    public ClimbPoint getClimbPoint() {
        clearInnerVar();
        final ClimbPoint byp = climbPoint;
        return byp;
    }

    public void clearInnerPoint() {
        this.climbPoint = new ClimbPoint();
    }

    @Override
    public void onChanged(CardWheelHorizontalView cardWheel, int oldValue, int newValue) {

    }
}
