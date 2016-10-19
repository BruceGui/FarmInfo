package com.getpoint.farminfomanager.fragment.Mission;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.getpoint.farminfomanager.R;
import com.getpoint.farminfomanager.entity.markers.DangerPointMarker;
import com.getpoint.farminfomanager.entity.points.BypassPoint;
import com.getpoint.farminfomanager.entity.points.DangerPoint;
import com.getpoint.farminfomanager.entity.points.PointItemType;
import com.getpoint.farminfomanager.utils.proxy.MissionItemProxy;
import com.getpoint.farminfomanager.weights.spinnerWheel.CardWheelHorizontalView;


/**
 * Created by Station on 2016/8/4.
 */
public class BypassPointFragment extends DangerPointFragment implements
        CardWheelHorizontalView.OnCardWheelChangedListener {


    private BypassPoint bypassPoint;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = super.onCreateView(inflater, container, savedInstanceState);

        bypassPoint = new BypassPoint();

        /**
         *  添加内点的监听函数
         *  使用一个临时变量存储内点信息，如果取消了点添加点，那么久删除这些点
         */
        Button addInerPoint = (Button) view.findViewById(R.id.id_add_danger_point);
        addInerPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DangerPoint bp = new DangerPoint(mapFragment.getCurrentCoord(),
                        0);

                bp.setPointType(PointItemType.BYPASSPOINT);
                bp.setPointNum(BypassPoint.INDICATE_NUM);
                bp.setFlyheight(getFlyHeight());

                MissionItemProxy newItem = new MissionItemProxy(missionProxy, bp);
                bypassPoint.addInnerPoint(bp);

                DangerPointMarker pointMarker = (DangerPointMarker) newItem.getMarker();
                pointMarker.setMarkerNum(bypassPoint.getInnerPoint().indexOf(bp));
                markerToAdd.add(mapFragment.updateMarker(pointMarker));

                innerIndex.setText(String.valueOf(bypassPoint.getInnerPoint().size()));
            }
        });

        return view;
    }

    public BypassPoint getBypassPoint() {
        clearInnerVar();
        final BypassPoint byp = bypassPoint;
        return byp;
    }

    /**
     * 由于Java对象赋值的特殊性，每次clear时都new一个新对象；
     */
    public void clearInnerPoint() {
        this.bypassPoint = new BypassPoint();
    }

    @Override
    public void onChanged(CardWheelHorizontalView cardWheel, int oldValue, int newValue) {

    }
}
